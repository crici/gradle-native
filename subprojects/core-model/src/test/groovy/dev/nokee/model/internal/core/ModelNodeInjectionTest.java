package dev.nokee.model.internal.core;

import lombok.val;
import org.gradle.api.plugins.ExtensionAware;
import org.junit.jupiter.api.Test;
import spock.lang.Subject;

import static dev.gradleplugins.grava.testing.util.ProjectTestUtils.objectFactory;
import static dev.nokee.model.internal.core.ModelNodeContext.injectCurrentModelNodeIfAllowed;
import static dev.nokee.model.internal.core.ModelNodeContext.of;
import static dev.nokee.model.internal.core.ModelTestUtils.node;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

@Subject(ModelNodeContext.class)
class ModelNodeInjectionTest {
	private final ModelNode node = node("x.y.z");

	@Test
	void includesModelNodeAsExtensionIfInstanceIsExplicitlyExtensionAware() {
		val instance = of(node).execute(node -> {
			return injectCurrentModelNodeIfAllowed(objectFactory().newInstance(ExplicitExtensionAwareType.class));
		});
		assertThat(instance.getExtensions().findByType(ModelNode.class), equalTo(node));
	}

	interface ExplicitExtensionAwareType extends ExtensionAware {}

	@Test
	void includesModelNodeAsExtensionIfInstanceIsImplicitlyExtensionAware() {
		val instance = of(node).execute(node -> {
			return injectCurrentModelNodeIfAllowed(objectFactory().newInstance(ImplicitExtensionAwareType.class));
		});
		assertThat(((ExtensionAware) instance).getExtensions().findByType(ModelNode.class), equalTo(node));
	}

	// All type created via ObjectFactory is implicitly ExtensionAware
	interface ImplicitExtensionAwareType {}

	@Test
	void doesNothingIfInstanceIsNotExtensionAware() {
		val instance = mock(NotExtensionAwareType.class);
		injectCurrentModelNodeIfAllowed(instance);
		verifyNoInteractions(instance);
	}

	static class NotExtensionAwareType {}

	@Test
	void doesNothingIfNoModelNodePresentInContext() {
		val instance = mock(ExplicitExtensionAwareType.class);
		injectCurrentModelNodeIfAllowed(instance);
		verifyNoInteractions(instance);
	}
}
