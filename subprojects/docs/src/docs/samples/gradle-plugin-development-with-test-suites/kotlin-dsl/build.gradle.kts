plugins {
	id("dev.gradleplugins.java-gradle-plugin") version("1.4.2")
	id("dev.gradleplugins.gradle-plugin-unit-test") version("1.4.2")
	id("dev.gradleplugins.gradle-plugin-functional-test") version("1.4.2")
	id("groovy-base") // for Spock testing
}

gradlePlugin {
	plugins {
		create("helloWorld") {
			id = "com.example.hello"
			implementationClass = "com.example.BasicPlugin"
		}
	}
}

repositories {
	mavenCentral()
}

test {
	dependencies {
		implementation(platform("org.spockframework:spock-bom:2.0-groovy-2.5"))
		implementation("org.spockframework:spock-core")
	}
	testTasks.configureEach { useJUnitPlatform() }
}

functionalTest {
	dependencies {
		implementation(platform("org.spockframework:spock-bom:2.0-groovy-2.5"))
		implementation("org.spockframework:spock-core")
		implementation(gradleTestKit())
	}
	testTasks.configureEach { useJUnitPlatform() }
}
