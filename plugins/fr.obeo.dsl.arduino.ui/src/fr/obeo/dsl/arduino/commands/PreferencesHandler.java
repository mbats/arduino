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
package fr.obeo.dsl.arduino.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.ui.PlatformUI;

import fr.obeo.dsl.arduino.preferences.ArduinoPreferences;

public class PreferencesHandler extends AbstractHandler {
	private ArduinoPreferences preferences = new ArduinoPreferences();

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		DirectoryDialog dialog = new DirectoryDialog(PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell());
		String arduinoSdk = preferences.getArduinoSdk();
		if (arduinoSdk != null && arduinoSdk.length() > 0) {
			dialog.setText(arduinoSdk);
		}
		dialog.setMessage("Select the path of the Arduino SDK installed on your computer");
		String arduinoSdkValue = dialog.open();

		if (arduinoSdkValue != null && arduinoSdkValue.length() > 0) {
			preferences.storeArduinoSdk(arduinoSdkValue);
		}
		return null;
	}
}
