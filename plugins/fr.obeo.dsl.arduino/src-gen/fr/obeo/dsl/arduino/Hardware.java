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
package fr.obeo.dsl.arduino;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Hardware</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link fr.obeo.dsl.arduino.Hardware#getPlatforms <em>Platforms</em>}</li>
 *   <li>{@link fr.obeo.dsl.arduino.Hardware#getModules <em>Modules</em>}</li>
 *   <li>{@link fr.obeo.dsl.arduino.Hardware#getConnectors <em>Connectors</em>}</li>
 * </ul>
 * </p>
 *
 * @see fr.obeo.dsl.arduino.ArduinoPackage#getHardware()
 * @model
 * @generated
 */
public interface Hardware extends NamedElement {
	/**
	 * Returns the value of the '<em><b>Platforms</b></em>' reference list.
	 * The list contents are of type {@link fr.obeo.dsl.arduino.Platform}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Platforms</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Platforms</em>' reference list.
	 * @see fr.obeo.dsl.arduino.ArduinoPackage#getHardware_Platforms()
	 * @model required="true"
	 * @generated
	 */
	EList<Platform> getPlatforms();

	/**
	 * Returns the value of the '<em><b>Modules</b></em>' reference list.
	 * The list contents are of type {@link fr.obeo.dsl.arduino.Module}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Modules</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Modules</em>' reference list.
	 * @see fr.obeo.dsl.arduino.ArduinoPackage#getHardware_Modules()
	 * @model
	 * @generated
	 */
	EList<Module> getModules();

	/**
	 * Returns the value of the '<em><b>Connectors</b></em>' containment reference list.
	 * The list contents are of type {@link fr.obeo.dsl.arduino.Connector}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Connectors</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Connectors</em>' containment reference list.
	 * @see fr.obeo.dsl.arduino.ArduinoPackage#getHardware_Connectors()
	 * @model containment="true"
	 * @generated
	 */
	EList<Connector> getConnectors();

} // Hardware
