package fr.obeo.dsl.arduino.simulator.design.launcher;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

import fr.obeo.dsl.debug.ide.sirius.ui.launch.DSLLaunchConfigurationTab;

public class TabGroup extends AbstractLaunchConfigurationTabGroup {

	public void createTabs(ILaunchConfigurationDialog arg0, String arg1) {
		setTabs(new ILaunchConfigurationTab[] { new DSLLaunchConfigurationTab(new String[]{"arduino",}), new CommonTab(), });
	}

}
