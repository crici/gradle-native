package dev.nokee.ide.xcode

import dev.gradleplugins.integtests.fixtures.AbstractGradleSpecification
import spock.lang.Unroll

class XcodeIdeIndexerTargetsFunctionalTest extends AbstractGradleSpecification implements XcodeIdeFixture {
	@Unroll
	def "creates an indexer target for known product types (e.g. #productType)"(productType) {
		given:
		buildFile << applyXcodeIdePlugin() << configureXcodeIdeProject('foo', productType)

		when:
		succeeds('xcode')

		then:
		xcodeProject('foo').targets*.name == ['Foo', '__indexer_Foo']
		xcodeProject('foo').getTargetByName('__indexer_Foo').productType == 'dev.nokee.product-type.indexer'
		// TODO: Ensure each product type has the right build settings configured

		where:
		productType << (XcodeIdeProductTypes.getKnownValues() - [XcodeIdeProductTypes.UNIT_TEST, XcodeIdeProductTypes.UI_TEST])
	}

	def "does not create schemes for indexer target"() {
		given:
		buildFile << applyXcodeIdePlugin() << configureXcodeIdeProject('foo')

		when:
		succeeds('xcode')

		then:
		xcodeProject('foo').schemes*.name == ['Foo']
	}
}
