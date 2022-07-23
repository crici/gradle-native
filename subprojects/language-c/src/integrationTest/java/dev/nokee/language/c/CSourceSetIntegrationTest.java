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
package dev.nokee.language.c;

import dev.nokee.internal.testing.AbstractPluginTest;
import dev.nokee.internal.testing.PluginRequirement;
import dev.nokee.internal.testing.junit.jupiter.Subject;
import dev.nokee.language.base.testers.HasCompileTaskTester;
import dev.nokee.language.base.testers.HasConfigurableSourceTester;
import dev.nokee.language.base.testers.LanguageSourceSetHasBuildableCompileTaskIntegrationTester;
import dev.nokee.language.base.testers.LanguageSourceSetHasBuildableSourceIntegrationTester;
import dev.nokee.language.base.testers.LanguageSourceSetTester;
import dev.nokee.language.c.internal.plugins.CSourceSetSpec;
import dev.nokee.language.c.tasks.CCompile;
import dev.nokee.language.nativebase.HasConfigurableHeadersTester;
import dev.nokee.language.nativebase.LanguageSourceSetHasBuildableHeadersIntegrationTester;
import dev.nokee.language.nativebase.LanguageSourceSetHasCompiledHeaderSearchPathsIntegrationTester;
import dev.nokee.language.nativebase.LanguageSourceSetHasCompiledHeadersIntegrationTester;
import dev.nokee.language.nativebase.LanguageSourceSetHasCompiledSourceIntegrationTester;
import dev.nokee.language.nativebase.LanguageSourceSetNativeCompileTaskIntegrationTester;
import dev.nokee.model.DomainObjectProvider;
import dev.nokee.model.internal.registry.ModelRegistry;
import dev.nokee.model.testers.HasPublicTypeTester;
import org.gradle.api.artifacts.Configuration;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static dev.nokee.internal.testing.GradleNamedMatchers.named;
import static dev.nokee.internal.testing.GradleProviderMatchers.providerOf;
import static dev.nokee.platform.base.internal.DomainObjectEntities.newEntity;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isA;

@PluginRequirement.Require(id = "dev.nokee.c-language-base")
class CSourceSetIntegrationTest extends AbstractPluginTest implements LanguageSourceSetTester
	, HasPublicTypeTester<CSourceSet>
	, HasConfigurableSourceTester
	, HasConfigurableHeadersTester
	, HasCompileTaskTester
	, LanguageSourceSetHasBuildableSourceIntegrationTester<CSourceSetSpec>
	, LanguageSourceSetHasBuildableHeadersIntegrationTester<CSourceSetSpec>
	, LanguageSourceSetHasBuildableCompileTaskIntegrationTester<CSourceSetSpec>
	, LanguageSourceSetHasCompiledSourceIntegrationTester<CSourceSetSpec>
	, LanguageSourceSetHasCompiledHeadersIntegrationTester<CSourceSetSpec>
	, LanguageSourceSetNativeCompileTaskIntegrationTester<CSourceSetSpec>
	, LanguageSourceSetHasCompiledHeaderSearchPathsIntegrationTester<CSourceSetSpec>
{
	@Subject DomainObjectProvider<CSourceSetSpec> subject;

	DomainObjectProvider<CSourceSetSpec> createSubject() {
		return project.getExtensions().getByType(ModelRegistry.class).register(newEntity("nopu", CSourceSetSpec.class)).as(CSourceSetSpec.class);
	}

	@Override
	public CSourceSetSpec subject() {
		return subject.get();
	}

	@Override
	public Configuration headerSearchPaths() {
		return subject.element("headerSearchPaths", Configuration.class).get();
	}

	@Test
	public void hasName() {
		assertThat(subject(), named("nopu"));
	}

	@Test
	void hasToString() {
		assertThat(subject(), Matchers.hasToString("C sources 'nopu'"));
	}

	@Test
	public void hasCompileTask() {
		assertThat(subject().getCompileTask(), providerOf(isA(CCompile.class)));
	}
}
