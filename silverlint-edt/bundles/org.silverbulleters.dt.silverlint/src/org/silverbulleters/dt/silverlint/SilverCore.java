/**
 * Copyright (C) 2022, SilverBulleters LLC
 */
package org.silverbulleters.dt.silverlint;

import org.eclipse.core.internal.runtime.InternalPlatform;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPreferenceStore;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleReference;
import org.silverbulleters.dt.silverlint.project.ProjectSetting;
import org.silverbulleters.dt.silverlint.sonarlint.LintService;
import org.silverbulleters.dt.silverlint.sonarlint.LintServiceManager;

public class SilverCore {

	public static final String PLUGIN_ID = "org.silverbulleters.dt.silverlint.ui";

	private static final SilverCore core = new SilverCore();

	private static final ILog log = core.getLog();

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

    public ILog getLog() {

        Bundle bundle = null;

        ClassLoader cl = getClass().getClassLoader();
        if (cl instanceof BundleReference)
            bundle = ((BundleReference)cl).getBundle();

        return InternalPlatform.getDefault().getLog(bundle);
    }

	public static void log(IStatus status) {
		log.log(status);
	}

	public static void logError(Throwable throwable) {
		log(createErrorStatus(throwable.getMessage(), throwable));
	}

	public static void logError(String message) {
		log(createErrorStatus(message, null));
	}

	public static void logWarn(String message) {
		log(createWarningStatus(message));
	}

	public static void logInfo(String message) {
		log(createInfoStatus(message));
	}

	public static IStatus createErrorStatus(String message, Throwable throwable) {
		return new Status(IStatus.ERROR, PLUGIN_ID, 0, message, throwable);
	}

	public static IStatus createWarningStatus(String message) {
		return new Status(IStatus.WARNING, PLUGIN_ID, 0, message, null);
	}

	public static IStatus createWarningStatus(final String message, Exception throwable) {
		return new Status(IStatus.WARNING, PLUGIN_ID, 0, message, throwable);
	}

	public static IStatus createInfoStatus(String message) {
		return new Status(IStatus.INFO, PLUGIN_ID, 0, message, null);
	}

}
