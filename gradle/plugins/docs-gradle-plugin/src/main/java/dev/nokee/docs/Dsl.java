package dev.nokee.docs;

public enum Dsl {
	GROOVY_DSL("groovy-dsl", "gradle", PluginManagementBlock.asGroovyDsl()), KOTLIN_DSL("kotlin-dsl", "gradle.kts", PluginManagementBlock.asKotlinDsl());

	private final String name;
	private final String extension;
	private final PluginManagementBlock pluginManagementBlock;

	Dsl(String name, String extension, PluginManagementBlock pluginManagementBlock) {
		this.name = name;
		this.extension = extension;
		this.pluginManagementBlock = pluginManagementBlock;
	}

	public String getSettingsFileName() {
		return "settings." + extension;
	}

	public PluginManagementBlock getSettingsPluginManagement() {
		return pluginManagementBlock;
	}
}
