/**
 * Copyright (C) 2020, SilverBulleters LLC
 */
package org.silverbulleters.dt.silverlint;

import java.nio.file.Path;
import java.util.Optional;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ProjectHelper {
	public final static ProjectSetting DEFAULT_PROJECT_SETTING = createDefaultProjectSetting();
	public Optional<IProject> getProjectByUri(Path pathToFile) {
		IProject project = null;
		for (IProject currentProject : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
			var pathProject = getProjectPath(currentProject);
			if (pathToFile.startsWith(pathProject)) {
				project = currentProject;
				break;
			}
		}
		return Optional.ofNullable(project);
	}

	public Path getProjectPath(IProject project) {
		return project.getLocation().toFile().toPath();
	}
	
	public ProjectSetting getProjectSettingByProject(IProject project) {
		return DEFAULT_PROJECT_SETTING;
	}
	
	private static ProjectSetting createDefaultProjectSetting() {
		var setting = new ProjectSetting();
		setting.setToken("11cc533e056fc23e2782fbb39a4e82e478061453");
		setting.setServerUrl("https://open.checkbsl.org/");
		setting.setProjectKey("demo-edt");
		return setting;
	}
}
