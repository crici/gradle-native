package dev.nokee.model.internal;

import dev.nokee.model.DomainObjectFactory;
import dev.nokee.model.DomainObjectProvider;
import dev.nokee.model.internal.core.ModelNode;
import dev.nokee.model.internal.core.NodeRegistration;
import dev.nokee.model.internal.core.NodeRegistrationFactoryLookup;
import dev.nokee.model.internal.type.ModelType;
import lombok.val;
import org.gradle.api.Action;

import java.util.HashMap;
import java.util.Map;

import static dev.nokee.model.internal.core.ModelNodeContext.getCurrentModelNode;

public class BaseNamedDomainObjectContainerProjection implements AbstractModelNodeBackedNamedDomainObjectContainer.Projection {
	private final PolymorphicDomainObjectInstantiator<Object> instantiator = new AbstractPolymorphicDomainObjectInstantiator<Object>(Object.class, "") {};
	private final Map<Class<?>, Class<?>> bindings = new HashMap<>();
	private final ModelNode node = getCurrentModelNode();

	@SuppressWarnings("unchecked")
	private <U> Class<U> toImplementationType(Class<U> type) {
		return (Class<U>) bindings.getOrDefault(type, type);
	}

	@Override
	public <U> DomainObjectProvider<U> register(String name, ModelType<U> type) {
		if (node.get(NodeRegistrationFactoryLookup.class).getSupportedTypes().contains(type)) {
			return node.register(node.get(NodeRegistrationFactoryLookup.class).get(type).create(name));
		}
		if (instantiator.getCreatableTypes().contains(type.getRawType())) {
			return node.register(NodeRegistration.unmanaged(name, ModelType.of(toImplementationType(type.getConcreteType())), () -> instantiator.newInstance(new NameAwareDomainObjectIdentifier() {
				@Override
				public Object getName() {
					return name;
				}
			}, toImplementationType(type.getConcreteType()))));
		}
		return node.register(NodeRegistration.of(name, type));
	}

	@Override
	public <U> DomainObjectProvider<U> register(String name, ModelType<U> type, Action<? super U> action) {
		val provider = register(name, type);
		provider.configure(action);
		return provider;
	}

	@Override
	public <U> void registerFactory(Class<U> type, DomainObjectFactory<? extends U> factory) {
		instantiator.registerFactory(type, factory);
	}

	@Override
	public <U> void registerBinding(Class<U> type, Class<? extends U> implementationType) {
		instantiator.registerBinding(type, implementationType);
		bindings.put(type, implementationType);
	}
}
