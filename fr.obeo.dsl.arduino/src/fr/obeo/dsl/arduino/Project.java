/**
 */
package fr.obeo.dsl.arduino;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Project</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link fr.obeo.dsl.arduino.Project#getHardwares <em>Hardwares</em>}</li>
 *   <li>{@link fr.obeo.dsl.arduino.Project#getSketches <em>Sketches</em>}</li>
 *   <li>{@link fr.obeo.dsl.arduino.Project#getModules <em>Modules</em>}</li>
 *   <li>{@link fr.obeo.dsl.arduino.Project#getPlatform <em>Platform</em>}</li>
 * </ul>
 * </p>
 *
 * @see fr.obeo.dsl.arduino.ArduinoPackage#getProject()
 * @model
 * @generated
 */
public interface Project extends EObject {
	/**
	 * Returns the value of the '<em><b>Hardwares</b></em>' containment reference list.
	 * The list contents are of type {@link fr.obeo.dsl.arduino.Hardware}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Hardwares</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Hardwares</em>' containment reference list.
	 * @see fr.obeo.dsl.arduino.ArduinoPackage#getProject_Hardwares()
	 * @model containment="true"
	 * @generated
	 */
	EList<Hardware> getHardwares();

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
	 * @see fr.obeo.dsl.arduino.ArduinoPackage#getProject_Sketches()
	 * @model containment="true"
	 * @generated
	 */
	EList<Sketch> getSketches();

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
	 * @see fr.obeo.dsl.arduino.ArduinoPackage#getProject_Modules()
	 * @model containment="true"
	 * @generated
	 */
	EList<Module> getModules();

	/**
	 * Returns the value of the '<em><b>Platform</b></em>' containment reference list.
	 * The list contents are of type {@link fr.obeo.dsl.arduino.Platform}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Platform</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Platform</em>' containment reference list.
	 * @see fr.obeo.dsl.arduino.ArduinoPackage#getProject_Platform()
	 * @model containment="true"
	 * @generated
	 */
	EList<Platform> getPlatform();

} // Project
