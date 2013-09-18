/**
 */
package fr.obeo.dsl.arduino;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Module</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link fr.obeo.dsl.arduino.Module#getPin <em>Pin</em>}</li>
 *   <li>{@link fr.obeo.dsl.arduino.Module#getKind <em>Kind</em>}</li>
 * </ul>
 * </p>
 *
 * @see fr.obeo.dsl.arduino.ArduinoPackage#getModule()
 * @model abstract="true"
 * @generated
 */
public interface Module extends NamedElement {
	/**
	 * Returns the value of the '<em><b>Pin</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link fr.obeo.dsl.arduino.Pin#getModule <em>Module</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Pin</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Pin</em>' reference.
	 * @see #setPin(Pin)
	 * @see fr.obeo.dsl.arduino.ArduinoPackage#getModule_Pin()
	 * @see fr.obeo.dsl.arduino.Pin#getModule
	 * @model opposite="module"
	 * @generated
	 */
	Pin getPin();

	/**
	 * Sets the value of the '{@link fr.obeo.dsl.arduino.Module#getPin <em>Pin</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Pin</em>' reference.
	 * @see #getPin()
	 * @generated
	 */
	void setPin(Pin value);

	/**
	 * Returns the value of the '<em><b>Kind</b></em>' attribute.
	 * The default value is <code>"digital"</code>.
	 * The literals are from the enumeration {@link fr.obeo.dsl.arduino.ModuleKind}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Kind</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Kind</em>' attribute.
	 * @see fr.obeo.dsl.arduino.ModuleKind
	 * @see #setKind(ModuleKind)
	 * @see fr.obeo.dsl.arduino.ArduinoPackage#getModule_Kind()
	 * @model default="digital"
	 * @generated
	 */
	ModuleKind getKind();

	/**
	 * Sets the value of the '{@link fr.obeo.dsl.arduino.Module#getKind <em>Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Kind</em>' attribute.
	 * @see fr.obeo.dsl.arduino.ModuleKind
	 * @see #getKind()
	 * @generated
	 */
	void setKind(ModuleKind value);

} // Module
