package dev.nokee.platform.ios.internal;

import dev.nokee.platform.base.internal.VariantIdentifier;
import dev.nokee.platform.base.internal.VariantInternal;
import dev.nokee.platform.base.internal.binaries.BinaryViewFactory;
import dev.nokee.platform.base.internal.dependencies.ResolvableComponentDependencies;
import dev.nokee.platform.ios.IosApplication;
import dev.nokee.platform.ios.internal.rules.IosDevelopmentBinaryConvention;
import dev.nokee.platform.nativebase.internal.BaseNativeVariant;
import dev.nokee.platform.nativebase.internal.dependencies.DefaultNativeComponentDependencies;
import dev.nokee.platform.nativebase.internal.dependencies.VariantComponentDependencies;
import lombok.Getter;
import org.gradle.api.Task;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.tasks.TaskProvider;

import javax.inject.Inject;

public class DefaultIosApplicationVariant extends BaseNativeVariant implements IosApplication, VariantInternal {
	private final DefaultNativeComponentDependencies dependencies;
	@Getter private final ResolvableComponentDependencies resolvableDependencies;
	@Getter private final Property<String> productBundleIdentifier;

	@Inject
	public DefaultIosApplicationVariant(VariantIdentifier<?> identifier, VariantComponentDependencies<DefaultNativeComponentDependencies> dependencies, ObjectFactory objects, ProviderFactory providers, TaskProvider<Task> assembleTask, BinaryViewFactory binaryViewFactory) {
		super(identifier, objects, providers, assembleTask, binaryViewFactory);
		this.dependencies = dependencies.getDependencies();
		this.resolvableDependencies = dependencies.getIncoming();
		this.productBundleIdentifier = objects.property(String.class);

		getDevelopmentBinary().convention(getBinaries().getElements().flatMap(IosDevelopmentBinaryConvention.INSTANCE));
	}

	@Override
	public DefaultNativeComponentDependencies getDependencies() {
		return dependencies;
	}
}
