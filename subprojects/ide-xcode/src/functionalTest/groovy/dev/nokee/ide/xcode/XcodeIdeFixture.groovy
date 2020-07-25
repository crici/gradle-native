package dev.nokee.ide.xcode

trait XcodeIdeFixture {
	String getXcodeIdePluginId() {
		return 'dev.nokee.xcode-ide'
	}

	// Must be kotlin dsl compatible
	String applyXcodeIdePlugin() {
		return """
			plugins {
				id("${xcodeIdePluginId}")
			}
		"""
	}

	// Must be kotlin dsl compatible
	String configureXcodeIdeProject(String name, XcodeIdeProductType productType = XcodeIdeProductTypes.APPLICATION) {
		return """
			xcode {
				projects.register("${name}") {
					targets.register("${name.capitalize()}") {
						productReference.set("${name.capitalize()}.app")
						productType.set(productTypes.of("${productType.identifier}"))
						buildConfigurations.register("Default") {
							productLocation.set(layout.projectDirectory.dir("${name.capitalize()}.app"))
							buildSettings.put("PRODUCT_NAME", ideTarget.productName)
						}
					}
				}
			}
		"""
	}
}
