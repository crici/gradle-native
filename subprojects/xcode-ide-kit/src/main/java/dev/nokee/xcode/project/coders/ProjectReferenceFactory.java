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

import dev.nokee.xcode.objects.PBXProject;
import dev.nokee.xcode.project.Codeable;
import dev.nokee.xcode.project.CodeableObjectFactory;
import dev.nokee.xcode.project.CodeableProjectReference;
import dev.nokee.xcode.project.KeyedObject;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public final class ProjectReferenceFactory<T extends PBXProject.ProjectReference & Codeable> implements CodeableObjectFactory<T> {
	@Override
	@SuppressWarnings("unchecked")
	public T create(KeyedObject map) {
		assert map.isa() == null;
		return (T) new CodeableProjectReference(map);
	}
}
