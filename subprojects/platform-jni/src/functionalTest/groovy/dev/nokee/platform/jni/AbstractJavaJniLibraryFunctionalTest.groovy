package dev.nokee.platform.jni

import dev.gradleplugins.integtests.fixtures.nativeplatform.RequiresInstalledToolChain
import dev.gradleplugins.integtests.fixtures.nativeplatform.ToolChainRequirement
import dev.nokee.language.c.CTaskNames
import dev.nokee.language.cpp.CppTaskNames
import dev.nokee.language.objectivec.ObjectiveCTaskNames
import dev.nokee.language.objectivecpp.ObjectiveCppTaskNames
import dev.nokee.platform.jni.fixtures.JavaJniCGreeterLib
import dev.nokee.platform.jni.fixtures.JavaJniCppGreeterLib
import dev.nokee.platform.jni.fixtures.JavaJniObjectiveCGreeterLib
import dev.nokee.platform.jni.fixtures.JavaJniObjectiveCppGreeterLib
import spock.lang.Requires
import spock.util.environment.OperatingSystem

import static org.hamcrest.CoreMatchers.containsString

abstract class AbstractJavaJniLibraryFunctionalTest extends AbstractJniLibraryFunctionalTest {
	def "generate JNI headers when compiling Java source code"() {
		makeSingleProject()
		componentUnderTest.writeToProject(testDirectory)

		when:
		file('build/generated/jni-headers').assertDoesNotExist()
		succeeds('compileJava')

		then:
		file('build/generated/jni-headers').assertHasDescendants('com_example_greeter_Greeter.h')
	}

	def "build fails when Java compilation fails"() {
		given:
		makeSingleProject()

		and:
		file("src/main/java/broken.java") << "broken!"

		expect:
		fails "assemble"
		failure.assertHasDescription("Execution failed for task ':compileJava'.")
		failure.assertHasCause("Compilation failed; see the compiler error output for details.")
	}

	protected List<String> getExpectedClasses() {
		return ['com/example/greeter/Greeter.class', 'com/example/greeter/NativeLoader.class']
	}

	@Override
	protected String getJvmPluginId() {
		return 'java'
	}
}

class JavaCJniLibraryFunctionalTest extends AbstractJavaJniLibraryFunctionalTest implements CTaskNames {
	@Override
	protected void makeSingleProject() {
		buildFile << '''
            plugins {
                id 'java'
                id 'dev.nokee.jni-library'
                id 'dev.nokee.c-language'
            }
        '''
		settingsFile << "rootProject.name = 'jni-greeter'"
	}

	@Override
	JavaJniCGreeterLib getComponentUnderTest() {
		return new JavaJniCGreeterLib('jni-greeter')
	}

	def "build fails when C compilation fails"() {
		given:
		makeSingleProject()

		and:
		file("src/main/c/broken.c") << "broken!"

		expect:
		fails "assemble"
		failure.assertHasDescription("Execution failed for task '${tasks.forSharedLibrary.compile}'.")
		failure.assertHasCause("A build operation failed.")
		failure.assertThatCause(containsString("C compiler failed while compiling broken.c"))
	}

	@Override
	protected String getNativePluginId() {
		return 'dev.nokee.c-language'
	}
}

class JavaCppJniLibraryFunctionalTest extends AbstractJavaJniLibraryFunctionalTest implements CppTaskNames {
	def "build fails when C++ compilation fails"() {
		given:
		makeSingleProject()

		and:
		file("src/main/cpp/broken.cpp") << "broken!"

		expect:
		fails "assemble"
		failure.assertHasDescription("Execution failed for task '${tasks.forSharedLibrary.compile}'.")
		failure.assertHasCause("A build operation failed.")
		failure.assertThatCause(containsString("C++ compiler failed while compiling broken.cpp"))
	}


//    def "can compile and link against a library"() {
// TODO: JNI Plugin should allow to work alone by embedding the runtime native dependencies into a JAR. It should also create an empty JAR when no language (not even Java). This Jar should be exposed via API outgoing dependencies.

