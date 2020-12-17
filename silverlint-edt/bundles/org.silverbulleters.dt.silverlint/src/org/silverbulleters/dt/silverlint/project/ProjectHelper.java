package org.silverbulleters.dt.silverlint.project;

import java.nio.file.Path;
import java.util.Optional;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.silverbulleters.dt.silverlint.PreferenceManager;
import org.silverbulleters.dt.silverlint.SilverCore;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ProjectHelper {
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
		return getProjectFromScope();
	}
	
	public ProjectSetting getProjectFromScope() {
		var store = SilverCore.getCore().getInstancePreferenseStore();
		var setting = new ProjectSetting();
		setting.setServerUrl(store.getString(PreferenceManager.SONAR_URL));
		setting.setToken(store.getString(PreferenceManager.SONAR_TOKEN));
		setting.setProjectKey(store.getString(PreferenceManager.SONAR_PROJECT_KEY));
		return setting;
	}
}
