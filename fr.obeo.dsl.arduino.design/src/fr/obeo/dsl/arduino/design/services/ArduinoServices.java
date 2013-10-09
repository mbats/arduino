package fr.obeo.dsl.arduino.design.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import fr.obeo.dsl.arduino.AnalogPin;
import fr.obeo.dsl.arduino.ArduinoFactory;
import fr.obeo.dsl.arduino.BooleanOperator;
import fr.obeo.dsl.arduino.Connector;
import fr.obeo.dsl.arduino.Constant;
import fr.obeo.dsl.arduino.Control;
import fr.obeo.dsl.arduino.DigitalPin;
import fr.obeo.dsl.arduino.Instruction;
import fr.obeo.dsl.arduino.MathOperator;
import fr.obeo.dsl.arduino.Module;
import fr.obeo.dsl.arduino.NumericalOperator;
import fr.obeo.dsl.arduino.OperatorKind;
import fr.obeo.dsl.arduino.Pin;
import fr.obeo.dsl.arduino.Platform;
import fr.obeo.dsl.arduino.Sensor;
import fr.obeo.dsl.arduino.Set;
import fr.obeo.dsl.arduino.Sketch;
import fr.obeo.dsl.arduino.Value;
import fr.obeo.dsl.arduino.Variable;
import fr.obeo.dsl.arduino.While;
import fr.obeo.dsl.viewpoint.business.api.session.Session;
import fr.obeo.dsl.viewpoint.business.api.session.SessionManager;

public class ArduinoServices {

	private static final String IMAGES_PATH = "/fr.obeo.dsl.arduino.design/images/";

	public void updateDigitalPins(Platform platform, String totalOfPins) {
		List<DigitalPin> pinsTmp = new ArrayList<DigitalPin>();
		pinsTmp.addAll(platform.getDigitalPins());

		int total = Integer.parseInt(totalOfPins);
		if (total > platform.getDigitalPins().size()) {
			// Create missing pins
			for (int i = pinsTmp.size(); i < total; i++) {
				DigitalPin pin = ArduinoFactory.eINSTANCE.createDigitalPin();
				pin.setId(i);
				platform.getDigitalPins().add(pin);
			}
		} else {
			if (total > 0) {
				// Delete the other pins
				for (int i = total; i < pinsTmp.size(); i++) {
					DigitalPin pin = pinsTmp.get(i);
					EcoreUtil.delete(pin);
				}
			} else {
				platform.getDigitalPins().clear();
			}
		}
	}

	public void updateAnalogPins(Platform platform, String totalOfPins) {
		List<AnalogPin> pinsTmp = new ArrayList<AnalogPin>();
		pinsTmp.addAll(platform.getAnalogPins());

		int total = Integer.parseInt(totalOfPins);
		if (total > platform.getAnalogPins().size()) {
			// Create missing pins
			for (int i = pinsTmp.size(); i < total; i++) {
				AnalogPin pin = ArduinoFactory.eINSTANCE.createAnalogPin();
				pin.setId(i);
				platform.getAnalogPins().add(pin);
			}
		} else {
			if (total > 0) {
				// Delete the other pins
				for (int i = total; i < pinsTmp.size(); i++) {
					AnalogPin pin = pinsTmp.get(i);
					EcoreUtil.delete(pin);
				}
			} else {
				platform.getAnalogPins().clear();
			}
		}
	}

	public boolean isValidConnector(Module module, Pin pin) {
		switch (module.getKind()) {
		case DIGITAL:
			return pin instanceof DigitalPin && getModule(pin) == null;

		default:
			return pin instanceof AnalogPin && getModule(pin) == null;
		}
	}

	public Module getModule(Pin pin) {
		List<Connector> connectors = getConnectors(pin);
		for (Connector connector : connectors) {
			if (connector.getPin().equals(pin)) {
				return connector.getModule();
			}
		}
		return null;
	}

	public List<Instruction> getInstructions(Instruction instruction) {
		List<Instruction> instructions = Lists.newArrayList();
		if (instruction instanceof Sensor) {
			instructions.addAll(((Sensor) instruction).getStatus());
		}
		instructions.add(instruction.getNext());
		return instructions;
	}

