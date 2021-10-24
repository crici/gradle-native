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
package dev.nokee.model.internal.core;

import com.google.common.collect.ImmutableList;
import dev.nokee.model.internal.type.ModelType;

final class DelegatedModelProjection implements ModelProjection {
	private final ModelNode delegate;

	public DelegatedModelProjection(ModelNode delegate) {
		this.delegate = delegate;
	}

	public ModelNode getDelegate() {
		return delegate;
	}

	@Override
	public ModelType<?> getType() {
		// TODO: Consider throwing an exception here
		return ModelNodeUtils.getProjections(delegate).findFirst().orElseThrow(RuntimeException::new).getType();
	}

	@Override
	public <T> boolean canBeViewedAs(ModelType<T> type) {
		return ModelNodeUtils.canBeViewedAs(delegate, type);
	}

	@Override
	public <T> T get(ModelType<T> type) {
		return ModelNodeUtils.get(delegate, type);
	}

	@Override
	public Iterable<String> getTypeDescriptions() {
		return ImmutableList.of(ModelNodeUtils.getTypeDescription(delegate).orElse("<unknown>"));
	}
}
