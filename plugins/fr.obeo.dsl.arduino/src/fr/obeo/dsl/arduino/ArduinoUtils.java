package fr.obeo.dsl.arduino;

import javax.management.OperationsException;

import org.eclipse.emf.ecore.EObject;

/**
 * Utility class for arduino.
 * 
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 *
 */
public final class ArduinoUtils {

	private ArduinoUtils() {
		// nothing to do here
	}

	/**
	 * Gets the {@link Pin} {@link Connector connected} to the given
	 * {@link Module}.
	 * 
	 * @param project the {@link Project}
	 * @param module
	 *            the {@link Module}
	 * @return the {@link Pin} {@link Connector connected} to the given
	 *         {@link Module} if any, <code>null</code> otherwise
	 */
	public static Pin getPin(Project project, Module module) {
		Pin res = null;

		final Hardware hardware = project.getHardware();
		if (hardware != null) {
			for (Connector connector : hardware.getConnectors()) {
				if (connector.getModule() == module) {
					res = connector.getPin();
					break;
				}
			}
		}

		return res;
	}

	/**
	 * Gets the {@link Module} {@link Connector connected} to the given
	 * {@link Pin}.
	 * @param project the {@link Project}
	 * @param module
	 *            the {@link Module}
	 * @return the {@link Module} {@link Connector connected} to the given
	 *         {@link Pin} if any, <code>null</code> otherwise
	 */
	public static Module getModule(Project project , Pin pin) {
		Module res = null;

		final Hardware hardware = project.getHardware();
		if (hardware != null) {
			for (Connector connector : hardware.getConnectors()) {
				if (connector.getModule() == pin) {
					res = connector.getModule();
					break;
				}
			}
		}

		return res;
	}

	/**
	 * Gets the containing {@link Project} of the given {@link EObject}.
	 * 
	 * @param eObj
	 *            the {@link OperationsException}
	 * @return the containing {@link Project} of the given {@link EObject} if
	 *         any, <code>null</code> otherwise
	 */
	public static Project getContainingProject(EObject eObj) {
		Project res = null;

		EObject current = eObj.eContainer();
		while (current != null) {
			if (current instanceof Project) {
				res = (Project) current;
				break;
			}
			current = current.eContainer();
		}

		return res;
	}

}