	public String getImage(Module module) {
		String imageName = module.getImage();
		return getImage(imageName);
	}

	public String getImage(Platform platform) {
		String imageName = platform.getImage();
		return getImage(imageName);
	}

	private String getImage(String imageName) {
		if (imageName != null && imageName.length() > 0) {
			return IMAGES_PATH + imageName;
		}
		return IMAGES_PATH + "default.svg";
	}



	public List<Platform> getPlatforms(EObject object) {
		List<Platform> result = Lists.newArrayList();
		Session session = SessionManager.INSTANCE.getSession(object);

		for (Resource resource : session.getSemanticResources()) {
			for (Iterator<EObject> iterator = resource.getAllContents(); iterator
					.hasNext();) {
				EObject content = iterator.next();
				if (content instanceof Platform) {
					result.add((Platform) content);
				}
			}
		}

		return result;
	}

	public List<Module> getModules(EObject object) {
		List<Module> result = Lists.newArrayList();
		Session session = SessionManager.INSTANCE.getSession(object);

		for (Resource resource : session.getSemanticResources()) {
			for (Iterator<EObject> iterator = resource.getAllContents(); iterator
					.hasNext();) {
				EObject content = iterator.next();
				if (content instanceof Module) {
					result.add((Module) content);
				}
			}
		}
		return result;
	}

	public List<Module> getConnectedModules(Sketch sketch) {
		List<Module> result = Lists.newArrayList();
		for (Connector connector : sketch.getHardware().getConnectors()) {
			result.add(connector.getModule());
		}
		return result;
	}

	public List<Connector> getConnectors(EObject object) {
		List<Connector> result = Lists.newArrayList();
		Session session = SessionManager.INSTANCE.getSession(object);

		for (Resource resource : session.getSemanticResources()) {
			for (Iterator<EObject> iterator = resource.getAllContents(); iterator
					.hasNext();) {
				EObject content = iterator.next();
				if (content instanceof Connector) {
					result.add((Connector) content);
				}
			}
		}
		return result;
	}

	public String computeStatusLabel(fr.obeo.dsl.arduino.Status status) {
		String label = status.getModule().getName();
		if (!hasSensorStatus(status)) {
			if (status.isStatus()) {
				label += " : off";
			} else {
				label += " : on";
			}
		}
		return label;
	}

	public String computeLevelLabel(fr.obeo.dsl.arduino.Level level) {
		String label = level.getModule().getName();
		if (level.getLevel() != null) {
			label += "=";
			Value value = level.getLevel();
			if (value instanceof Variable) {
				label += ((Variable) value).getName();
			} else {
				label += computeLabel(value);
			}
		}
		return label;
	}

	public String computeLabel(Value value) {
		if (value instanceof Variable) {
			return ((Variable) value).getName();
		}
		if (value instanceof Constant) {
			return value.getValue();
		}
		if (value instanceof MathOperator) {
			return "(" + computeLabel(((MathOperator) value).getLeft())
					+ getOperator(((MathOperator) value).getOperator())
					+ computeLabel(((MathOperator) value).getRight()) + ")";
		}
		return null;
	}

	private boolean hasSensorStatus(fr.obeo.dsl.arduino.Status status) {
		return status.getSensor() != null;
	}

	public String computeLabel(While instruction) {
		String label = "While ";
		if (instruction.getCondition() != null
				&& instruction.getCondition().getLeft() != null
				&& instruction.getCondition().getRight() != null) {
			BooleanOperator condition = instruction.getCondition();
			label += computeLabel(condition.getLeft()) + " ";
			label += getOperator(condition.getOperator());
			label += " " + computeLabel(condition.getRight());
		}
		return label;
	}

	public String computeLabel(Set set) {
		String label = "Set ";
		if (set.getVariable() != null && set.getValue() != null) {
			label += set.getVariable().getName() + " = "
					+ computeLabel(set.getValue());
		}
		return label;
	}

	public String computeLabel(String operator) {
		return operator;
	}

