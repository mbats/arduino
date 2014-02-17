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
package fr.obeo.dsl.arduino.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.sirius.business.api.session.Session;

import fr.obeo.dsl.arduino.utils.ArduinoServices;
import fr.obeo.dsl.arduino.utils.ProjectServices;

public class OpenHardwareKitHandler extends AbstractHandler {
	ArduinoServices service = new ArduinoServices();
	ProjectServices projectServices = new ProjectServices();

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Session session = service.getSession();
		if (session != null) {
			if (ProjectServices.isViewpointEnabled(session,
					ProjectServices.HARDWARE_KIT_VP)) {
				projectServices
				.unActivateHardwareKitDefinition(new NullProgressMonitor());
			} else {
				projectServices
						.activateHardwareKitDefinition(new NullProgressMonitor());
			}
		}
		return null;
	}
}