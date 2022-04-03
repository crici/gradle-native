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
package dev.nokee.xcode.workspace;

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.Test;

import static dev.nokee.internal.testing.GradleNamedMatchers.named;
import static dev.nokee.xcode.workspace.WorkspaceSettings.AutoCreateSchemes.values;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.is;

class WorkspaceSettingsOption_AutoCreateSchemesTest {
	@Test
	void hasName() {
		assertThat(asList(values()), everyItem(named("IDEWorkspaceSharedSettings_AutocreateContextsIfNeeded")));
	}

	@Test
	@SuppressWarnings("UnstableApiUsage")
	void checkEquals() {
		new EqualsTester()
			.addEqualityGroup(WorkspaceSettings.AutoCreateSchemes.Enabled)
			.addEqualityGroup(WorkspaceSettings.AutoCreateSchemes.Disabled)
			.testEquals();
	}

	@Test
	void checkTrueValue() {
		assertThat(WorkspaceSettings.AutoCreateSchemes.Enabled.get(), is(true));
	}

	@Test
	void checkFalseValue() {
		assertThat(WorkspaceSettings.AutoCreateSchemes.Disabled.get(), is(false));
	}
}
