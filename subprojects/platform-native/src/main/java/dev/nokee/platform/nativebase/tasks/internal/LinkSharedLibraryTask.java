package dev.nokee.platform.nativebase.tasks.internal;

import dev.nokee.platform.nativebase.tasks.LinkSharedLibrary;
import org.gradle.api.file.RegularFile;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.CacheableTask;

@CacheableTask
public abstract class LinkSharedLibraryTask extends org.gradle.nativeplatform.tasks.LinkSharedLibrary implements LinkSharedLibrary, ObjectFilesToBinaryTask {
	@Override
	public Provider<RegularFile> getBinaryFile() {
		return getLinkedFile();
	}
}
