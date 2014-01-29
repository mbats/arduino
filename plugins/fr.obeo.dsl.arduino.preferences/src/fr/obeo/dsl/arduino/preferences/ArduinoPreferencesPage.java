package fr.obeo.dsl.arduino.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class ArduinoPreferencesPage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	public ArduinoPreferencesPage() {
		super(GRID);
	}

	@Override
	protected Control createContents(Composite parent) {
		Control control = super.createContents(parent);
		return control;
	}

	private void createArduinoSdk(Composite parent) {
		addField(new StringFieldEditor(ArduinoPreferences.ARDUINO_SDK_ID,
				"Arduino SDK :", getFieldEditorParent()));
	}

	public void init(IWorkbench workbench) {
		setPreferenceStore(ArduinoPreferencesPlugin.getDefault()
				.getPreferenceStore());
		setDescription("Give the path of the Arduino SDK installed on your computer (i.e /home/user/path/arduino/arduino-1.0.5/)");
	}

	@Override
	protected void createFieldEditors() {
		createArduinoSdk((Composite) getControl());
	}
}
