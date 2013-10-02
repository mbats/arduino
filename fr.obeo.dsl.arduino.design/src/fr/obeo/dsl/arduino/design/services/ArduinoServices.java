package fr.obeo.dsl.arduino.design.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
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
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.google.common.collect.Lists;

import fr.obeo.dsl.arduino.AnalogPin;
import fr.obeo.dsl.arduino.ArduinoFactory;
import fr.obeo.dsl.arduino.Connector;
import fr.obeo.dsl.arduino.DigitalPin;
import fr.obeo.dsl.arduino.Instruction;
import fr.obeo.dsl.arduino.Module;
import fr.obeo.dsl.arduino.Pin;
import fr.obeo.dsl.arduino.Platform;
import fr.obeo.dsl.arduino.Sketch;
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

	public Instruction getInstructions(Instruction instruction) {
		return instruction.getNext();
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
}