package dev.nokee.platform.nativebase.internal;

import com.google.common.collect.ImmutableList;
import dev.nokee.runtime.nativebase.BinaryLinkage;
import dev.nokee.runtime.nativebase.TargetLinkage;
import dev.nokee.runtime.nativebase.internal.TargetLinkages;
import lombok.AccessLevel;
import lombok.Getter;
import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.SetProperty;

import javax.inject.Inject;

public class TargetLinkageRule implements Action<Project> {
	private final SetProperty<TargetLinkage> targetLinkages;
	@Getter(AccessLevel.PROTECTED) private final ObjectFactory objects;
	@Getter(AccessLevel.PROTECTED) private final DependencyHandler dependencies;

	@Inject
	public TargetLinkageRule(SetProperty<TargetLinkage> targetLinkages, String componentName, ObjectFactory objects, DependencyHandler dependencies) {
		this.targetLinkages = targetLinkages;
		this.objects = objects;
		this.dependencies = dependencies;
		targetLinkages.convention(ImmutableList.of(TargetLinkages.SHARED));
	}

	@Override
	public void execute(Project project) {
		this.targetLinkages.disallowChanges();
		this.targetLinkages.finalizeValue();
	}
}
