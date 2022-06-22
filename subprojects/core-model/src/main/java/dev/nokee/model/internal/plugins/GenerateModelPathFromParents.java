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
package dev.nokee.model.internal.plugins;

import dev.nokee.model.internal.core.ModelActionWithInputs;
import dev.nokee.model.internal.core.ModelNode;
import dev.nokee.model.internal.core.ModelPath;
import dev.nokee.model.internal.core.ModelPathComponent;
import dev.nokee.model.internal.core.ParentComponent;
import dev.nokee.model.internal.core.ParentUtils;
import dev.nokee.model.internal.names.ElementNameComponent;
import dev.nokee.utils.Optionals;
import lombok.val;

import java.util.Collections;
import java.util.stream.Collectors;

// ComponentFromEntity<ElementNameComponent> read-only
final class GenerateModelPathFromParents extends ModelActionWithInputs.ModelAction2<ParentComponent, ElementNameComponent> {
	@Override
	protected void execute(ModelNode entity, ParentComponent parent, ElementNameComponent elementName) {
		if (!entity.has(ModelPathComponent.class)) {
			val segments = ParentUtils.stream(parent).flatMap(it -> Optionals.stream(it.find(ElementNameComponent.class))).map(it -> it.get().toString()).collect(Collectors.toList());
			Collections.reverse(segments);
			segments.add(elementName.get().toString());
			segments.remove(0);
			entity.addComponent(new ModelPathComponent(ModelPath.path(segments)));
		}
	}
}
