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
package fr.obeo.dsl.arduino.design;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.eclipse.sirius.business.api.componentization.ViewpointRegistry;
import org.eclipse.sirius.viewpoint.description.Viewpoint;

/**
 * The activator class controls the plug-in life cycle
 */
public class ArduinoDesignerPlugin extends AbstractUIPlugin {
    // The plug-in ID
    public static final String PLUGIN_ID = "fr.obeo.dsl.arduino.design";

    // The shared instance
    private static ArduinoDesignerPlugin plugin;

    private static Set<Viewpoint> viewpoints; 

    /**
     * The constructor
     */
    public ArduinoDesignerPlugin() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     */
    @Override
	public void start(BundleContext context) throws Exception {
      super.start(context);
	  plugin = this;
	  viewpoints = new HashSet<Viewpoint>();
	  viewpoints.addAll(ViewpointRegistry.getInstance().registerFromPlugin(PLUGIN_ID + "/description/arduino.odesign")); 
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
    @Override
	public void stop(BundleContext context) throws Exception {
	plugin = null;
	if (viewpoints != null) {
	    for (final Viewpoint viewpoint: viewpoints) {
		ViewpointRegistry.getInstance().disposeFromPlugin(viewpoint);
	    }
	    viewpoints.clear();
	    viewpoints = null; 
	}
	super.stop(context);
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static ArduinoDesignerPlugin getDefault() {
	return plugin;
    }
}
