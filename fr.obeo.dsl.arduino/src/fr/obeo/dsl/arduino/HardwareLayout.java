/**
 */
package fr.obeo.dsl.arduino;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Hardware Layout</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link fr.obeo.dsl.arduino.HardwareLayout#getArduino <em>Arduino</em>}</li>
 *   <li>{@link fr.obeo.dsl.arduino.HardwareLayout#getModules <em>Modules</em>}</li>
 * </ul>
 * </p>
 *
 * @see fr.obeo.dsl.arduino.ArduinoPackage#getHardwareLayout()
 * @model
 * @generated
 */
public interface HardwareLayout extends EObject {
	/**
	 * Returns the value of the '<em><b>Arduino</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Arduino</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Arduino</em>' containment reference.
	 * @see #setArduino(Platform)
	 * @see fr.obeo.dsl.arduino.ArduinoPackage#getHardwareLayout_Arduino()
	 * @model containment="true" required="true"
	 * @generated
	 */
	Platform getArduino();

	/**
	 * Sets the value of the '{@link fr.obeo.dsl.arduino.HardwareLayout#getArduino <em>Arduino</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Arduino</em>' containment reference.
	 * @see #getArduino()
	 * @generated
	 */
	void setArduino(Platform value);

	/**
	 * Returns the value of the '<em><b>Modules</b></em>' containment reference list.
	 * The list contents are of type {@link fr.obeo.dsl.arduino.Module}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Modules</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Modules</em>' containment reference list.
	 * @see fr.obeo.dsl.arduino.ArduinoPackage#getHardwareLayout_Modules()
	 * @model containment="true"
	 * @generated
	 */
	EList<Module> getModules();

} // HardwareLayout
