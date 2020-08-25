package dev.nokee.testing.xctest.internal.plugins;

import dev.nokee.language.base.internal.DaggerLanguageSourceSetInstantiatorComponent;
import dev.nokee.language.c.CHeaderSet;
import dev.nokee.language.objectivec.ObjectiveCSourceSet;
import dev.nokee.platform.base.internal.DomainObjectStore;
import dev.nokee.platform.base.internal.GroupId;
import dev.nokee.platform.base.internal.NamingSchemeFactory;
import dev.nokee.platform.ios.ObjectiveCIosApplicationExtension;
import dev.nokee.platform.ios.internal.DefaultObjectiveCIosApplicationExtension;
import dev.nokee.platform.nativebase.internal.BaseNativeComponent;
import dev.nokee.testing.xctest.internal.DefaultUiTestXCTestTestSuiteComponent;
import dev.nokee.testing.xctest.internal.DefaultUnitTestXCTestTestSuiteComponent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.val;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.tasks.TaskContainer;

import javax.inject.Inject;

import static dev.nokee.model.DomainObjectIdentifier.named;

public class ObjectiveCXCTestTestSuitePlugin implements Plugin<Project> {
	@Getter(AccessLevel.PROTECTED) private final TaskContainer tasks;
	@Getter(AccessLevel.PROTECTED) private final ProjectLayout layout;
	@Getter(AccessLevel.PROTECTED) private final ProviderFactory providers;
	@Getter(AccessLevel.PROTECTED) private final ObjectFactory objects;

	@Inject
	public ObjectiveCXCTestTestSuitePlugin(TaskContainer tasks, ProjectLayout layout, ProviderFactory providers, ObjectFactory objects) {
		this.tasks = tasks;
		this.layout = layout;
		this.providers = providers;
		this.objects = objects;
	}

	@Override
	public void apply(Project project) {
		project.getPluginManager().withPlugin("dev.nokee.objective-c-ios-application", appliedPlugin -> {
			BaseNativeComponent<?> application = ((DefaultObjectiveCIosApplicationExtension) project.getExtensions().getByType(ObjectiveCIosApplicationExtension.class)).getComponent();
			val store = project.getExtensions().getByType(DomainObjectStore.class);

			val sourceSetInstantiator = DaggerLanguageSourceSetInstantiatorComponent.factory().create(project).get();

			val unitTestComponent = store.register(DefaultUnitTestXCTestTestSuiteComponent.newUnitTest(getObjects(), new NamingSchemeFactory(project.getName())));
			unitTestComponent.configure(component -> {
				component.getSourceCollection().add(sourceSetInstantiator.create(named("objc"), ObjectiveCSourceSet.class).from("src/unitTest/objc"));
				component.getSourceCollection().add(sourceSetInstantiator.create(named("headers"), CHeaderSet.class).from("src/unitTest/headers"));
				component.getTestedComponent().value(application).disallowChanges();
				component.getGroupId().set(GroupId.of(project::getGroup));
				component.finalizeExtension(project);
				component.getVariantCollection().disallowChanges().realize(); // Force realization, for now
			});
			unitTestComponent.get();

			val uiTestComponent = store.register(DefaultUiTestXCTestTestSuiteComponent.newUiTest(getObjects(), new NamingSchemeFactory(project.getName())));
			uiTestComponent.configure(component -> {
				component.getSourceCollection().add(sourceSetInstantiator.create(named("objc"), ObjectiveCSourceSet.class).from("src/uiTest/objc"));
				component.getSourceCollection().add(sourceSetInstantiator.create(named("headers"), CHeaderSet.class).from("src/uiTest/headers"));
				component.getTestedComponent().value(application).disallowChanges();
				component.getGroupId().set(GroupId.of(project::getGroup));
				component.finalizeExtension(project);
				component.getVariantCollection().disallowChanges().realize(); // Force realization, for now
			});
			uiTestComponent.get();
		});
	}
}
