/**
 *  Copyright (c) 2013 Obeo.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *    Obeo - initial API and implementation
 */
package fr.obeo.dsl.arduino.preferences;

import org.eclipse.jface.preference.DirectoryFieldEditor;
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
		DirectoryFieldEditor sdk = new DirectoryFieldEditor(
				ArduinoPreferences.ARDUINO_SDK_ID, "Arduino SDK :",
				getFieldEditorParent());
		addField(sdk);

		StringFieldEditor port = new StringFieldEditor(
				ArduinoPreferences.ARDUINO_SERIAL_PORT,
				"Arduino Serial Port :", getFieldEditorParent());
		addField(port);
	}

	public void init(IWorkbench workbench) {
		setPreferenceStore(ArduinoPreferencesPlugin.getDefault()
				.getPreferenceStore());

		setDescription("Give the path of the Arduino SDK installed on your computer (e.g. /home/user/path/arduino/arduino-1.0.5/) and the serial port of the Arduino linked to your computer (e.g. com3, /dev/ttyACM0, /dev/tty.usbmodem1421, ...).\n");
	}

	@Override
	protected void createFieldEditors() {
		createArduinoSdk((Composite) getControl());
	}
}
