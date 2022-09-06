package org.silverbulleters.dt.silverlint;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;

public class PreferenceManager {

	public static final String SONAR_URL = "SONAR_URL";
	public static final String SONAR_TOKEN = "SONAR_TOKEN";
	public static final String SONAR_PROJECT_KEY = "SONAR_PROJECT_KEY";
	
	private final String pluginId;
	private final String DEFAULT_SONAR_URL = "http://localhost:9000/";
	
	private IPreferenceStore preferenceStore;
	private IPropertyChangeListener listener;
	private Map<IProject, IPreferenceStore> storeByProjects = Collections.synchronizedMap(new HashMap<>());
	
	public PreferenceManager(String pluginId) {
		this.pluginId = pluginId;
		
		listener = new IPropertyChangeListener() {		
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				SilverCore.getCore().getLintManager().stopAll();
				// FIXME: происходит несколько раз. Переделать
				initialize();
			}
		};
	}
	
	public void initialize() {
		preferenceStore = new ScopedPreferenceStore(InstanceScope.INSTANCE, pluginId);
		preferenceStore.setDefault(SONAR_URL, DEFAULT_SONAR_URL);
		preferenceStore.setDefault(SONAR_TOKEN, "");
		preferenceStore.addPropertyChangeListener(listener);
	}
	
	public IPreferenceStore getStoreByProject(IProject project) {
		IPreferenceStore store;
		if (storeByProjects.containsKey(project)) {
			store = storeByProjects.get(project);
		} else {
			store = new ScopedPreferenceStore(new ProjectScope(project), SilverCore.PLUGIN_ID);
			storeByProjects.put(project, store);
		}
		return store;
	}

	public IPreferenceStore getPreferenceStore() {
		return preferenceStore;
	}
}
