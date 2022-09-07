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

public class BSLPlugin extends AbstractUIPlugin {

	public static final String PLUGIN_ID = SilverCore.PLUGIN_ID; //$NON-NLS-1$

	private static BSLPlugin plugin;

	private BundleContext bundleContext;
	private SilverCore core;

	public static BSLPlugin getDefault() {
		return plugin;
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

	public BundleContext getBundleContext() {
		return bundleContext;
	}

	public SilverCore getCore() {
		return core;
	}
}
