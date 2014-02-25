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

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.sirius.common.tools.api.util.ReflectionHelper;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

import fr.obeo.dsl.arduino.menus.ArduinoUiActivator;

public class ArduinoProjectWizard extends BasicNewProjectResourceWizard {
	private WizardNewProjectCreationPage wizardNewProjectCreationPage;
	protected IWorkbench workbench;

	public ArduinoProjectWizard() {
		super();
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		super.init(workbench, selection);
		this.workbench = workbench;
		this.selection = selection;
		setWindowTitle("New Arduino Project"); //$NON-NLS-1$ 
		setNeedsProgressMonitor(true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addPages() {
		wizardNewProjectCreationPage = new WizardNewProjectCreationPage(
				"Create a new Arduino project") {

			@Override
			public void createControl(Composite parent) {
				super.createControl(parent);

				Composite control = (Composite) getControl();
				GridLayout layout = new GridLayout();
				control.setLayout(layout);
				Dialog.applyDialogFont(control);
			}
		};

		wizardNewProjectCreationPage.setInitialProjectName("NewArduinoProject");//$NON-NLS-1$ 
		wizardNewProjectCreationPage.setTitle("Create a new Arduino project"); //$NON-NLS-1$ 
		wizardNewProjectCreationPage.setDescription("Enter a project name"); //$NON-NLS-1$    

		// Fix for VP-3711 to avoid a NPE on 3.8
		ReflectionHelper.setFieldValueWithoutException(this,
				"newProjectCreationPage", wizardNewProjectCreationPage);

		addPage(wizardNewProjectCreationPage);
	}

	@Override
	public boolean performFinish() {
		final ArduinoModelingProjectCreationOperation arduinoModelingProjectCreationOperation = new ArduinoModelingProjectCreationOperation(
				wizardNewProjectCreationPage.getProjectHandle());

		try {
			getContainer().run(true, false,
					arduinoModelingProjectCreationOperation);
		} catch (InvocationTargetException e) {
			ArduinoUiActivator.log(Status.ERROR, "Create project failed", e);
		} catch (InterruptedException e) {
			ArduinoUiActivator.log(Status.ERROR, "Create project failed", e);
		}
		return true;
	}
}
