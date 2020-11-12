package dev.nokee.platform.nativebase.internal;

import com.google.common.collect.ImmutableSet;
import dev.nokee.language.nativebase.internal.ObjectSourceSet;
import dev.nokee.platform.base.internal.BinaryIdentifier;
import dev.nokee.platform.base.internal.tasks.TaskViewFactory;
import dev.nokee.platform.nativebase.BundleBinary;
import dev.nokee.platform.nativebase.internal.dependencies.NativeIncomingDependencies;
import dev.nokee.platform.nativebase.tasks.LinkBundle;
import dev.nokee.platform.nativebase.tasks.internal.LinkBundleTask;
import dev.nokee.platform.nativebase.tasks.internal.ObjectFilesToBinaryTask;
import dev.nokee.runtime.nativebase.OperatingSystemFamily;
import dev.nokee.runtime.nativebase.internal.DefaultTargetMachine;
import lombok.AccessLevel;
import lombok.Getter;
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
import org.gradle.nativeplatform.tasks.AbstractLinkTask;

import javax.inject.Inject;

public class BundleBinaryInternal extends BaseNativeBinary implements BundleBinary, Buildable {
	private final TaskProvider<LinkBundleTask> linkTask;
	@Getter(AccessLevel.PROTECTED) private final TaskContainer tasks;

	@Inject
	public BundleBinaryInternal(BinaryIdentifier<?> identifier, DefaultTargetMachine targetMachine, DomainObjectSet<ObjectSourceSet> objectSourceSets, TaskProvider<LinkBundleTask> linkTask, NativeIncomingDependencies dependencies, ObjectFactory objects, ProjectLayout layout, ProviderFactory providers, ConfigurationContainer configurations, TaskContainer tasks, TaskViewFactory taskViewFactory) {
		super(identifier, objectSourceSets, targetMachine, dependencies, objects, layout, providers, configurations, taskViewFactory);

		this.linkTask = linkTask;
		this.tasks = tasks;

		linkTask.configure(this::configureBundleTask);
	}

	private void configureBundleTask(LinkBundleTask task) {
		task.setDescription("Links the bundle.");
		task.source(getObjectFiles());

		task.getTargetPlatform().set(getTargetPlatform());
		task.getTargetPlatform().finalizeValueOnRead();
		task.getTargetPlatform().disallowChanges();

		// Until we model the build type
		task.getDebuggable().set(false);

		task.getDestinationDirectory().convention(getLayout().getBuildDirectory().dir(identifier.getOutputDirectoryBase("libs")));
		task.getLinkedFile().convention(getBundleLinkedFile());

		task.getLinkerArgs().addAll("-Xlinker", "-bundle"); // Required when not building swift

		task.getToolChain().set(selectNativeToolChain(getTargetMachine()));
		task.getToolChain().finalizeValueOnRead();
		task.getToolChain().disallowChanges();
	}

	private Provider<RegularFile> getBundleLinkedFile() {
		return getLayout().getBuildDirectory().file(getBaseName().map(it -> {
			OperatingSystemFamily osFamily = getTargetMachine().getOperatingSystemFamily();
			OperatingSystemOperations osOperations = OperatingSystemOperations.of(osFamily);
			return osOperations.getExecutableName(identifier.getOutputDirectoryBase("libs") + "/" + it);
		}));
	}

	@Override
	public TaskDependency getBuildDependencies() {
		return task -> ImmutableSet.of(getLinkTask().get());
	}

	@Override
	public TaskProvider<LinkBundle> getLinkTask() {
		return getTasks().named(linkTask.getName(), LinkBundle.class);
	}

	@Override
	public TaskProvider<ObjectFilesToBinaryTask> getCreateOrLinkTask() {
		return getTasks().named(linkTask.getName(), ObjectFilesToBinaryTask.class);
	}

	@Override
	public boolean isBuildable() {
		return super.isBuildable() && isBuildable(linkTask.get());
	}

	private static boolean isBuildable(LinkBundle linkTask) {
		AbstractLinkTask linkTaskInternal = (AbstractLinkTask)linkTask;
		return isBuildable(linkTaskInternal.getToolChain().get(), linkTaskInternal.getTargetPlatform().get());
	}
}
