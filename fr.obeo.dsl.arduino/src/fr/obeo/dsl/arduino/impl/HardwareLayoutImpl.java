/**
 */
package fr.obeo.dsl.arduino.impl;

import fr.obeo.dsl.arduino.ArduinoPackage;
import fr.obeo.dsl.arduino.HardwareLayout;
import fr.obeo.dsl.arduino.Module;
import fr.obeo.dsl.arduino.Platform;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Hardware Layout</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link fr.obeo.dsl.arduino.impl.HardwareLayoutImpl#getArduino <em>Arduino</em>}</li>
 *   <li>{@link fr.obeo.dsl.arduino.impl.HardwareLayoutImpl#getModules <em>Modules</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class HardwareLayoutImpl extends EObjectImpl implements HardwareLayout {
	/**
	 * The cached value of the '{@link #getArduino() <em>Arduino</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getArduino()
	 * @generated
	 * @ordered
	 */
	protected Platform arduino;

	/**
	 * The cached value of the '{@link #getModules() <em>Modules</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModules()
	 * @generated
	 * @ordered
	 */
	protected EList<Module> modules;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected HardwareLayoutImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ArduinoPackage.Literals.HARDWARE_LAYOUT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Platform getArduino() {
		return arduino;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetArduino(Platform newArduino, NotificationChain msgs) {
		Platform oldArduino = arduino;
		arduino = newArduino;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ArduinoPackage.HARDWARE_LAYOUT__ARDUINO, oldArduino, newArduino);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setArduino(Platform newArduino) {
		if (newArduino != arduino) {
			NotificationChain msgs = null;
			if (arduino != null)
				msgs = ((InternalEObject)arduino).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ArduinoPackage.HARDWARE_LAYOUT__ARDUINO, null, msgs);
			if (newArduino != null)
				msgs = ((InternalEObject)newArduino).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ArduinoPackage.HARDWARE_LAYOUT__ARDUINO, null, msgs);
			msgs = basicSetArduino(newArduino, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ArduinoPackage.HARDWARE_LAYOUT__ARDUINO, newArduino, newArduino));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Module> getModules() {
		if (modules == null) {
			modules = new EObjectContainmentEList<Module>(Module.class, this, ArduinoPackage.HARDWARE_LAYOUT__MODULES);
		}
		return modules;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ArduinoPackage.HARDWARE_LAYOUT__ARDUINO:
				return basicSetArduino(null, msgs);
			case ArduinoPackage.HARDWARE_LAYOUT__MODULES:
				return ((InternalEList<?>)getModules()).basicRemove(otherEnd, msgs);
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
			case ArduinoPackage.HARDWARE_LAYOUT__ARDUINO:
				return getArduino();
			case ArduinoPackage.HARDWARE_LAYOUT__MODULES:
				return getModules();
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
			case ArduinoPackage.HARDWARE_LAYOUT__ARDUINO:
				setArduino((Platform)newValue);
				return;
			case ArduinoPackage.HARDWARE_LAYOUT__MODULES:
				getModules().clear();
				getModules().addAll((Collection<? extends Module>)newValue);
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
			case ArduinoPackage.HARDWARE_LAYOUT__ARDUINO:
				setArduino((Platform)null);
				return;
			case ArduinoPackage.HARDWARE_LAYOUT__MODULES:
				getModules().clear();
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
			case ArduinoPackage.HARDWARE_LAYOUT__ARDUINO:
				return arduino != null;
			case ArduinoPackage.HARDWARE_LAYOUT__MODULES:
				return modules != null && !modules.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //HardwareLayoutImpl
