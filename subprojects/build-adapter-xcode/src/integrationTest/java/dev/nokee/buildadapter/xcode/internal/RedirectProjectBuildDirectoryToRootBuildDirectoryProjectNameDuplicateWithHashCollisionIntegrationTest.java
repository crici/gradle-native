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
package dev.nokee.buildadapter.xcode.internal;

import dev.nokee.buildadapter.xcode.internal.plugins.RedirectProjectBuildDirectoryToRootBuildDirectory;
import dev.nokee.buildadapter.xcode.internal.plugins.RedirectProjectBuildDirectoryToRootBuildDirectory.HashFunction;
import dev.nokee.internal.testing.util.ProjectTestUtils;
import net.nokeedev.testing.junit.jupiter.io.TestDirectory;
import net.nokeedev.testing.junit.jupiter.io.TestDirectoryExtension;
import org.gradle.api.Action;
import org.gradle.api.Project;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;

import static dev.nokee.internal.testing.FileSystemMatchers.aFile;
import static dev.nokee.internal.testing.FileSystemMatchers.withAbsolutePath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.when;

@ExtendWith({TestDirectoryExtension.class, MockitoExtension.class})
class RedirectProjectBuildDirectoryToRootBuildDirectoryProjectNameDuplicateWithHashCollisionIntegrationTest {
	static HashFunction algorithm = Mockito.mock(HashFunction.class);
	static Action<Project> subject = new RedirectProjectBuildDirectoryToRootBuildDirectory(algorithm);;
	@TestDirectory static Path testDirectory;
	static Project rootProject;
	static Project projectA;
	static Project projectB;
	static Project projectB_A;

	@BeforeAll
	static void setup() {
		rootProject = ProjectTestUtils.createRootProject(testDirectory);
		projectA = ProjectTestUtils.createChildProject(rootProject, "A");
		projectB = ProjectTestUtils.createChildProject(rootProject, "B");
		projectB_A = ProjectTestUtils.createChildProject(projectB, "A");

		when(algorithm.hash(":A[0]")).thenReturn(42L);
		when(algorithm.hash(":B[0]")).thenReturn(52L);
		when(algorithm.hash(":B:A[0]")).thenReturn(42L);
		when(algorithm.hash(":B:A[1]")).thenReturn(62L);
		subject.execute(rootProject);
	}

	@Test
	void doesNotUseSameBuildDirectoryBetweenAmbiguousProject() {
		assertThat(projectA.getBuildDir(), not(equalTo(projectB_A.getBuildDir())));
	}

	@Test
	void hasProject_A_BuildDirectory() {
		assertThat(projectA.getBuildDir(), aFile(withAbsolutePath(endsWith("/build/subprojects/A-16"))));
	}

	@Test
	void hasProject_B_BuildDirectory() {
		assertThat(projectB.getBuildDir(), aFile(withAbsolutePath(endsWith("/build/subprojects/B-1g"))));
	}

	@Test
	void hasProject_A_B_BuildDirectory() {
		assertThat(projectB_A.getBuildDir(), aFile(withAbsolutePath(endsWith("/build/subprojects/A-1q"))));
	}
}