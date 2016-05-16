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
package fr.obeo.dsl.arduino.impl;

import fr.obeo.dsl.arduino.ArduinoPackage;
import fr.obeo.dsl.arduino.BooleanOperator;
import fr.obeo.dsl.arduino.MathOperator;
import fr.obeo.dsl.arduino.OperatorKind;
import fr.obeo.dsl.arduino.Sensor;
import fr.obeo.dsl.arduino.Status;
import fr.obeo.dsl.arduino.Value;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Sensor</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link fr.obeo.dsl.arduino.impl.SensorImpl#getValue <em>Value</em>}</li>
 *   <li>{@link fr.obeo.dsl.arduino.impl.SensorImpl#getLeft <em>Left</em>}</li>
 *   <li>{@link fr.obeo.dsl.arduino.impl.SensorImpl#getRight <em>Right</em>}</li>
 *   <li>{@link fr.obeo.dsl.arduino.impl.SensorImpl#getOperator <em>Operator</em>}</li>
 *   <li>{@link fr.obeo.dsl.arduino.impl.SensorImpl#getStatus <em>Status</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SensorImpl extends ModuleInstructionImpl implements Sensor {
	/**
	 * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected static final String VALUE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected String value = VALUE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getLeft() <em>Left</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLeft()
	 * @generated
	 * @ordered
	 */
	protected Value left;

	/**
	 * The cached value of the '{@link #getRight() <em>Right</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRight()
	 * @generated
	 * @ordered
	 */
	protected Value right;

	/**
	 * The default value of the '{@link #getOperator() <em>Operator</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOperator()
	 * @generated
	 * @ordered
	 */
	protected static final OperatorKind OPERATOR_EDEFAULT = OperatorKind.EQUAL;

	/**
	 * The cached value of the '{@link #getOperator() <em>Operator</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOperator()
	 * @generated
	 * @ordered
	 */
	protected OperatorKind operator = OPERATOR_EDEFAULT;

	/**
	 * The cached value of the '{@link #getStatus() <em>Status</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStatus()
	 * @generated
	 * @ordered
	 */
	protected EList<Status> status;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SensorImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ArduinoPackage.Literals.SENSOR;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getValue() {
		return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setValue(String newValue) {
		String oldValue = value;
		value = newValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ArduinoPackage.SENSOR__VALUE, oldValue, value));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Value getLeft() {
		if (left != null && left.eIsProxy()) {
			InternalEObject oldLeft = (InternalEObject)left;
			left = (Value)eResolveProxy(oldLeft);
			if (left != oldLeft) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ArduinoPackage.SENSOR__LEFT, oldLeft, left));
			}
		}
		return left;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Value basicGetLeft() {
		return left;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLeft(Value newLeft) {
		Value oldLeft = left;
		left = newLeft;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ArduinoPackage.SENSOR__LEFT, oldLeft, left));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Value getRight() {
		if (right != null && right.eIsProxy()) {
			InternalEObject oldRight = (InternalEObject)right;
			right = (Value)eResolveProxy(oldRight);
			if (right != oldRight) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ArduinoPackage.SENSOR__RIGHT, oldRight, right));
			}
		}
		return right;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Value basicGetRight() {
		return right;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRight(Value newRight) {
		Value oldRight = right;
		right = newRight;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ArduinoPackage.SENSOR__RIGHT, oldRight, right));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OperatorKind getOperator() {
		return operator;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOperator(OperatorKind newOperator) {
		OperatorKind oldOperator = operator;
		operator = newOperator == null ? OPERATOR_EDEFAULT : newOperator;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ArduinoPackage.SENSOR__OPERATOR, oldOperator, operator));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Status> getStatus() {
		if (status == null) {
			status = new EObjectWithInverseResolvingEList<Status>(Status.class, this, ArduinoPackage.SENSOR__STATUS, ArduinoPackage.STATUS__SENSOR);
		}
		return status;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ArduinoPackage.SENSOR__STATUS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getStatus()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ArduinoPackage.SENSOR__STATUS:
				return ((InternalEList<?>)getStatus()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ArduinoPackage.SENSOR__VALUE:
				return getValue();
			case ArduinoPackage.SENSOR__LEFT:
				if (resolve) return getLeft();
				return basicGetLeft();
			case ArduinoPackage.SENSOR__RIGHT:
				if (resolve) return getRight();
				return basicGetRight();
			case ArduinoPackage.SENSOR__OPERATOR:
				return getOperator();
			case ArduinoPackage.SENSOR__STATUS:
				return getStatus();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ArduinoPackage.SENSOR__VALUE:
				setValue((String)newValue);
				return;
			case ArduinoPackage.SENSOR__LEFT:
				setLeft((Value)newValue);
				return;
			case ArduinoPackage.SENSOR__RIGHT:
				setRight((Value)newValue);
				return;
			case ArduinoPackage.SENSOR__OPERATOR:
				setOperator((OperatorKind)newValue);
				return;
			case ArduinoPackage.SENSOR__STATUS:
				getStatus().clear();
				getStatus().addAll((Collection<? extends Status>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case ArduinoPackage.SENSOR__VALUE:
				setValue(VALUE_EDEFAULT);
				return;
			case ArduinoPackage.SENSOR__LEFT:
				setLeft((Value)null);
				return;
			case ArduinoPackage.SENSOR__RIGHT:
				setRight((Value)null);
				return;
			case ArduinoPackage.SENSOR__OPERATOR:
				setOperator(OPERATOR_EDEFAULT);
				return;
			case ArduinoPackage.SENSOR__STATUS:
				getStatus().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case ArduinoPackage.SENSOR__VALUE:
				return VALUE_EDEFAULT == null ? value != null : !VALUE_EDEFAULT.equals(value);
			case ArduinoPackage.SENSOR__LEFT:
				return left != null;
			case ArduinoPackage.SENSOR__RIGHT:
				return right != null;
			case ArduinoPackage.SENSOR__OPERATOR:
				return operator != OPERATOR_EDEFAULT;
			case ArduinoPackage.SENSOR__STATUS:
				return status != null && !status.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == Value.class) {
			switch (derivedFeatureID) {
				case ArduinoPackage.SENSOR__VALUE: return ArduinoPackage.VALUE__VALUE;
				default: return -1;
			}
		}
		if (baseClass == MathOperator.class) {
			switch (derivedFeatureID) {
				case ArduinoPackage.SENSOR__LEFT: return ArduinoPackage.MATH_OPERATOR__LEFT;
				case ArduinoPackage.SENSOR__RIGHT: return ArduinoPackage.MATH_OPERATOR__RIGHT;
				case ArduinoPackage.SENSOR__OPERATOR: return ArduinoPackage.MATH_OPERATOR__OPERATOR;
				default: return -1;
			}
		}
		if (baseClass == BooleanOperator.class) {
			switch (derivedFeatureID) {
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == Value.class) {
			switch (baseFeatureID) {
				case ArduinoPackage.VALUE__VALUE: return ArduinoPackage.SENSOR__VALUE;
				default: return -1;
			}
		}
		if (baseClass == MathOperator.class) {
			switch (baseFeatureID) {
				case ArduinoPackage.MATH_OPERATOR__LEFT: return ArduinoPackage.SENSOR__LEFT;
				case ArduinoPackage.MATH_OPERATOR__RIGHT: return ArduinoPackage.SENSOR__RIGHT;
				case ArduinoPackage.MATH_OPERATOR__OPERATOR: return ArduinoPackage.SENSOR__OPERATOR;
				default: return -1;
			}
		}
		if (baseClass == BooleanOperator.class) {
			switch (baseFeatureID) {
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (value: "); //$NON-NLS-1$
		result.append(value);
		result.append(", operator: "); //$NON-NLS-1$
		result.append(operator);
		result.append(')');
		return result.toString();
	}

} //SensorImpl
