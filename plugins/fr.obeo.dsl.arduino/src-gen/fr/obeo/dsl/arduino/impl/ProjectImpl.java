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
import fr.obeo.dsl.arduino.Hardware;
import fr.obeo.dsl.arduino.Module;
import fr.obeo.dsl.arduino.Platform;
import fr.obeo.dsl.arduino.Project;
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
 * An implementation of the model object '<em><b>Project</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link fr.obeo.dsl.arduino.impl.ProjectImpl#getHardware <em>Hardware</em>}</li>
 *   <li>{@link fr.obeo.dsl.arduino.impl.ProjectImpl#getSketch <em>Sketch</em>}</li>
 *   <li>{@link fr.obeo.dsl.arduino.impl.ProjectImpl#getModules <em>Modules</em>}</li>
 *   <li>{@link fr.obeo.dsl.arduino.impl.ProjectImpl#getPlatform <em>Platform</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ProjectImpl extends EObjectImpl implements Project {
	/**
	 * The cached value of the '{@link #getHardware() <em>Hardware</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHardware()
	 * @generated
	 * @ordered
	 */
	protected Hardware hardware;

	/**
	 * The cached value of the '{@link #getSketch() <em>Sketch</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSketch()
	 * @generated
	 * @ordered
	 */
	protected Sketch sketch;

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
	 * The cached value of the '{@link #getPlatform() <em>Platform</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPlatform()
	 * @generated
	 * @ordered
	 */
	protected EList<Platform> platform;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ProjectImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ArduinoPackage.Literals.PROJECT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Hardware getHardware() {
		return hardware;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetHardware(Hardware newHardware, NotificationChain msgs) {
		Hardware oldHardware = hardware;
		hardware = newHardware;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ArduinoPackage.PROJECT__HARDWARE, oldHardware, newHardware);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHardware(Hardware newHardware) {
		if (newHardware != hardware) {
			NotificationChain msgs = null;
			if (hardware != null)
				msgs = ((InternalEObject)hardware).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ArduinoPackage.PROJECT__HARDWARE, null, msgs);
			if (newHardware != null)
				msgs = ((InternalEObject)newHardware).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ArduinoPackage.PROJECT__HARDWARE, null, msgs);
			msgs = basicSetHardware(newHardware, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ArduinoPackage.PROJECT__HARDWARE, newHardware, newHardware));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Sketch getSketch() {
		return sketch;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSketch(Sketch newSketch, NotificationChain msgs) {
		Sketch oldSketch = sketch;
		sketch = newSketch;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ArduinoPackage.PROJECT__SKETCH, oldSketch, newSketch);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSketch(Sketch newSketch) {
		if (newSketch != sketch) {
			NotificationChain msgs = null;
			if (sketch != null)
				msgs = ((InternalEObject)sketch).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ArduinoPackage.PROJECT__SKETCH, null, msgs);
			if (newSketch != null)
				msgs = ((InternalEObject)newSketch).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ArduinoPackage.PROJECT__SKETCH, null, msgs);
			msgs = basicSetSketch(newSketch, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ArduinoPackage.PROJECT__SKETCH, newSketch, newSketch));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Module> getModules() {
		if (modules == null) {
			modules = new EObjectContainmentEList<Module>(Module.class, this, ArduinoPackage.PROJECT__MODULES);
		}
		return modules;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Platform> getPlatform() {
		if (platform == null) {
			platform = new EObjectContainmentEList<Platform>(Platform.class, this, ArduinoPackage.PROJECT__PLATFORM);
		}
		return platform;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ArduinoPackage.PROJECT__HARDWARE:
				return basicSetHardware(null, msgs);
			case ArduinoPackage.PROJECT__SKETCH:
				return basicSetSketch(null, msgs);
			case ArduinoPackage.PROJECT__MODULES:
				return ((InternalEList<?>)getModules()).basicRemove(otherEnd, msgs);
			case ArduinoPackage.PROJECT__PLATFORM:
				return ((InternalEList<?>)getPlatform()).basicRemove(otherEnd, msgs);
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
			case ArduinoPackage.PROJECT__HARDWARE:
				return getHardware();
			case ArduinoPackage.PROJECT__SKETCH:
				return getSketch();
			case ArduinoPackage.PROJECT__MODULES:
				return getModules();
			case ArduinoPackage.PROJECT__PLATFORM:
				return getPlatform();
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
			case ArduinoPackage.PROJECT__HARDWARE:
				setHardware((Hardware)newValue);
				return;
			case ArduinoPackage.PROJECT__SKETCH:
				setSketch((Sketch)newValue);
				return;
			case ArduinoPackage.PROJECT__MODULES:
				getModules().clear();
				getModules().addAll((Collection<? extends Module>)newValue);
				return;
			case ArduinoPackage.PROJECT__PLATFORM:
				getPlatform().clear();
				getPlatform().addAll((Collection<? extends Platform>)newValue);
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
			case ArduinoPackage.PROJECT__HARDWARE:
				setHardware((Hardware)null);
				return;
			case ArduinoPackage.PROJECT__SKETCH:
				setSketch((Sketch)null);
				return;
			case ArduinoPackage.PROJECT__MODULES:
				getModules().clear();
				return;
			case ArduinoPackage.PROJECT__PLATFORM:
				getPlatform().clear();
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
			case ArduinoPackage.PROJECT__HARDWARE:
				return hardware != null;
			case ArduinoPackage.PROJECT__SKETCH:
				return sketch != null;
			case ArduinoPackage.PROJECT__MODULES:
				return modules != null && !modules.isEmpty();
			case ArduinoPackage.PROJECT__PLATFORM:
				return platform != null && !platform.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //ProjectImpl
