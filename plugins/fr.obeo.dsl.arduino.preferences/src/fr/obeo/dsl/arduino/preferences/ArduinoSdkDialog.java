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
package fr.obeo.dsl.arduino.preferences;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;

public class ArduinoSdkDialog extends Dialog {

	public ArduinoSdkDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Arduino SDK is missing");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		Link link = new Link(composite, SWT.NONE);
		String message = "Set the path to the Arduino SDK in the Arduino Designer Preferences to be able to compile and upload code to the target";
		link.setText(message);

		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER)
				.grab(true, false)
				.hint(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH, SWT.DEFAULT)
				.applyTo(link);
		applyDialogFont(composite);

		return composite;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				false);
	}

	@Override
	protected void buttonPressed(int buttonId) {
		// Close dialog
		setReturnCode(buttonId);
		close();
	}
}
