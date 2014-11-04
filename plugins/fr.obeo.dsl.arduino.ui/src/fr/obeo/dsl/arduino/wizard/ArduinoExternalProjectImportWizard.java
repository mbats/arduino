package fr.obeo.dsl.arduino.wizard;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionListener;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.business.api.session.SessionManagerListener;
import org.eclipse.sirius.viewpoint.description.Viewpoint;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.internal.wizards.datatransfer.DataTransferMessages;

import fr.obeo.dsl.arduino.utils.ProjectServices;

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
		setWindowTitle(DataTransferMessages.DataTransfer_importTitle);
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
		// ProjectServices service = new ProjectServices();
		// service.closeProjects(new NullProgressMonitor());
		boolean created = openProject(mainPage);
		return true;
	}

	private boolean openProject(ArduinoWizardProjectsImportPage mainPage) {
		boolean created = mainPage.createProjects();
		final SessionManagerListener listener = new SessionManagerListener() {

			@Override
			public void viewpointSelected(Viewpoint selectedSirius) {
			}

			@Override
			public void viewpointDeselected(Viewpoint deselectedSirius) {
			}

			@Override
			public void notifyRemoveSession(Session removedSession) {
			}

			@Override
			public void notifyAddSession(Session newSession) {
			}

			@Override
			public void notify(final Session updated, int notification) {
				if (updated.isOpen() && notification == SessionListener.OPENED) {
					final ProjectServices service = new ProjectServices();
					service.openDashboard(updated);
					SessionManager.INSTANCE.removeSessionsListener(this);
				}

			}
		};
		SessionManager.INSTANCE.addSessionsListener(listener);
		return created;
	}
}
