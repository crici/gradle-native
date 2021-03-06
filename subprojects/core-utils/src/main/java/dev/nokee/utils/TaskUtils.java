package dev.nokee.utils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import lombok.EqualsAndHashCode;
import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.file.ProjectLayout;

import static java.util.Objects.requireNonNull;

/**
 * Utilities for Gradle {@link Task} instances.
 */
public final class TaskUtils {
	/**
	 * Returns the relative path from {@link ProjectLayout#getBuildDirectory()} (or {@link Project#getBuildDir()}) to the specified task temporary directory.
	 * Calling {@link Task#getTemporaryDir()} creates the directory immediately.
	 * To avoid unnecessary directory creation during the configuration phase, we can use this method together with {@link ProjectLayout#getBuildDirectory()} to create a side-effect free path to the task's temporary directory.
	 *
	 * @param task the task to return the temporary directory path.
	 * @return the task's temporary directory relative path from the build directory.
	 */
	public static String temporaryDirectoryPath(Task task) {
		return "tmp/" + task.getName();
	}

	/**
	 * Returns an action that will configure {@link Task#dependsOn(Object...)}.
	 *
	 * @param path a task dependency path as describe by {@link Task#dependsOn(Object...)}.
	 * @param paths an array of task dependency path as describe by {@link Task#dependsOn(Object...)}.
	 * @return an action that configures the task's dependencies.
	 */
	public static Action<Task> configureDependsOn(Object path, Object... paths) {
		// At least one dependency must be specified.
		return new ConfigureDependsOnAction(ImmutableList.builder().add(path).add(paths).build());
	}

	@EqualsAndHashCode
	private static final class ConfigureDependsOnAction implements Action<Task> {
		private final Iterable<Object> paths;

		public ConfigureDependsOnAction(Iterable<Object> paths) {
			this.paths = paths;
		}

		@Override
		public void execute(Task task) {
			task.dependsOn(Iterables.toArray(paths, Object.class));
		}

		@Override
		public String toString() {
			return "TaskUtils.configureDependsOn(" + paths + ")";
		}
	}

	/**
	 * Returns an action that configures the {@link Task#setGroup(String)}.
	 *
	 * @param group a task group string.
	 * @return an action that configures the task's group.
	 */
	public static Action<Task> configureGroup(String group) {
		return new ConfigureGroupAction(group);
	}

	@EqualsAndHashCode
	private static final class ConfigureGroupAction implements Action<Task> {
		private final String group;

		public ConfigureGroupAction(String group) {
			this.group = requireNonNull(group);
		}

		@Override
		public void execute(Task task) {
			task.setGroup(group);
		}

		@Override
		public String toString() {
			return "TaskUtils.configureGroup(" + group + ")";
		}
	}
}
