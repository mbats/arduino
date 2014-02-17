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

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.PlatformUI;

import fr.obeo.dsl.arduino.menus.ArduinoUiActivator;
import fr.obeo.dsl.arduino.utils.ArduinoServices;
import fr.obeo.dsl.arduino.utils.ProjectServices;

public class DeleteProjectHandler extends AbstractHandler {
	ArduinoServices service = new ArduinoServices();
	ProjectServices projectService = new ProjectServices();

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final IProject project = service.getWorkspaceProject();

		boolean confirmation = MessageDialog.openConfirm(PlatformUI
				.getWorkbench().getActiveWorkbenchWindow().getShell(),
				"Confirm",
				"Do you want to delete the project " + project.getName()
						+ " ? (cannot be undone)");
		if (confirmation) {
			projectService.closeOpenedEditors();

			try {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.run(false, false, new IRunnableWithProgress() {

							@Override
							public void run(IProgressMonitor monitor)
									throws InvocationTargetException,
									InterruptedException {
								try {
									project.delete(true, true, monitor);
								} catch (CoreException e) {
									ArduinoUiActivator.log(Status.ERROR,
											"Delete project failed", e);
								}
							}
						});
			} catch (InvocationTargetException e) {
				ArduinoUiActivator
						.log(Status.ERROR, "Delete project failed", e);
			} catch (InterruptedException e) {
				ArduinoUiActivator
						.log(Status.ERROR, "Delete project failed", e);
			}

		}
		return null;
	}

	public static IProject getCurrentSelectedProject() {
		IProject project = null;
		ISelectionService selectionService = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getSelectionService();

		ISelection selection = selectionService.getSelection();

		if (selection instanceof IStructuredSelection) {
			Object element = ((IStructuredSelection) selection)
					.getFirstElement();

			if (element instanceof IResource) {
				project = ((IResource) element).getProject();
			}
		}
		return project;
	}

}
