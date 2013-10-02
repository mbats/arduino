/**
 */
package fr.obeo.dsl.arduino;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Module</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link fr.obeo.dsl.arduino.Module#getKind <em>Kind</em>}</li>
 *   <li>{@link fr.obeo.dsl.arduino.Module#getImage <em>Image</em>}</li>
 * </ul>
 * </p>
 *
 * @see fr.obeo.dsl.arduino.ArduinoPackage#getModule()
 * @model abstract="true"
 * @generated
 */
public interface Module extends NamedElement {
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

	/**
	 * Returns the value of the '<em><b>Image</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Image</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Image</em>' attribute.
	 * @see #setImage(String)
	 * @see fr.obeo.dsl.arduino.ArduinoPackage#getModule_Image()
	 * @model
	 * @generated
	 */
	String getImage();

	/**
	 * Sets the value of the '{@link fr.obeo.dsl.arduino.Module#getImage <em>Image</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Image</em>' attribute.
	 * @see #getImage()
	 * @generated
	 */
	void setImage(String value);

} // Module
