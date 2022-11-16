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
package dev.nokee.xcode.project.coders;

import com.google.common.collect.ImmutableMap;
import dev.nokee.xcode.project.Decoder;
import dev.nokee.xcode.project.Encoder;
import dev.nokee.xcode.project.ValueCoder;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DictionaryCoderTests {
	@InjectMocks DictionaryCoder subject;

	@Nested
	class WhenDecoding {
		@Mock Decoder decoder;

		@Test
		void canDecode() {
			when(decoder.decodeDictionary()).thenReturn(ImmutableMap.of("K1", "V1", "K2", "V2"));
			assertThat(subject.decode(decoder), equalTo(ImmutableMap.of("K1", "V1", "K2", "V2")));
		}
	}

	@Nested
	class WhenEncoding {
		@Mock Encoder encoder;

		@Test
		void canEncode() {
			subject.encode(ImmutableMap.of("K1", "V1", "K2", "V2"), encoder);
			verify(encoder).encodeDictionary(ImmutableMap.of("K1", "V1", "K2", "V2"));
		}
	}
}