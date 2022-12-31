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
import dev.nokee.xcode.project.ValueDecoder;

public final class SourceTreeDecoder implements ValueDecoder<PBXSourceTree, String> {
	@Override
	public PBXSourceTree decode(String object, Context context) {
		return PBXSourceTree.of(object);
	}

	@Override
	public CoderType<?> getDecodeType() {
		return CoderType.of(PBXSourceTree.class);
	}
}