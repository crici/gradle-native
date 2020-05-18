package dev.nokee.language.base.internal;

/**
 * Interface for source set transformation.
 * A source set transformation is a DSL convenience for writing Gradle build logic.
 * It's a simple way to logically convert a source set of one type to another.
 * Both the IN and OUT type can be the same if you choose to.
 *
 * Typically, the source set transform is used to create task(s) that to process the initial source set into another source set.
 * For example, when compiling C++ sources, we would transform a {@code SourceSet<UTTypeCppSource>} into {@code SourceSet<UTTypeObjectCode>}.
 * We would create a compile task with the correct wiring.
 * The source set transform makes this process more convenient.
 *
 * You can implement this class, create an anonymous class, or use method reference.
 *
 * @param <IN> the {@link UTType} of the initial source set
 * @param <OUT> the {@link UTType} of the transformed source set
 * @since 0.4
 */
public interface SourceSetTransform<IN extends UTType, OUT extends UTType> {
	SourceSet<OUT> transform(SourceSet<IN> sourceSet);
}
