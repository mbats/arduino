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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

import fr.obeo.dsl.arduino.utils.ProjectServices;

public class ArduinoModelingProjectCreationOperation extends
		WorkspaceModifyOperation {

	private IProject project;

	public ArduinoModelingProjectCreationOperation(IProject project) {
		this.project = project;
	}

	@Override
	protected void execute(IProgressMonitor monitor) throws CoreException,
			InvocationTargetException, InterruptedException {
		try {
			final ProjectServices service = new ProjectServices();

			monitor.beginTask("Load project", 100);
			monitor.subTask("Close project");
			service.closeProjects(monitor);
			monitor.worked(50);
			monitor.subTask("Create project");
			service.createProject(monitor, project);
			monitor.worked(50);
		} finally {
			monitor.done();
		}
	}
}
