/*
 * Copyright 2022 the original author or authors.
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
package dev.nokee.xcode.objects.swiftpackage;

import dev.nokee.xcode.objects.swiftpackage.XCRemoteSwiftPackageReference.VersionRequirement.UpToNextMajorVersion;
import org.junit.jupiter.api.Test;

import static dev.nokee.xcode.objects.swiftpackage.XCRemoteSwiftPackageReference.VersionRequirement.Kind.UP_TO_NEXT_MAJOR_VERSION;
import static dev.nokee.xcode.objects.swiftpackage.XCRemoteSwiftPackageReference.VersionRequirement.upToNextMajorVersion;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;

class XCRemoteSwiftPackageReferenceVersionRequirementUpToNextMajorVersionTest {
	UpToNextMajorVersion subject = upToNextMajorVersion("8.5");

	@Test
	void hasKind() {
		assertThat(subject.getKind(), equalTo(UP_TO_NEXT_MAJOR_VERSION));
	}

	@Test
	void hasMinimumVersion() {
		assertThat(subject.getMinimumVersion(), equalTo("8.5"));
	}

	@Test
	void checkToString() {
		assertThat(subject, hasToString("require minimum version '8.5' up to next major version"));
	}
}
