package dev.nokee.utils;

import com.google.common.testing.EqualsTester;
import lombok.val;
import org.gradle.api.Action;
import org.gradle.api.artifacts.Configuration;
import org.junit.jupiter.api.Test;

import static dev.nokee.internal.testing.utils.ConfigurationTestUtils.testConfiguration;
import static dev.nokee.utils.ConfigurationUtils.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConfigurationUtils_AsDeclarableTest {
	@Test
	void canConfigureConfigurationAsDeclarableBucket()  {
		val configuration = testConfiguration(asDeclarable());
		assertThat("should not be consumable", configuration.isCanBeConsumed(), equalTo(false));
		assertThat("should not be resolvable", configuration.isCanBeResolved(), equalTo(false));
	}

	@Test
	@SuppressWarnings("UnstableApiUsage")
	void checkEquals() {
		new EqualsTester()
			.addEqualityGroup(asDeclarable(), asDeclarable())
			.addEqualityGroup(asResolvable())
			.addEqualityGroup(asConsumable())
			.addEqualityGroup((Action<Configuration>) it -> {})
			.testEquals();
	}

	@Test
	void checkToString() {
		assertThat(asDeclarable(), hasToString("ConfigurationUtils.asDeclarable()"));
	}
}
