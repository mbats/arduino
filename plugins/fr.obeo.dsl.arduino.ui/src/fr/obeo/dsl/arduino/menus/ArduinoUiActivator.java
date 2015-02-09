/**
 *  Copyright (c) 2013 Obeo.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *    Obeo - initial API and implementation
 */
package fr.obeo.dsl.arduino.menus;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class ArduinoUiActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "fr.obeo.dsl.arduino.ui"; //$NON-NLS-1$

	// The shared instance
	private static ArduinoUiActivator plugin;

	/**
	 * The constructor
	 */
	public ArduinoUiActivator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		// SiriusDiagramEditorPlugin.getInstance().getPreferenceStore().setValue(org.eclipse.sirius.diagram.tools.api.preferences.SiriusDiagramPreferencesKeys.PREF_OLD_UI.name(),
		// Boolean.TRUE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static ArduinoUiActivator getDefault() {
		return plugin;
	}

	/**
	 * A helper to log plugin errors.
	 * 
	 * @param severity
	 *            the error severity.
	 * @param message
	 *            the error message.
	 * @param exception
	 *            the error exception.
	 */
	public static void log(int severity, String message, Throwable exception) {
		getDefault().getLog().log(
				new Status(severity, PLUGIN_ID, message, exception));
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 *
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
