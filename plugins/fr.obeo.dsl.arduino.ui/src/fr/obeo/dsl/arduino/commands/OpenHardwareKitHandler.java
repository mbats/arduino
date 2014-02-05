package fr.obeo.dsl.arduino.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.sirius.business.api.session.Session;

import fr.obeo.dsl.arduino.utils.ArduinoServices;
import fr.obeo.dsl.arduino.utils.ProjectServices;

public class OpenHardwareKitHandler extends AbstractHandler {
	ArduinoServices service = new ArduinoServices();
	ProjectServices projectServices = new ProjectServices();

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Session session = service.getSession();
		if (session != null) {
			if (ProjectServices.isViewpointEnabled(session,
					ProjectServices.HARDWARE_KIT_VP)) {
				projectServices
				.unActivateHardwareKitDefinition(new NullProgressMonitor());
			} else {
				projectServices
						.activateHardwareKitDefinition(new NullProgressMonitor());
			}
		}
		return null;
	}
}