package dev.nokee.model.internal.core;

import dev.nokee.model.KnownDomainObject;
import dev.nokee.model.internal.registry.ModelNodeBackedKnownDomainObject;
import dev.nokee.model.internal.type.ModelType;
import lombok.EqualsAndHashCode;
import org.gradle.api.Action;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class ModelActions {
	private ModelActions() {}

	public static ModelAction doNothing() {
		return ModelNodeAction.DO_NOTHING;
	}

	private enum ModelNodeAction implements ModelAction {
		DO_NOTHING {
			@Override
			public void execute(ModelNode node) {
				// do nothing.
			}

			@Override
			public String toString() {
				return "ModelActions.doNothing()";
			}
		}
	}

	public static <T> ModelAction executeUsingProjection(ModelType<T> type, Action<? super T> action) {
		return new ExecuteUsingProjectionModelAction<>(type, action);
	}

	@EqualsAndHashCode
	private static final class ExecuteUsingProjectionModelAction<T> implements ModelAction {
		private final ModelType<T> type;
		private final Action<? super T> action;

		private ExecuteUsingProjectionModelAction(ModelType<T> type, Action<? super T> action) {
			this.type = Objects.requireNonNull(type);
			this.action = Objects.requireNonNull(action);
		}

		@Override
		public void execute(ModelNode node) {
			action.execute(node.get(type));
		}

		@Override
		public String toString() {
			return "ModelActions.executeUsingProjection(" + type + ", " + action + ")";
		}
	}

	/**
	 * Returns an action that will only be executed once regardless of the node.
	 *
	 * @param action  the action to execute only once
	 * @return an action that will only execute once regardless of the node, never null.
	 */
	public static ModelAction once(ModelAction action) {
		return new OnceModelAction(action);
	}

	@EqualsAndHashCode
	private static final class OnceModelAction implements ModelAction {
		private final ModelAction action;
		@EqualsAndHashCode.Exclude private final Set<ModelPath> alreadyExecuted = new HashSet<>();

		public OnceModelAction(ModelAction action) {
			this.action = Objects.requireNonNull(action);
		}

		@Override
		public void execute(ModelNode node) {
			if (alreadyExecuted.add(node.getPath())) {
				action.execute(node);
			}
		}

		@Override
		public String toString() {
			return "ModelActions.once(" + action + ")";
		}
	}

	/**
	 * Returns an action that will register the specified registration on the node.
	 *
	 * @param registration  the node to register
	 * @return an action that will register a child node, never null.
	 */
	public static ModelAction register(NodeRegistration<?> registration) {
		return new RegisterModelAction(registration);
	}

	@EqualsAndHashCode
	private static final class RegisterModelAction implements ModelAction {
		private final NodeRegistration<?> registration;

		public RegisterModelAction(NodeRegistration<?> registration) {
			this.registration = Objects.requireNonNull(registration);
		}

		@Override
		public void execute(ModelNode node) {
			node.register(registration);
		}

		@Override
		public String toString() {
			return "ModelActions.register(" + registration + ")";
		}
	}

	/**
	 * Returns an action that will execute only if the specified predicate matches.
	 *
	 * @param spec  the predicate to match
	 * @param action  the action to execute
	 * @return an action that will execute the specified action only for the matching predicate, never null.
	 */
	public static ModelAction onlyIf(ModelSpec spec, ModelAction action) {
		return new OnlyIfModelAction(spec, action);
	}

	@EqualsAndHashCode
	private static final class OnlyIfModelAction implements ModelAction {
		private final ModelSpec spec;
		private final ModelAction action;

		public OnlyIfModelAction(ModelSpec spec, ModelAction action) {
			this.spec = Objects.requireNonNull(spec);
			this.action = Objects.requireNonNull(action);
		}

		@Override
		public void execute(ModelNode node) {
			if (spec.isSatisfiedBy(node)) {
				action.execute(node);
			}
		}

		@Override
		public String toString() {
			return "ModelActions.onlyIf(" + spec + ", " + action + ")";
		}
	}

	/**
	 * Returns an action that will execute the specified action using as a known projection type.
	 *
	 * @param type  the projection type
	 * @param action  the action to execute
	 * @param <T>  the projection type
	 * @return an action that will execute for known domain object, never null.
	 */
	public static <T> ModelAction executeAsKnownProjection(ModelType<T> type, Action<? super KnownDomainObject<T>> action) {
		return new ExecuteAsKnownProjectionModelAction<>(type, action);
	}

	// TODO: Should we also ensure the node is at least registered (or discovered)?
	@EqualsAndHashCode
	private static class ExecuteAsKnownProjectionModelAction<T> implements ModelAction {
		private final ModelType<T> type;
		private final Action<? super KnownDomainObject<T>> action;

		public ExecuteAsKnownProjectionModelAction(ModelType<T> type, Action<? super KnownDomainObject<T>> action) {
			this.type = Objects.requireNonNull(type);
			this.action = Objects.requireNonNull(action);
		}

		@Override
		public void execute(ModelNode node) {
			action.execute(new ModelNodeBackedKnownDomainObject<>(type, node));
		}

		@Override
		public String toString() {
			return "ModelActions.executeAsKnownProjection(" + type + ", " + action + ")";
		}
	}
}
