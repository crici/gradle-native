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
package dev.nokee.language.nativebase;

import dev.nokee.internal.testing.util.ProjectTestUtils;
import dev.nokee.language.base.ConfigurableSourceSet;
import dev.nokee.language.base.LanguageSourceSet;
import dev.nokee.language.base.testers.LanguageSourceSetIntegrationTester;
import dev.nokee.language.nativebase.tasks.NativeSourceCompile;
import dev.nokee.model.internal.core.ModelElements;
import dev.nokee.model.internal.core.ModelProperties;
import lombok.val;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.attributes.LibraryElements;
import org.gradle.api.attributes.Usage;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Set;

import static dev.nokee.internal.testing.FileSystemMatchers.aFile;
import static dev.nokee.internal.testing.GradleProviderMatchers.providerOf;
import static dev.nokee.internal.testing.util.ProjectTestUtils.createDependency;
import static dev.nokee.internal.testing.util.ProjectTestUtils.objectFactory;
import static dev.nokee.utils.ConfigurationUtils.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

public abstract class NativeLanguageSourceSetIntegrationTester<T extends LanguageSourceSet> extends LanguageSourceSetIntegrationTester<T> {
	public abstract T subject();

	public abstract Project project();

	public abstract String name();

	public abstract String variantName();

	private Configuration headerSearchPaths() {
		return project().getConfigurations().getByName(variantName() + "HeaderSearchPaths");
	}

	private ConfigurableSourceSet headers() {
		return ModelProperties.getProperty(subject(), "headers")
			.as(ConfigurableSourceSet.class).get();
	}

	@Test
	@SuppressWarnings("unchecked")
	void includesHeadersBuildDependenciesInSourceSetBuildDependencies() {
		val anyTask = mock(Task.class);
		val buildTask = project().getTasks().create("buildSomeHeaders");
		headers().from(project().getObjects().fileCollection().from("foo.txt").builtBy(buildTask));
		assertThat((Set<Task>) subject().getBuildDependencies().getDependencies(anyTask), hasItem(buildTask));
	}

	@Nested
	class NativeCompileTaskTest implements NativeCompileTaskTester, NativeCompileTaskObjectFilesTester<NativeSourceCompile> {
		public NativeSourceCompile subject() {
			return ModelElements.of(NativeLanguageSourceSetIntegrationTester.this.subject()).element("compile", NativeSourceCompile.class).get();
		}

		@Override
		public String languageSourceSetName() {
			return name();
		}

		@Test
		void linksHeaderSourcePathsConfigurationToCompileTaskIncludes() throws IOException {
			val hdrs = Files.createTempDirectory("hdrs").toFile();
			headerSearchPaths().getDependencies().add(createDependency(objectFactory().fileCollection().from(hdrs)));
			assertThat(subject().getHeaderSearchPaths(), providerOf(hasItem(aFile(hdrs))));
		}

		@Test
		void includesSourceSetHeaderFilesInCompileTaskHeaders() throws IOException {
			val path = Files.createTempDirectory("headers").toFile();
			headers().from(path);
			assertThat(subject().getHeaderSearchPaths(), providerOf(hasItem(aFile(path))));
		}

		@Test
		void linksHeaderSourcePathsConfigurationToCompileTaskAsFrameworkCompileArguments() throws IOException {
			val artifact = Files.createTempDirectory("Kuqo.framework").toFile();
			val frameworkProducer = ProjectTestUtils.createChildProject(project());
			frameworkProducer.getConfigurations().create("apiElements",
				configureAsConsumable()
					.andThen(configureAttributes(forUsage(project().getObjects().named(Usage.class, Usage.C_PLUS_PLUS_API))))
					.andThen(configureAttributes(it -> it.attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE,
						project().getObjects().named(LibraryElements.class, "framework-bundle"))))
					.andThen(it -> it.getOutgoing().artifact(artifact, t -> t.setType("framework")))
			);

			headerSearchPaths().getDependencies().add(createDependency(frameworkProducer));
			assertThat(subject().getHeaderSearchPaths(), providerOf(allOf(not(hasItem(aFile(artifact))), not(hasItem(aFile(artifact.getParentFile()))))));
			assertThat(subject().getCompilerArgs(), providerOf(containsInRelativeOrder(
				"-F", artifact.getParentFile().getAbsolutePath()
				)));
		}
	}
}
