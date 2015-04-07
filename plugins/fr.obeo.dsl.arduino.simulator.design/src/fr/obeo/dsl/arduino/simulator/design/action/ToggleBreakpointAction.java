package fr.obeo.dsl.arduino.simulator.design.action;

import fr.obeo.dsl.arduino.simulator.design.launcher.LauncherDelegate;
import fr.obeo.dsl.debug.ide.sirius.ui.action.AbstractToggleBreakpointAction;

public class ToggleBreakpointAction extends AbstractToggleBreakpointAction {

	@Override
	protected String getModelIdentifier() {
		return LauncherDelegate.MODEL_IDENTIFIER;
	}

}