	private String getOperator(OperatorKind operator) {
		switch (operator) {
		case AND:
			return "&";
		case DIFF:
			return "!=";
		case DIV:
			return "/";
		case EQUAL:
			return "=";
		case LOWER:
			return "<";
		case LOWER_OR_EQUAL:
			return "<=";
		case MAX:
			return "max";
		case MIN:
			return "min";
		case MINUS:
			return "-";
		case MUL:
			return "*";
		case NOT:
			return "not";
		case OR:
			return "or";
		case PLUS:
			return "+";
		case POURCENT:
			return "%";
		case UPPER:
			return ">";
		case UPPER_OR_EQUAL:
			return ">=";
		}
		return null;
	}

	public OperatorKind getOperator(String operator) {
		if (operator.equals("&")) {
			return OperatorKind.AND;
		}
		if (operator.equals("!=")) {
			return OperatorKind.DIFF;
		}
		if (operator.equals("/")) {
			return OperatorKind.DIV;
		}
		if (operator.equals("=")) {
			return OperatorKind.EQUAL;
		}
		if (operator.equals("<")) {
			return OperatorKind.LOWER;
		}
		if (operator.equals("<=")) {
			return OperatorKind.LOWER_OR_EQUAL;
		}
		if (operator.equals("max")) {
			return OperatorKind.MAX;
		}
		if (operator.equals("min")) {
			return OperatorKind.MIN;
		}
		if (operator.equals("-")) {
			return OperatorKind.MINUS;
		}
		if (operator.equals("*")) {
			return OperatorKind.MUL;
		}
		if (operator.equals("not")) {
			return OperatorKind.NOT;
		}
		if (operator.equals("or")) {
			return OperatorKind.OR;
		}
		if (operator.equals("+")) {
			return OperatorKind.PLUS;
		}
		if (operator.equals("%")) {
			return OperatorKind.POURCENT;
		}
		if (operator.equals(">")) {
			return OperatorKind.UPPER;
		}
		if (operator.equals(">=")) {
			return OperatorKind.UPPER_OR_EQUAL;
		}
		return null;
	}

	public Value getValue(Sketch sketch, String value) {
		for (Instruction instruction : sketch.getInstructions()) {
			if (instruction instanceof Value
					&& value.equals(((Value) instruction).getValue())) {
				return (Value) instruction;
			}
			if (instruction instanceof Variable
					&& value.equals(((Variable) instruction).getName())) {
				return (Value) instruction;
			}
		}
		if (isInteger(value)) {
			fr.obeo.dsl.arduino.Constant constant = ArduinoFactory.eINSTANCE
					.createConstant();
			constant.setValue(value);
			sketch.getInstructions().add(constant);
			return constant;
		}
		Variable var = ArduinoFactory.eINSTANCE.createVariable();
		var.setName(value);
		var.setValue("0");
		sketch.getInstructions().add(var);
		return var;
	}

	private boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public void editLabel(While instruction, Sketch sketch, String left,
			String operator, String right) {
		BooleanOperator condition = instruction.getCondition();
		if (condition == null) {
			condition = ArduinoFactory.eINSTANCE.createBooleanOperator();
			sketch.getInstructions().add(condition);
			instruction.setCondition(condition);
		}

		Value oldLeft = condition.getLeft();
		Value oldRight = condition.getRight();

		condition.setLeft(getValue(sketch, left));
		condition.setOperator(getOperator(operator));
		condition.setRight(getValue(sketch, right));

		deleteUnusedValue(sketch, oldLeft);
		deleteUnusedValue(sketch, oldRight);
	}

	public void editLabel(Set instruction, Sketch sketch, String variable,
			String value) {
		Value oldVariable = instruction.getVariable();
		Value oldValue = instruction.getValue();
		instruction.setVariable((Variable) getValue(sketch, variable));
		instruction.setValue(getValue(sketch, value));

		// Clean unused values
		deleteUnusedValue(sketch, oldVariable);
		deleteUnusedValue(sketch, oldValue);
	}

	public void deleteUnusedValues(Sketch sketch) {
		ImmutableList<Instruction> instructions = ImmutableList.copyOf(sketch
				.getInstructions());
		for (Instruction instruction : instructions) {
			if (instruction instanceof Value) {
				deleteUnusedValue(sketch, (Value) instruction);
			}
		}
	}

