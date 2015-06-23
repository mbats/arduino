package fr.obeo.dsl.arduino.simulator.design;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.ILaunchGroup;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.sirius.business.api.componentization.ViewpointRegistry;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.ui.business.api.viewpoint.ViewpointSelectionCallback;
import org.eclipse.sirius.viewpoint.description.Viewpoint;
import org.eclipse.ui.PlatformUI;

import fr.obeo.dsl.arduino.Project;
import fr.obeo.dsl.arduino.design.ArduinoDesignerUtils;
import fr.obeo.dsl.arduino.simulator.design.launcher.LauncherDelegate;
import fr.obeo.dsl.debug.ide.Activator;
import fr.obeo.dsl.debug.ide.launch.AbstractDSLLaunchConfigurationDelegate;

public class DebugHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		Project firstInstruction = ArduinoDesignerUtils.getOpenedProject();

		// Activate Simulator viewpoint
		enableSimulatorViewpoint(SessionManager.INSTANCE
				.getSession(firstInstruction));
		IFile file = getArduinoModel(firstInstruction);

		if (file != null) {
			// try to save dirty editors
			// PlatformUI.getWorkbench().getActiveWorkbenchWindow()
			// .getActivePage().saveAllEditors(true);

			try {
				ILaunchConfiguration[] configurations = getLaunchConfigurations(file);
				String mode = "debug";
				if (configurations.length == 0) {
					// try to create a launch configuration
					configurations = createLaunchConfiguration(file,
							firstInstruction, mode);
				}

				// launch
				if (configurations.length == 1) {
					configurations[0].launch(mode, new NullProgressMonitor());
				} else {
					// more than one configuration applies
					// open launch dialog for selection
					final ILaunchGroup group = DebugUITools.getLaunchGroup(
							configurations[0], mode);
					DebugUITools.openLaunchConfigurationDialogOnGroup(
							PlatformUI.getWorkbench()
									.getActiveWorkbenchWindow().getShell(),
							new StructuredSelection(configurations[0]),
							group.getIdentifier(), null);
				}

			} catch (CoreException e) {
				Activator.getDefault().error(e);
			}
		}

		return null;
	}

	private static void enableSimulatorViewpoint(final Session session) {
		if (session != null) {
			session.getTransactionalEditingDomain()
					.getCommandStack()
					.execute(
							new RecordingCommand(session
									.getTransactionalEditingDomain()) {
								@Override
								protected void doExecute() {
									ViewpointSelectionCallback callback = new ViewpointSelectionCallback();

									for (Viewpoint vp : ViewpointRegistry
											.getInstance().getViewpoints()) {
										if ("ArduinoSimulator".equals(vp
												.getName()))
											callback.selectViewpoint(vp,
													session,
													new NullProgressMonitor());
									}
								}
							});
		}
	}

	private IFile getArduinoModel(Project firstInstruction) {
		Resource resource = firstInstruction.eResource();
		if (resource != null) {
			URI uri = resource.getURI();
			uri = resource.getResourceSet().getURIConverter().normalize(uri);
			String scheme = uri.scheme();
			if ("platform".equals(scheme) && uri.segmentCount() > 1
					&& "resource".equals(uri.segment(0))) {
				StringBuffer platformResourcePath = new StringBuffer();
				for (int j = 1, size = uri.segmentCount(); j < size; ++j) {
					platformResourcePath.append('/');
					platformResourcePath.append(uri.segment(j));
				}
				return ResourcesPlugin.getWorkspace().getRoot()
						.getFile(new Path(platformResourcePath.toString()));
			}
		}
		return null;
	}

	/**
	 * Creates a {@link ILaunchConfiguration}. If the
	 * <code>firstInstruction</code> is <code>null</code> the launch
	 * configuration dialog is opened.
	 * 
	 * @param file
	 *            the selected model {@link IFile}
	 * @param firstInstruction
	 *            the first {@link EObject instruction} or <code>null</code> for
	 *            interactive selection
	 * @param mode
	 *            the {@link ILaunchConfiguration#getModes() mode}
	 * @return an array of possible {@link ILaunchConfiguration}, can be empty
	 *         but not <code>null</code>
	 * @throws CoreException
	 *             if {@link ILaunchConfiguration} initialization fails of
	 *             models can't be loaded
	 */
	protected ILaunchConfiguration[] createLaunchConfiguration(
			final IResource file, EObject firstInstruction, final String mode)
			throws CoreException {
		final ILaunchConfiguration[] res;

		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType type = manager
				.getLaunchConfigurationType(getLaunchConfigurationTypeID());

		ILaunchConfigurationWorkingCopy configuration = type.newInstance(null,
				file.getName());
		configuration.setMappedResources(new IResource[] { file, });
		configuration.setAttribute(
				AbstractDSLLaunchConfigurationDelegate.RESOURCE_URI, file
						.getFullPath().toString());
		if (firstInstruction == null) {
			// open configuration for further editing
			final ILaunchGroup group = DebugUITools.getLaunchGroup(
					configuration, mode);
			if (group != null) {
				configuration.doSave();
				DebugUITools.openLaunchConfigurationDialog(PlatformUI
						.getWorkbench().getActiveWorkbenchWindow().getShell(),
						configuration, group.getIdentifier(), null);
			}
			res = new ILaunchConfiguration[] {};
		} else {
			configuration
					.setAttribute(
							AbstractDSLLaunchConfigurationDelegate.FIRST_INSTRUCTION_URI,
							EcoreUtil.getURI(firstInstruction).toString());
			// save and return new configuration
			configuration.doSave();
			res = new ILaunchConfiguration[] { configuration, };
		}
		return res;
	}

	private String getLaunchConfigurationTypeID() {
		return LauncherDelegate.TYPE_ID;
	}

	/**
	 * Get all {@link ILaunchConfiguration} that target the given
	 * {@link IResource}.
	 * 
	 * @param resource
	 *            root file to execute
	 * @return {@link ILaunchConfiguration}s using resource
	 */
	protected ILaunchConfiguration[] getLaunchConfigurations(IResource resource) {
		final List<ILaunchConfiguration> configurations = new ArrayList<ILaunchConfiguration>();

		final ILaunchManager manager = DebugPlugin.getDefault()
				.getLaunchManager();
		final ILaunchConfigurationType type = manager
				.getLaunchConfigurationType(getLaunchConfigurationTypeID());

		// try to find existing configurations using the same file
		try {
			for (ILaunchConfiguration configuration : manager
					.getLaunchConfigurations(type)) {
				if (configuration
						.hasAttribute(AbstractDSLLaunchConfigurationDelegate.RESOURCE_URI)) {
					final String pathString = configuration
							.getAttribute(
									AbstractDSLLaunchConfigurationDelegate.RESOURCE_URI,
									"");
					try {
						IFile file = ResourcesPlugin.getWorkspace().getRoot()
								.getFile(new Path(pathString));
						if (resource != null && resource.equals(file)) {
							configurations.add(configuration);
						}
					} catch (IllegalArgumentException e) {
					}
				}
			}
		} catch (CoreException e) {
			// could not load configurations, ignore
			e.toString();
		}

		return configurations.toArray(new ILaunchConfiguration[configurations
				.size()]);
	}
}
