package dev.nokee.docs

import dev.nokee.docs.fixtures.html.BakedHtmlFixture
import dev.nokee.docs.fixtures.html.HtmlTag
import groovy.json.JsonSlurper
import spock.lang.Specification

class ProperBakedHtmlTest extends Specification {
	def "has alt text on all images"() {
		expect:
		def fixture = new BakedHtmlFixture(new File(System.getProperty('bakedContentDirectory')).toPath())
		fixture.findAllHtml().collect { it.findAll(HtmlTag.IMG) }.each {
			it*.assertHasAltText()
		}
	}

	def "has proper canonical links"() {
		expect:
		def fixture = new BakedHtmlFixture(new File(System.getProperty('bakedContentDirectory')).toPath())
		fixture.findAllHtml().each {
			def canonicalLinks = it.findAll(HtmlTag.LINK).findAll { it.isCanonical() }
			assert canonicalLinks.size() == 1
			assert canonicalLinks.first().href.present
			assert canonicalLinks.first().href.get() == it.getCanonicalPath()
		}
	}

	def "has proper open-graph URL"() {
		expect:
		def fixture = new BakedHtmlFixture(new File(System.getProperty('bakedContentDirectory')).toPath())
		fixture.findAllHtml().each {
			def openGraphUrls = it.findAll(HtmlTag.META).findAll { it.openGraphUrl }
			assert openGraphUrls.size() == 1
			assert openGraphUrls.first().content == it.getCanonicalPath()
		}
	}

	def "has proper description"() {
		// It seems 160 characters is a good limit
		expect:
		def fixture = new BakedHtmlFixture(new File(System.getProperty('bakedContentDirectory')).toPath())
		fixture.findAllHtml().each {
			def metaDescriptions = it.findAll(HtmlTag.META).findAll { it.description }
			assert metaDescriptions.size() == 1, "${it.uri} does not have the right meta description tag count"
			assert metaDescriptions.first().content.size() > 0, "${it.uri} cannot have empty meta description tag"
			assert metaDescriptions.first().content.size() <= 160
		}
	}

	def "has proper keywords"() {
		expect:
		def fixture = new BakedHtmlFixture(new File(System.getProperty('bakedContentDirectory')).toPath())
		fixture.findAllHtml().each {
			def metaKeywords = it.findAll(HtmlTag.META).findAll { it.keywords }
			assert metaKeywords.size() == 1, "${it.uri} does not have the right meta keyword tag count"
			assert metaKeywords.first().content.size() > 0, "${it.uri} cannot have empty meta keyword tag"
			assert !metaKeywords.first().content.startsWith('[') && !metaKeywords.first().content.endsWith(']')
		}
	}

	def "has proper twitter meta description"() {
		expect:
		def fixture = new BakedHtmlFixture(new File(System.getProperty('bakedContentDirectory')).toPath())
		fixture.findAllHtml().each {
			def twitterDescriptions = it.findAll(HtmlTag.META).findAll { it.twitterDescription }
			assert twitterDescriptions.size() == 1, "${it.uri} does not have the right meta twitter description tag count"
			assert twitterDescriptions.first().content.size() > 0, "${it.uri} cannot have empty meta keyword tag"
			assert twitterDescriptions.first().content.size() <= 160
		}
	}

	def "has breadcrumb structured data"() {
		expect:
		def fixture = new BakedHtmlFixture(new File(System.getProperty('bakedContentDirectory')).toPath())
		fixture.allHtmlWithoutRedirection.each {
			// Retrieve structured data tag
			def scripts = it.findAll(HtmlTag.SCRIPT).findAll { it.type.present && it.type.get() == 'application/ld+json' }
			assert scripts.size() == 1, "${it.uri} does not have the right structured data tag count"

			// Retrieve structured data
			assert scripts.first().text.present
			def data = new JsonSlurper().parse(scripts.first().text.get().bytes)

			// Retrieve breadcrumb data
			def breadcrumb = data.findAll { it.'@type' == 'BreadcrumbList' }
			assert breadcrumb.size() == 1
			assert breadcrumb.first().itemListElement.size() == it.breadcrumbs.size()

			GroovyCollections.transpose(breadcrumb.first().itemListElement, it.breadcrumbs).eachWithIndex { obj, index ->
				def left = obj[0]
				assert left.'@type' == 'ListItem'
				assert left.position == (index + 1)

				def right = obj[1]
				// We are not asserting the name for now because it doesn't exactly align with the URL
				assert left.item == right.item
			}
		}
	}
}
