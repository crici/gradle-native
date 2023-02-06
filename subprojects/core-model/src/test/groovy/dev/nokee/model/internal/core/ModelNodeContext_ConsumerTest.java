/*
 * Copyright 2020-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.nokee.model.internal.core;

import dev.nokee.internal.testing.testdoubles.Captor;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static dev.nokee.internal.testing.invocations.InvocationMatchers.calledOnce;
import static dev.nokee.internal.testing.invocations.InvocationMatchers.calledOnceWith;
import static dev.nokee.internal.testing.invocations.InvocationMatchers.withCaptured;
import static dev.nokee.internal.testing.reflect.MethodInformation.method;
import static dev.nokee.internal.testing.testdoubles.Answers.doThrow;
import static dev.nokee.internal.testing.testdoubles.MockitoBuilder.any;
import static dev.nokee.internal.testing.testdoubles.MockitoBuilder.newMock;
import static dev.nokee.internal.testing.testdoubles.TestDouble.callTo;
import static dev.nokee.internal.testing.testdoubles.TestDoubleTypes.ofConsumer;
import static dev.nokee.internal.testing.testdoubles.TestDoubleTypes.ofFunction;
import static dev.nokee.model.internal.core.ModelTestUtils.node;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ModelNodeContext_ConsumerTest {
	private final ModelNode node = node("x.y.z");
	private final ModelNodeContext subject = ModelNodeContext.of(node);

	@Test
	void calledWithCurrentModelNode() {
		assertThat(newMock(ofFunction(ModelNode.class, ModelNode.class)).executeWith(subject::execute)
			.to(method(Function<ModelNode, ModelNode>::apply)), calledOnceWith(node));
	}

	@Test
	void canAccessCurrentModelNodeWhileExecutingInContext() {
		val consumer = newMock(ofConsumer(Object.class)).when(any(callTo(method(Consumer<Object>::accept))).capture(ModelNodeContext::getCurrentModelNode)).executeWith(subject::execute);
		assertThat(consumer.to(method(Consumer<Object>::accept)), calledOnce(withCaptured(node)));
	}

	@Test
	void cannotAccessCurrentModelNodeAfterContextIsExecution() {
		newMock(ofConsumer(Object.class)).executeWith(subject::execute);
		assertThrows(NullPointerException.class, ModelNodeContext::getCurrentModelNode);
	}

	@Test
	void cannotAccessCurrentModelNodeWhenExceptionThrowDuringContextExecution() {
		assertThrows(RuntimeException.class,
			() -> newMock(ofConsumer(Object.class)).when(any(callTo(method(Consumer<Object>::accept))).then(doThrow(new RuntimeException("Expected exception")))).executeWith(subject::execute));
		assertThrows(NullPointerException.class, ModelNodeContext::getCurrentModelNode);
	}

	@Test
	void canExecuteNestedContext() {
		val nestedNode = node("a.b.c");
		val consumer = newMock(ofConsumer(Object.class)).when(any(callTo(method(Consumer<Object>::accept))).capture(new NestedModelNodeContextCaptor(nestedNode))).executeWith(subject::execute);
		assertThat(consumer.to(method(Consumer<Object>::accept)), calledOnce(withCaptured(contains(node, nestedNode, node))));
	}

	static class NestedModelNodeContextCaptor implements Captor<List<ModelNode>> {
		private final ModelNode nestedNode;

		NestedModelNodeContextCaptor(ModelNode nestedNode) {
			this.nestedNode = nestedNode;
		}

		@Override
		public List<ModelNode> capture() {
			val allValues = new ArrayList<ModelNode>();
			allValues.add(ModelNodeContext.getCurrentModelNode());
			ModelNodeContext.of(nestedNode).execute(modelNode -> {
				allValues.add(ModelNodeContext.getCurrentModelNode());
			});
			allValues.add(ModelNodeContext.getCurrentModelNode());
			return allValues;
		}
	}
}
