package dev.nokee.ide.xcode.internal.plugins;

import com.google.common.collect.ImmutableList;
import dev.nokee.ide.base.internal.IdeProjectExtension;
import dev.nokee.ide.base.internal.IdeProjectInternal;
import dev.nokee.ide.base.internal.plugins.AbstractIdePlugin;
import dev.nokee.ide.xcode.*;
import dev.nokee.ide.xcode.internal.*;
import dev.nokee.ide.xcode.internal.services.XcodeIdeGidGeneratorService;
import dev.nokee.ide.xcode.internal.tasks.GenerateXcodeIdeProjectTask;
import dev.nokee.ide.xcode.internal.tasks.GenerateXcodeIdeWorkspaceTask;
import dev.nokee.ide.xcode.internal.tasks.SyncXcodeIdeProduct;
import lombok.Value;
import org.gradle.api.*;
import org.gradle.api.file.Directory;
import org.gradle.api.file.FileSystemLocation;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Provider;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.internal.Actions;
import org.gradle.plugins.ide.internal.IdeArtifactRegistry;
import org.gradle.plugins.ide.internal.IdeProjectMetadata;

import javax.inject.Inject;
import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static dev.nokee.internal.ProjectUtils.getPrefixableProjectPath;
import static dev.nokee.internal.ProjectUtils.isRootProject;

public abstract class XcodeIdePlugin extends AbstractIdePlugin {
	public static final String XCODE_EXTENSION_NAME = "xcode";

