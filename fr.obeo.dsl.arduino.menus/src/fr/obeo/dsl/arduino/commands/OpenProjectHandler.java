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
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.PlatformUI;

import fr.obeo.dsl.arduino.utils.ArduinoServices;
import fr.obeo.dsl.arduino.utils.ProjectServices;
import fr.obeo.dsl.viewpoint.DDiagram;
import fr.obeo.dsl.viewpoint.DRepresentation;
import fr.obeo.dsl.viewpoint.business.api.dialect.DialectManager;
import fr.obeo.dsl.viewpoint.business.api.session.Session;
import fr.obeo.dsl.viewpoint.ui.business.api.dialect.DialectUIManager;

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
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							monitor.done();
						}
					});
		} catch (InvocationTargetException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
