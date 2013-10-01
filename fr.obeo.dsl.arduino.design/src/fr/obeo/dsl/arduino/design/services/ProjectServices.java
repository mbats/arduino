package fr.obeo.dsl.arduino.design.services;

import java.io.IOException;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.transaction.RecordingCommand;

import com.google.common.collect.Maps;

import fr.obeo.dsl.arduino.ArduinoFactory;
import fr.obeo.dsl.viewpoint.business.api.componentization.ViewpointRegistry;
import fr.obeo.dsl.viewpoint.business.api.session.Session;
import fr.obeo.dsl.viewpoint.business.api.session.SessionManager;
import fr.obeo.dsl.viewpoint.business.api.session.resource.AirdResource;
import fr.obeo.dsl.viewpoint.description.Viewpoint;
import fr.obeo.dsl.viewpoint.ui.business.api.viewpoint.ViewpointSelectionCallback;

public class ProjectServices {
	public static final String ARDUINO_VP = "Arduino";

	public void createProject() {
		final Session session = (Session) SessionManager.INSTANCE.getSessions()
				.toArray()[0];
		final String semanticModelPath = getSemanticModelPath(session);
		initSemanticModel(session, semanticModelPath);

		final String[] viewpointsToActivate = { ARDUINO_VP };
		enableViewpoints(session, viewpointsToActivate);

		openDashboard(session);
	}

	private void openDashboard(final Session session) {
		// TODO Open dashboard
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
			final String semanticModelPath) {
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
										new NullProgressMonitor());

								// Add ardublock kit
								final URI defaultKitModelURI = URI
										.createPlatformPluginURI(
												"/fr.obeo.dsl.arduino.design/resources/ArdublockKit.arduino",
												true);
								session.addSemanticResource(defaultKitModelURI,
										new NullProgressMonitor());
								
								session.save(new NullProgressMonitor());
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
}
