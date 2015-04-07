package fr.obeo.dsl.arduino.simulator.design.launcher;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.ui.IEditorPart;

import fr.obeo.dsl.arduino.Project;
import fr.obeo.dsl.arduino.design.ArduinoDesignerUtils;
import fr.obeo.dsl.arduino.simulator.Simulator;
import fr.obeo.dsl.arduino.simulator.debug.SimulatorDebugger;
import fr.obeo.dsl.arduino.simulator.design.Activator;
import fr.obeo.dsl.arduino.simulator.design.SiriusSimulator;
import fr.obeo.dsl.debug.ide.IDSLDebugger;
import fr.obeo.dsl.debug.ide.event.DSLDebugEventDispatcher;
import fr.obeo.dsl.debug.ide.sirius.ui.launch.AbstractDSLLaunchConfigurationDelegateUI;

public class LauncherDelegate extends AbstractDSLLaunchConfigurationDelegateUI {

	public static final String TYPE_ID = "fr.obeo.dsl.arduino.simulator.design.launchConfiguration";

	public static final String MODEL_IDENTIFIER = "fr.obeo.dsl.arduino.simulator.design.debugModel";

	@Override
	protected String getLaunchConfigurationTypeID() {
		return TYPE_ID;
	}

	@Override
	protected EObject getFirstInstruction(ISelection selection) {
		return ArduinoDesignerUtils.getOpenedProject();
	}

	@Override
	protected EObject getFirstInstruction(IEditorPart editor) {
		return ArduinoDesignerUtils.getOpenedProject();
	}

	@Override
	protected EObject getFirstInstruction(ILaunchConfiguration configuration) {
		return ArduinoDesignerUtils.getOpenedProject();
	}

	@Override
	protected IDSLDebugger getDebugger(ILaunchConfiguration configuration,
			DSLDebugEventDispatcher dispatcher, EObject firstInstruction,
			IProgressMonitor monitor) {
		final Simulator simulator = new SiriusSimulator(
				(Project) firstInstruction,
				SessionManager.INSTANCE.getSession(firstInstruction));
		final SimulatorDebugger result = new SimulatorDebugger(dispatcher,
				simulator);
		simulator.setDebugger(result);
		return result;
	}

	@Override
	protected String getDebugTargetName(ILaunchConfiguration configuration,
			EObject firstInstruction) {
		return ((Project) firstInstruction).getHardware().getName();
	}

	@Override
	protected String getDebugJobName(ILaunchConfiguration configuration,
			EObject firstInstruction) {
		return ((Project) firstInstruction).getHardware().getName();
	}

	@Override
	protected String getPluginID() {
		return Activator.PLUGIN_ID;
	}

	@Override
	protected String getModelIdentifier() {
		return MODEL_IDENTIFIER;
	}

	@Override
	public IResource getLaunchableResource(IEditorPart editorpart) {
		return getResource();
	}

	@Override
	public IResource getLaunchableResource(ISelection selection) {
		return getResource();
	}

	private IResource getResource() {
		final IResource res;

		final URI resourceURI = ArduinoDesignerUtils.getOpenedProject()
				.eResource().getURI();
		if (resourceURI.isPlatformResource()) {
			final String pathString = resourceURI.toPlatformString(true);
			res = ResourcesPlugin.getWorkspace().getRoot()
					.getFile(new Path(pathString));
		} else {
			res = null;
		}

		return res;
	}

}