	@Override
	public void doApply(Project project) {
		DefaultXcodeIdeProjectExtension projectExtension = registerExtension(project);
		Optional<DefaultXcodeIdeWorkspaceExtension> workspaceExtension = asWorkspaceExtensionIfAvailable(projectExtension);

		getLifecycleTask().configure(task -> {
			task.dependsOn(projectExtension.getProjects());
		});

		workspaceExtension.ifPresent(extension -> {
			extension.getWorkspace().getProjects().set(extension.getProjects());

			extension.getWorkspace().getGeneratorTask().configure(task -> {
				task.getWorkspaceLocation().set(getLayout().getProjectDirectory().dir(project.getName() + ".xcworkspace"));
				task.getProjectLocations().set(getArtifactRegistry().getIdeProjectFiles(XcodeIdeProjectMetadata.class).getElements());
				task.getDerivedDataLocation().set(".gradle/XcodeDerivedData");
			});

			addWorkspace(extension.getWorkspace());
		});

		// TODO: Add a task for cleaning the Xcode derived data for the workspace/project.
		//  It could be something like cleanXcodeDerivedData or just the same cleanXcode task.
		//  See https://pewpewthespells.com/blog/xcode_deriveddata_hashes.html
		//  The reason for cleaning the derived data is mainly because Xcode sometimes gets into a bad states.
		//  Cleaning that directory also deletes indexing data.
		//  We should probably delete the DerivedData when cleaning Xcode anyway
		getCleanTask().configure(task -> {
			task.delete(getProviders().provider(() -> projectExtension.getProjects().stream().map(XcodeIdeProject::getLocation).collect(Collectors.toList())));
			workspaceExtension.ifPresent(extension -> {
				task.delete(extension.getWorkspace().getLocation());
				task.delete(extension.getWorkspace().getGeneratorTask().flatMap(GenerateXcodeIdeWorkspaceTask::getDerivedDataLocation));
			});
		});
		getTasks().withType(GenerateXcodeIdeProjectTask.class).configureEach(task -> task.shouldRunAfter(getCleanTask()));

		Provider<XcodeIdeGidGeneratorService> xcodeIdeGidGeneratorService = project.getGradle().getSharedServices().registerIfAbsent("xcodeIdeGidGeneratorService", XcodeIdeGidGeneratorService.class, Actions.doNothing());
		projectExtension.getProjects().withType(DefaultXcodeIdeProject.class).configureEach(xcodeProject -> {
			xcodeProject.getGeneratorTask().configure( task -> {
				FileSystemLocation projectLocation = getLayout().getProjectDirectory().dir(xcodeProject.getName() + ".xcodeproj");
				task.getProjectLocation().set(projectLocation);
				task.usesService(xcodeIdeGidGeneratorService);
				task.getGidGenerator().set(xcodeIdeGidGeneratorService);
				task.getGradleCommand().set(toGradleCommand(project.getGradle()));
				task.getBridgeTaskPath().set(toBridgeTaskPath(project));
				task.getAdditionalGradleArguments().set(getAdditionalBuildArguments(project));
				task.getSources().from(getBuildFiles(project));
			});
		});
		addProjectExtension(projectExtension);

		project.getTasks().addRule(getObjects().newInstance(XcodeIdeBridge.class, projectExtension.getProjects(), project));

		project.getPluginManager().withPlugin("dev.nokee.objective-c-ios-application", appliedPlugin -> {
			project.getPluginManager().apply(XcodeIdeObjectiveCIosApplicationPlugin.class);
		});
		project.getPluginManager().withPlugin("dev.nokee.swift-ios-application", appliedPlugin -> {
			project.getPluginManager().apply(XcodeIdeSwiftIosApplicationPlugin.class);
		});

		project.getPluginManager().withPlugin("dev.nokee.c-application", appliedPlugin -> {
			project.getPluginManager().apply(XcodeIdeNativeApplicationPlugin.class);
		});
		project.getPluginManager().withPlugin("dev.nokee.cpp-application", appliedPlugin -> {
			project.getPluginManager().apply(XcodeIdeNativeApplicationPlugin.class);
		});
		project.getPluginManager().withPlugin("dev.nokee.objective-c-application", appliedPlugin -> {
			project.getPluginManager().apply(XcodeIdeNativeApplicationPlugin.class);
		});
		project.getPluginManager().withPlugin("dev.nokee.objective-cpp-application", appliedPlugin -> {
			project.getPluginManager().apply(XcodeIdeNativeApplicationPlugin.class);
		});
		project.getPluginManager().withPlugin("dev.nokee.swift-application", appliedPlugin -> {
			project.getPluginManager().apply(XcodeIdeSwiftApplicationPlugin.class);
		});
		project.getPluginManager().withPlugin("dev.nokee.c-library", appliedPlugin -> {
			project.getPluginManager().apply(XcodeIdeNativeLibraryPlugin.class);
		});
		project.getPluginManager().withPlugin("dev.nokee.cpp-library", appliedPlugin -> {
			project.getPluginManager().apply(XcodeIdeNativeLibraryPlugin.class);
		});
		project.getPluginManager().withPlugin("dev.nokee.objective-c-library", appliedPlugin -> {
			project.getPluginManager().apply(XcodeIdeNativeLibraryPlugin.class);
		});
		project.getPluginManager().withPlugin("dev.nokee.objective-cpp-library", appliedPlugin -> {
			project.getPluginManager().apply(XcodeIdeNativeLibraryPlugin.class);
		});
		project.getPluginManager().withPlugin("dev.nokee.swift-library", appliedPlugin -> {
			project.getPluginManager().apply(XcodeIdeSwiftLibraryPlugin.class);
		});
	}

	@Override
	protected String getLifecycleTaskName() {
		return XCODE_EXTENSION_NAME;
	}

	@Override
	protected String getIdeDisplayName() {
		return "Xcode";
	}

	@Override
	protected IdeProjectMetadata newIdeProjectMetadata(Provider<IdeProjectInternal> ideProject) {
		return new XcodeIdeProjectMetadata(ideProject.map(DefaultXcodeIdeProject.class::cast));
	}

	// TODO: Implicit init script should probably also be added to make the user realize those are affecting your build.
	// TODO: Implicit gradle.properties should probably also be added to the build
	// NOTE: For the implicit files, we have to ensure it's obvious the files are not part of the build but part of the machine but affecting the build
	// CAUTION: The implicit gradle.properties is often use for storing credentials, we should be careful with that file.
	private List<File> getBuildFiles(Project project) {
		ImmutableList.Builder<File> result = ImmutableList.builder();
		if (project.getBuildFile().exists()) {
			result.add(project.getBuildFile());
		}

		if (isRootProject(project)) {
			if (project.getGradle().getStartParameter().getSettingsFile() != null) {
				result.add(project.getGradle().getStartParameter().getSettingsFile());
			} else if (project.file("settings.gradle").exists()) {
				result.add(project.file("settings.gradle"));
			} else if (project.file("settings.gradle.kts").exists()) {
				result.add(project.file("settings.gradle.kts"));
			}

			if (project.file("gradle.properties").exists()) {
				result.add(project.file("gradle.properties"));
			}

			project.getGradle().getStartParameter().getInitScripts().forEach(result::add);
		}
		return result.build();
	}

