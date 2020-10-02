package dev.nokee.platform.jni.internal;

import dev.nokee.platform.base.Binary;
import org.gradle.api.file.RegularFile;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.api.tasks.bundling.Jar;

public abstract class AbstractJarBinary implements Binary {
	private final TaskProvider<Jar> jarTask;

	public AbstractJarBinary(TaskProvider<Jar> jarTask) {
		this.jarTask = jarTask;
	}

	public TaskProvider<Jar> getJarTask() {
		return jarTask;
	}

	public Provider<RegularFile> getArchiveFile() {
		return this.jarTask.flatMap(Jar::getArchiveFile);
	}
}
