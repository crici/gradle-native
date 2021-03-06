package dev.nokee.internal;

import com.google.common.testing.EqualsTester;
import lombok.val;
import org.gradle.internal.Cast;
import org.junit.jupiter.api.Test;
import spock.lang.Subject;

import static dev.nokee.internal.Factories.constant;
import static dev.nokee.internal.Factories.memoize;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;
import static org.mockito.Mockito.*;

@Subject(Factories.class)
class Factories_MemoizeTest {
	@Test
	void callsDelegateFactoryOnlyOnce() {
		Factory<MyType> delegate = Cast.uncheckedCast(mock(Factory.class));
		val factory = memoize(delegate);
		factory.create();
		factory.create();
		factory.create();
		verify(delegate, times(1)).create();
	}

	@Test
	void alwaysReturnsValueFromDelegateFactory() {
		val factory = memoize(constant(42));
		assertThat(factory.create(), equalTo(42));
		assertThat(factory.create(), equalTo(42));
		assertThat(factory.create(), equalTo(42));
	}

	@Test
	@SuppressWarnings("UnstableApiUsage")
	void checkEquals() {
		new EqualsTester()
			.addEqualityGroup(memoize(constant(42)), memoize(constant(42)), alreadyCreated(constant(42)))
			.addEqualityGroup(memoize(constant(24)))
			.addEqualityGroup(memoize(constant("foo")))
			.testEquals();
	}

	private static <T> Factory<T> alreadyCreated(Factory<T> delegate) {
		val result = memoize(delegate);
		result.create();
		return result;
	}

	@Test
	void checkToString() {
		assertThat(memoize(constant(42)), hasToString("Factories.memoize(Factories.constant(42))"));
	}

	interface MyType {}
}
