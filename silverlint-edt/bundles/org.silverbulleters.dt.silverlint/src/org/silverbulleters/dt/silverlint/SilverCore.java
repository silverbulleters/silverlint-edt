/**
 * Copyright (C) 2020, SilverBulleters LLC
 */
package org.silverbulleters.dt.silverlint;

import org.silverbulleters.dt.silverlint.project.ProjectHelper;
import org.silverbulleters.dt.silverlint.sonarlint.LintService;

import lombok.Getter;

public class SilverCore {
	@Getter(lazy = true)
	private static final SilverCore core = new SilverCore();
	
	@Getter
    private LintService lintService;
	
	private SilverCore() {
		super();
		initServices();
	}
	
    private void initServices() {
    	lintService = new LintService(ProjectHelper.DEFAULT_PROJECT_SETTING);
    }
}
