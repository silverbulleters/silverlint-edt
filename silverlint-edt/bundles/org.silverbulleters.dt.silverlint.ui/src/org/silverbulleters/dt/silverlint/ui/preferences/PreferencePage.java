package org.silverbulleters.dt.silverlint.ui.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.silverbulleters.dt.silverlint.PreferenceManager;
import org.silverbulleters.dt.silverlint.SilverCore;
import org.silverbulleters.dt.silverlint.ui.BSLPlugin;

public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    private String sonarUrlOriginal = "";
    private String sonarTokenOriginal = "";

	public PreferencePage() {
		setPreferenceStore(BSLPlugin.getDefault().getCore().getInstancePreferenceStore());
	}

	@Override
	public void init(IWorkbench workbench) {
		// none
	}

	@Override
	protected void createFieldEditors() {

        sonarUrlOriginal = getPreferenceStore().getString(PreferenceManager.SONAR_URL);
        sonarTokenOriginal = getPreferenceStore().getString(PreferenceManager.SONAR_TOKEN);

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

    @Override
    public boolean performOk() {
        boolean result = super.performOk();
        String currentUrl = getPreferenceStore().getString(PreferenceManager.SONAR_URL);
        String currentToken = getPreferenceStore().getString(PreferenceManager.SONAR_TOKEN);
        if (!currentUrl.equals(sonarUrlOriginal)) {
            SilverCore.logInfo("Изменен адрес сервера SonarQube: "
                    + getPreferenceStore().getString(PreferenceManager.SONAR_URL));
            sonarUrlOriginal = currentUrl;
        }
        if (!currentToken.equals(sonarTokenOriginal)) {
            SilverCore.logInfo("Изменен ключ авторизации");
            sonarTokenOriginal = currentToken;
        }
        SilverCore.getCore().getLintManager().stopAll();
        return result;
    }

}
