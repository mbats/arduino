package fr.obeo.dsl.arduino.wizard;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.PlatformUI;

import fr.obeo.dsl.arduino.design.services.ProjectServices;
import fr.obeo.dsl.viewpoint.ui.tools.internal.wizards.ModelingProjectWizard;

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
}
