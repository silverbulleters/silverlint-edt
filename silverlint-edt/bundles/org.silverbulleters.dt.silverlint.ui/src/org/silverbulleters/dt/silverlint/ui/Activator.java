/**
 * Copyright (C) 2022, SilverBulleters LLC
 */
package org.silverbulleters.dt.silverlint.ui;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.silverbulleters.dt.silverlint.SilverCore;

import lombok.Getter;

public class Activator extends AbstractUIPlugin {
	public static final String PLUGIN_ID = SilverCore.PLUGIN_ID; //$NON-NLS-1$
	private static Activator plugin;
	@Getter
	private BundleContext bundleContext;
	@Getter
	private IPreferenceStore preferenceStore;
	@Getter
	private SilverCore core;

	public static Activator getDefault() {
		return plugin;
	}

	public static void log(IStatus status) {
		plugin.getLog().log(status);
	}

	public static void logError(Throwable throwable) {
		log(createErrorStatus(throwable.getMessage(), throwable));
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

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		this.bundleContext = bundleContext;
		plugin = this;	
		core = SilverCore.getCore();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		plugin = null;
		super.stop(bundleContext);
		core.clean();
		core = null;
	}
}
