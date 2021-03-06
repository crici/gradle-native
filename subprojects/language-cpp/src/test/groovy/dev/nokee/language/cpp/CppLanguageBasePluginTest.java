package dev.nokee.language.cpp;

import dev.nokee.language.base.internal.plugins.LanguageBasePlugin;
import dev.nokee.language.cpp.internal.plugins.CppLanguageBasePlugin;
import dev.nokee.language.nativebase.NativeHeaderSet;
import lombok.val;
import org.junit.jupiter.api.Test;
import spock.lang.Subject;

import static dev.gradleplugins.grava.testing.util.ProjectTestUtils.rootProject;
import static dev.nokee.scripts.testing.DefaultImporterMatchers.hasDefaultImportFor;
import static java.util.Collections.singletonMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Subject(CppLanguageBasePlugin.class)
class CppLanguageBasePluginTest {
	@Test
	void appliesLanguageBasePlugin() {
		val project = rootProject();
		project.apply(singletonMap("plugin", CppLanguageBasePlugin.class));
		assertTrue(project.getPlugins().hasPlugin(LanguageBasePlugin.class), "should apply language base plugin");
	}

	@Test
	void defaultImportSourceSetTypes() {
		val project = rootProject();
		project.apply(singletonMap("plugin", CppLanguageBasePlugin.class));
		assertThat(project, hasDefaultImportFor(CppSourceSet.class));
		assertThat(project, hasDefaultImportFor(CppHeaderSet.class));
		assertThat(project, hasDefaultImportFor(NativeHeaderSet.class));
	}
}
