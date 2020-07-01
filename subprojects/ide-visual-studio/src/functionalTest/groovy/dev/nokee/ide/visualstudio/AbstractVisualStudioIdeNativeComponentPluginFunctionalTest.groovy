package dev.nokee.ide.visualstudio

import dev.gradleplugins.integtests.fixtures.AbstractGradleSpecification
import dev.gradleplugins.test.fixtures.sources.SourceElement

abstract class AbstractVisualStudioIdeNativeComponentPluginFunctionalTest extends AbstractGradleSpecification/*AbstractXcodeIdeFunctionalSpec*/ {
	protected abstract void makeSingleProject();

	protected abstract SourceElement getComponentUnderTest();

	protected abstract String getProjectName();

	protected String getSchemeName() {
		return projectName
	}

	protected String getWorkspaceName() {
		return projectName
	}

	protected abstract List<String> getAllTasksForBuildAction()

	protected List<String> getAllTasksToXcode() {
		return [":${projectName}VisualStudioProject", ':visualStudioSolution', ':visualStudioz']
	}

	def "can generate Visual Studio IDE files"() {
		println testDirectory
		settingsFile << """
			rootProject.name = '${projectName}'
		"""
		makeSingleProject()
		componentUnderTest.writeToProject(testDirectory)

		when:
		succeeds('visualStudio')

		then:
		result.assertTasksExecuted(allTasksToXcode)
	}

//
//	@Requires({ SystemUtils.IS_OS_MAC })
//	def "can build from Xcode IDE"() {
//		useXcodebuildTool()
//		settingsFile << configurePluginClasspathAsBuildScriptDependencies() << """
//			rootProject.name = '${projectName}'
//		"""
//		makeSingleProject()
//		componentUnderTest.writeToProject(testDirectory)
//
//		when:
//		succeeds('xcode')
//
//		then:
//		result.assertTasksExecuted(allTasksToXcode)
//
//		and:
//		def result = xcodebuild.withWorkspace(xcodeWorkspace(workspaceName)).withScheme(schemeName).succeeds()
//		result.assertTasksExecuted(allTasksForBuildAction, ":_xcode___${projectName}_${schemeName}_Default")
//	}
//
//	@Requires({ SystemUtils.IS_OS_MAC })
//	def "can clean relocated xcode derived data relative to workspace"() {
//		useXcodebuildTool()
//		settingsFile << configurePluginClasspathAsBuildScriptDependencies() << """
//			rootProject.name = '${projectName}'
//		"""
//		makeSingleProject()
//		componentUnderTest.writeToProject(testDirectory)
//
//		when: 'can generate relocated Xcode derived data workspace'
//		succeeds('xcode')
//		then:
//		result.assertTasksExecuted(allTasksToXcode)
//		and:
//		xcodeWorkspace(workspaceName).assertDerivedDataLocationRelativeToWorkspace('.gradle/XcodeDerivedData')
//		and:
//		file('.gradle/XcodeDerivedData').assertDoesNotExist()
//
//		when: 'Xcode build inside relocated derived data'
//		xcodebuild.withWorkspace(xcodeWorkspace(workspaceName)).withScheme(schemeName).succeeds()
//		then:
//		file('.gradle/XcodeDerivedData').assertIsDirectory()
//
//		when: 'clean keeps Xcode derived data'
//		succeeds('clean')
//		then:
//		file('.gradle/XcodeDerivedData').assertIsDirectory()
//
//		when: 'clean Xcode deletes derived data'
//		succeeds('cleanXcode')
//		then:
//		file('.gradle/XcodeDerivedData').assertDoesNotExist()
//		file('.gradle').assertExists()
//	}
}
