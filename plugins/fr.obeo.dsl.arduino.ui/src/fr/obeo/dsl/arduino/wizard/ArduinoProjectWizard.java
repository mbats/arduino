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
package fr.obeo.dsl.arduino.wizard;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.sirius.ui.tools.internal.wizards.ModelingProjectWizard;
import org.eclipse.ui.PlatformUI;

import fr.obeo.dsl.arduino.menus.ArduinoUiActivator;
import fr.obeo.dsl.arduino.utils.ProjectServices;

public class ArduinoProjectWizard extends ModelingProjectWizard {
	@Override
	public boolean performFinish() {
		// Close all existing projects
		final ProjectServices service = new ProjectServices();

		try {
			getContainer().run(false, false, new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException {
					monitor.beginTask("Close all projects", 100);
					service.closeProjects(monitor);
					monitor.done();
				}
			});

			// Create new project or open it if already exists
			super.performFinish();

			// Init model and activate arduino viewpoints, open dashboard
			PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.run(false, false, new IRunnableWithProgress() {

						@Override
						public void run(IProgressMonitor monitor)
								throws InvocationTargetException,
								InterruptedException {
							service.createProject(monitor);
						}
					});
		} catch (InvocationTargetException e) {
			ArduinoUiActivator.log(Status.ERROR, "Create project failed", e);
		} catch (InterruptedException e) {
			ArduinoUiActivator.log(Status.ERROR, "Create project failed", e);
		}
		return true;
	}
}
