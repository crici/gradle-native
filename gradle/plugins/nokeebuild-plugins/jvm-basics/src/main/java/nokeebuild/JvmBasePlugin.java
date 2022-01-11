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
package nokeebuild;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.SourceSetContainer;

import javax.inject.Inject;

import static nokeebuild.UseLombok.lombokVersion;

abstract /*final*/ class JvmBasePlugin implements Plugin<Project> {
	@Inject
	public JvmBasePlugin() {}

	@Override
	public void apply(Project project) {
		project.getPluginManager().withPlugin("java-base", ignored -> {
			project.getPluginManager().apply("nokeebuild.repositories");
			sourceSets(project).configureEach(new UseLombok(project, lombokVersion(project)));
		});
	}

	private static SourceSetContainer sourceSets(Project project) {
		return project.getExtensions().getByType(SourceSetContainer.class);
	}
}
