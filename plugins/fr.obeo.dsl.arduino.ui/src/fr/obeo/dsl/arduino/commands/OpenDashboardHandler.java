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
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import fr.obeo.dsl.arduino.menus.ArduinoUiActivator;
import fr.obeo.dsl.arduino.utils.ArduinoServices;
import fr.obeo.dsl.arduino.utils.ProjectServices;
import fr.obeo.dsl.arduino.views.DashboardView;

public class OpenDashboardHandler extends AbstractHandler {
	ArduinoServices service = new ArduinoServices();
	ProjectServices projectServices = new ProjectServices();

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			HandlerUtil.getActiveWorkbenchWindow(event).getActivePage()
					.showView(DashboardView.VIEW_ID);
		} catch (PartInitException e) {
			ArduinoUiActivator.log(Status.ERROR, "Open dashboard failed", e);
		}
		return null;
	}
}
