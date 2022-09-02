/**
 * Copyright (C) 2022, SilverBulleters LLC
 */
package org.silverbulleters.dt.silverlint;

import org.eclipse.jface.preference.IPreferenceStore;
import org.silverbulleters.dt.silverlint.project.ProjectSetting;
import org.silverbulleters.dt.silverlint.sonarlint.LintService;
import org.silverbulleters.dt.silverlint.sonarlint.LintServiceManager;

public class SilverCore {

	public static final String PLUGIN_ID = "org.silverbulleters.dt.silverlint.ui";

	private static final SilverCore core = new SilverCore();

	private final PreferenceManager preferenceManager;
	private final LintServiceManager lintManager;
    private LintService lintService;
	
	private SilverCore() {
		super();	
		preferenceManager = new PreferenceManager(PLUGIN_ID);
		preferenceManager.initialize();
		
		lintManager = new LintServiceManager();
	}
	
    public void initServices(ProjectSetting project) {
    	lintService = new LintService(project);
    }
    
    public void clean() {
    	lintManager.stopAll();
    }
    
    public IPreferenceStore getInstancePreferenceStore() {
    	return preferenceManager.getPreferenceStore();
    }

	public PreferenceManager getPreferenceManager() {
		return preferenceManager;
	}

	public LintServiceManager getLintManager() {
		return lintManager;
	}

	public LintService getLintService() {
		return lintService;
	}

	public static SilverCore getCore() {
		return SilverCore.core;
	}
}
