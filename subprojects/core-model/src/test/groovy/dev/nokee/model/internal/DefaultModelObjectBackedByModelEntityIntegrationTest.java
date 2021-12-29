/*
 * Copyright 2021 the original author or authors.
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
package dev.nokee.model.internal;

import dev.nokee.model.DomainObjectIdentifier;
import dev.nokee.model.DomainObjectProvider;
import dev.nokee.model.internal.core.ModelIdentifier;
import dev.nokee.model.internal.core.ModelNode;
import dev.nokee.model.internal.core.ModelProjections;
import dev.nokee.model.internal.registry.ModelConfigurer;
import dev.nokee.model.internal.state.ModelState;
import dev.nokee.model.internal.state.ModelStates;
import dev.nokee.model.internal.type.ModelType;
import dev.nokee.utils.ActionTestUtils;
import dev.nokee.utils.ClosureTestUtils;
import dev.nokee.utils.ProviderUtils;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import static dev.nokee.model.internal.core.ModelTestUtils.node;
import static dev.nokee.model.internal.type.ModelType.of;
import static dev.nokee.utils.TransformerTestUtils.aTransformer;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

class DefaultModelObjectBackedByModelEntityIntegrationTest implements ModelObjectTester<DefaultModelObjectBackedByModelEntityIntegrationTest.MyType> {
	private static final MyType myTypeInstance = Mockito.mock(MyType.class);
	private final ModelConfigurer modelConfigurer = Mockito.mock(ModelConfigurer.class);
	private final ModelNode node = newEntity(modelConfigurer);
	private final DefaultModelObject<MyType> subject = DefaultModelObject.of(of(MyType.class), node);

	private static ModelNode newEntity(ModelConfigurer modelConfigurer) {
		val entity = node("qibe", ModelProjections.createdUsing(of(MyType.class), () -> myTypeInstance), builder -> builder.withConfigurer(modelConfigurer));
		entity.addComponent(ModelIdentifier.of("qibe", Object.class));
		entity.addComponent(new FullyQualifiedNameComponent("testQibe"));
		return entity;
	}

	@Override
	public DomainObjectProvider<MyType> subject() {
		return subject;
	}

	@Override
	public ModelType<?> aKnownType() {
		return of(MyType.class);
	}

	private static void assertDoesNotChangeEntityState(ModelNode entity, Executable executable) {
		val expectedState = ModelStates.getState(entity);
		assertDoesNotThrow(executable);
		assertEquals(expectedState, ModelStates.getState(entity));
	}

	@Test
	void doesNotChangeEntityStateOnFlatMap() {
		assertDoesNotChangeEntityState(node, () -> subject.flatMap(aTransformer()));
	}

	@Test
	void realizesEntityWhenFlatMappedProviderQueried() {
		subject.flatMap(ProviderUtils::fixed).getOrNull();
		assertThat(ModelStates.getState(node), equalTo(ModelState.Realized));
	}

	@Test
	void doesNotChangeEntityStateOnMap() {
		assertDoesNotChangeEntityState(node, () -> subject.map(aTransformer()));
	}

	@Test
	void realizesEntityWhenMappedProviderQueried() {
		subject.map(it -> it).getOrNull();
		assertThat(ModelStates.getState(node), equalTo(ModelState.Realized));
	}

	@Test
	void realizesEntityWhenObjectQueried() {
		subject.get();
		assertThat(ModelStates.getState(node), equalTo(ModelState.Realized));
	}

	@Test
	void throwExceptionWhenCreatingKnownObjectWithWrongProjectionType() {
		val ex = assertThrows(IllegalArgumentException.class, () -> DefaultModelObject.of(of(WrongType.class), node));
		assertThat(ex.getMessage(),
			equalTo("node 'qibe' cannot be viewed as interface dev.nokee.model.internal.DefaultModelObjectBackedByModelEntityIntegrationTest$WrongType"));
	}

	@Test
	void throwsNullPointerExceptionWhenProjectionTypeIsNull() {
		assertThrows(NullPointerException.class, () -> DefaultModelObject.of(null, node));
	}

	@Test
	void throwsNullPointerExceptionWhenEntityIsNull() {
		assertThrows(NullPointerException.class, () -> DefaultModelObject.of(of(Object.class), null));
	}

	@Test
	void forwardsConfigureUsingActionToModelConfigurer() {
		subject.configure(ActionTestUtils.doSomething());
		verify(modelConfigurer).configure(any());
	}

	@Test
	void forwardsConfigureUsingClosureToModelConfigurer() {
		subject.configure(ClosureTestUtils.doSomething(Object.class));
		verify(modelConfigurer).configure(any());
	}

	@Test
	void returnsIdentifierFromEntity() {
		assertEquals(node.getComponent(DomainObjectIdentifier.class), subject.getIdentifier());
	}

	@Test
	void usesFullyQualifiedNamedComponentAsConfigurableProviderName() {
		assertEquals("testQibe", subject.asProvider().getName());
	}

	@Test
	void forwardsConfigureUsingActionOnConfigurableProviderToModelConfigurer() {
		subject.asProvider().configure(ActionTestUtils.doSomething());
		verify(modelConfigurer).configure(any());
	}

	// TODO: If projection is a Task asProvider should return a TaskProvider instead

	@Override
	public void isAutoConversionToProviderViaCallable() {
		// TODO: Provide implementation that returns a stable provider instance
	}

	interface MyType {}
	interface WrongType {}
}
