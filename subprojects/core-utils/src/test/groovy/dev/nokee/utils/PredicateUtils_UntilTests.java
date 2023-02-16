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
package dev.nokee.utils;

import dev.nokee.util.internal.NotPredicate;
import org.junit.jupiter.api.Test;

import static com.google.common.base.Predicates.instanceOf;
import static dev.nokee.utils.PredicateUtils.until;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class PredicateUtils_UntilTests {
	@Test
	void canCreateUntilInstanceOfPredicate() {
		assertThat(until(Object.class), equalTo(new NotPredicate<>(instanceOf(Object.class))));
	}
}
