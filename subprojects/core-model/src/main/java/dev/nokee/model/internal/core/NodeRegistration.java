package dev.nokee.model.internal.core;

import dev.nokee.internal.Factory;
import dev.nokee.model.internal.type.ModelType;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.val;

import java.util.ArrayList;
import java.util.List;

import static dev.nokee.model.internal.core.ModelRegistration.builder;

// The major difference between {@link ModelRegistration} and {@link NodeRegistration} is the fact that ModelRegistration is absolute, e.g. starts from root node where NodeRegistration is relative, e.g. relative to a model node.
@ToString
@EqualsAndHashCode
public final class NodeRegistration<T> {
	private final String name;
	private final ModelType<T> defaultProjectionType;
	private final List<ModelProjection> projections = new ArrayList<>();
	private final List<NodeAction> actionRegistrations = new ArrayList<>();

	private NodeRegistration(String name, ModelType<T> defaultProjectionType, ModelProjection defaultProjection) {
		this.name = name;
		this.defaultProjectionType = defaultProjectionType;
		projections.add(defaultProjection);
	}

	ModelRegistration<T> scope(ModelPath path) {
		val builder = builder()
			.withPath(path.child(name))
			.withDefaultProjectionType(defaultProjectionType);
		projections.forEach(builder::withProjection);
		actionRegistrations.stream().map(it -> it.scope(path.child(name))).forEach(builder::action);
		return builder.build();
	}

	public static <T> NodeRegistration<T> of(String name, ModelType<T> type, Object... parameters) {
		return new NodeRegistration<>(name, type, ModelProjections.managed(type, parameters));
	}

	public static <T> NodeRegistration<T> unmanaged(String name, ModelType<T> type, Factory<T> factory) {
		return new NodeRegistration<>(name, type, ModelProjections.createdUsing(type, factory));
	}

	public NodeRegistration<T> withProjection(ModelProjection projection) {
		projections.add(projection);
		return this;
	}

	public NodeRegistration<T> action(NodeAction action) {
		actionRegistrations.add(action);
		return this;
	}

	public ModelType<T> getDefaultProjectionType() {
		return defaultProjectionType;
	}
}
