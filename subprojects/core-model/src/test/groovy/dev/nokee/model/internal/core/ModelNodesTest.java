package dev.nokee.model.internal.core;

import com.google.common.testing.NullPointerTester;
import dev.gradleplugins.grava.testing.util.ProjectTestUtils;
import dev.nokee.model.internal.type.ModelType;
import lombok.val;
import org.gradle.api.plugins.ExtensionAware;
import org.junit.jupiter.api.Test;

import static dev.nokee.model.internal.core.ModelNodes.inject;
import static dev.nokee.model.internal.core.ModelNodes.of;
import static dev.nokee.model.internal.core.ModelTestUtils.node;
import static dev.nokee.model.internal.type.ModelType.untyped;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ModelNodesTest {
	@Test
	void canAccessModelNodeFromExtensibleAware() {
		assertThat("model node is accessible on decorated objects", of(decoratedExtensionAwareObjectWithModelNode()), isA(ModelNode.class));
	}

	@Test
	void canAccessModelNodeFromModelNodeAware() {
		assertThat("model node is accessible on decorated objects", of(decoratedModelNodeAwareObjectWithModelNode()), isA(ModelNode.class));
	}

	@Test
	void throwsExceptionIfObjectIsNotExtensibleAware() {
		assertThrows(IllegalArgumentException.class, () -> of(object()), "cannot get model node for non-ExtensibleAware types");
	}

	@Test
	void throwsExceptionIfObjectIsNotDecoratedWithModelNode() {
		assertThrows(IllegalArgumentException.class, () -> of(undecoratedObject()), "cannot get model node if not present");
	}

	@Test
	void canInjectModelNode() {
		val node = node("a.b.c");
		assertEquals(node, of(inject(undecoratedObject(), node)), "should be able to inject model node in ExtensibleAware types");
	}

	@Test
	@SuppressWarnings("UnstableApiUsage")
	void checkNulls() {
		new NullPointerTester()
			.setDefault(ModelNode.class, node())
			.setDefault(ModelType.class, untyped())
			.testAllPublicStaticMethods(ModelNodes.class);
	}

	private static Object decoratedExtensionAwareObjectWithModelNode() {
		val node = node("a.b.c");
		val object = ProjectTestUtils.objectFactory().newInstance(MyType.class);
		((ExtensionAware) object).getExtensions().add(ModelNode.class, "__NOKEE_modelNode", node);
		return object;
	}

	private static Object decoratedModelNodeAwareObjectWithModelNode() {
		val node = node("a.b.c");
		val object = ProjectTestUtils.objectFactory().newInstance(MyType.class);
		((ExtensionAware) object).getExtensions().add(ModelNode.class, "__NOKEE_modelNode", node);
		return new ModelNodeAware() {
			@Override
			public ModelNode getNode() {
				return node;
			}
		};
	}

	private static Object object() {
		return new Object();
	}

	private static Object undecoratedObject() {
		return ProjectTestUtils.objectFactory().newInstance(MyType.class);
	}

	interface MyType {}
}
