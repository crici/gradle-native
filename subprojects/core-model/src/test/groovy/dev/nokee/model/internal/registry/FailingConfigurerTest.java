package dev.nokee.model.internal.registry;

import org.junit.jupiter.api.Test;

import static dev.nokee.model.internal.core.ModelTestActions.doSomething;
import static dev.nokee.model.internal.registry.ModelConfigurer.failingConfigurer;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FailingConfigurerTest {
	@Test
	void throwsException() {
		assertThrows(UnsupportedOperationException.class, () -> failingConfigurer().configure(doSomething()));
	}
}