	private List<String> getAdditionalBuildArguments(Project project) {
		ImmutableList.Builder<String> result = ImmutableList.builder();
		project.getGradle().getStartParameter().getInitScripts().forEach(initScriptFile -> {
			result.add("--init-script", quote(initScriptFile.getAbsolutePath()));
		});
		return result.build();
	}

	private static String quote(String value) {
		return "\"" + value + "\"";
	}

	//region Xcode IDE extension registration
	private DefaultXcodeIdeProjectExtension registerExtension(Project project) {
		if (isRootProject(project)) {
			return registerWorkspaceExtension(project);
		}
		return registerProjectExtension(project);
	}

	private DefaultXcodeIdeWorkspaceExtension registerWorkspaceExtension(Project project) {
		DefaultXcodeIdeWorkspaceExtension extension = getObjects().newInstance(DefaultXcodeIdeWorkspaceExtension.class);
		project.getExtensions().add(XcodeIdeWorkspaceExtension.class, XCODE_EXTENSION_NAME, extension);
		return extension;
	}

	private DefaultXcodeIdeProjectExtension registerProjectExtension(Project project) {
		DefaultXcodeIdeProjectExtension extension = getObjects().newInstance(DefaultXcodeIdeProjectExtension.class);
		project.getExtensions().add(XcodeIdeProjectExtension.class, XCODE_EXTENSION_NAME, extension);
		return extension;
	}

	private static Optional<DefaultXcodeIdeWorkspaceExtension> asWorkspaceExtensionIfAvailable(DefaultXcodeIdeProjectExtension projectExtension) {
		if (projectExtension instanceof XcodeIdeWorkspaceExtension) {
			return Optional.of((DefaultXcodeIdeWorkspaceExtension) projectExtension);
		}
		return Optional.empty();
	}
	//endregion

	@Inject
	protected abstract ObjectFactory getObjects();

	@Inject
	protected abstract TaskContainer getTasks();

	@Inject
	protected abstract ProjectLayout getLayout();

	@Inject
	protected abstract ProviderFactory getProviders();

	@Inject
	protected abstract IdeArtifactRegistry getArtifactRegistry();

	/**
	 * Returns the task name format to use inside Xcode legacy target when delegating to Gradle.
	 * When Gradle is invoked with tasks following the name format, it is delegated to {@link XcodeIdeBridge} via {@link TaskContainer#addRule(Rule)}.
	 *
	 * @param project the {@link Project} instance the task belongs to
	 * @return a fully qualified task path format for the {@literal PBXLegacyTarget} target type to realize using the build settings from within Xcode IDE.
	 */
	private static String toBridgeTaskPath(Project project) {
		return getPrefixableProjectPath(project) + ":_xcode__${ACTION}_${PROJECT_NAME}_${TARGET_NAME}_${CONFIGURATION}";
	}

	/**
	 * Task rule for bridging Xcode IDE with Gradle.
	 */
	protected static abstract class XcodeIdeBridge implements Rule {
		private final NamedDomainObjectSet<XcodeIdeProject> xcodeProjects;
		private final Project project;
		private final XcodeIdePropertyAdapter xcodePropertyAdapter;

		@Inject
		public XcodeIdeBridge(NamedDomainObjectSet<XcodeIdeProject> xcodeProjects, Project project) {
			this.xcodeProjects = xcodeProjects;
			this.project = project;
			this.xcodePropertyAdapter = new XcodeIdePropertyAdapter(project);
		}

		@Inject
		protected abstract TaskContainer getTasks();

		@Inject
		protected abstract ObjectFactory getObjects();

		@Override
		public String getDescription() {
			return "Xcode IDE bridge tasks begin with _xcode. Do not call these directly.";
		}

