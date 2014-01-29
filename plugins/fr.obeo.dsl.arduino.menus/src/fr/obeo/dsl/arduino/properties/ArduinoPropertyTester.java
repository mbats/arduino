package fr.obeo.dsl.arduino.properties;

import org.eclipse.core.expressions.PropertyTester;

import fr.obeo.dsl.arduino.Project;
import fr.obeo.dsl.arduino.utils.ArduinoServices;

public class ArduinoPropertyTester extends PropertyTester {
	ArduinoServices service = new ArduinoServices();
	public ArduinoPropertyTester() {
	}

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		Project project = service.getArduinoProject();
		if (project == null) {
			return false;
		}

		if ("existHardware".equals(property) && project.getHardware() != null) {
			return true;
		}

		if ("existSketch".equals(property) && project.getSketch() != null) {
			return true;
		}

		if ("validSketch".equals(property)
				&& service.isValidSketch(project.getSketch())) {
			return true;
		}
		return false;
	}
}
