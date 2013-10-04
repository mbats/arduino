package fr.obeo.dsl.arduino.design.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.google.common.collect.Lists;

import fr.obeo.dsl.arduino.AnalogPin;
import fr.obeo.dsl.arduino.ArduinoFactory;
import fr.obeo.dsl.arduino.BooleanOperator;
import fr.obeo.dsl.arduino.Connector;
import fr.obeo.dsl.arduino.DigitalPin;
import fr.obeo.dsl.arduino.Instruction;
import fr.obeo.dsl.arduino.Module;
import fr.obeo.dsl.arduino.OperatorKind;
import fr.obeo.dsl.arduino.Pin;
import fr.obeo.dsl.arduino.Platform;
import fr.obeo.dsl.arduino.Sensor;
import fr.obeo.dsl.arduino.Sketch;
import fr.obeo.dsl.arduino.Value;
import fr.obeo.dsl.arduino.Variable;
import fr.obeo.dsl.arduino.While;
import fr.obeo.dsl.arduino.design.Activator;
import fr.obeo.dsl.arduino.gen.main.Generate;
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

	public void upload(Sketch sketch) {
		System.out.println("Generate code");

		IFile file = ResourcesPlugin
				.getWorkspace()
				.getRoot()
				.getFile(
						new Path(sketch.eResource().getURI()
								.toPlatformString(true)));
		IFolder folder = file.getProject().getFolder("code");
		File genFolder = folder.getRawLocation().makeAbsolute().toFile();

		try {
			Generate generator = new Generate(sketch.eResource().getURI(),
					genFolder, new ArrayList<Object>());
			generator.doGenerate(new BasicMonitor());
		} catch (IOException e) {
			Activator.log(Status.ERROR, "Code generation failed", e);
		}
		executeCommand("make", genFolder, null);
		executeCommand("make", genFolder, "upload");
	}

	private void executeCommand(String command, File directory, String arg) {
		ProcessBuilder builder;
		if (arg != null) {
			builder = new ProcessBuilder(command, arg);
		} else {
			builder = new ProcessBuilder(command);
		}

		builder.directory(directory);
		Map<String, String> env = builder.environment();
		env.put("ARDUINO_DIR", "/usr/share/arduino");
		env.put("ARDMK_DIR", "/usr/share/arduino");
		env.put("ARDMK_PATH", "/usr/bin");
		env.put("AVR_TOOLS_DIR", "/usr");
		try {
			Process process = builder.start();
			inheritIO(process.getInputStream(), System.out);
			inheritIO(process.getErrorStream(), System.err);
			process.waitFor();
		} catch (IOException e) {
			Activator
					.log(Status.ERROR, "Run command " + command + " failed", e);
		} catch (InterruptedException e) {
			Activator
					.log(Status.ERROR, "Run command " + command + " failed", e);
		}
	}

	private static void inheritIO(final InputStream src, final PrintStream dest) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Scanner sc = new Scanner(src);
				while (sc.hasNextLine()) {
					dest.println(sc.nextLine());
				}
			}
		}).start();
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

	private String computeLabel(Value value) {
		if (value instanceof Variable) {
			return ((Variable) value).getName();
		}
		return value.getValue();
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

		// Clean unused values
		if (oldLeft != null && isNotUsedAnymore(sketch, oldLeft)) {
			EcoreUtil.delete(oldLeft);
		}
		if (oldRight != null && isNotUsedAnymore(sketch, oldRight)) {
			EcoreUtil.delete(oldRight);
		}
	}

	private boolean isNotUsedAnymore(Sketch sketch, Value value) {
		ResourceSet resourceSet = value.eResource().getResourceSet();
		ECrossReferenceAdapter adapter = new ECrossReferenceAdapter();
		resourceSet.eAdapters().add(adapter);
		Collection<Setting> refs = adapter.getInverseReferences(value, true);
		return refs.size() == 1;
	}
}