		@Override
		public void apply(String taskName) {
			if (taskName.startsWith("_xcode")) {
				XcodeIdeRequest request = XcodeIdeRequest.of(taskName);
				String action = request.getAction();
				if (action.equals("clean")) {
					Task bridgeTask = getTasks().create(taskName);
					bridgeTask.dependsOn("clean");
				} else if ("".equals(action) || "build".equals(action)) {
					final XcodeIdeTarget target = findXcodeTarget(request);
					SyncXcodeIdeProduct bridgeTask = getTasks().create(taskName, SyncXcodeIdeProduct.class);
					bridgeProductBuild(bridgeTask, target, request);
				} else {
					throw new GradleException("Unrecognized bridge action from Xcode '" + action + "'");
				}
			}
		}

		private XcodeIdeTarget findXcodeTarget(XcodeIdeRequest request) {
			String projectName = request.getProjectName();
			XcodeIdeProject project = xcodeProjects.findByName(projectName);
			if (project == null) {
				throw new GradleException(String.format("Unknown Xcode IDE project '%s', try re-generating the Xcode IDE configuration using '%s:xcode' task.", projectName, getPrefixableProjectPath(this.project)));
			}

			String targetName = request.getTargetName();
			XcodeIdeTarget target = project.getTargets().findByName(targetName);
			if (target == null) {
				throw new GradleException(String.format("Unknown Xcode IDE target '%s', try re-generating the Xcode IDE configuration using '%s:xcode' task.", targetName, getPrefixableProjectPath(this.project)));
			}
			return target;
		}

		private void bridgeProductBuild(SyncXcodeIdeProduct bridgeTask, XcodeIdeTarget target, XcodeIdeRequest request) {
			// Library or executable
			final String configurationName = request.getConfiguration();
			XcodeIdeBuildConfiguration configuration = target.getBuildConfigurations().findByName(configurationName);
			if (configuration == null) {
				throw new GradleException(String.format("Unknown Xcode IDE configuration '%s', try re-generating the Xcode IDE configuration using '%s:xcode' task.", configurationName, getPrefixableProjectPath(this.project)));
			}

			// For XCTest bundle, we have to copy the binary to the BUILT_PRODUCTS_DIR, otherwise Xcode doesn't find the tests.
			// For legacy target, we can work around this by setting the CONFIGURATION_BUILD_DIR to the parent of where Gradle put the built binaries.
			// However, XCTest target are a bit more troublesome and it doesn't seem to play by the same rules as the legacy target.
			// To simplify the configuration, let's always copy the product where Xcode expect it to be.
			// It remove complexity and fragility on the Gradle side.
			final Directory builtProductsPath = getObjects().directoryProperty().fileValue(new File(xcodePropertyAdapter.getBuiltProductsDir())).get();
			bridgeTask.getProductLocation().convention(configuration.getProductLocation());
			bridgeTask.getDestinationLocation().convention(builtProductsPath.file(target.getProductReference().get()));
		}
	}

	// TODO: Converge XcodeIdeRequest, XcodeIdeBridge and XcodeIdePropertyAdapter. All three have overlapping responsibilities.
	//  Specifically for XcodeIdeBridge, we may want to attach the product sync task directly to the XcodeIde* model an convert the lifecycle task type to Task.
	//  It would make the bridge task more dummy and open for further customization of the Xcode delegation by allowing configuring the bridge task.
	// TODO: XcodeIdeRequest should convert the action string/null to an XcodeIdeAction enum
	@Value
	public static class XcodeIdeRequest {
		private static final Pattern LIFECYCLE_TASK_PATTERN = Pattern.compile("_xcode__(?<action>build|clean)?_(?<project>[a-zA-Z\\-_]+)_(?<target>[a-zA-Z\\-_]+)_(?<configuration>[a-zA-Z\\-_]+)");
		String action;
		String projectName;
		String targetName;
		String configuration;

		public static XcodeIdeRequest of(String taskName) {
			Matcher m = LIFECYCLE_TASK_PATTERN.matcher(taskName);
			if (m.matches()) {
				return new XcodeIdeRequest(Optional.ofNullable(m.group("action")).orElse("build"), m.group("project"), m.group("target"), m.group("configuration"));
			}
			throw new GradleException(String.format("Unable to match the lifecycle task name '%s', it is most likely a bug. Please report it at https://github.com/nokeedev/gradle-native/issues.", taskName));
		}
	}
}
