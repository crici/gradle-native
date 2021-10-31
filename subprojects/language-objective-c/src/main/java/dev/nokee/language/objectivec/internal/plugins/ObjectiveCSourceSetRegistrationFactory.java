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
package dev.nokee.language.objectivec.internal.plugins;

import dev.nokee.language.base.internal.LanguageSourceSetIdentifier;
import dev.nokee.language.base.internal.LanguageSourceSetRegistrationFactory;
import dev.nokee.language.base.internal.ModelBackedLanguageSourceSetLegacyMixIn;
import dev.nokee.language.nativebase.internal.AttachHeaderSearchPathsToCompileTaskRule;
import dev.nokee.language.nativebase.internal.HeaderSearchPathsConfigurationRegistrationActionFactory;
import dev.nokee.language.nativebase.internal.HeadersPropertyRegistrationActionFactory;
import dev.nokee.language.nativebase.internal.NativeCompileTaskRegistrationActionFactory;
import dev.nokee.language.objectivec.ObjectiveCSourceSet;
import dev.nokee.language.objectivec.internal.tasks.ObjectiveCCompileTask;
import dev.nokee.language.objectivec.tasks.ObjectiveCCompile;
import dev.nokee.model.internal.core.ModelRegistration;
import lombok.val;
import org.gradle.language.objectivec.internal.DefaultObjectiveCSourceSet;

public final class ObjectiveCSourceSetRegistrationFactory {
	private final LanguageSourceSetRegistrationFactory sourceSetRegistrationFactory;
	private final HeadersPropertyRegistrationActionFactory headersPropertyFactory;
	private final HeaderSearchPathsConfigurationRegistrationActionFactory resolvableHeadersRegistrationFactory;
	private final NativeCompileTaskRegistrationActionFactory compileTaskRegistrationFactory;

	public ObjectiveCSourceSetRegistrationFactory(LanguageSourceSetRegistrationFactory sourceSetRegistrationFactory, HeadersPropertyRegistrationActionFactory headersPropertyFactory, HeaderSearchPathsConfigurationRegistrationActionFactory resolvableHeadersRegistrationFactory, NativeCompileTaskRegistrationActionFactory compileTaskRegistrationFactory) {
		this.sourceSetRegistrationFactory = sourceSetRegistrationFactory;
		this.headersPropertyFactory = headersPropertyFactory;
		this.resolvableHeadersRegistrationFactory = resolvableHeadersRegistrationFactory;
		this.compileTaskRegistrationFactory = compileTaskRegistrationFactory;
	}

	public ModelRegistration create(LanguageSourceSetIdentifier identifier) {
		return create(identifier, true);
	}

	public ModelRegistration create(LanguageSourceSetIdentifier identifier, boolean isLegacy) {
		val builder = sourceSetRegistrationFactory.create(identifier, ObjectiveCSourceSet.class, DefaultObjectiveCSourceSet.class);
		if (!isLegacy) {
			builder.action(headersPropertyFactory.create(identifier))
				.action(compileTaskRegistrationFactory.create(identifier, ObjectiveCCompile.class, ObjectiveCCompileTask.class))
				.action(resolvableHeadersRegistrationFactory.create(identifier))
				.action(new AttachHeaderSearchPathsToCompileTaskRule(identifier));
		}
		return builder.build();
	}

	public static class DefaultObjectiveCSourceSet implements ObjectiveCSourceSet, ModelBackedLanguageSourceSetLegacyMixIn<ObjectiveCSourceSet> {}
}