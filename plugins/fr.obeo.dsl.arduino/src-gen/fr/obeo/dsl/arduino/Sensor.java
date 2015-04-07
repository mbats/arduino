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
 * A representation of the model object '<em><b>Sensor</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link fr.obeo.dsl.arduino.Sensor#getStatus <em>Status</em>}</li>
 * </ul>
 * </p>
 *
 * @see fr.obeo.dsl.arduino.ArduinoPackage#getSensor()
 * @model
 * @generated
 */
public interface Sensor extends ModuleInstruction, BooleanOperator {
	/**
	 * Returns the value of the '<em><b>Status</b></em>' reference list.
	 * The list contents are of type {@link fr.obeo.dsl.arduino.Status}.
	 * It is bidirectional and its opposite is '{@link fr.obeo.dsl.arduino.Status#getSensor <em>Sensor</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Status</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Status</em>' reference list.
	 * @see fr.obeo.dsl.arduino.ArduinoPackage#getSensor_Status()
	 * @see fr.obeo.dsl.arduino.Status#getSensor
	 * @model opposite="sensor"
	 * @generated
	 */
	EList<Status> getStatus();

} // Sensor
