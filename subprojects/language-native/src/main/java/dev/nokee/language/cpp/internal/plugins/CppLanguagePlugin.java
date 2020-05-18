package dev.nokee.language.cpp.internal.plugins;

import dev.nokee.platform.nativebase.internal.plugins.DomainKnowledgeToolchainsRules;
import dev.nokee.platform.nativebase.internal.plugins.NativePlatformCapabilitiesMarkerPlugin;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.nativeplatform.toolchain.internal.plugins.StandardToolChainsPlugin;

public class CppLanguagePlugin implements Plugin<Project> {
	@Override
	public void apply(Project project) {
		project.getPluginManager().apply(StandardToolChainsPlugin.class);
		project.getPluginManager().apply(NativePlatformCapabilitiesMarkerPlugin.class);
		project.getPluginManager().apply(DomainKnowledgeToolchainsRules.class);
	}
}
