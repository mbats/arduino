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
package fr.obeo.dsl.arduino.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.handlers.HandlerUtil;

import fr.obeo.dsl.arduino.preferences.ArduinoPreferencesPage;

public class PreferencesHandler extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IPreferencePage page = new ArduinoPreferencesPage();
		PreferenceManager manager = new PreferenceManager();
		IPreferenceNode node = new PreferenceNode("0", page);
		manager.addToRoot(node);
		Shell shell = HandlerUtil.getActiveWorkbenchWindowChecked(event)
				.getShell();
		PreferenceDialog pref = PreferencesUtil.createPreferenceDialogOn(shell,
				page.getDescription(), null, null);
		if (pref != null)
			pref.open();
		return null;
	}
}
