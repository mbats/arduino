package fr.obeo.dsl.arduino.simulator.design.action;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;

import fr.obeo.dsl.arduino.design.ArduinoDesignerUtils;
import fr.obeo.dsl.arduino.simulator.design.launcher.LauncherDelegate;
import fr.obeo.dsl.debug.ide.sirius.ui.action.AbstractDebugAsAction;

public class DebugAsAction extends AbstractDebugAsAction {

	@Override
	protected String getLaunchConfigurationTypeID() {
		return LauncherDelegate.TYPE_ID;
	}

	@Override
	protected EObject getFirstInstruction(
			Collection<? extends EObject> selections) {
		return ArduinoDesignerUtils.getOpenedProject();
	}

}
