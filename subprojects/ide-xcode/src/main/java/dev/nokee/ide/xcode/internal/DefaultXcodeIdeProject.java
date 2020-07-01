package dev.nokee.ide.xcode.internal;

import dev.nokee.ide.base.internal.IdeProjectInternal;
import dev.nokee.ide.xcode.XcodeIdeProject;
import dev.nokee.ide.xcode.XcodeIdeTarget;
import dev.nokee.ide.xcode.internal.tasks.GenerateXcodeIdeProjectTask;
import lombok.Getter;
import org.gradle.api.Action;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.file.FileSystemLocation;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;

import javax.inject.Inject;

public abstract class DefaultXcodeIdeProject implements XcodeIdeProject, IdeProjectInternal {
	@Getter private final String name;
	@Getter private final TaskProvider<GenerateXcodeIdeProjectTask> generatorTask;
	@Getter private final NamedDomainObjectContainer<XcodeIdeTarget> targets;

	@Inject
	public DefaultXcodeIdeProject(String name) {
		this.name = name;
		this.targets = getObjects().domainObjectContainer(XcodeIdeTarget.class, this::newTarget);
		generatorTask = getTasks().register(name + "XcodeProject", GenerateXcodeIdeProjectTask.class, this);
	}

	@Inject
	protected abstract TaskContainer getTasks();

	@Inject
	protected abstract ObjectFactory getObjects();

	@Override
	public Provider<FileSystemLocation> getLocation() {
		return generatorTask.flatMap(GenerateXcodeIdeProjectTask::getProjectLocation);
	}

	@Override
	public void targets(Action<? super NamedDomainObjectContainer<XcodeIdeTarget>> action) {
		action.execute(targets);
	}

	private XcodeIdeTarget newTarget(String name) {
		return getObjects().newInstance(DefaultXcodeIdeTarget.class, name);
	}

	@Override
	public String getDisplayName() {
		return "Xcode project";
	}
}
