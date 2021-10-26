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
package dev.nokee.platform.nativebase.internal.dependencies;

import dev.nokee.model.internal.core.ModelNode;
import dev.nokee.model.internal.core.ModelNodeAware;
import dev.nokee.model.internal.core.ModelNodeContext;
import dev.nokee.model.internal.core.ModelProperties;
import dev.nokee.platform.base.DependencyBucket;
import dev.nokee.platform.nativebase.NativeApplicationComponentDependencies;

public final class ModelBackedNativeApplicationComponentDependencies implements NativeApplicationComponentDependencies, ModelNodeAware {
	private final ModelNode node = ModelNodeContext.getCurrentModelNode();

	@Override
	public DependencyBucket getCompileOnly() {
		return ModelProperties.getProperty(this, "compileOnly").as(DependencyBucket.class).get();
	}

	@Override
	public DependencyBucket getImplementation() {
		return ModelProperties.getProperty(this, "implementation").as(DependencyBucket.class).get();
	}

	@Override
	public DependencyBucket getRuntimeOnly() {
		return ModelProperties.getProperty(this, "runtimeOnly").as(DependencyBucket.class).get();
	}

	@Override
	public DependencyBucket getLinkOnly() {
		return ModelProperties.getProperty(this, "linkOnly").as(DependencyBucket.class).get();
	}

	@Override
	public ModelNode getNode() {
		return node;
	}
}