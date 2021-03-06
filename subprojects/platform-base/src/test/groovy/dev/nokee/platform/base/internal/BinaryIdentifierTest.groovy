package dev.nokee.platform.base.internal

import dev.nokee.model.internal.ProjectIdentifier
import dev.nokee.platform.base.Binary
import dev.nokee.platform.base.Component
import dev.nokee.platform.base.Variant
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification
import spock.lang.Subject

@Subject(BinaryIdentifier)
class BinaryIdentifierTest extends Specification {
	def "can create identifier owned by variant"() {
		given:
		def projectIdentifier = ProjectIdentifier.of('root')
		def componentIdentifier = ComponentIdentifier.ofMain(Component, projectIdentifier)
		def variantIdentifier = VariantIdentifier.of('debug', Variant, componentIdentifier)

		when:
		def result = BinaryIdentifier.of(BinaryName.of('foo'), TestableBinary, variantIdentifier)

		then:
		result.name.get() == 'foo'
		result.type == TestableBinary
		result.ownerIdentifier == variantIdentifier
		result.parentIdentifier.present
		result.parentIdentifier.get() == variantIdentifier
	}

	def "can create identifier owned by component"() {
		given:
		def projectIdentifier = ProjectIdentifier.of('root')
		def componentIdentifier = ComponentIdentifier.ofMain(Component, projectIdentifier)

		when:
		def result = BinaryIdentifier.of(BinaryName.of('foo'), TestableBinary, componentIdentifier)

		then:
		result.name.get() == 'foo'
		result.type == TestableBinary
		result.ownerIdentifier == componentIdentifier
		result.parentIdentifier.present
		result.parentIdentifier.get() == componentIdentifier
	}

	def "throws exception if binary name is null"() {
		given:
		def projectIdentifier = ProjectIdentifier.of('root')
		def componentIdentifier = ComponentIdentifier.ofMain(Component, projectIdentifier)
		def variantIdentifier = VariantIdentifier.of('debug', Variant, componentIdentifier)

		when:
		BinaryIdentifier.of(null, TestableBinary, variantIdentifier)

		then:
		def ex = thrown(IllegalArgumentException)
		ex.message == 'Cannot construct a binary identifier because the task name is null.'
	}

	def "throws exception if type is null"() {
		given:
		def projectIdentifier = ProjectIdentifier.of('root')
		def componentIdentifier = ComponentIdentifier.ofMain(Component, projectIdentifier)
		def variantIdentifier = VariantIdentifier.of('debug', Variant, componentIdentifier)

		when:
		BinaryIdentifier.of(BinaryName.of('foo'), null, variantIdentifier)

		then:
		def ex = thrown(IllegalArgumentException)
		ex.message == 'Cannot construct a binary identifier because the task type is null.'
	}

	def "throws exception if owner is null"() {
		when:
		BinaryIdentifier.of(BinaryName.of('foo'), TestableBinary, null)

		then:
		def ex = thrown(IllegalArgumentException)
		ex.message == 'Cannot construct a task identifier because the owner identifier is null.'
	}

	def "throws exception if owner is anything other then component or variant"() {
		given:
		def projectIdentifier = ProjectIdentifier.of('root')

		when:
		BinaryIdentifier.of(BinaryName.of('foo'), TestableBinary, projectIdentifier)

		then:
		def ex = thrown(IllegalArgumentException)
		ex.message == 'Cannot construct a task identifier because the owner identifier is invalid, only ComponentIdentifier and VariantIdentifier are accepted.'
	}

	def "has meaningful toString() implementation"() {
		given:
		def rootProject = ProjectBuilder.builder().withName('foo').build()
		def childProject = ProjectBuilder.builder().withName('bar').withParent(rootProject).build()

		and:
		def ownerProject = ProjectIdentifier.of(childProject)
		def ownerComponent = ComponentIdentifier.ofMain(Component, ownerProject)
		def ownerVariant = VariantIdentifier.of('macosRelease', Variant, ownerComponent)

		expect:
		BinaryIdentifier.of(BinaryName.of('bar'), TestableBinary, ownerComponent).toString() == "binary ':bar:main:bar' (${TestableBinary.simpleName})"
		BinaryIdentifier.of(BinaryName.of('jar'), TestableBinary, ownerVariant).toString() == "binary ':bar:main:macosRelease:jar' (${TestableBinary.simpleName})"
	}

	interface TestableBinary extends Binary {}
}
