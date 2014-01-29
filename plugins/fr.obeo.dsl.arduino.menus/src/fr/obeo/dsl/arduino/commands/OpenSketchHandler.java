package fr.obeo.dsl.arduino.commands;

import java.util.Collection;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.sirius.business.api.dialect.DialectManager;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.ui.business.api.dialect.DialectUIManager;
import org.eclipse.sirius.viewpoint.DDiagram;
import org.eclipse.sirius.viewpoint.DRepresentation;

import fr.obeo.dsl.arduino.utils.ArduinoServices;

public class OpenSketchHandler extends AbstractHandler {
	ArduinoServices service = new ArduinoServices();

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		System.out.println("Open sketch");
		Session session = service.getSession();
		DialectUIManager.INSTANCE.openEditor(session,
				getSketchDiagram(session), new NullProgressMonitor());
		return null;
	}

	private DDiagram getSketchDiagram(Session session) {
		Collection<DRepresentation> representations = DialectManager.INSTANCE
				.getAllRepresentations(session);
		for (DRepresentation representation : representations) {
			if ("Sketch".equals(representation.getName())) {
				return (DDiagram) representation;
			}
		}
		return null;
	}

}
