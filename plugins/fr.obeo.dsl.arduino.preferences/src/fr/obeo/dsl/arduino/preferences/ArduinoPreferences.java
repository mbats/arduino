package fr.obeo.dsl.arduino.preferences;

import org.eclipse.jface.preference.IPreferenceStore;

public class ArduinoPreferences {
	/**
	 * Key for user answer preference in preference store.
	 */
	public static final String ARDUINO_SDK_ID = "arduino_sdk_preference";

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
}
