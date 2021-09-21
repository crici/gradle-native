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
package dev.nokee.runtime.nativebase.internal;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasToString;

class WellKnownTargetLinkageInstancesTest {
	@Test
	void checkSharedLibraryLinkage() {
		assertThat(TargetLinkages.SHARED, hasToString("shared"));
	}

	@Test
	void checkStaticLibraryLinkage() {
		assertThat(TargetLinkages.STATIC, hasToString("static"));
	}

	@Test
	void checkBundleLinkage() {
		assertThat(TargetLinkages.BUNDLE, hasToString("bundle"));
	}

	@Test
	void checkExecutableLinkage() {
		assertThat(TargetLinkages.EXECUTABLE, hasToString("executable"));
	}
}
