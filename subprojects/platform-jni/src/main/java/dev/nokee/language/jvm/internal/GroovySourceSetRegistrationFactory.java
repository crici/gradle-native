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
package dev.nokee.language.jvm.internal;

import dev.nokee.language.base.internal.HasConfigurableSourceMixIn;
import dev.nokee.language.base.internal.LanguageSourceSetIdentifier;
import dev.nokee.language.base.internal.ModelBackedLanguageSourceSetLegacyMixIn;
import dev.nokee.language.jvm.GroovySourceSet;
import dev.nokee.model.internal.core.ModelComponent;
import dev.nokee.model.internal.core.ModelElements;
import dev.nokee.model.internal.core.ModelRegistration;
import dev.nokee.utils.TaskDependencyUtils;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.internal.plugins.DslObject;
import org.gradle.api.reflect.HasPublicType;
import org.gradle.api.reflect.TypeOf;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.TaskDependency;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.api.tasks.compile.GroovyCompile;
import org.gradle.api.tasks.util.PatternFilterable;

import static dev.nokee.utils.TaskDependencyUtils.of;

public final class GroovySourceSetRegistrationFactory {
	public ModelRegistration create(LanguageSourceSetIdentifier identifier) {
		assert identifier.getName().get().equals("groovy");
		return ModelRegistration.managedBuilder(identifier, DefaultGroovySourceSet.class)
			.withComponent(JvmSourceSetTag.tag())
			.withComponent(DefaultGroovySourceSet.Tag.tag())
			.build();
	}

	public static SourceDirectorySet asSourceDirectorySet(SourceSet sourceSet) {
		return ((org.gradle.api.tasks.GroovySourceSet) new DslObject(sourceSet).getConvention().getPlugins().get("groovy")).getGroovy();
	}

	public static class DefaultGroovySourceSet implements GroovySourceSet, HasPublicType, ModelBackedLanguageSourceSetLegacyMixIn<GroovySourceSet>, HasConfigurableSourceMixIn {
		public TaskProvider<GroovyCompile> getCompileTask() {
			return (TaskProvider<GroovyCompile>) ModelElements.of(this).element("compile", GroovyCompile.class).asProvider();
		}

		@Override
		public GroovySourceSet from(Object... paths) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFrom(Object... paths) {
			throw new UnsupportedOperationException();
		}

		@Override
		public PatternFilterable getFilter() {
			throw new UnsupportedOperationException();
		}

		@Override
		public GroovySourceSet convention(Object... path) {
			throw new UnsupportedOperationException();
		}

		@Override
		public TaskDependency getBuildDependencies() {
			return TaskDependencyUtils.composite(getSource().getBuildDependencies(), of(getCompileTask()));
		}

		@Override
		public TypeOf<?> getPublicType() {
			return TypeOf.typeOf(GroovySourceSet.class);
		}

		public static final class Tag implements ModelComponent {
			private static final Tag INSTANCE = new Tag();

			public static Tag tag() {
				return INSTANCE;
			}
		}
	}
}
