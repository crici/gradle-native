/*
 * Copyright 2023 the original author or authors.
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
package dev.nokee.xcode.buildsettings;

import dev.nokee.xcode.DefaultXCBuildSettingSearchContext;
import dev.nokee.xcode.XCBuildSettingsEmptyLayer;
import org.junit.jupiter.api.Test;

import static dev.nokee.xcode.buildsettings.XCBuildSettingTestUtils.nullBuildSetting;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anEmptyMap;

class XCBuildSettingsEmptyLayerTests {
	XCBuildSettingsEmptyLayer subject = new XCBuildSettingsEmptyLayer();

	@Test
	void alwaysReturnNullBuildSetting() {
		assertThat(subject.find(new DefaultXCBuildSettingSearchContext("MY_VAR")), nullBuildSetting("MY_VAR"));
	}

	@Test
	void hasNoBuildSettings() {
		assertThat(subject.findAll(), anEmptyMap());
	}
}