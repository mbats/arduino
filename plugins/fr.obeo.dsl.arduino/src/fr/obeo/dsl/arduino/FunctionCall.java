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
 * A representation of the model object '<em><b>Function Call</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link fr.obeo.dsl.arduino.FunctionCall#getDefinition <em>Definition</em>}</li>
 *   <li>{@link fr.obeo.dsl.arduino.FunctionCall#getParameters <em>Parameters</em>}</li>
 * </ul>
 * </p>
 *
 * @see fr.obeo.dsl.arduino.ArduinoPackage#getFunctionCall()
 * @model
 * @generated
 */
public interface FunctionCall extends Instruction {
	/**
	 * Returns the value of the '<em><b>Definition</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Definition</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Definition</em>' reference.
	 * @see #setDefinition(Function)
	 * @see fr.obeo.dsl.arduino.ArduinoPackage#getFunctionCall_Definition()
	 * @model required="true"
	 * @generated
	 */
	Function getDefinition();

	/**
	 * Sets the value of the '{@link fr.obeo.dsl.arduino.FunctionCall#getDefinition <em>Definition</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Definition</em>' reference.
	 * @see #getDefinition()
	 * @generated
	 */
	void setDefinition(Function value);

	/**
	 * Returns the value of the '<em><b>Parameters</b></em>' reference list.
	 * The list contents are of type {@link fr.obeo.dsl.arduino.Parameter}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parameters</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameters</em>' reference list.
	 * @see fr.obeo.dsl.arduino.ArduinoPackage#getFunctionCall_Parameters()
	 * @model
	 * @generated
	 */
	EList<Parameter> getParameters();

} // FunctionCall
