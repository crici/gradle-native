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

import dev.nokee.xcode.objects.files.PBXSourceTree;
import dev.nokee.xcode.project.ValueEncoder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class SourceTreeEncoderTests {
	ValueEncoder.Context context = new ThrowingEncoderContext();
	SourceTreeEncoder subject = new SourceTreeEncoder();

	@ParameterizedTest
	@ArgumentsSource(KnownSourceTreeProvider.class)
	void canEncodeKnownSourceTree(PBXSourceTree knownSourceTree) {
		assertThat(subject.encode(knownSourceTree, context), equalTo(knownSourceTree.toString()));
	}

	@Test
	void canEncodeAdhocSourceTree() {
		assertThat(subject.encode(PBXSourceTree.of("MY_CUSTOM_PATH"), context), equalTo("MY_CUSTOM_PATH"));
	}

	@Test
	void hasEncodeType() {
		assertThat(subject.getEncodeType(), equalTo(CoderType.of(PBXSourceTree.class)));
	}
}