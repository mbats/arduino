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

		if ("validHardware".equals(property) && service.isValidHardware()) {
			return true;
		}

		if ("validSketch".equals(property)
				&& service.isValidSketch(project.getSketch())) {
			return true;
		}
		return false;
	}
}
