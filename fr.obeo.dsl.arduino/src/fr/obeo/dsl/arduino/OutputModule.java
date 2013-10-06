/**
 */
package fr.obeo.dsl.arduino;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Output Module</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link fr.obeo.dsl.arduino.OutputModule#isLevel <em>Level</em>}</li>
 * </ul>
 * </p>
 *
 * @see fr.obeo.dsl.arduino.ArduinoPackage#getOutputModule()
 * @model
 * @generated
 */
public interface OutputModule extends Module {

	/**
	 * Returns the value of the '<em><b>Level</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Level</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Level</em>' attribute.
	 * @see #setLevel(boolean)
	 * @see fr.obeo.dsl.arduino.ArduinoPackage#getOutputModule_Level()
	 * @model
	 * @generated
	 */
	boolean isLevel();

	/**
	 * Sets the value of the '{@link fr.obeo.dsl.arduino.OutputModule#isLevel <em>Level</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Level</em>' attribute.
	 * @see #isLevel()
	 * @generated
	 */
	void setLevel(boolean value);
} // OutputModule
