package org.silverbulleters.dt.silverlint;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

import lombok.Getter;

public class PreferenceManager {
	public static final String SONAR_ENABLE = "SONAR_ENABLE";
	public static final String SONAR_URL = "SONAR_URL";
	public static final String SONAR_TOKEN = "SONAR_TOKEN";
	public static final String SONAR_PROJECT_KEY = "SONAR_PROJECT_KEY";
	
	private final String pluginId;
	private final String DEFAULT_SONAR_URL = "http://localhost:9000/";
	
	@Getter
	private IPreferenceStore preferenceStore;
	private IPropertyChangeListener listener;
	
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
		preferenceStore.setDefault(SONAR_ENABLE, true);
		preferenceStore.setDefault(SONAR_URL, DEFAULT_SONAR_URL);
		preferenceStore.setDefault(SONAR_TOKEN, "");
		preferenceStore.setDefault(SONAR_PROJECT_KEY, "");
		preferenceStore.addPropertyChangeListener(listener);
	}
	
}
