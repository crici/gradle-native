package dev.nokee.platform.base;

import org.gradle.api.Action;
import org.gradle.api.Transformer;
import org.gradle.api.provider.Provider;
import org.gradle.api.specs.Spec;

import java.util.List;
import java.util.Set;

/**
 * A view of the binaries that are created and configured as they are required.
 *
 * @param <T> type of the elements in this view
 * @since 0.3
 */
public interface BinaryView<T extends Binary> {
	/**
	 * Registers an action to execute to configure each binary in the view.
	 * The action is only executed for those binaries that are required.
	 * Fails if any binary has already been finalized.
	 *
	 * @param action The action to execute on each binary for configuration.
	 */
	void configureEach(Action<? super T> action);

	/**
	 * Registers an action to execute to configure each binary in the view.
	 * The action is only executed for those elements that are required.
	 * Fails if any matching element has already been finalized.
	 *
	 * This method is equivalent to <code>binaries.withType(Foo).configureEach { ... }</code>.
	 *
	 * @param type The type of binary to select.
	 * @param <S> The base type of the binary to configure.
	 * @param action The action to execute on each element for configuration.
	 */
	<S extends T> void configureEach(Class<S> type, Action<? super S> action);

	/**
	 * Registers an action to execute to configure each element in the view matching the given specification.
	 * The action is only executed for those elements that are required.
	 * Fails if any element has already been finalized.
	 *
	 * @param spec a specification to satisfy. The spec is applied to each binary prior to configuration.
	 * @param action The action to execute on each element for configuration.
	 * @since 0.4
	 */
	void configureEach(Spec<? super T> spec, Action<? super T> action);

	/**
	 * Returns a binary view containing the objects in this collection of the given type.
	 * The returned collection is live, so that when matching objects are later added to this collection, they are also visible in the filtered binary view.
	 *
	 * @param type The type of binary to find.
	 * @param <S> The base type of the new binary view.
	 * @return The matching binaries. Returns an empty collection if there are no such objects in this collection.
	 */
	<S extends T> BinaryView<S> withType(Class<S> type);

	/**
	 * Returns the contents of this view as a {@link Provider} of {@code <T>} instances.
	 *
	 * <p>The returned {@link Provider} is live, and tracks changes of the view.</p>
	 *
	 * @return a provider containing all the elements included in this view.
	 */
	Provider<Set<? extends T>> getElements();

	/**
	 * Returns the contents of this view as a {@link Set} of {@code <T>} instances.
	 *
	 * @return a set containing all the elements included in this view.
	 * @since 0.4
	 */
	Set<? extends T> get();

	/**
	 * Returns a list containing the results of applying the given mapper function to each element in the view.
	 *
	 * <p>The returned {@link Provider} is live, and tracks changes of the view.</p>
	 *
	 * @param mapper a transform function to apply on each element of this view
	 * @param <S> the type of the mapped elements
	 * @return a provider containing the transformed elements included in this view.
	 * @since 0.4
	 */
	<S> Provider<List<? extends S>> map(Transformer<? extends S, ? super T> mapper);

	/**
	 * Returns a single list containing all elements yielded from results of mapper function being invoked on each element of this view.
	 *
	 * <p>The returned {@link Provider} is live, and tracks changes of the view.</p>
	 *
	 * @param mapper a transform function to apply on each element of this view
	 * @param <S> the type of the mapped elements
	 * @return a provider containing the mapped elements of this view.
	 * @since 0.4
	 */
	<S> Provider<List<? extends S>> flatMap(Transformer<Iterable<? extends S>, ? super T> mapper);

	/**
	 * Returns a single list containing all elements matching the given specification.
	 *
	 * <p>The returned {@link Provider} is live, and tracks changes of the view.</p>
	 *
	 * @param spec a specification to satisfy
	 * @return a provider containing the filtered elements of this view.
	 * @since 0.4
	 */
	Provider<List<? extends T>> filter(Spec<? super T> spec);
}
