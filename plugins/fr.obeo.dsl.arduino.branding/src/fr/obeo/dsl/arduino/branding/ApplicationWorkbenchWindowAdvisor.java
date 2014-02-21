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
package fr.obeo.dsl.arduino.branding;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.handlers.IHandlerService;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	public ApplicationWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	public ActionBarAdvisor createActionBarAdvisor(
			IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setInitialSize(new Point(400, 300));
		configurer.setShowCoolBar(true);
		configurer.setShowStatusLine(false);
		configurer.setTitle("Arduino Designer"); //$NON-NLS-1$
	}

	@Override
	public void postWindowOpen() {
		super.postWindowOpen();

		IWorkbenchWindowConfigurer workbenchWindowConfigurer = getWindowConfigurer();
		IActionBarConfigurer actionBarConfigurer = workbenchWindowConfigurer
				.getActionBarConfigurer();
		IMenuManager menuManager = actionBarConfigurer.getMenuManager();

		IContributionItem[] menuItems = menuManager.getItems();
		for (int i = 0; i < menuItems.length; i++) {
			IContributionItem menuItem = menuItems[i];

			// Hack to remove the Run menu - it seems you cannot do this using
			// the "org.eclipse.ui.activities" extension
			if ("org.eclipse.ui.run".equals(menuItem.getId())) {
				menuManager.remove(menuItem);
			}
		}

		menuManager.update(true);
	}
}
