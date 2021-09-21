/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.nokee.buildadapter.cmake.internal.tasks;

import dev.nokee.core.exec.CommandLine;
import dev.nokee.core.exec.CommandLineTool;
import dev.nokee.core.exec.ProcessBuilderEngine;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;

public abstract class CMakeMakeAdapterTask extends DefaultTask {
	@Input
	public abstract Property<String> getTargetName();

	@Internal
	public abstract Property<CommandLineTool> getMakeTool();

	@Internal
	public abstract DirectoryProperty getWorkingDirectory();

	@OutputFile
	public abstract RegularFileProperty getBuiltFile();

	@TaskAction
	private void doExec() {
		getMakeTool().get().withArguments(getTargetName().get()).newInvocation().workingDirectory(getWorkingDirectory().get().getAsFile()).buildAndSubmit(new ProcessBuilderEngine()).waitFor().assertNormalExitValue();
	}
}
