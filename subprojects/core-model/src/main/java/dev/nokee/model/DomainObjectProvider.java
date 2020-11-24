package dev.nokee.model;

import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import org.gradle.api.Action;
import org.gradle.api.Transformer;
import org.gradle.api.provider.Provider;
import org.gradle.util.ConfigureUtil;

import static java.util.Objects.requireNonNull;

public interface DomainObjectProvider<T> {
	DomainObjectIdentifier getIdentifier();

	Class<T> getType();

	void configure(Action<? super T> action);

	default void configure(@DelegatesTo(type = "T", strategy = Closure.DELEGATE_FIRST) Closure<Void> closure) {
		configure(ConfigureUtil.configureUsing(requireNonNull(closure)));
	}

	T get();

	<S> Provider<S> map(Transformer<? extends S, ? super T> transformer);

	<S> Provider<S> flatMap(Transformer<? extends Provider<? extends S>, ? super T> transformer);
}
