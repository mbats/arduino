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
package fr.obeo.dsl.arduino.provider;


import fr.obeo.dsl.arduino.ArduinoFactory;
import fr.obeo.dsl.arduino.ArduinoPackage;
import fr.obeo.dsl.arduino.Sketch;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link fr.obeo.dsl.arduino.Sketch} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class SketchItemProvider
	extends NamedElementItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SketchItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			addPreviousPropertyDescriptor(object);
			addNextPropertyDescriptor(object);
			addHardwarePropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Previous feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPreviousPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Instruction_previous_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Instruction_previous_feature", "_UI_Instruction_type"),
				 ArduinoPackage.Literals.INSTRUCTION__PREVIOUS,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Next feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addNextPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Instruction_next_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Instruction_next_feature", "_UI_Instruction_type"),
				 ArduinoPackage.Literals.INSTRUCTION__NEXT,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Hardware feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addHardwarePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Sketch_hardware_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Sketch_hardware_feature", "_UI_Sketch_type"),
				 ArduinoPackage.Literals.SKETCH__HARDWARE,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(ArduinoPackage.Literals.SKETCH__INSTRUCTIONS);
			childrenFeatures.add(ArduinoPackage.Literals.SKETCH__FUNCTIONS);
		}
		return childrenFeatures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EStructuralFeature getChildFeature(Object object, Object child) {
		// Check the type of the specified child object and return the proper feature to use for
		// adding (see {@link AddCommand}) it as a child.

		return super.getChildFeature(object, child);
	}

	/**
	 * This returns Sketch.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/Sketch"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((Sketch)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_Sketch_type") :
			getString("_UI_Sketch_type") + " " + label;
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(Sketch.class)) {
			case ArduinoPackage.SKETCH__INSTRUCTIONS:
			case ArduinoPackage.SKETCH__FUNCTIONS:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
				return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
	 * that can be created under this object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);

		newChildDescriptors.add
			(createChildParameter
				(ArduinoPackage.Literals.SKETCH__INSTRUCTIONS,
				 ArduinoFactory.eINSTANCE.createSketch()));

		newChildDescriptors.add
			(createChildParameter
				(ArduinoPackage.Literals.SKETCH__INSTRUCTIONS,
				 ArduinoFactory.eINSTANCE.createStatus()));

		newChildDescriptors.add
			(createChildParameter
				(ArduinoPackage.Literals.SKETCH__INSTRUCTIONS,
				 ArduinoFactory.eINSTANCE.createLevel()));

		newChildDescriptors.add
			(createChildParameter
				(ArduinoPackage.Literals.SKETCH__INSTRUCTIONS,
				 ArduinoFactory.eINSTANCE.createDelay()));

		newChildDescriptors.add
			(createChildParameter
				(ArduinoPackage.Literals.SKETCH__INSTRUCTIONS,
				 ArduinoFactory.eINSTANCE.createRepeat()));

		newChildDescriptors.add
			(createChildParameter
				(ArduinoPackage.Literals.SKETCH__INSTRUCTIONS,
				 ArduinoFactory.eINSTANCE.createSensor()));

		newChildDescriptors.add
			(createChildParameter
				(ArduinoPackage.Literals.SKETCH__INSTRUCTIONS,
				 ArduinoFactory.eINSTANCE.createWhile()));

		newChildDescriptors.add
			(createChildParameter
				(ArduinoPackage.Literals.SKETCH__INSTRUCTIONS,
				 ArduinoFactory.eINSTANCE.createVariable()));

		newChildDescriptors.add
			(createChildParameter
				(ArduinoPackage.Literals.SKETCH__INSTRUCTIONS,
				 ArduinoFactory.eINSTANCE.createSet()));

		newChildDescriptors.add
			(createChildParameter
				(ArduinoPackage.Literals.SKETCH__INSTRUCTIONS,
				 ArduinoFactory.eINSTANCE.createNumericalOperator()));

		newChildDescriptors.add
			(createChildParameter
				(ArduinoPackage.Literals.SKETCH__INSTRUCTIONS,
				 ArduinoFactory.eINSTANCE.createBooleanOperator()));

		newChildDescriptors.add
			(createChildParameter
				(ArduinoPackage.Literals.SKETCH__INSTRUCTIONS,
				 ArduinoFactory.eINSTANCE.createConstant()));

		newChildDescriptors.add
			(createChildParameter
				(ArduinoPackage.Literals.SKETCH__INSTRUCTIONS,
				 ArduinoFactory.eINSTANCE.createIf()));

		newChildDescriptors.add
			(createChildParameter
				(ArduinoPackage.Literals.SKETCH__INSTRUCTIONS,
				 ArduinoFactory.eINSTANCE.createFunctionCall()));

		newChildDescriptors.add
			(createChildParameter
				(ArduinoPackage.Literals.SKETCH__INSTRUCTIONS,
				 ArduinoFactory.eINSTANCE.createParameterCall()));

		newChildDescriptors.add
			(createChildParameter
				(ArduinoPackage.Literals.SKETCH__FUNCTIONS,
				 ArduinoFactory.eINSTANCE.createFunction()));
	}

}
