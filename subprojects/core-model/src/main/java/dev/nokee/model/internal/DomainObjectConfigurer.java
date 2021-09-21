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
package dev.nokee.model.internal;

import dev.nokee.model.DomainObjectIdentifier;
import org.gradle.api.Action;

public interface DomainObjectConfigurer<T> {
	<S extends T> void configureEach(DomainObjectIdentifier owner, Class<S> type, Action<? super S> action);
	<S extends T> void configure(DomainObjectIdentifier owner, String name, Class<S> type, Action<? super S> action);
	<S extends T> void configure(TypeAwareDomainObjectIdentifier<S> identifier, Action<? super S> action);
	<S extends T> void whenElementKnown(DomainObjectIdentifier owner, Class<S> type, Action<? super TypeAwareDomainObjectIdentifier<S>> action);
}
