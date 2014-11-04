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

import org.eclipse.jface.preference.IPreferenceStore;

public class ArduinoPreferences {
	
	/**
	 * Keys for user answer preference in preference store.
	 */
	public static final String ARDUINO_SDK_ID = "arduino_sdk_preference";
	public static final String ARDUINO_SERIAL_PORT = "arduino_serial_port_preference";

	/**
	 * The preference store.
	 */
	private IPreferenceStore preferenceStore = ArduinoPreferencesPlugin
			.getDefault().getPreferenceStore();

	/**
	 * Store the Arduino SDK path.
	 * 
	 * @param answerId
	 *            The answer is a string
	 */
	public void storeArduinoSdk(String answerId) {
		preferenceStore.setValue(ARDUINO_SDK_ID, answerId);
	}

	/**
	 * Get Arduino sdk.
	 * 
	 * @return String representing the Arduino SDK path
	 */
	public String getArduinoSdk() {
		return preferenceStore.getString(ARDUINO_SDK_ID);
	}

	/**
	 * Get Arduino serial port.
	 * 
	 * @return String representing the Arduino serial port
	 */
	public String getArduinoSerialPort() {
		return preferenceStore.getString(ARDUINO_SERIAL_PORT);
	}

	/**
	 * Store the Arduino SDK path.
	 * 
	 * @param answerId
	 *            The answer is a string
	 */
	public void storeArduinoSerialPort(String answerPort) {
		preferenceStore.setValue(ARDUINO_SERIAL_PORT, answerPort);
	}
}
