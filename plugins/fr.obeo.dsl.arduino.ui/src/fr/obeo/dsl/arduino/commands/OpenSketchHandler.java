package fr.obeo.dsl.arduino.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.ui.business.api.dialect.DialectUIManager;

import fr.obeo.dsl.arduino.utils.ArduinoServices;

public class OpenSketchHandler extends AbstractHandler {
	ArduinoServices service = new ArduinoServices();

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		System.out.println("Open sketch");
		Session session = service.getSession();
		DialectUIManager.INSTANCE.openEditor(session,
				service.getDiagram(session, "Sketch"),
				new NullProgressMonitor());
		return null;
	}
}
