package fr.obeo.dsl.arduino.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import fr.obeo.dsl.arduino.Project;
import fr.obeo.dsl.arduino.utils.ArduinoServices;

public class UploadSketchHandler extends AbstractHandler {
	ArduinoServices service = new ArduinoServices();

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		System.out.println("Upload sketch");

		Project project = service.getArduinoProject();
		if (project != null) {
			service.upload(project.getSketch());
		}

		return null;
	}
}
