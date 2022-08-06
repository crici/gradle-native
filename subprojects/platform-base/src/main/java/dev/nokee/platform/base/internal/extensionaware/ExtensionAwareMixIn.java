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
package dev.nokee.platform.base.internal.extensionaware;

import dev.nokee.model.internal.core.ModelNodes;
import dev.nokee.model.internal.tags.ModelTag;
import dev.nokee.platform.base.internal.DomainObjectEntities;
import org.gradle.api.plugins.ExtensionAware;
import org.gradle.api.plugins.ExtensionContainer;

@DomainObjectEntities.Tag(ExtensionAwareMixIn.Tag.class)
public interface ExtensionAwareMixIn extends ExtensionAware {
	@Override
	default ExtensionContainer getExtensions() {
		return ModelNodes.of(this).get(ExtensionAwareComponent.class).get();
	}

	interface Tag extends ModelTag {}
}
