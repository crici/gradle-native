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
package dev.nokee.platform.nativebase.testers;

import dev.nokee.internal.testing.ConfigurationMatchers;
import dev.nokee.internal.testing.TaskMatchers;
import dev.nokee.platform.nativebase.SharedLibraryBinary;
import dev.nokee.platform.nativebase.tasks.LinkSharedLibrary;
import dev.nokee.platform.nativebase.tasks.internal.LinkSharedLibraryTask;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.attributes.Usage;
import org.gradle.api.provider.ListProperty;
import org.gradle.nativeplatform.toolchain.NativeToolChain;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static dev.nokee.internal.testing.FileSystemMatchers.aFile;
import static dev.nokee.internal.testing.FileSystemMatchers.withAbsolutePath;
import static dev.nokee.internal.testing.GradleNamedMatchers.named;
import static dev.nokee.internal.testing.GradleProviderMatchers.presentProvider;
import static dev.nokee.internal.testing.GradleProviderMatchers.providerOf;
import static dev.nokee.internal.testing.util.ProjectTestUtils.createDependency;
import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public abstract class SharedLibraryBinaryIntegrationTester implements SharedLibraryBinaryTester {
	public abstract SharedLibraryBinary subject();

	public abstract Project project();

	public abstract String variantName();

	public abstract String displayName();

	private Configuration linkLibraries() {
		return project().getConfigurations().getByName(variantName() + "LinkLibraries");
	}

	@Test
	void linkTasksPointsToTheExpectedTask() {
		assertThat(subject().getLinkTask(), providerOf(allOf(named("link" + capitalize(variantName())), isA(LinkSharedLibrary.class))));
	}

	@Nested
	class LinkTaskTest {
		public LinkSharedLibraryTask subject() {
			return (LinkSharedLibraryTask) project().getTasks().getByName("link" + capitalize(variantName()));
		}

		@Test
		void hasLinkerArgs() {
			assertThat("not null as per contract", subject().getLinkerArgs(), notNullValue(ListProperty.class));
			assertThat("there should be a value", subject().getLinkerArgs(), presentProvider());
		}

		@Test
		void hasDescription() {
			assertThat(subject(), TaskMatchers.description("Links the " + displayName() + "."));
		}
	}

	@Nested
	class LinkLibrariesConfigurationTest {
		public Configuration subject() {
			return linkLibraries();
		}

		@Test
		void hasDescription() {
			assertThat(subject(), ConfigurationMatchers.description("Link libraries for " + displayName() + "."));
		}

		@Test
		void isResolvable() {
			assertThat(subject(), ConfigurationMatchers.resolvable());
		}

		@Test
		void hasNativeLinkUsage() {
			assertThat(subject(), ConfigurationMatchers.attributes(hasEntry(is(Usage.USAGE_ATTRIBUTE), named("native-link"))));
		}
	}

	@Nested
	class RuntimeLibrariesConfigurationTest {
		public Configuration subject() {
			return project().getConfigurations().getByName(variantName() + "RuntimeLibraries");
		}

		@Test
		void hasDescription() {
			assertThat(subject(), ConfigurationMatchers.description("Runtime libraries for " + displayName() + "."));
		}

		@Test
		void isResolvable() {
			assertThat(subject(), ConfigurationMatchers.resolvable());
		}

		@Test
		void hasNativeRuntimeUsage() {
			assertThat(subject(), ConfigurationMatchers.attributes(hasEntry(is(Usage.USAGE_ATTRIBUTE), named("native-runtime"))));
		}
	}
}
