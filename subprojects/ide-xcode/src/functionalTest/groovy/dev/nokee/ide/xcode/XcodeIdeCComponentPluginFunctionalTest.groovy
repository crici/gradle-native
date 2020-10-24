package dev.nokee.ide.xcode

import dev.gradleplugins.fixtures.sources.SourceElement
import dev.nokee.language.c.CTaskNames
import dev.nokee.platform.jni.fixtures.CGreeter
import dev.nokee.platform.nativebase.fixtures.CGreeterApp
import dev.nokee.platform.nativebase.fixtures.CGreeterLib
import dev.nokee.platform.nativebase.fixtures.CGreeterTest
import dev.nokee.testing.nativebase.NativeTestSuite
import org.junit.Assume

class XcodeIdeCApplicationFunctionalTest extends AbstractXcodeIdeNativeComponentPluginFunctionalTest implements CTaskNames {
	@Override
	protected void makeSingleProject() {
		buildFile << """
			plugins {
				id 'dev.nokee.c-application'
				id 'dev.nokee.xcode-ide'
			}
		"""
	}

	@Override
	protected SourceElement getComponentUnderTest() {
		return new CGreeterApp()
	}

	@Override
	protected String getProjectName() {
		return "app"
	}
}

class XcodeIdeCApplicationWithNativeTestSuiteFunctionalTest extends AbstractXcodeIdeNativeComponentPluginFunctionalTest implements CTaskNames {
	@Override
	protected void makeSingleProject() {
		makeSingleProjectWithoutSources()
		new CGreeterApp().writeToProject(testDirectory)
	}

	@Override
	protected void makeSingleProjectWithoutSources() {
		buildFile << """
			plugins {
				id 'dev.nokee.c-application'
				id 'dev.nokee.xcode-ide'
				id 'dev.nokee.native-unit-testing'
			}

			import ${NativeTestSuite.canonicalName}

			testSuites {
				test(${NativeTestSuite.simpleName}) {
					testedComponent application
				}
			}
		"""
	}

	@Override
	protected SourceElement getComponentUnderTest() {
		return new CGreeterTest()
	}

	@Override
	protected String configureCustomSourceLayout() {
		Assume.assumeTrue(false)
		return super.configureCustomSourceLayout()
	}

	@Override
	protected String getProjectName() {
		return "app"
	}

	@Override
	protected String getGroupName() {
		return "app-test"
	}
}

class XcodeIdeCLibraryFunctionalTest extends AbstractXcodeIdeNativeComponentPluginFunctionalTest implements CTaskNames {
	@Override
	protected void makeSingleProject() {
		buildFile << """
			plugins {
				id 'dev.nokee.c-library'
				id 'dev.nokee.xcode-ide'
			}
		"""
	}

	@Override
	protected SourceElement getComponentUnderTest() {
		return new CGreeter().asLib()
	}

	@Override
	protected String getProjectName() {
		return "lib"
	}
}

class XcodeIdeCLibraryWithNativeTestSuiteFunctionalTest extends AbstractXcodeIdeNativeComponentPluginFunctionalTest implements CTaskNames {
	@Override
	protected void makeSingleProject() {
		makeSingleProjectWithoutSources()
		new CGreeterLib().writeToProject(testDirectory)
	}

	@Override
	protected void makeSingleProjectWithoutSources() {
		buildFile << """
			plugins {
				id 'dev.nokee.c-library'
				id 'dev.nokee.xcode-ide'
				id 'dev.nokee.native-unit-testing'
			}

			import ${NativeTestSuite.canonicalName}

			testSuites {
				test(${NativeTestSuite.simpleName}) {
					testedComponent library
				}
			}
		"""
	}

	@Override
	protected SourceElement getComponentUnderTest() {
		return new CGreeterTest()
	}

	@Override
	protected String configureCustomSourceLayout() {
		Assume.assumeTrue(false)
		return super.configureCustomSourceLayout()
	}

	@Override
	protected String getProjectName() {
		return 'lib'
	}

	protected String getGroupName() {
		return 'lib-test'
	}
}

class XcodeIdeCLibraryWithStaticLinkageFunctionalTest extends XcodeIdeCLibraryFunctionalTest {
	@Override
	protected void makeSingleProject() {
		super.makeSingleProject()
		buildFile << """
			library {
				targetLinkages = [linkages.static]
			}
		"""
	}
}

class XcodeIdeCLibraryWithSharedLinkageFunctionalTest extends XcodeIdeCLibraryFunctionalTest {
	@Override
	protected void makeSingleProject() {
		super.makeSingleProject()
		buildFile << """
			library {
				targetLinkages = [linkages.shared]
			}
		"""
	}
}

class XcodeIdeCLibraryWithBothLinkageFunctionalTest extends XcodeIdeCLibraryFunctionalTest {
	@Override
	protected void makeSingleProject() {
		super.makeSingleProject()
		buildFile << """
			library {
				targetLinkages = [linkages.static, linkages.shared]
			}
		"""
	}

	@Override
	protected String getSchemeName() {
		return "${super.schemeName}Shared"
	}
}
