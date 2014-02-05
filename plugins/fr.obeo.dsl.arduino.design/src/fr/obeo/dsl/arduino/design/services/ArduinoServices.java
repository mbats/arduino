package fr.obeo.dsl.arduino.design.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.sirius.business.api.dialect.DialectManager;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.ui.business.api.dialect.DialectUIManager;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.description.RepresentationDescription;
import org.eclipse.sirius.viewpoint.description.Viewpoint;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import fr.obeo.dsl.arduino.AnalogPin;
import fr.obeo.dsl.arduino.ArduinoFactory;
import fr.obeo.dsl.arduino.BooleanOperator;
import fr.obeo.dsl.arduino.Connector;
import fr.obeo.dsl.arduino.Constant;
import fr.obeo.dsl.arduino.Control;
import fr.obeo.dsl.arduino.DigitalPin;
import fr.obeo.dsl.arduino.Hardware;
import fr.obeo.dsl.arduino.If;
import fr.obeo.dsl.arduino.Instruction;
import fr.obeo.dsl.arduino.Level;
import fr.obeo.dsl.arduino.MathOperator;
import fr.obeo.dsl.arduino.Module;
import fr.obeo.dsl.arduino.ModuleInstruction;
import fr.obeo.dsl.arduino.NumericalOperator;
import fr.obeo.dsl.arduino.OperatorKind;
import fr.obeo.dsl.arduino.OutputModule;
import fr.obeo.dsl.arduino.Pin;
import fr.obeo.dsl.arduino.Platform;
import fr.obeo.dsl.arduino.Project;
import fr.obeo.dsl.arduino.Sensor;
import fr.obeo.dsl.arduino.Set;
import fr.obeo.dsl.arduino.Sketch;
import fr.obeo.dsl.arduino.Status;
import fr.obeo.dsl.arduino.Value;
import fr.obeo.dsl.arduino.Variable;
import fr.obeo.dsl.arduino.While;

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
			if (((Sensor) instruction).getStatus().size() > 0) {
				instructions.addAll(((Sensor) instruction).getStatus());
			} else {
				ResourceSet resourceSet = instruction.eResource()
						.getResourceSet();
				ECrossReferenceAdapter adapter = new ECrossReferenceAdapter();
				resourceSet.eAdapters().add(adapter);
				Collection<Setting> refs = adapter.getInverseReferences(
						instruction, true);
				for (Setting setting : refs) {
					if (setting.getEObject() instanceof Level) {
						instructions.add((Instruction) setting.getEObject());
					}
				}
			}
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

	public List<Module> getStatusModules(Sketch sketch) {
		List<Module> result = new ArrayList<Module>();
		List<Module> modules = ImmutableList
				.copyOf(getConnectedModules(sketch));

		for (Module module : modules) {
			if (module instanceof OutputModule) {
				result.add(module);
			}
		}
		return result;
	}

	public List<Module> getLevelModules(Sketch sketch) {
		List<Module> result = new ArrayList<Module>();
		List<Module> modules = ImmutableList.copyOf(getStatusModules(sketch));

		for (Module module : modules) {
			if (module.isLevel()) {
				result.add(module);
			}
		}
		return result;
	}

	public Sketch getSketch(EObject eObject) {
		if (eObject instanceof Sketch) {
			return (Sketch) eObject;
		}

		while (eObject != null && !(eObject instanceof Sketch)) {
			eObject = eObject.eContainer();
		}

		return (Sketch) eObject;
	}

	public List<Module> getConnectedModules(Sketch sketch) {
		return getConnectedModules(sketch.getHardware());
	}

	private List<Module> getConnectedModules(Hardware hardware) {
		List<Module> result = Lists.newArrayList();
		for (Connector connector : hardware.getConnectors()) {
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
			label += computeLabel(value);
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
		if (value instanceof Sensor) {
			return ((Sensor) value).getModule().getName();
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

	public String computeLabelOperator(OperatorKind operator) {
		return getOperator(operator);
	}

	public String computeLabelOperator(MathOperator operator) {
		return getOperator(operator.getOperator());
	}

	public String computeLabel(Sensor instruction) {
		return instruction.getModule().getName();
	}

	public String computeLabel(If instruction) {
		String label = "If ";
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
		return getOperator(getOperator(operator));
	}

	public String getOperator(OperatorKind operator) {
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
		if (operator.equals("&") || operator.equals("and")) {
			return OperatorKind.AND;
		}
		if (operator.equals("!=") || operator.equals("diff")) {
			return OperatorKind.DIFF;
		}
		if (operator.equals("/") || operator.equals("div")) {
			return OperatorKind.DIV;
		}
		if (operator.equals("=") || operator.equals("equal")) {
			return OperatorKind.EQUAL;
		}
		if (operator.equals("<") || operator.equals("lower")) {
			return OperatorKind.LOWER;
		}
		if (operator.equals("<=") || operator.equals("lowerOrEqual")) {
			return OperatorKind.LOWER_OR_EQUAL;
		}
		if (operator.equals("max")) {
			return OperatorKind.MAX;
		}
		if (operator.equals("min")) {
			return OperatorKind.MIN;
		}
		if (operator.equals("-") || operator.equals("minus")) {
			return OperatorKind.MINUS;
		}
		if (operator.equals("*") || operator.equals("mul")) {
			return OperatorKind.MUL;
		}
		if (operator.equals("not")) {
			return OperatorKind.NOT;
		}
		if (operator.equals("or")) {
			return OperatorKind.OR;
		}
		if (operator.equals("+") || operator.equals("plus")) {
			return OperatorKind.PLUS;
		}
		if (operator.equals("%") || operator.equals("pourcent")) {
			return OperatorKind.POURCENT;
		}
		if (operator.equals(">") || operator.equals("upper")) {
			return OperatorKind.UPPER;
		}
		if (operator.equals(">=") || operator.equals("upperOrEqual")) {
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

	public void editLabel(Value value, String newValue) {

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

	public List<Sensor> getSensors(EObject container) {
		List<Sensor> sensors = Lists.newArrayList();
		if (container instanceof Sketch) {
			List<Instruction> instructions = ((Sketch) container)
					.getInstructions();
			for (Instruction instruction : instructions) {
				if (instruction instanceof Status) {
					if (((Status) instruction).getSensor() != null) {
						sensors.add(((Status) instruction).getSensor());
					}
				}
				if (instruction instanceof Sensor) {
					if (instruction.getNext() == null
							|| isNotUsedAnymore((Sketch) container,
									(Sensor) instruction)) {
						sensors.add((Sensor) instruction);
					}
				}
			}
		} else if (container instanceof MathOperator) {
			Instruction left = ((MathOperator) container).getLeft();
			Instruction right = ((MathOperator) container).getRight();
			if (left instanceof Sensor) {
				sensors.add((Sensor) left);
			}
			if (right instanceof Sensor) {
				sensors.add((Sensor) right);
			}
		}
		return sensors;
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
				} else if (instruction instanceof If) {
					BooleanOperator condition = ((If) instruction)
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
				} else if (instruction instanceof While) {
					BooleanOperator condition = ((While) instruction)
							.getCondition();
					if (condition != null) {
						operators.add(condition);
					}
				} else if (instruction instanceof If) {
					BooleanOperator condition = ((If) instruction)
							.getCondition();
					if (condition != null) {
						operators.add(condition);
					}
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

		Constant constant = ArduinoFactory.eINSTANCE.createConstant();
		constant.setValue(String.valueOf(value));
		sketch.getInstructions().add(constant);
		return constant;
	}

	public Instruction getLastInstruction(Sketch sketch) {
		if (sketch != null) {
			Instruction instruction = sketch;
			while (instruction != null && instruction.getNext() != null
					&& !(instruction.getNext() instanceof Sketch)) {
				instruction = instruction.getNext();
			}

			if (instruction != null && instruction.getNext() instanceof Sketch) {
				return sketch;
			}
			return instruction;
		}

		return null;
	}

	public Instruction getLastInstruction(Control control) {
		if (control != null && control.getInstructions().size() > 0) {
			Instruction instruction = control.getInstructions().get(0);
			while (instruction != null && instruction.getNext() != null) {
				instruction = instruction.getNext();
			}

			if (control.getInstructions().size() > 1) {
				return instruction;
			}
		}

		return null;
	}

	public boolean isLastInstructionValid(Sketch container) {
		if (getLastInstruction(container) != null) {
			return true;
		}
		return false;
	}

	public boolean isLastInstructionValid(Control container) {
		if (getLastInstruction(container) != null) {
			return true;
		}
		return false;
	}

	public void removeWire(Hardware hardware, Module module) {
		List<Connector> connectors = getConnectors(hardware);
		for (Connector connector : connectors) {
			if (connector.getModule().equals(module)) {
				EcoreUtil.delete(connector);
			}
		}
	}

	public void removeWire(Hardware hardware, Platform platform) {
		List<Connector> connectors = getConnectors(hardware);
		for (Connector connector : connectors) {
			if (connector.getPin().eContainer().equals(platform)) {
				EcoreUtil.delete(connector);
			}
		}
	}

	public boolean isInvalidSketch(Project project) {
		Sketch sketch = project.getSketch();
		if (sketch == null) {
			return true;
		}
		fr.obeo.dsl.arduino.utils.ArduinoServices service = new fr.obeo.dsl.arduino.utils.ArduinoServices();
		return !(service.isValidSketch(sketch));
	}

	public boolean isInvalidHardware(Project project) {
		return project.getHardware() == null
				|| project.getHardware().getPlatforms().size() == 0
				|| project.getHardware().getModules().size() == 0
				|| getConnectedModules(project.getHardware()).size() == 0;
	}

	public boolean isValidHardware(Project project) {
		return !isInvalidHardware(project);
	}

	public boolean isUploadable(Project project) {
		return isValidHardware(project) && !isInvalidSketch(project);
	}

	public String getImage(ModuleInstruction instruction) {
		return "/fr.obeo.dsl.arduino.design/images/"
				+ instruction.getModule().getImage();
	}

	public void updateValue(Level level, String newValue) {
		newValue = newValue.trim();
		Sketch sketch = getSketch(level);
		Value value = getValue(sketch, newValue);
		level.setLevel(value);
		deleteUnusedValues(sketch);
	}

	public void addVariable(Instruction container, Variable variable) {
		if (container instanceof MathOperator) {
			addMathOperatorValue((MathOperator) container, variable);
		} else if (container instanceof Set) {
			((Set) container).setVariable(variable);
		}
		deleteUnusedValues(getSketch(variable));
	}

	public void addValue(Instruction container, Constant value) {
		if (container instanceof MathOperator) {
			addMathOperatorValue((MathOperator) container, value);
		} else if (container instanceof Set) {
			((Set) container).setValue(value);
		}
		deleteUnusedValues(getSketch(value));
	}

	private void addMathOperatorValue(MathOperator container, Value value) {
		Value left = container.getLeft();
		Value right = container.getRight();

		if (left == null && right == null) {
			container.setLeft(value);
		} else if (left != null && right == null) {
			container.setRight(value);
		} else if (left == null && right != null) {
			container.setLeft(value);
		} else if (left != null && right != null) {
			container.setLeft(value);
		}
		deleteUnusedValues(getSketch(value));
	}

	public void updateValue(Instruction container, Value newValue,
			Value oldValue) {
		if (container instanceof Set) {
			if (newValue instanceof Variable) {
				((Set) container).setVariable((Variable) newValue);
			} else {
				((Set) container).setValue(newValue);
			}
		} else if (container instanceof MathOperator) {
			Value left = ((MathOperator) container).getLeft();
			Value right = ((MathOperator) container).getRight();
			if (oldValue.equals(left)) {
				((MathOperator) container).setLeft(newValue);
			} else if (oldValue.equals(right)) {
				((MathOperator) container).setRight(newValue);
			}

		} else if (container instanceof Level) {
			((Level) container).setLevel(newValue);
		}
		deleteUnusedValues(getSketch(container));
	}

	public void openHardwareDiagram(Hardware hardware) {
		Session session = SessionManager.INSTANCE.getSession(hardware);
		DRepresentation hardwareDiagram = getHardwareDiagram(hardware);
		DialectUIManager.INSTANCE.openEditor(session, hardwareDiagram,
				new NullProgressMonitor());
	}

	private RepresentationDescription getHardwareDiagramDescription(
			Session session) {
		for (Viewpoint vp : session.getSelectedViewpoints(false)) {
			for (RepresentationDescription representationDescription : vp
					.getOwnedRepresentations()) {
				if (representationDescription.getName().equals(
						"Hardware Sketch")) {
					return representationDescription;
				}
			}
		}
		return null;
	}

	private DRepresentation getHardwareDiagram(Hardware hardware) {
		fr.obeo.dsl.arduino.utils.ArduinoServices service = new fr.obeo.dsl.arduino.utils.ArduinoServices();
		Session session = SessionManager.INSTANCE.getSession(hardware);
		DRepresentation hardwareDiagram = service.getHardwareDiagram(session);
		// Create representation if does not exist
		if (hardwareDiagram == null) {
			hardwareDiagram = (DDiagram) DialectManager.INSTANCE
					.createRepresentation("Hardware", hardware,
							getHardwareDiagramDescription(session), session,
							new NullProgressMonitor());
		}

		return hardwareDiagram;
	}

}
