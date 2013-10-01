package fr.obeo.dsl.arduino.wizard;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;

import fr.obeo.dsl.arduino.design.services.ArduinoServices;
import fr.obeo.dsl.arduino.design.services.ProjectServices;
import fr.obeo.dsl.viewpoint.ui.tools.internal.wizards.ModelingProjectWizard;

public class ArduinoProjectWizard extends ModelingProjectWizard {
	@Override
	public boolean performFinish() {
		// Close all existing projects
		ProjectServices service = new ProjectServices();
		service.closeProjects();

		// Create new project or open it if already exists
		super.performFinish();


		// Init model and activate arduino viewpoints, open dashboard
		service.createProject();

		return true;
	}



}