	@Override
	protected void makeSingleProject() {
		buildFile << '''
            plugins {
                id 'java'
                id 'dev.nokee.jni-library'
                id 'dev.nokee.cpp-language'
            }
        '''
		settingsFile << "rootProject.name = 'jni-greeter'"
	}

	@Override
	JavaJniCppGreeterLib getComponentUnderTest() {
		return new JavaJniCppGreeterLib('jni-greeter')
	}

	@Override
	protected String getNativePluginId() {
		return 'dev.nokee.cpp-language'
	}
}

@RequiresInstalledToolChain(ToolChainRequirement.GCC_COMPATIBLE)
@Requires({!OperatingSystem.current.windows})
class JavaObjectiveCJniLibraryFunctionalTest extends AbstractJavaJniLibraryFunctionalTest implements ObjectiveCTaskNames {
	def "build fails when Objective-C compilation fails"() {
		given:
		makeSingleProject()

		and:
		file("src/main/objc/broken.m") << "broken!"

		expect:
		fails "assemble"
		failure.assertHasDescription("Execution failed for task '${tasks.forSharedLibrary.compile}'.")
		failure.assertHasCause("A build operation failed.")
		failure.assertThatCause(containsString("Objective-C compiler failed while compiling broken.m"))
	}


//	def "can compile and link against a library"() {
// TODO: JNI Plugin should allow to work alone by embedding the runtime native dependencies into a JAR. It should also create an empty JAR when no language (not even Java). This Jar should be exposed via API outgoing dependencies.

	@Override
	protected void makeSingleProject() {
		buildFile << '''
			plugins {
				id 'java'
				id 'dev.nokee.jni-library'
				id 'dev.nokee.objective-c-language'
			}

			library.variants.configureEach {
				sharedLibrary.linkTask.configure {
					linkerArgs.add('-lobjc')
				}
			}
		'''
		settingsFile << "rootProject.name = 'jni-greeter'"
	}

	@Override
	JavaJniObjectiveCGreeterLib getComponentUnderTest() {
		return new JavaJniObjectiveCGreeterLib('jni-greeter')
	}

	@Override
	protected String getNativePluginId() {
		return 'dev.nokee.objective-c-language'
	}
}

@RequiresInstalledToolChain(ToolChainRequirement.GCC_COMPATIBLE)
@Requires({!OperatingSystem.current.windows})
class JavaObjectiveCppJniLibraryFunctionalTest extends AbstractJavaJniLibraryFunctionalTest implements ObjectiveCppTaskNames {
	def "build fails when Objective-C++ compilation fails"() {
		given:
		makeSingleProject()

		and:
		file("src/main/objcpp/broken.mm") << "broken!"

		expect:
		fails "assemble"
		failure.assertHasDescription("Execution failed for task '${tasks.forSharedLibrary.compile}'.")
		failure.assertHasCause("A build operation failed.")
		failure.assertThatCause(containsString("Objective-C++ compiler failed while compiling broken.mm"))
	}


//	def "can compile and link against a library"() {
// TODO: JNI Plugin should allow to work alone by embedding the runtime native dependencies into a JAR. It should also create an empty JAR when no language (not even Java). This Jar should be exposed via API outgoing dependencies.

	@Override
	protected void makeSingleProject() {
		buildFile << '''
			plugins {
				id 'java'
				id 'dev.nokee.jni-library'
				id 'dev.nokee.objective-cpp-language'
			}

			library.variants.configureEach {
				sharedLibrary.linkTask.configure {
					linkerArgs.add('-lobjc')
				}
			}
		'''
		settingsFile << "rootProject.name = 'jni-greeter'"
	}

	@Override
	JavaJniObjectiveCppGreeterLib getComponentUnderTest() {
		return new JavaJniObjectiveCppGreeterLib('jni-greeter')
	}

	@Override
	protected String getNativePluginId() {
		return 'dev.nokee.objective-cpp-language'
	}
}
