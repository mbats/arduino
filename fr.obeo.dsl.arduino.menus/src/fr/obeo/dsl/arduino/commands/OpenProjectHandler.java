package fr.obeo.dsl.arduino.commands;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.sirius.business.api.dialect.DialectManager;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.ui.business.api.dialect.DialectUIManager;
import org.eclipse.sirius.viewpoint.DDiagram;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.PlatformUI;

import fr.obeo.dsl.arduino.menus.ArduinoUiActivator;
import fr.obeo.dsl.arduino.utils.ArduinoServices;
import fr.obeo.dsl.arduino.utils.ProjectServices;

public class OpenProjectHandler extends AbstractHandler {
	ArduinoServices service = new ArduinoServices();

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		FileDialog dialog = new FileDialog(PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell(), SWT.OPEN);
		dialog.setFilterExtensions(new String[] { ".project" });
		final String result = dialog.open();

		service.closeOpenedEditors();
		openProject(result);

		openDashboard();

		return null;
	}

	private void openDashboard() {
		Session session = service.getSession();
		DialectUIManager.INSTANCE.openEditor(session,
				getDashboardDiagram(session), new NullProgressMonitor());

	}

	private void openProject(final String projectPath) {
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.run(true, true, new IRunnableWithProgress() {
						@Override
						public void run(IProgressMonitor monitor) {
							monitor.beginTask("Open project" + projectPath, 100);
							ProjectServices service = new ProjectServices();
							service.closeProjects(monitor);
							monitor.worked(25);

							IProjectDescription description;
							try {
								description = ResourcesPlugin.getWorkspace()
										.loadProjectDescription(
												new Path(projectPath));
								IProject project = ResourcesPlugin
										.getWorkspace().getRoot()
										.getProject(description.getName());
								monitor.subTask("Open project : "
										+ description.getName());
								project.create(description, null);
								monitor.worked(25);
								project.open(null);
								monitor.worked(25);
							} catch (CoreException e) {
								ArduinoUiActivator.log(Status.ERROR, "Open project failed", e);
							}
							monitor.done();
						}
					});
		} catch (InvocationTargetException e1) {
			ArduinoUiActivator.log(Status.ERROR, "Open project failed", e1);
		} catch (InterruptedException e1) {
			ArduinoUiActivator.log(Status.ERROR, "Open project failed", e1);
		}
	}

	private DDiagram getDashboardDiagram(Session session) {
		Collection<DRepresentation> representations = DialectManager.INSTANCE
				.getAllRepresentations(session);
		for (DRepresentation representation : representations) {
			if ("Dashboard".equals(representation.getName())) {
				return (DDiagram) representation;
			}
		}
		return null;
	}
}
