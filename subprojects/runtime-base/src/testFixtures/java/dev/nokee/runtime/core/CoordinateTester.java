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
package dev.nokee.runtime.core;

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isA;

public interface CoordinateTester<T> {
	Coordinate<T> createSubject();

	@Test
	default void hasAxis() {
		assertThat(createSubject().getAxis().getType(), equalTo(axisType(this)));
	}

	@Test
	default void hasValue() {
		assertThat(createSubject().getValue(), isA(axisType(this)));
	}

	static Class<?> axisType(CoordinateTester<?> thiz) {
		return (Class<?>) ((ParameterizedType) thiz.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
	}
}
