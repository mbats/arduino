/**
 */
package fr.obeo.dsl.arduino;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Arduino</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link fr.obeo.dsl.arduino.Arduino#getHardware <em>Hardware</em>}</li>
 *   <li>{@link fr.obeo.dsl.arduino.Arduino#getSketches <em>Sketches</em>}</li>
 * </ul>
 * </p>
 *
 * @see fr.obeo.dsl.arduino.ArduinoPackage#getArduino()
 * @model
 * @generated
 */
public interface Arduino extends EObject {
	/**
	 * Returns the value of the '<em><b>Hardware</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Hardware</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Hardware</em>' containment reference.
	 * @see #setHardware(HardwareLayout)
	 * @see fr.obeo.dsl.arduino.ArduinoPackage#getArduino_Hardware()
	 * @model containment="true" required="true"
	 * @generated
	 */
	HardwareLayout getHardware();

	/**
	 * Sets the value of the '{@link fr.obeo.dsl.arduino.Arduino#getHardware <em>Hardware</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Hardware</em>' containment reference.
	 * @see #getHardware()
	 * @generated
	 */
	void setHardware(HardwareLayout value);

	/**
	 * Returns the value of the '<em><b>Sketches</b></em>' containment reference list.
	 * The list contents are of type {@link fr.obeo.dsl.arduino.Sketch}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sketches</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sketches</em>' containment reference list.
	 * @see fr.obeo.dsl.arduino.ArduinoPackage#getArduino_Sketches()
	 * @model containment="true"
	 * @generated
	 */
	EList<Sketch> getSketches();

} // Arduino
