package dev.nokee.platform.base.internal;

import dev.nokee.model.internal.Value;
import dev.nokee.platform.base.Variant;
import dev.nokee.utils.TransformerUtils;
import lombok.EqualsAndHashCode;
import org.gradle.api.Action;
import org.gradle.api.Transformer;
import org.gradle.api.provider.Provider;

import static java.util.Objects.requireNonNull;

public final class VariantProvider<T extends Variant> {
	private final VariantIdentifier<T> identifier;
	private final Value<T> value;

	public VariantProvider(VariantIdentifier<T> identifier, Value<T> value) {
		this.identifier = identifier;
		this.value = value;
	}

	public BuildVariantInternal getBuildVariant() {
		return (BuildVariantInternal) identifier.getBuildVariant();
	}

	public Class<T> getType() {
		return identifier.getType();
	}

	public T get() {
		return value.get();
	}

	public void configure(Action<? super T> action) {
		value.mapInPlace(configureInPlace(action));
	}

	public <S> Provider<S> map(Transformer<? extends S, ? super T> mapper) {
		return value.map(mapper);
	}

	public <S> Provider<S> flatMap(Transformer<? extends Provider<? extends S>, ? super T> mapper) {
		return value.flatMap(mapper);
	}

	private static <T> TransformerUtils.Transformer<T, T> configureInPlace(Action<? super T> action) {
		return new ConfigureInPlaceTransformer<>(action);
	}

	@EqualsAndHashCode
	private static final class ConfigureInPlaceTransformer<T> implements TransformerUtils.Transformer<T, T> {
		private final Action<? super T> action;

		public ConfigureInPlaceTransformer(Action<? super T> action) {
			this.action = requireNonNull(action);
		}

		@Override
		public T transform(T t) {
			action.execute(t);
			return t;
		}

		@Override
		public String toString() {
			return "TransformerUtils.configureInPlace(" + action + ")";
		}
	}
}
