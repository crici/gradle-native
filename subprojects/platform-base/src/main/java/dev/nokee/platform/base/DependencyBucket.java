/*
 * Copyright 2020-2021 the original author or authors.
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
package dev.nokee.platform.base;

import dev.nokee.model.internal.core.ModelNodes;
import dev.nokee.platform.base.internal.dependencies.DependencyBucketProjection;
import org.gradle.api.Action;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ModuleDependency;

public interface DependencyBucket {
	default String getName() {
		return ModelNodes.of(this).get(DependencyBucketProjection.class).getName();
	}

	default void addDependency(Object notation) {
		ModelNodes.of(this).get(DependencyBucketProjection.class).addDependency(notation);
	}

	default void addDependency(Object notation, Action<? super ModuleDependency> action) {
		ModelNodes.of(this).get(DependencyBucketProjection.class).addDependency(notation, action);
	}

	default Configuration getAsConfiguration() {
		return ModelNodes.of(this).get(Configuration.class);
	}
}
