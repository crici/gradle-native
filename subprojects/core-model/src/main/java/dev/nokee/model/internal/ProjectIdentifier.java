package dev.nokee.model.internal;

import lombok.EqualsAndHashCode;
import lombok.var;
import org.gradle.api.Project;
import org.gradle.util.Path;

import java.util.Optional;

@EqualsAndHashCode(doNotUseGetters = true)
public final class ProjectIdentifier implements DomainObjectIdentifierInternal {
	private final Path projectPath;
	private final String projectName;

	private ProjectIdentifier(Path projectPath, String projectName) {
		this.projectPath = projectPath;
		this.projectName = projectName;
	}

	public String getName() {
		return projectName;
	}

	public Path getPath() {
		return projectPath;
	}

	@Override
	public Optional<? extends DomainObjectIdentifierInternal> getParentIdentifier() {
		return Optional.empty();
	}

	public static ProjectIdentifier of(String name) {
		return new ProjectIdentifier(Path.ROOT, name);
	}

	public static ProjectIdentifier ofRootProject() {
		return new ProjectIdentifier(Path.ROOT, null);
	}

	public static ProjectIdentifier ofChildProject(String... projectNames) {
		var path = Path.ROOT;
		for (String projectName : projectNames) {
			path = path.child(projectName);
		}
		return new ProjectIdentifier(path, projectNames[projectNames.length - 1]);
	}

	public static ProjectIdentifier of(Project project) {
		return new ProjectIdentifier(Path.path(project.getPath()), project.getName());
	}

	@Override
	public String getDisplayName() {
		return "project '" + getPath() + "'";
	}

	@Override
	public String toString() {
		return "project '" + getPath() + "'";
	}
}
