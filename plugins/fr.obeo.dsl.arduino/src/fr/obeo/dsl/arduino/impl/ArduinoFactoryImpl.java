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

import fr.obeo.dsl.arduino.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ArduinoFactoryImpl extends EFactoryImpl implements ArduinoFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ArduinoFactory init() {
		try {
			ArduinoFactory theArduinoFactory = (ArduinoFactory)EPackage.Registry.INSTANCE.getEFactory(ArduinoPackage.eNS_URI);
			if (theArduinoFactory != null) {
				return theArduinoFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new ArduinoFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ArduinoFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case ArduinoPackage.HARDWARE: return createHardware();
			case ArduinoPackage.PLATFORM: return createPlatform();
			case ArduinoPackage.DIGITAL_PIN: return createDigitalPin();
			case ArduinoPackage.ANALOG_PIN: return createAnalogPin();
			case ArduinoPackage.SKETCH: return createSketch();
			case ArduinoPackage.PROJECT: return createProject();
			case ArduinoPackage.STATUS: return createStatus();
			case ArduinoPackage.LEVEL: return createLevel();
			case ArduinoPackage.DELAY: return createDelay();
			case ArduinoPackage.INPUT_MODULE: return createInputModule();
			case ArduinoPackage.OUTPUT_MODULE: return createOutputModule();
			case ArduinoPackage.CONNECTOR: return createConnector();
			case ArduinoPackage.REPEAT: return createRepeat();
			case ArduinoPackage.SENSOR: return createSensor();
			case ArduinoPackage.WHILE: return createWhile();
			case ArduinoPackage.VARIABLE: return createVariable();
			case ArduinoPackage.SET: return createSet();
			case ArduinoPackage.NUMERICAL_OPERATOR: return createNumericalOperator();
			case ArduinoPackage.BOOLEAN_OPERATOR: return createBooleanOperator();
			case ArduinoPackage.CONSTANT: return createConstant();
			case ArduinoPackage.IF: return createIf();
			case ArduinoPackage.FUNCTION: return createFunction();
			case ArduinoPackage.PARAMETER_DEFINITION: return createParameterDefinition();
			case ArduinoPackage.FUNCTION_CALL: return createFunctionCall();
			case ArduinoPackage.PARAMETER_CALL: return createParameterCall();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case ArduinoPackage.TIME:
				return createTimeFromString(eDataType, initialValue);
			case ArduinoPackage.MODULE_KIND:
				return createModuleKindFromString(eDataType, initialValue);
			case ArduinoPackage.OPERATOR_KIND:
				return createOperatorKindFromString(eDataType, initialValue);
			case ArduinoPackage.LIBRARY:
				return createLibraryFromString(eDataType, initialValue);
			case ArduinoPackage.PARAMETER_TYPE:
				return createParameterTypeFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case ArduinoPackage.TIME:
				return convertTimeToString(eDataType, instanceValue);
			case ArduinoPackage.MODULE_KIND:
				return convertModuleKindToString(eDataType, instanceValue);
			case ArduinoPackage.OPERATOR_KIND:
				return convertOperatorKindToString(eDataType, instanceValue);
			case ArduinoPackage.LIBRARY:
				return convertLibraryToString(eDataType, instanceValue);
			case ArduinoPackage.PARAMETER_TYPE:
				return convertParameterTypeToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Hardware createHardware() {
		HardwareImpl hardware = new HardwareImpl();
		return hardware;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Platform createPlatform() {
		PlatformImpl platform = new PlatformImpl();
		return platform;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DigitalPin createDigitalPin() {
		DigitalPinImpl digitalPin = new DigitalPinImpl();
		return digitalPin;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AnalogPin createAnalogPin() {
		AnalogPinImpl analogPin = new AnalogPinImpl();
		return analogPin;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Sketch createSketch() {
		SketchImpl sketch = new SketchImpl();
		return sketch;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Project createProject() {
		ProjectImpl project = new ProjectImpl();
		return project;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Status createStatus() {
		StatusImpl status = new StatusImpl();
		return status;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Level createLevel() {
		LevelImpl level = new LevelImpl();
		return level;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Delay createDelay() {
		DelayImpl delay = new DelayImpl();
		return delay;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InputModule createInputModule() {
		InputModuleImpl inputModule = new InputModuleImpl();
		return inputModule;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OutputModule createOutputModule() {
		OutputModuleImpl outputModule = new OutputModuleImpl();
		return outputModule;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Connector createConnector() {
		ConnectorImpl connector = new ConnectorImpl();
		return connector;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Repeat createRepeat() {
		RepeatImpl repeat = new RepeatImpl();
		return repeat;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Sensor createSensor() {
		SensorImpl sensor = new SensorImpl();
		return sensor;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public While createWhile() {
		WhileImpl while_ = new WhileImpl();
		return while_;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Variable createVariable() {
		VariableImpl variable = new VariableImpl();
		return variable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Set createSet() {
		SetImpl set = new SetImpl();
		return set;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NumericalOperator createNumericalOperator() {
		NumericalOperatorImpl numericalOperator = new NumericalOperatorImpl();
		return numericalOperator;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BooleanOperator createBooleanOperator() {
		BooleanOperatorImpl booleanOperator = new BooleanOperatorImpl();
		return booleanOperator;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Constant createConstant() {
		ConstantImpl constant = new ConstantImpl();
		return constant;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public If createIf() {
		IfImpl if_ = new IfImpl();
		return if_;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Function createFunction() {
		FunctionImpl function = new FunctionImpl();
		return function;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ParameterDefinition createParameterDefinition() {
		ParameterDefinitionImpl parameterDefinition = new ParameterDefinitionImpl();
		return parameterDefinition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FunctionCall createFunctionCall() {
		FunctionCallImpl functionCall = new FunctionCallImpl();
		return functionCall;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ParameterCall createParameterCall() {
		ParameterCallImpl parameterCall = new ParameterCallImpl();
		return parameterCall;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Time createTimeFromString(EDataType eDataType, String initialValue) {
		Time result = Time.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertTimeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModuleKind createModuleKindFromString(EDataType eDataType, String initialValue) {
		ModuleKind result = ModuleKind.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertModuleKindToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OperatorKind createOperatorKindFromString(EDataType eDataType, String initialValue) {
		OperatorKind result = OperatorKind.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertOperatorKindToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Library createLibraryFromString(EDataType eDataType, String initialValue) {
		Library result = Library.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertLibraryToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ParameterType createParameterTypeFromString(EDataType eDataType, String initialValue) {
		ParameterType result = ParameterType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertParameterTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ArduinoPackage getArduinoPackage() {
		return (ArduinoPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static ArduinoPackage getPackage() {
		return ArduinoPackage.eINSTANCE;
	}

} //ArduinoFactoryImpl
