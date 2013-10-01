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
import fr.obeo.dsl.viewpoint.business.api.session.Session;
import fr.obeo.dsl.viewpoint.business.api.session.SessionManager;

public class HardwareServices {

	public void createHardware() {
		final Session session = (Session) SessionManager.INSTANCE.getSessions()
				.toArray()[0];
		session.getTransactionalEditingDomain()
				.getCommandStack()
				.execute(
						new RecordingCommand(session
								.getTransactionalEditingDomain()) {
							@Override
							protected void doExecute() {
								Resource semanticModel = (Resource) session.getAllSessionResources().toArray()[1];
								semanticModel.getContents().get(0);
							}
						});

	}
}
