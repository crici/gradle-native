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

import dev.nokee.xcode.objects.PBXContainerItemProxy;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static dev.nokee.xcode.project.CodeablePBXContainerItemProxy.CodingKeys.containerPortal;
import static dev.nokee.xcode.project.CodeablePBXContainerItemProxy.CodingKeys.proxyType;
import static dev.nokee.xcode.project.CodeablePBXContainerItemProxy.CodingKeys.remoteGlobalIDString;
import static dev.nokee.xcode.project.CodeablePBXContainerItemProxy.CodingKeys.remoteInfo;
import static dev.nokee.xcode.project.PBXObjectMatchers.matchesObject;
import static dev.nokee.xcode.project.PBXObjectMatchers.matchesOptional;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CodeablePBXContainerItemProxyTests {
	@Mock
	KeyedObject map;
	@InjectMocks
	CodeablePBXContainerItemProxy subject;

	@ParameterizedTest
	@NullSource
	@MockitoSource(PBXContainerItemProxy.ContainerPortal.class)
	void checkGetContainerPortal(PBXContainerItemProxy.ContainerPortal expectedValue) {
		when(map.tryDecode(any())).thenReturn(expectedValue);
		assertThat(subject.getContainerPortal(), matchesObject(expectedValue));
		verify(map).tryDecode(containerPortal);
	}

	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {"407EB587257A418900686B1F"})
	void checkGetRemoteGlobalIDString(String expectedValue) {
		when(map.tryDecode(any())).thenReturn(expectedValue);
		assertThat(subject.getRemoteGlobalIDString(), matchesObject(expectedValue));
		verify(map).tryDecode(remoteGlobalIDString);
	}

	@ParameterizedTest
	@NullSource
	@EnumSource(PBXContainerItemProxy.ProxyType.class)
	void checkGetProxyType(PBXContainerItemProxy.ProxyType expectedValue) {
		when(map.tryDecode(any())).thenReturn(expectedValue);
		assertThat(subject.getProxyType(), matchesObject(expectedValue));
		verify(map).tryDecode(proxyType);
	}

	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {"FooBar"})
	void checkGetRemoteInfo(String expectedValue) {
		when(map.tryDecode(any())).thenReturn(expectedValue);
		assertThat(subject.getRemoteInfo(), matchesOptional(expectedValue));
		verify(map).tryDecode(remoteInfo);
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
}
