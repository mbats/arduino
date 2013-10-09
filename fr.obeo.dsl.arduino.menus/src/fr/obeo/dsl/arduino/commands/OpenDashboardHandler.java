package fr.obeo.dsl.arduino.commands;

import java.util.Collection;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.NullProgressMonitor;

import fr.obeo.dsl.arduino.utils.ArduinoServices;
import fr.obeo.dsl.viewpoint.DDiagram;
import fr.obeo.dsl.viewpoint.DRepresentation;
import fr.obeo.dsl.viewpoint.business.api.dialect.DialectManager;
import fr.obeo.dsl.viewpoint.business.api.session.Session;
import fr.obeo.dsl.viewpoint.ui.business.api.dialect.DialectUIManager;

public class OpenDashboardHandler extends AbstractHandler {
	ArduinoServices service = new ArduinoServices();
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		if (service.getWorkspaceProject() == null) {
			new NewProjectHandler().execute(event);
		}

		Session session = service.getSession();
		DialectUIManager.INSTANCE.openEditor(session,
				getDashboardDiagram(session), new NullProgressMonitor());
		return null;
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
