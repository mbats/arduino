package fr.obeo.dsl.arduino.utils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.sirius.business.api.componentization.ViewpointRegistry;
import org.eclipse.sirius.business.api.dialect.DialectManager;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.ui.business.api.dialect.DialectUIManager;
import org.eclipse.sirius.ui.business.api.viewpoint.ViewpointSelectionCallback;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.description.Viewpoint;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;

import com.google.common.collect.Maps;

import fr.obeo.dsl.arduino.ArduinoFactory;
import fr.obeo.dsl.arduino.menus.ArduinoUiActivator;

public class ProjectServices {
	ArduinoServices service = new ArduinoServices();
	public static final String ARDUINO_VP = "Arduino";
	public static final String HARDWARE_KIT_VP = "Hardware Kit";

	public void createProject(IProgressMonitor monitor) {
		final Session session = service.getSession();
		final String semanticModelPath = getSemanticModelPath(session);
		initSemanticModel(session, semanticModelPath, monitor);

		final String[] viewpointsToActivate = { ARDUINO_VP };
		enableViewpoints(session, viewpointsToActivate);

		openDashboard(session);
	}

	public void openProject(final String projectPath) {
		try {
			ProjectServices service = new ProjectServices();
			service.closeOpenedEditors();
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
								ArduinoUiActivator.log(Status.ERROR,
										"Open project failed", e);
							}
							monitor.done();
						}
					});
		} catch (InvocationTargetException e1) {
			ArduinoUiActivator.log(Status.ERROR, "Open project failed", e1);
		} catch (InterruptedException e1) {
			ArduinoUiActivator.log(Status.ERROR, "Open project failed", e1);
		}
		Session session = service.getSession();
		while (session == null) {
			session = service.getSession();
		}
		openDashboard(session);
	}

	public void openDashboard(final Session session) {
		Collection<DRepresentation> representations = DialectManager.INSTANCE
				.getAllRepresentations(session);
		for (DRepresentation representation : representations) {
			if ("Dashboard".equals(representation.getName())) {
				DialectUIManager.INSTANCE.openEditor(session, representation,
						new NullProgressMonitor());
				return;
			}
		}
	}

	private String getSemanticModelPath(final Session session) {
		Resource aird = (Resource) session.getAllSessionResources().toArray()[0];
		String airdUri = aird.getURI().toPlatformString(true);
		final String semanticModelPath = airdUri.substring(0,
				airdUri.lastIndexOf("/") + 1)
				+ "model.arduino";
		return semanticModelPath;
	}

	private void initSemanticModel(final Session session,
			final String semanticModelPath, final IProgressMonitor monitor) {
		session.getTransactionalEditingDomain()
				.getCommandStack()
				.execute(
						new RecordingCommand(session
								.getTransactionalEditingDomain()) {
							@Override
							protected void doExecute() {

								final URI semanticModelURI = URI
										.createPlatformResourceURI(
												semanticModelPath, true);
								Resource res = new ResourceSetImpl()
										.createResource(semanticModelURI);
								// Add the initial model object to the contents.
								final EObject rootObject = ArduinoFactory.eINSTANCE
										.createProject();

								if (rootObject != null) {
									res.getContents().add(rootObject);
								}
								try {
									res.save(Maps.newHashMap());
								} catch (IOException e) {
									ArduinoUiActivator.log(Status.ERROR,
											"Init semantic model failed", e);
								}

								session.addSemanticResource(semanticModelURI,
										monitor);

								// Add ardublock kit
								final URI defaultKitModelURI = URI
										.createPlatformPluginURI(
												"/fr.obeo.dsl.arduino.design/resources/ArdublockKit.arduino",
												true);
								session.addSemanticResource(defaultKitModelURI,
										monitor);

								session.save(monitor);
							}
						});
	}

	public static void enableViewpoints(final Session session,
			final String... viewpointsToActivate) {
		if (session != null) {
			session.getTransactionalEditingDomain()
					.getCommandStack()
					.execute(
							new RecordingCommand(session
									.getTransactionalEditingDomain()) {
								@Override
								protected void doExecute() {
									ViewpointSelectionCallback callback = new ViewpointSelectionCallback();

									for (Viewpoint vp : ViewpointRegistry
											.getInstance().getViewpoints()) {
										for (String viewpoint : viewpointsToActivate) {
											if (viewpoint.equals(vp.getName()))
												callback.selectViewpoint(
														vp,
														session,
														new NullProgressMonitor());
										}
									}
								}
							});
		}
	}

	public static void disableViewpoints(final Session session,
			final String... viewpointsToActivate) {
		if (session != null) {
			session.getTransactionalEditingDomain()
					.getCommandStack()
					.execute(
							new RecordingCommand(session
									.getTransactionalEditingDomain()) {
								@Override
								protected void doExecute() {
									ViewpointSelectionCallback callback = new ViewpointSelectionCallback();

									for (Viewpoint vp : ViewpointRegistry
											.getInstance().getViewpoints()) {
										for (String viewpoint : viewpointsToActivate) {
											if (viewpoint.equals(vp.getName()))
												callback.deselectViewpoint(
														vp,
														session,
														new NullProgressMonitor());
										}
									}
								}
							});
		}
	}

	public static boolean isViewpointEnabled(final Session session,
			final String viewpointToCheck) {
		if (session != null) {
			for (Viewpoint vp : session.getSelectedViewpoints(false)) {
				if (viewpointToCheck.equals(vp.getName()))
					return true;
			}
		}
		return false;
	}

	public void closeProjects(IProgressMonitor monitor) {
		monitor.subTask("Close projects");

		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		for (IProject project : root.getProjects()) {
			try {
				project.close(monitor);
				project.delete(false, false, monitor);
				monitor.worked(25);
			} catch (CoreException e) {
				ArduinoUiActivator.log(Status.ERROR, "Close project failed", e);
			}
		}
	}

	public void closeOpenedEditors() {
		for (IEditorReference editorRef : PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage()
				.getEditorReferences()) {
			IEditorPart editor = editorRef.getEditor(false);
			editor.doSave(new NullProgressMonitor());
			DialectUIManager.INSTANCE.closeEditor(editor, false);
		}
	}

	public void activateHardwareKitDefinition(IProgressMonitor monitor) {
		final Session session = service.getSession();
		final String[] viewpointsToActivate = { HARDWARE_KIT_VP };
		enableViewpoints(session, viewpointsToActivate);

		openDashboard(session);
	}

	public void unActivateHardwareKitDefinition(IProgressMonitor monitor) {
		final Session session = service.getSession();
		final String[] viewpointsToActivate = { HARDWARE_KIT_VP };
		disableViewpoints(session, viewpointsToActivate);

		openDashboard(session);
	}
}