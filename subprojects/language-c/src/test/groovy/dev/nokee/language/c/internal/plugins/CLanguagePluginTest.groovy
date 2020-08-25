package dev.nokee.language.c.internal.plugins


import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification
import spock.lang.Subject

@Subject(CLanguagePlugin.class)
class CLanguagePluginTest extends Specification {
	def project = ProjectBuilder.builder().build()

	def "applies base plugin"() {
		when:
		project.apply plugin: CLanguagePlugin

		then:
		project.plugins.hasPlugin(CLanguageBasePlugin)
	}
}
