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
package dev.nokee.xcode.project;

import dev.nokee.xcode.objects.swiftpackage.XCRemoteSwiftPackageReference.VersionRequirement.Kind;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static dev.nokee.xcode.project.CodeableVersionRequirementUpToNextMajorVersion.CodingKeys.kind;
import static dev.nokee.xcode.project.CodeableVersionRequirementUpToNextMajorVersion.CodingKeys.minimumVersion;
import static dev.nokee.xcode.project.PBXObjectMatchers.matchesObject;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasToString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CodeableVersionRequirementUpToNextMajorVersionTests {
	@Mock
	KeyedObject map;
	@InjectMocks
	CodeableVersionRequirementUpToNextMajorVersion subject;

	@ParameterizedTest
	@NullSource
	@EnumSource(value = Kind.class, names = "UP_TO_NEXT_MAJOR_VERSION")
	void checkGetKind(Kind expectedValue) {
		when(map.tryDecode(any())).thenReturn(expectedValue);
		assertThat(subject.getKind(), matchesObject(expectedValue));
		verify(map).tryDecode(kind);
	}

	@ParameterizedTest
	@NullSource
	@ValueSource(strings = "1.2.3")
	void checkGetMinimumVersion(String expectedValue) {
		when(map.tryDecode(any())).thenReturn(expectedValue);
		assertThat(subject.getMinimumVersion(), matchesObject(expectedValue));
		verify(map).tryDecode(minimumVersion);
	}

	@Test
	void forwardsEncodingToDelegate() {
		Codeable.EncodeContext context = mock(Codeable.EncodeContext.class);
		subject.encode(context);
		verify(map).encode(context);
	}

	@Test
	void forwardsIsaToDelegate() {
		subject.isa();
		verify(map).isa();
	}

	@Test
	void forwardsGlobalIdToDelegate() {
		subject.globalId();
		verify(map).globalId();
	}

	@Test
	void forwardsTryDecodeToDelegate() {
		CodingKey key = mock(CodingKey.class);
		subject.tryDecode(key);
		verify(map).tryDecode(key);
	}

	@Test
	void checkToString() {
		when(map.tryDecode(minimumVersion)).thenReturn("1.3");
		assertThat(subject, hasToString("require minimum version '1.3' up to next major version"));
	}
}
