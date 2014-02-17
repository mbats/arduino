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
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.PlatformUI;

import fr.obeo.dsl.arduino.utils.ArduinoServices;
import fr.obeo.dsl.arduino.utils.ProjectServices;

public class OpenProjectHandler extends AbstractHandler {
	ArduinoServices service = new ArduinoServices();
	ProjectServices projectServices = new ProjectServices();

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		FileDialog dialog = new FileDialog(PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell(), SWT.OPEN);
		dialog.setFilterExtensions(new String[] { ".project" });
		final String result = dialog.open();
		if (result != null) {
			projectServices.openProject(result);
		}
		return null;
	}

}
