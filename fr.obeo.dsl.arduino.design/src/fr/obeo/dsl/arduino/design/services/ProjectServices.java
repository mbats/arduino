package fr.obeo.dsl.arduino.design.services;

import java.io.IOException;
import java.util.Collection;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.ui.IEditorPart;

import com.google.common.collect.Maps;

import fr.obeo.dsl.arduino.ArduinoFactory;
import fr.obeo.dsl.viewpoint.DDiagram;
import fr.obeo.dsl.viewpoint.DRepresentation;
import fr.obeo.dsl.viewpoint.business.api.componentization.ViewpointRegistry;
import fr.obeo.dsl.viewpoint.business.api.dialect.DialectManager;
import fr.obeo.dsl.viewpoint.business.api.session.Session;
import fr.obeo.dsl.viewpoint.business.api.session.SessionManager;
import fr.obeo.dsl.viewpoint.business.api.session.resource.AirdResource;
import fr.obeo.dsl.viewpoint.description.Viewpoint;
import fr.obeo.dsl.viewpoint.ui.business.api.dialect.DialectUIManager;
import fr.obeo.dsl.viewpoint.ui.business.api.viewpoint.ViewpointSelectionCallback;

public class ProjectServices {
	public static final String ARDUINO_VP = "Arduino";

	public void createProject(IProgressMonitor monitor) {
		final Session session = (Session) SessionManager.INSTANCE.getSessions()
				.toArray()[0];
		final String semanticModelPath = getSemanticModelPath(session);
		initSemanticModel(session, semanticModelPath, monitor);

		final String[] viewpointsToActivate = { ARDUINO_VP };
		enableViewpoints(session, viewpointsToActivate);

		openDashboard(session);
	}

	private IEditorPart openDashboard(final Session session) {
		Collection<DRepresentation> representations = DialectManager.INSTANCE
				.getAllRepresentations(session);
		for (DRepresentation representation : representations) {
			if ("Dashboard".equals(representation.getName())) {
				return DialectUIManager.INSTANCE.openEditor(session,
						representation, new NullProgressMonitor());

			}
		}
		return null;
	}

	private DDiagram getHardwareDiagram(Session session) {

		// TODO create representation if does not exist
		// EObject seamntic = session.getSemanticResources().
		// DialectManager.INSTANCE.createRepresentation("Hardware", semantic,
		// description, session, new NullProgressMonitor());
		return null;
	}

	private String getSemanticModelPath(final Session session) {
		AirdResource aird = (AirdResource) session.getAllSessionResources()
				.toArray()[0];
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
									e.printStackTrace();
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
												callback.selectViewpoint(vp,
														session);
										}
									}
								}
							});
		}
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
