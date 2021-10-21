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
package dev.nokee.testing.nativebase;

import dev.nokee.internal.testing.AbstractPluginTest;
import dev.nokee.internal.testing.PluginRequirement;
import dev.nokee.platform.base.testers.ComponentTester;
import dev.nokee.platform.base.testers.DependencyAwareComponentTester;
import dev.nokee.platform.nativebase.NativeComponentDependencies;
import dev.nokee.testing.base.TestSuiteContainer;
import org.junit.jupiter.api.BeforeEach;

@PluginRequirement.Require(id = "dev.nokee.native-unit-testing")
class NativeTestSuiteTest extends AbstractPluginTest implements ComponentTester<NativeTestSuite>, DependencyAwareComponentTester<NativeComponentDependencies> {
	private NativeTestSuite subject;

	@BeforeEach
	void createSubject() {
		subject = project().getExtensions().getByType(TestSuiteContainer.class).register("feme", NativeTestSuite.class).get();
	}

	@Override
	public NativeTestSuite subject() {
		return subject;
	}
}