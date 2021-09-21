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

import dev.nokee.internal.testing.Assumptions;

class CoordinateAxisFactory_OfTypeAndNameFactoryMethodTest implements CoordinateAxisTester<TestAxis>, CoordinateAxisFactoryTester {
	@Override
	public <T> CoordinateAxis<T> createSubject(Class<T> type) {
		return Assumptions.skipCurrentTestExecution("Testing CoordinateAxis.of(Class, String) factory method.");
	}

	@Override
	public <T> CoordinateAxis<T> createSubject(Class<T> type, String name) {
		return CoordinateAxis.of(type, name);
	}

	@Override
	public CoordinateAxis<TestAxis> createSubject() {
		return CoordinateAxis.of(TestAxis.class, "test");
	}
}
