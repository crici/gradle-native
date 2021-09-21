/*
 * Copyright 2020 the original author or authors.
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

/**
 * A model node state listener.
 */
public interface ModelNodeListener {
	/**
	 * A listener that does nothing on all callback.
	 *
	 * @return a model node listener that perform no operation on callbacks, never null
	 */
	static ModelNodeListener noOpListener() {
		return new ModelNodeListener() {
			@Override
			public void created(ModelNode node) {
				// do nothing
			}

			@Override
			public void initialized(ModelNode modelNode) {
				// do nothing
			}

			@Override
			public void registered(ModelNode modelNode) {
				// do nothing
			}

			@Override
			public void realized(ModelNode node) {
				// do nothing
			}
		};
	}

	/**
	 * When the model node transition to {@link ModelNode.State#Created}.
	 *
	 * @param node  the model node that transitioned to the created state
	 */
	void created(ModelNode node);

	/**
	 * When the model node transition to {@link ModelNode.State#Initialized}.
	 *
	 * @param node  the model node that transitioned to the initialized state
	 */
	void initialized(ModelNode node);

	/**
	 * When the model node transition to {@link ModelNode.State#Registered}.
	 *
	 * @param node  the model node that transitioned to the registered state
	 */
	void registered(ModelNode node);

	/**
	 * When the model node transition to {@link ModelNode.State#Realized}.
	 *
	 * @param node  the model node that transitioned to the realized state
	 */
	void realized(ModelNode node);
}
