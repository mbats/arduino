package fr.obeo.dsl.arduino.wizard;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;

public class ArduinoExternalProjectImportWizard extends Wizard implements
		IImportWizard {
	private static final String EXTERNAL_PROJECT_SECTION = "ExternalProjectImportWizard";//$NON-NLS-1$
	private ArduinoWizardProjectsImportPage mainPage;
	private IStructuredSelection currentSelection = null;
	private String initialPath = null;

	/**
	 * Constructor for ExternalProjectImportWizard.
	 */
	public ArduinoExternalProjectImportWizard() {
		this(null);
	}

	/**
	 * Constructor for ExternalProjectImportWizard.
	 * 
	 * @param initialPath
	 *            Default path for wizard to import
	 * @since 3.5
	 */
	public ArduinoExternalProjectImportWizard(String initialPath) {
		super();
		this.initialPath = initialPath;
		setNeedsProgressMonitor(true);
		IDialogSettings workbenchSettings = IDEWorkbenchPlugin.getDefault()
				.getDialogSettings();

		IDialogSettings wizardSettings = workbenchSettings
				.getSection(EXTERNAL_PROJECT_SECTION);
		if (wizardSettings == null) {
			wizardSettings = workbenchSettings
					.addNewSection(EXTERNAL_PROJECT_SECTION);
		}
		setDialogSettings(wizardSettings);
	}

	/*
	 * (non-Javadoc) Method declared on IWizard.
	 */
	public void addPages() {
		super.addPages();
		mainPage = new ArduinoWizardProjectsImportPage(
				"wizardExternalProjectsPage", initialPath, currentSelection); //$NON-NLS-1$
		addPage(mainPage);
	}

	/*
	 * (non-Javadoc) Method declared on IWorkbenchWizard.
	 */
	public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
		setWindowTitle("Open");
		setDefaultPageImageDescriptor(IDEWorkbenchPlugin
				.getIDEImageDescriptor("wizban/importproj_wiz.png")); //$NON-NLS-1$
		this.currentSelection = currentSelection;
	}

	/*
	 * (non-Javadoc) Method declared on IWizard.
	 */
	public boolean performCancel() {
		mainPage.performCancel();
		return true;
	}

	/*
	 * (non-Javadoc) Method declared on IWizard.
	 */
	public boolean performFinish() {
		return mainPage.createProjects();
	}
}
