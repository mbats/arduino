/**
 */
package fr.obeo.dsl.arduino.impl;

import fr.obeo.dsl.arduino.Arduino;
import fr.obeo.dsl.arduino.ArduinoPackage;
import fr.obeo.dsl.arduino.HardwareLayout;
import fr.obeo.dsl.arduino.Sketch;

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
 * An implementation of the model object '<em><b>Arduino</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link fr.obeo.dsl.arduino.impl.ArduinoImpl#getHardware <em>Hardware</em>}</li>
 *   <li>{@link fr.obeo.dsl.arduino.impl.ArduinoImpl#getSketches <em>Sketches</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ArduinoImpl extends EObjectImpl implements Arduino {
	/**
	 * The cached value of the '{@link #getHardware() <em>Hardware</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHardware()
	 * @generated
	 * @ordered
	 */
	protected HardwareLayout hardware;

	/**
	 * The cached value of the '{@link #getSketches() <em>Sketches</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSketches()
	 * @generated
	 * @ordered
	 */
	protected EList<Sketch> sketches;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ArduinoImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ArduinoPackage.Literals.ARDUINO;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public HardwareLayout getHardware() {
		return hardware;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetHardware(HardwareLayout newHardware, NotificationChain msgs) {
		HardwareLayout oldHardware = hardware;
		hardware = newHardware;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ArduinoPackage.ARDUINO__HARDWARE, oldHardware, newHardware);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHardware(HardwareLayout newHardware) {
		if (newHardware != hardware) {
			NotificationChain msgs = null;
			if (hardware != null)
				msgs = ((InternalEObject)hardware).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ArduinoPackage.ARDUINO__HARDWARE, null, msgs);
			if (newHardware != null)
				msgs = ((InternalEObject)newHardware).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ArduinoPackage.ARDUINO__HARDWARE, null, msgs);
			msgs = basicSetHardware(newHardware, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ArduinoPackage.ARDUINO__HARDWARE, newHardware, newHardware));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Sketch> getSketches() {
		if (sketches == null) {
			sketches = new EObjectContainmentEList<Sketch>(Sketch.class, this, ArduinoPackage.ARDUINO__SKETCHES);
		}
		return sketches;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ArduinoPackage.ARDUINO__HARDWARE:
				return basicSetHardware(null, msgs);
			case ArduinoPackage.ARDUINO__SKETCHES:
				return ((InternalEList<?>)getSketches()).basicRemove(otherEnd, msgs);
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
			case ArduinoPackage.ARDUINO__HARDWARE:
				return getHardware();
			case ArduinoPackage.ARDUINO__SKETCHES:
				return getSketches();
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
			case ArduinoPackage.ARDUINO__HARDWARE:
				setHardware((HardwareLayout)newValue);
				return;
			case ArduinoPackage.ARDUINO__SKETCHES:
				getSketches().clear();
				getSketches().addAll((Collection<? extends Sketch>)newValue);
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
			case ArduinoPackage.ARDUINO__HARDWARE:
				setHardware((HardwareLayout)null);
				return;
			case ArduinoPackage.ARDUINO__SKETCHES:
				getSketches().clear();
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
			case ArduinoPackage.ARDUINO__HARDWARE:
				return hardware != null;
			case ArduinoPackage.ARDUINO__SKETCHES:
				return sketches != null && !sketches.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //ArduinoImpl
