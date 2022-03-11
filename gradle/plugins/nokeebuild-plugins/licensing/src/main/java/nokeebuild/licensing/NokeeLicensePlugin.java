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
package nokeebuild.licensing;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

import javax.inject.Inject;

abstract /*final*/ class NokeeLicensePlugin implements Plugin<Project> {
	@Inject
	public NokeeLicensePlugin() {}

	@Override
	public void apply(Project project) {
		project.getExtensions().create(LicenseExtension.class, "license", NokeeLicenseExtension.class);
		project.getPluginManager().apply(LicenseBasePlugin.class);
	}
}
