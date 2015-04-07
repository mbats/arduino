package fr.obeo.dsl.arduino.design;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.ui.business.api.session.IEditingSession;
import org.eclipse.sirius.ui.business.api.session.SessionUIManager;

import fr.obeo.dsl.arduino.Project;

/**
 * Utility for arduino designer.
 * 
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 *
 */
public final class ArduinoDesignerUtils {

	/**
	 * Constructor.
	 */
	private ArduinoDesignerUtils() {
		// nothing to do here
	}

	/**
	 * Gets the opened {@link Project}.
	 * 
	 * @return the opened {@link Project} if nay, <code>null</code> otherwise
	 */
	public static Project getOpenedProject() {
		Project res = null;

		for (IEditingSession session : SessionUIManager.INSTANCE
				.getUISessions()) {
			for (Resource resource : session.getSession()
					.getSemanticResources()) {
				for (EObject eObj : resource.getContents()) {
					if (eObj instanceof Project
							&& eObj.eResource().getURI().isPlatformResource()) {
						res = (Project) eObj;
						break;
					}
				}
			}
		}

		return res;
	}

}
