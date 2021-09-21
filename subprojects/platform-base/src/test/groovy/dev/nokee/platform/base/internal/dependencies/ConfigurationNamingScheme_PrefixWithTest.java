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
package dev.nokee.platform.base.internal.dependencies;

import com.google.common.testing.EqualsTester;
import com.google.common.testing.NullPointerTester;
import org.junit.jupiter.api.Test;
import spock.lang.Subject;

import static dev.nokee.platform.base.internal.dependencies.ConfigurationNamingScheme.identity;
import static dev.nokee.platform.base.internal.dependencies.ConfigurationNamingScheme.prefixWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Subject(ConfigurationNamingScheme.class)
class ConfigurationNamingScheme_PrefixWithTest {
	@Test
	void capitalizeNameBeforePrefixing() {
		assertThat(prefixWith("foo").configurationName("bar"), equalTo("fooBar"));
		assertThat(prefixWith("foo").configurationName("far"), equalTo("fooFar"));
		assertThat(prefixWith("bar").configurationName("far"), equalTo("barFar"));
	}

	@Test
	void appendOnlyWhenConfigurationNameAlreadyCapitalized() {
		assertThat(prefixWith("foo").configurationName("Bar"), equalTo("fooBar"));
		assertThat(prefixWith("foo").configurationName("Far"), equalTo("fooFar"));
		assertThat(prefixWith("bar").configurationName("Far"), equalTo("barFar"));
	}

	@Test
	void returnsIdentityNamingSchemeForEmptyPrefix() {
		assertThat(prefixWith(""), equalTo(identity()));
	}

	@Test
	@SuppressWarnings("UnstableApiUsage")
	void checkNulls() {
		new NullPointerTester().testAllPublicInstanceMethods(prefixWith("foo"));
	}

	@Test
	@SuppressWarnings("UnstableApiUsage")
	void checkEquals() {
		new EqualsTester()
			.addEqualityGroup(prefixWith("foo"), prefixWith("foo"))
			.addEqualityGroup(prefixWith("bar"))
			.addEqualityGroup(identity())
			.testEquals();
	}
}
