package org.silverbulleters.dt.silverlint.sonarlint;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.eclipse.core.resources.IProject;
import org.silverbulleters.dt.silverlint.project.ProjectHelper;
import org.silverbulleters.dt.silverlint.project.ProjectSetting;

import lombok.Getter;

public class LintServiceManager {
	@Getter
	private Map<IProject, LintService> pool = Collections.synchronizedMap(new HashMap<>());
	
	public LintService getService(IProject project) {
		var serviceOpt = getServiceByProject(project);
		if (serviceOpt.isEmpty()) {
			var service = createServiveByProject(project);
			registerService(service, project);
			return service;
		}
		return serviceOpt.get();
	}
	
	public Optional<LintService> getServiceByProject(IProject project) {
		var service = pool.get(project);
		return Optional.ofNullable(service);
	}

	public LintService createServiveByProject(IProject project) {
		LintService service;
		if (pool.containsKey(project)) {
			service = pool.get(project);
		} else {
			ProjectSetting projectSetting = ProjectHelper.getProjectFromScope();
			service = new LintService(projectSetting);	
		}
		return service;
	}
	
	public void registerService(LintService service, IProject project) {
		pool.put(project, service);
	}

	public void removeService(LintService service) {
		if (pool.containsValue(service)) {
			pool.entrySet().stream().filter(entry -> entry.getValue() == service).forEach(entry -> {
				pool.remove(entry.getKey());
			});
		}
	}

	public void removeService(IProject project) {
		if (pool.containsKey(project)) {
			pool.remove(project);
		}
	}
	
	public void stopAll( ) {
		pool.values().forEach(LintService::stop);
	}
}
