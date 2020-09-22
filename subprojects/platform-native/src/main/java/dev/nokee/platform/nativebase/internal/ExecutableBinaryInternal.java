package dev.nokee.platform.nativebase.internal;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import dev.nokee.core.exec.CommandLine;
import dev.nokee.core.exec.ProcessBuilderEngine;
import dev.nokee.language.base.internal.GeneratedSourceSet;
import dev.nokee.platform.base.internal.NamingScheme;
import dev.nokee.platform.nativebase.ExecutableBinary;
import dev.nokee.platform.nativebase.tasks.LinkExecutable;
import dev.nokee.platform.nativebase.tasks.internal.LinkExecutableTask;
import dev.nokee.platform.nativebase.tasks.internal.ObjectFilesToBinaryTask;
import dev.nokee.runtime.nativebase.OperatingSystemFamily;
import dev.nokee.runtime.nativebase.internal.DefaultTargetMachine;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.commons.io.FilenameUtils;
import org.gradle.api.Buildable;
import org.gradle.api.DomainObjectSet;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.file.RegularFile;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Provider;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskDependency;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.nativeplatform.toolchain.Swiftc;

import javax.inject.Inject;
import java.io.File;
import java.util.stream.Stream;

public class ExecutableBinaryInternal extends BaseNativeBinary implements ExecutableBinary, Buildable {
	private final TaskProvider<LinkExecutableTask> linkTask;
	@Getter(AccessLevel.PROTECTED) private final TaskContainer tasks;

	@Inject
	public ExecutableBinaryInternal(NamingScheme names, DomainObjectSet<GeneratedSourceSet> objectSourceSets, DefaultTargetMachine targetMachine, TaskProvider<LinkExecutableTask> linkTask, ObjectFactory objects, ProjectLayout layout, ProviderFactory providers, ConfigurationContainer configurations, TaskContainer tasks) {
		super(names, objectSourceSets, targetMachine, objects, layout, providers, configurations);
		this.linkTask = linkTask;
		this.tasks = tasks;

		linkTask.configure(this::configureExecutableTask);
		linkTask.configure(task -> {
			task.getLinkerArgs().addAll(task.getToolChain().map(it -> {
				if (it instanceof Swiftc && targetMachine.getOperatingSystemFamily().isMacOs()) {
					// TODO: Support DEVELOPER_DIR or request the xcrun tool from backend
					return ImmutableList.of("-sdk", CommandLine.of("xcrun", "--show-sdk-path").execute(new ProcessBuilderEngine()).waitFor().assertNormalExitValue().getStandardOutput().getAsString().trim());
				}
				return ImmutableList.of();
			}));
		});
	}

	static Stream<String> toFrameworkFlags(File it) {
		return ImmutableList.of("-F", it.getParent(), "-framework", FilenameUtils.removeExtension(it.getName())).stream();
	}

	private void configureExecutableTask(LinkExecutableTask task) {
		task.setDescription("Links the executable.");
		task.source(getObjectFiles());

		task.getTargetPlatform().set(getTargetPlatform());
		task.getTargetPlatform().finalizeValueOnRead();
		task.getTargetPlatform().disallowChanges();

		// Until we model the build type
		task.getDebuggable().set(false);

		task.getDestinationDirectory().convention(getLayout().getBuildDirectory().dir(getNames().getOutputDirectoryBase("exes")));
		task.getLinkedFile().convention(getExecutableLinkedFile());

		task.getToolChain().set(selectNativeToolChain(getTargetMachine()));
		task.getToolChain().finalizeValueOnRead();
		task.getToolChain().disallowChanges();
	}

	private Provider<RegularFile> getExecutableLinkedFile() {
		return getLayout().getBuildDirectory().file(getBaseName().map(it -> {
			OperatingSystemFamily osFamily = getTargetMachine().getOperatingSystemFamily();
			OperatingSystemOperations osOperations = OperatingSystemOperations.of(osFamily);
			return osOperations.getExecutableName(getNames().getOutputDirectoryBase("exes") + "/" + it);
		}));
	}

	@Override
	public TaskProvider<LinkExecutable> getLinkTask() {
		return getTasks().named(linkTask.getName(), LinkExecutable.class);
	}

	@Override
	public TaskProvider<ObjectFilesToBinaryTask> getCreateOrLinkTask() {
		return getTasks().named(linkTask.getName(), ObjectFilesToBinaryTask.class);
	}

	@Override
	public TaskDependency getBuildDependencies() {
		return task -> ImmutableSet.of(getLinkTask().get());
	}
}
