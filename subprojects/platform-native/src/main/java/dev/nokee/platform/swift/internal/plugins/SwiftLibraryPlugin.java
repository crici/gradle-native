package dev.nokee.platform.swift.internal.plugins;

import dev.nokee.language.swift.SwiftSourceSet;
import dev.nokee.language.swift.internal.plugins.SwiftLanguageBasePlugin;
import dev.nokee.model.internal.core.ModelNodes;
import dev.nokee.model.internal.core.NodeRegistration;
import dev.nokee.model.internal.core.NodeRegistrationFactoryRegistry;
import dev.nokee.platform.base.ComponentContainer;
import dev.nokee.platform.nativebase.internal.*;
import dev.nokee.platform.nativebase.internal.plugins.NativeComponentBasePlugin;
import dev.nokee.platform.swift.SwiftLibrary;
import dev.nokee.platform.swift.SwiftLibrarySources;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.val;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.model.ObjectFactory;
import org.gradle.nativeplatform.toolchain.plugins.SwiftCompilerPlugin;
import org.gradle.util.GUtil;

import javax.inject.Inject;

import static dev.nokee.language.base.internal.plugins.LanguageBasePlugin.sourceSet;
import static dev.nokee.model.internal.core.ModelActions.register;
import static dev.nokee.model.internal.core.ModelNodes.discover;
import static dev.nokee.model.internal.core.ModelProjections.createdUsing;
import static dev.nokee.model.internal.core.NodePredicate.self;
import static dev.nokee.model.internal.type.ModelType.of;
import static dev.nokee.platform.base.internal.plugins.ComponentModelBasePlugin.component;
import static dev.nokee.platform.base.internal.plugins.ComponentModelBasePlugin.componentSourcesOf;
import static dev.nokee.platform.nativebase.internal.plugins.NativeComponentBasePlugin.*;
import static dev.nokee.platform.nativebase.internal.plugins.NativeComponentBasePlugin.configureBuildVariants;

public class SwiftLibraryPlugin implements Plugin<Project> {
	private static final String EXTENSION_NAME = "library";
	@Getter(AccessLevel.PROTECTED) private final ObjectFactory objects;

	@Inject
	public SwiftLibraryPlugin(ObjectFactory objects) {
		this.objects = objects;
	}

	@Override
	public void apply(Project project) {
		project.getPluginManager().apply(SwiftCompilerPlugin.class);

		// Create the component
		project.getPluginManager().apply(NativeComponentBasePlugin.class);
		project.getPluginManager().apply(SwiftLanguageBasePlugin.class);
		val components = project.getExtensions().getByType(ComponentContainer.class);
		ModelNodes.of(components).get(NodeRegistrationFactoryRegistry.class).registerFactory(of(SwiftLibrary.class), name -> swiftLibrary(name, project));
		val componentProvider = components.register("main", SwiftLibrary.class, configureUsingProjection(DefaultNativeLibraryComponent.class, baseNameConvention(GUtil.toCamelCase(project.getName())).andThen(configureBuildVariants())));
		val extension = componentProvider.get();

		// Other configurations
		project.afterEvaluate(getObjects().newInstance(TargetMachineRule.class, extension.getTargetMachines(), EXTENSION_NAME));
		project.afterEvaluate(getObjects().newInstance(TargetLinkageRule.class, extension.getTargetLinkages(), EXTENSION_NAME));
		project.afterEvaluate(getObjects().newInstance(TargetBuildTypeRule.class, extension.getTargetBuildTypes(), EXTENSION_NAME));
		project.afterEvaluate(finalizeModelNodeOf(componentProvider));

		project.getExtensions().add(SwiftLibrary.class, EXTENSION_NAME, extension);
	}

	public static NodeRegistration<SwiftLibrary> swiftLibrary(String name, Project project) {
		return component(name, SwiftLibrary.class)
			.withProjection(createdUsing(of(DefaultNativeLibraryComponent.class), nativeLibraryProjection(name, project)))
			.action(self(discover()).apply(register(sources())));
	}

	private static NodeRegistration<SwiftLibrarySources> sources() {
		return componentSourcesOf(SwiftLibrarySources.class)
			.action(self(discover()).apply(register(sourceSet("swift", SwiftSourceSet.class))));
	}
}
