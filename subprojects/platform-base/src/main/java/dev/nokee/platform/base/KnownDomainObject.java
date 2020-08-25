package dev.nokee.platform.base;

import dev.nokee.platform.base.internal.DomainObjectIdentity;
import org.gradle.api.Action;
import org.gradle.api.Transformer;
import org.gradle.api.provider.Provider;

public interface KnownDomainObject<T> {
	void configure(Action<? super T> action);

	Class<? extends T> getType();

	DomainObjectIdentity getIdentity();

	<S> Provider<S> map(Transformer<? extends S, ? super T> transformer);

	<S> Provider<S> flatMap(Transformer<? extends Provider<? extends S>, ? super T> transformer);
}
