package org.silverbulleters.dt.silverlint.ui.preferences;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;
import org.silverbulleters.dt.silverlint.PreferenceManager;
import org.silverbulleters.dt.silverlint.SilverCore;
import org.silverbulleters.dt.silverlint.ui.Activator;

public class ProjectPreferensePage extends PropertyPage implements IWorkbenchPropertyPage {
	private final SilverCore core = Activator.getDefault().getCore();
	private final PreferenceManager preferenceManager = core.getPreferenseManager();
	private IProject project;

	@Override
	protected Control createContents(Composite parent) {
		initialize();
		var layout = new GridLayout(1, false);
		parent.setLayout(layout);
		var gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		var container = new Composite(parent, 0);
		container.setLayoutData(gridData);
		var field = new StringFieldEditor(PreferenceManager.SONAR_PROJECT_KEY, "Ключ проекта", container);
		field.setStringValue(getPreferenceStore().getString(PreferenceManager.SONAR_PROJECT_KEY));
		field.setPropertyChangeListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if (event.getSource() instanceof FieldEditor) {
					var field = (FieldEditor) event.getSource();
					if (field.getPreferenceName().equals(PreferenceManager.SONAR_PROJECT_KEY)) {
						getPreferenceStore().setValue(PreferenceManager.SONAR_PROJECT_KEY,
								(String) event.getNewValue());			
						core.getLintManager().stopByProject(project);
					}
				}
			}
		});

		return parent;
	}

	private void initialize() {
		project = (IProject) getElement().getAdapter(IProject.class);
		setPreferenceStore(preferenceManager.getStoreByProject(project));
	}

}
