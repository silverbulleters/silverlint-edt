package org.silverbulleters.dt.silverlint.ui.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.silverbulleters.dt.silverlint.PreferenceManager;
import org.silverbulleters.dt.silverlint.ui.Activator;

public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PreferencePage() {
		setPreferenceStore(Activator.getDefault().getCore().getInstancePreferenceStore());
	}

	@Override
	public void init(IWorkbench workbench) {
		// none
	}

	@Override
	protected void createFieldEditors() {
		var parent = getFieldEditorParent();
		var sonarUrl = new StringFieldEditor(PreferenceManager.SONAR_URL, "Адрес сервера SonarQube", parent);
		addField(sonarUrl);

		var sonarToken = new StringFieldEditor(PreferenceManager.SONAR_TOKEN, "Ключ авторизации", parent) {
			@Override
			protected void doFillIntoGrid(Composite parent, int numColumns) {
				super.doFillIntoGrid(parent, numColumns);
				getTextControl().setEchoChar('*');
			}

		};
		addField(sonarToken);
	}

}