	private void deleteUnusedValue(Sketch sketch, Value value) {
		if (value != null && isNotUsedAnymore(sketch, value)) {
			EcoreUtil.delete(value);
		}
	}

	private boolean isNotUsedAnymore(Sketch sketch, Value value) {
		ResourceSet resourceSet = value.eResource().getResourceSet();
		ECrossReferenceAdapter adapter = new ECrossReferenceAdapter();
		resourceSet.eAdapters().add(adapter);
		Collection<Setting> refs = adapter.getInverseReferences(value, true);
		return refs.size() == 1;
	}

	public List<Variable> getVariables(EObject container) {
		List<Variable> variables = Lists.newArrayList();
		if (container instanceof Set) {
			variables.add(((Set) container).getVariable());
		} else if (container instanceof MathOperator) {
			Value left = ((MathOperator) container).getLeft();
			Value right = ((MathOperator) container).getRight();
			if (left instanceof Variable) {
				variables.add((Variable) left);
			}
			if (right instanceof Variable) {
				variables.add((Variable) right);
			}
		}
		return variables;
	}

	public List<NumericalOperator> getNumericalOperators(EObject container) {
		List<NumericalOperator> operators = Lists.newArrayList();
		if (container instanceof Set) {
			Value value = ((Set) container).getValue();
			if (value instanceof NumericalOperator) {
				operators.add((NumericalOperator) value);
			}
		} else if (container instanceof MathOperator) {
			Value left = ((MathOperator) container).getLeft();
			Value right = ((MathOperator) container).getRight();
			if (left instanceof NumericalOperator) {
				operators.add((NumericalOperator) left);
			}
			if (right instanceof NumericalOperator) {
				operators.add((NumericalOperator) right);
			}
		}
		return operators;
	}

	public List<BooleanOperator> getBooleanOperators(EObject container) {
		List<BooleanOperator> operators = Lists.newArrayList();
		if (container instanceof Sketch) {
			List<Instruction> instructions = ((Sketch) container)
					.getInstructions();
			for (Instruction instruction : instructions) {
				if (instruction instanceof While) {
					BooleanOperator condition = ((While) instruction)
							.getCondition();
					if (condition != null) {
						operators.add(condition);
					}
				}
			}
		} else if (container instanceof Control) {
			List<Instruction> instructions = ((Control) container)
					.getInstructions();
			for (Instruction instruction : instructions) {
				if (instruction instanceof BooleanOperator) {
					operators.add((BooleanOperator) instruction);
				}
			}
		} else if (container instanceof MathOperator) {
			Instruction left = ((MathOperator) container).getLeft();
			Instruction right = ((MathOperator) container).getRight();
			if (left instanceof BooleanOperator) {
				operators.add((BooleanOperator) left);
			}
			if (right instanceof BooleanOperator) {
				operators.add((BooleanOperator) right);
			}
		}

		return operators;
	}

	public List<Constant> getConstants(EObject container) {
		List<Constant> constants = Lists.newArrayList();
		if (container instanceof Set) {
			Value value = ((Set) container).getValue();
			if (value instanceof Constant) {
				constants.add((Constant) value);
			}
		} else if (container instanceof MathOperator) {
			Value left = ((MathOperator) container).getLeft();
			Value right = ((MathOperator) container).getRight();
			if (left instanceof Constant) {
				constants.add((Constant) left);
			}
			if (right instanceof Constant) {
				constants.add((Constant) right);
			}
		}
		return constants;
	}

	public Constant createConstant(Sketch sketch, int value) {
		for (Iterator<EObject> iterator = sketch.eAllContents(); iterator
				.hasNext();) {
			EObject object = iterator.next();
			if (object instanceof Constant) {
				if (((Constant) object).getValue().equals(value)) {
					return (Constant) object;
				}
			}
		}

		Constant constant = ArduinoFactory.eINSTANCE
					.createConstant();
		constant.setValue(String.valueOf(value));
			sketch.getInstructions().add(constant);
		return constant;
	}
}
