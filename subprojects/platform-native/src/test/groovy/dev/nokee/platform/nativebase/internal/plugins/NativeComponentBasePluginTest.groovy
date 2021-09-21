/*
 * Copyright 2020-2021 the original author or authors.
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
package dev.nokee.platform.nativebase.internal.plugins

import dev.gradleplugins.grava.testing.util.ProjectTestUtils
import dev.nokee.platform.base.internal.plugins.ComponentModelBasePlugin
import spock.lang.Specification

class NativeComponentBasePluginTest extends Specification {
	def project = ProjectTestUtils.rootProject()

	def "applies component model base plugin"() {
		when:
		project.apply plugin: NativeComponentBasePlugin

		then:
		project.plugins.hasPlugin(ComponentModelBasePlugin)
	}

	// TODO: Find another way to assert this
//	def "registers native application component"() {
//		when:
//		project.apply plugin: NativeComponentBasePlugin
//
//		then:
//		project.extensions.getByType(ComponentInstantiator).creatableTypes.contains(DefaultNativeApplicationComponent)
//	}

	// TODO: Find another way to assert this
//	def "registers native library component"() {
//		when:
//		project.apply plugin: NativeComponentBasePlugin
//
//		then:
//		project.extensions.getByType(ComponentInstantiator).creatableTypes.contains(DefaultNativeLibraryComponent)
//	}
}
