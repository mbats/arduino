package fr.obeo.dsl.arduino.commands;

import java.util.Collection;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.NullProgressMonitor;

import fr.obeo.dsl.viewpoint.DDiagram;
import fr.obeo.dsl.viewpoint.DRepresentation;
import fr.obeo.dsl.viewpoint.business.api.dialect.DialectManager;
import fr.obeo.dsl.viewpoint.business.api.session.Session;
import fr.obeo.dsl.viewpoint.business.api.session.SessionManager;
import fr.obeo.dsl.viewpoint.ui.business.api.dialect.DialectUIManager;

public class OpenHardwareHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		System.out.println("Open hardware");
		Session session = (Session) SessionManager.INSTANCE.getSessions()
				.toArray()[0];
		DialectUIManager.INSTANCE.openEditor(session,
				getHardwareDiagram(session), new NullProgressMonitor());
		return null;
	}

	private DDiagram getHardwareDiagram(Session session) {
		Collection<DRepresentation> representations = DialectManager.INSTANCE
				.getAllRepresentations(session);
		for (DRepresentation representation : representations) {
			if ("Hardware".equals(representation.getName())) {
				return (DDiagram) representation;
			}
		}
		// TODO create representation if does not exist
		// EObject seamntic = session.getSemanticResources().
		// DialectManager.INSTANCE.createRepresentation("Hardware", semantic,
		// description, session, new NullProgressMonitor());
		return null;
	}

}
