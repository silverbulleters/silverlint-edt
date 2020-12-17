/**
 * Copyright (C) 2020, SilverBulleters LLC
 */
package org.silverbulleters.dt.silverlint;

import org.eclipse.jface.preference.IPreferenceStore;
import org.silverbulleters.dt.silverlint.project.ProjectSetting;
import org.silverbulleters.dt.silverlint.sonarlint.LintService;
import org.silverbulleters.dt.silverlint.sonarlint.LintServiceManager;

import lombok.Getter;

public class SilverCore {
	public static final String PLUGIN_ID = "org.silverbulleters.dt.silverlint.ui";
	
	@Getter(lazy = true)
	private static final SilverCore core = new SilverCore();
	
	private final PreferenceManager preferenseManager;
	
	@Getter
	private final LintServiceManager lintManager;
	
	@Getter
    private LintService lintService;
	
	private SilverCore() {
		super();	
		preferenseManager = new PreferenceManager(PLUGIN_ID);	
		preferenseManager.initialize();
		
		lintManager = new LintServiceManager();
	}
	
    public void initServices(ProjectSetting project) {
    	lintService = new LintService(project);
    }
    
    public void clean() {
    	lintManager.stopAll();
    }
    
    public IPreferenceStore getInstancePreferenseStore() {
    	return preferenseManager.getPreferenceStore();
    }
 
}
