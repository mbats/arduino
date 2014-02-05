package fr.obeo.dsl.arduino.commands;

import java.util.Collection;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.sirius.business.api.dialect.DialectManager;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.ui.business.api.dialect.DialectUIManager;
import org.eclipse.sirius.viewpoint.DRepresentation;

import fr.obeo.dsl.arduino.utils.ArduinoServices;

public class OpenHardwareHandler extends AbstractHandler {
	ArduinoServices service = new ArduinoServices();

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		System.out.println("Open hardware");
		Session session = service.getSession();
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
