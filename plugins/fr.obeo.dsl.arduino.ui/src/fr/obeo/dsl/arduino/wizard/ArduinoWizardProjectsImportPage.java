package fr.obeo.dsl.arduino.wizard;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.layout.PixelConverter;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.sirius.ui.business.api.dialect.DialectEditor;
import org.eclipse.sirius.ui.business.api.dialect.DialectUIManager;
import org.eclipse.sirius.ui.business.api.session.IEditingSession;
import org.eclipse.sirius.ui.business.api.session.SessionUIManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.WizardDataTransferPage;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.internal.ide.StatusUtil;
import org.eclipse.ui.internal.wizards.datatransfer.ILeveledImportStructureProvider;
import org.eclipse.ui.statushandlers.StatusManager;

import fr.obeo.dsl.arduino.menus.ArduinoUiActivator;

/**
 * The WizardProjectsImportPage is the page that allows the user to import
 * projects from a particular location.
 */
public class ArduinoWizardProjectsImportPage extends WizardDataTransferPage {

	/**
	 * The name of the folder containing metadata information for the workspace.
	 */
	public static final String METADATA_FOLDER = ".metadata"; //$NON-NLS-1$

	/**
	 * The import structure provider.
	 * 
	 * @since 3.4
	 */
	private ILeveledImportStructureProvider structureProvider;

	/**
	 * @since 3.5
	 * 
	 */
	private final class ProjectLabelProvider extends LabelProvider implements
			IColorProvider {

		public String getText(Object element) {
			return ((ProjectRecord) element).getProjectLabel();
		}

		public Color getBackground(Object element) {
			return null;
		}

		public Color getForeground(Object element) {
			ProjectRecord projectRecord = (ProjectRecord) element;
			if (projectRecord.hasConflicts)
				return getShell().getDisplay().getSystemColor(SWT.COLOR_GRAY);
			return null;
		}
	}

	/**
	 * Class declared public only for test suite.
	 * 
	 */
	public class ProjectRecord {
		File projectSystemFile;

		String projectName;

		Object parent;

		int level;

		boolean hasConflicts;

		IProjectDescription description;

		/**
		 * Create a record for a project based on the info in the file.
		 * 
		 * @param file
		 */
		ProjectRecord(File file) {
			projectSystemFile = file;
			setProjectName();
		}

		/**
		 * @param file
		 *            The Object representing the .project file
		 * @param parent
		 *            The parent folder of the .project file
		 * @param level
		 *            The number of levels deep in the provider the file is
		 */
		ProjectRecord(Object file, Object parent, int level) {
			this.parent = parent;
			this.level = level;
			setProjectName();
		}

		/**
		 * Set the name of the project based on the projectFile.
		 */
		private void setProjectName() {
			try {

				// If we don't have the project name try again
				if (projectName == null) {
					IPath path = new Path(projectSystemFile.getPath());
					// if the file is in the default location, use the directory
					// name as the project name
					if (isDefaultLocation(path)) {
						projectName = path.segment(path.segmentCount() - 2);
						description = IDEWorkbenchPlugin.getPluginWorkspace()
								.newProjectDescription(projectName);
					} else {
						description = IDEWorkbenchPlugin.getPluginWorkspace()
								.loadProjectDescription(path);
						projectName = description.getName();
					}

				}
			} catch (CoreException e) {
				// no good couldn't get the name
			}
		}

		/**
		 * Returns whether the given project description file path is in the
		 * default location for a project
		 * 
		 * @param path
		 *            The path to examine
		 * @return Whether the given path is the default location for a project
		 */
		private boolean isDefaultLocation(IPath path) {
			// The project description file must at least be within the project,
			// which is within the workspace location
			if (path.segmentCount() < 2)
				return false;
			return path.removeLastSegments(2).toFile()
					.equals(Platform.getLocation().toFile());
		}

		/**
		 * Get the name of the project
		 * 
		 * @return String
		 */
		public String getProjectName() {
			return projectName;
		}

		/**
		 * Gets the label to be used when rendering this project record in the
		 * UI.
		 * 
		 * @return String the label
		 * @since 3.4
		 */
		public String getProjectLabel() {
			return projectName;
		}

		/**
		 * @return Returns the hasConflicts.
		 */
		public boolean hasConflicts() {
			return hasConflicts;
		}
	}

	// dialog store id constants
	private final static String STORE_DIRECTORIES = "WizardProjectsImportPage.STORE_DIRECTORIES";//$NON-NLS-1$

	private Combo directoryPathField;

	private TreeViewer projectsList;
	private ProjectRecord selectedProject = null;

	private ProjectRecord[] selectedProjects = new ProjectRecord[0];

	// Keep track of the directory that we browsed to last time
	// the wizard was invoked.
	private static String previouslyBrowsedDirectory = ""; //$NON-NLS-1$

	private Label projectFromDirectoryRadio;

	private Button browseDirectoriesButton;

	private IProject[] wsProjects;

	// The initial path to set
	private String initialPath;

	// The last selected path to minimize searches
	private String lastPath;
	// The last time that the file or folder at the selected path was modified
	// to mimize searches
	private long lastModified;

	/**
	 * Creates a new project creation wizard page.
	 * 
	 */
	public ArduinoWizardProjectsImportPage() {
		this("wizardExternalProjectsPage", null, null); //$NON-NLS-1$
	}

	/**
	 * Create a new instance of the receiver.
	 * 
	 * @param pageName
	 */
	public ArduinoWizardProjectsImportPage(String pageName) {
		this(pageName, null, null);
	}

	/**
	 * More (many more) parameters.
	 * 
	 * @param pageName
	 * @param initialPath
	 * @param currentSelection
	 * @since 3.5
	 */
	public ArduinoWizardProjectsImportPage(String pageName, String initialPath,
			IStructuredSelection currentSelection) {
		super(pageName);
		this.initialPath = initialPath;
		setPageComplete(false);
		setTitle("Open project");
		setDescription("Open an existing project.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	public void createControl(Composite parent) {

		initializeDialogUnits(parent);

		Composite workArea = new Composite(parent, SWT.NONE);
		setControl(workArea);

		workArea.setLayout(new GridLayout());
		workArea.setLayoutData(new GridData(GridData.FILL_BOTH
				| GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL));

		createProjectsRoot(workArea);
		createProjectsList(workArea);
		restoreWidgetValues();
		Dialog.applyDialogFont(workArea);

	}

	/**
	 * Create the checkbox list for the found projects.
	 * 
	 * @param workArea
	 */
	private void createProjectsList(Composite workArea) {

		Label title = new Label(workArea, SWT.NONE);
		title.setText("Projects:");

		Composite listComposite = new Composite(workArea, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginWidth = 0;
		layout.makeColumnsEqualWidth = false;
		listComposite.setLayout(layout);

		listComposite.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
				| GridData.GRAB_VERTICAL | GridData.FILL_BOTH));

		projectsList = new TreeViewer(listComposite, SWT.BORDER);
		projectsList.getTree().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedProject = (ProjectRecord) e.item.getData();
				if (selectedProject != null) {
					setPageComplete(true);
				}
			}
		});
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.widthHint = new PixelConverter(projectsList.getControl())
				.convertWidthInCharsToPixels(25);
		gridData.heightHint = new PixelConverter(projectsList.getControl())
				.convertHeightInCharsToPixels(10);
		projectsList.getControl().setLayoutData(gridData);
		projectsList.setContentProvider(new ITreeContentProvider() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java
			 * .lang.Object)
			 */
			public Object[] getChildren(Object parentElement) {
				return null;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.jface.viewers.IStructuredContentProvider#getElements
			 * (java.lang.Object)
			 */
			public Object[] getElements(Object inputElement) {
				return getProjectRecords();
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java
			 * .lang.Object)
			 */
			public boolean hasChildren(Object element) {
				return false;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.jface.viewers.ITreeContentProvider#getParent(java
			 * .lang.Object)
			 */
			public Object getParent(Object element) {
				return null;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
			 */
			public void dispose() {

			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse
			 * .jface.viewers.Viewer, java.lang.Object, java.lang.Object)
			 */
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
			}

		});

		projectsList.setLabelProvider(new ProjectLabelProvider());

		projectsList.setInput(this);
		projectsList.setComparator(new ViewerComparator());
	}

	/**
	 * Create the area where you select the root directory for the projects.
	 * 
	 * @param workArea
	 *            Composite
	 */
	private void createProjectsRoot(Composite workArea) {

		// project specification group
		Composite projectGroup = new Composite(workArea, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		layout.makeColumnsEqualWidth = false;
		layout.marginWidth = 0;
		projectGroup.setLayout(layout);
		projectGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// new project from directory radio button
		projectFromDirectoryRadio = new Label(projectGroup, SWT.NORMAL);
		projectFromDirectoryRadio.setText("Select directory:");

		// project location entry combo
		this.directoryPathField = new Combo(projectGroup, SWT.BORDER);

		GridData directoryPathData = new GridData(
				GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		directoryPathData.widthHint = new PixelConverter(directoryPathField)
				.convertWidthInCharsToPixels(25);
		directoryPathField.setLayoutData(directoryPathData);

		// browse button
		browseDirectoriesButton = new Button(projectGroup, SWT.PUSH);
		browseDirectoriesButton.setText("Browse...");
		setButtonLayoutData(browseDirectoriesButton);

		browseDirectoriesButton.addSelectionListener(new SelectionAdapter() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetS
			 * elected(org.eclipse.swt.events.SelectionEvent)
			 */
			public void widgetSelected(SelectionEvent e) {
				handleLocationDirectoryButtonPressed();
			}

		});

		directoryPathField.addTraverseListener(new TraverseListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.TraverseListener#keyTraversed(org.eclipse
			 * .swt.events.TraverseEvent)
			 */
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_RETURN) {
					e.doit = false;
					updateProjectsList(directoryPathField.getText().trim());
				}
			}

		});

		directoryPathField.addFocusListener(new FocusAdapter() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.FocusListener#focusLost(org.eclipse.swt
			 * .events.FocusEvent)
			 */
			public void focusLost(org.eclipse.swt.events.FocusEvent e) {
				updateProjectsList(directoryPathField.getText().trim());
			}

		});

		directoryPathField.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				updateProjectsList(directoryPathField.getText().trim());
			}
		});
	}

	private void directoryRadioSelected() {
		directoryPathField.setEnabled(true);
		browseDirectoriesButton.setEnabled(true);
		updateProjectsList(directoryPathField.getText());
		directoryPathField.setFocus();
	}

	/*
	 * (non-Javadoc) Method declared on IDialogPage. Set the focus on path
	 * fields when page becomes visible.
	 */
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			this.directoryPathField.setFocus();
		}
	}

	/**
	 * Update the list of projects based on path. Method declared public only
	 * for test suite.
	 * 
	 * @param path
	 */
	public void updateProjectsList(final String path) {
		// on an empty path empty selectedProjects
		if (path == null || path.length() == 0) {
			setMessage("Select a directory to search for existing Arduino Designer projects.");
			selectedProjects = new ProjectRecord[0];
			projectsList.refresh(true);
			setPageComplete(selectedProject != null);
			lastPath = path;
			return;
		}

		final File directory = new File(path);
		long modified = directory.lastModified();
		if (path.equals(lastPath) && lastModified == modified) {
			// since the file/folder was not modified and the path did not
			// change, no refreshing is required
			return;
		}

		lastPath = path;
		lastModified = modified;

		// We can't access the radio button from the inner class so get the
		// status beforehand
		try {
			getContainer().run(true, true, new IRunnableWithProgress() {

				/*
				 * (non-Javadoc)
				 * 
				 * @see
				 * org.eclipse.jface.operation.IRunnableWithProgress#run(org
				 * .eclipse.core.runtime.IProgressMonitor)
				 */
				public void run(IProgressMonitor monitor) {

					monitor.beginTask("Searching for projects", 100);
					selectedProjects = new ProjectRecord[0];
					Collection files = new ArrayList();
					monitor.worked(10);
					if (directory.isDirectory()) {

						if (!collectProjectFilesFromDirectory(files, directory,
								null, monitor)) {
							return;
						}
						Iterator filesIterator = files.iterator();
						selectedProjects = new ProjectRecord[files.size()];
						int index = 0;
						monitor.worked(50);
						monitor.subTask("Pocessing results");
						while (filesIterator.hasNext()) {
							File file = (File) filesIterator.next();
							selectedProjects[index] = new ProjectRecord(file);
							index++;
						}
					} else {
						monitor.worked(60);
					}
					monitor.done();
				}

			});
		} catch (InvocationTargetException e) {
			IDEWorkbenchPlugin.log(e.getMessage(), e);
		} catch (InterruptedException e) {
			// Nothing to do if the user interrupts.
		}

		projectsList.refresh(true);
		ProjectRecord[] projects = getProjectRecords();
		boolean displayWarning = false;
		for (int i = 0; i < projects.length; i++) {
			if (projects[i].hasConflicts) {
				displayWarning = true;
			}
		}

		if (displayWarning) {
			setMessage(
					"Some projects cannot be imported because they already exist in the workspace",
					WARNING);
		} else {
			setMessage("Select a directory to search for existing Arduino Designer projects.");
		}
		setPageComplete(selectedProject != null);
		if (selectedProjects.length == 0) {
			setMessage("No projects are found, select another directory",
					WARNING);
		}
	}

	/**
	 * Collect the list of .project files that are under directory into files.
	 * 
	 * @param files
	 * @param directory
	 * @param directoriesVisited
	 *            Set of canonical paths of directories, used as recursion guard
	 * @param monitor
	 *            The monitor to report to
	 * @return boolean <code>true</code> if the operation was completed.
	 */
	private boolean collectProjectFilesFromDirectory(Collection files,
			File directory, Set directoriesVisited, IProgressMonitor monitor) {

		if (monitor.isCanceled()) {
			return false;
		}
		monitor.subTask("Checking:" + directory.getPath());
		File[] contents = directory.listFiles();
		if (contents == null)
			return false;

		// Initialize recursion guard for recursive symbolic links
		if (directoriesVisited == null) {
			directoriesVisited = new HashSet();
			try {
				directoriesVisited.add(directory.getCanonicalPath());
			} catch (IOException exception) {
				StatusManager.getManager().handle(
						StatusUtil.newStatus(IStatus.ERROR,
								exception.getLocalizedMessage(), exception));
			}
		}

		// first look for project description files
		final String dotProject = IProjectDescription.DESCRIPTION_FILE_NAME;
		for (int i = 0; i < contents.length; i++) {
			File file = contents[i];
			if (file.isFile() && file.getName().equals(dotProject)) {
				files.add(file);
			}
		}
		// no project description found or search for nested projects enabled,
		// so recurse into sub-directories
		for (int i = 0; i < contents.length; i++) {
			if (contents[i].isDirectory()) {
				if (!contents[i].getName().equals(METADATA_FOLDER)) {
					try {
						String canonicalPath = contents[i].getCanonicalPath();
						if (!directoriesVisited.add(canonicalPath)) {
							// already been here --> do not recurse
							continue;
						}
					} catch (IOException exception) {
						StatusManager.getManager().handle(
								StatusUtil.newStatus(IStatus.ERROR,
										exception.getLocalizedMessage(),
										exception));

					}
					collectProjectFilesFromDirectory(files, contents[i],
							directoriesVisited, monitor);
				}
			}
		}
		return true;
	}

	/**
	 * The browse button has been selected. Select the location.
	 */
	protected void handleLocationDirectoryButtonPressed() {

		DirectoryDialog dialog = new DirectoryDialog(
				directoryPathField.getShell(), SWT.SHEET);
		dialog.setMessage("Select directory of the project to open");

		String dirName = directoryPathField.getText().trim();
		if (dirName.length() == 0) {
			dirName = previouslyBrowsedDirectory;
		}

		if (dirName.length() == 0) {
			dialog.setFilterPath(IDEWorkbenchPlugin.getPluginWorkspace()
					.getRoot().getLocation().toOSString());
		} else {
			File path = new File(dirName);
			if (path.exists()) {
				dialog.setFilterPath(new Path(dirName).toOSString());
			}
		}

		String selectedDirectory = dialog.open();
		if (selectedDirectory != null) {
			previouslyBrowsedDirectory = selectedDirectory;
			directoryPathField.setText(previouslyBrowsedDirectory);
			updateProjectsList(selectedDirectory);
		}
	}

	/**
	 * Create the selected projects
	 * 
	 * @return boolean <code>true</code> if all project creations were
	 *         successful.
	 */
	public boolean createProjects() {
		saveWidgetValues();

		final Object selected = selectedProject;
		createdProjects = new ArrayList();
		WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
			protected void execute(IProgressMonitor monitor)
					throws InvocationTargetException, InterruptedException {
				try {
					monitor.beginTask("", 1); //$NON-NLS-1$
					if (monitor.isCanceled()) {
						throw new OperationCanceledException();
					}
					if (selectedProject != null)
						createExistingProject((ProjectRecord) selectedProject,
								new SubProgressMonitor(monitor, 1));
				} finally {
					monitor.done();
				}
			}
		};
		// run the new project creation operation
		try {
			getContainer().run(true, true, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			// one of the steps resulted in a core exception
			Throwable t = e.getTargetException();
			String message = "Creation Problems";
			IStatus status;
			if (t instanceof CoreException) {
				status = ((CoreException) t).getStatus();
			} else {
				status = new Status(IStatus.ERROR,
						IDEWorkbenchPlugin.IDE_WORKBENCH, 1, message, t);
			}
			ErrorDialog.openError(getShell(), message, null, status);
			return false;
		}

		return true;
	}

	List createdProjects;

	/**
	 * Performs clean-up if the user cancels the wizard without doing anything
	 */
	public void performCancel() {
	}

	/**
	 * Create the project described in record. If it is successful return true.
	 * 
	 * @param record
	 * @return boolean <code>true</code> if successful
	 * @throws InterruptedException
	 */
	private boolean createExistingProject(final ProjectRecord record,
			final IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {
		final String projectName = record.getProjectName();
		final IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final IProject project = workspace.getRoot().getProject(projectName);

		for (IEditingSession uiSession : SessionUIManager.INSTANCE
				.getUISessions()) {
			for (DialectEditor editor : uiSession.getEditors()) {
				if (editor instanceof DiagramEditor) {
					final EditPartViewer graphicalViewer = ((DiagramEditor) editor)
							.getDiagramGraphicalViewer();

					if (graphicalViewer != null) {
						graphicalViewer.setSelection(new StructuredSelection());
						Display.getDefault().syncExec(new Runnable() {

							@Override
							public void run() {
								graphicalViewer.flush();

							}
						});
						/*
						 * We need to spin the ui thread so that the editor has
						 * the change to update its action enablement before we
						 * close and unload everything.
						 * 
						 * We just hope to be lucky and to be scheduled after
						 * the runnables launched by the arrange action in
						 * particular.
						 */
						SWTThreadingUtils.waitForAsyncExecsToFinish(Display
								.getDefault());
					}
				} else {
					DialectUIManager.INSTANCE.setSelection(editor,
							Collections.EMPTY_LIST);
				}
			}
			SWTThreadingUtils.waitForAsyncExecsToFinish(Display.getDefault());
		}

		SWTThreadingUtils.waitForAsyncExecsToFinish(Display.getDefault());

		// for (Session openedSession : SessionManager.INSTANCE.getSessions()) {
		// openedSession.save(monitor);
		// openedSession.close(monitor);
		// }

		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		for (IProject projectToClose : root.getProjects()) {
			try {
				if (projectToClose.isOpen()) {
					System.out.println("Closing : " + projectToClose.getName());
					projectToClose.close(monitor);
					projectToClose.delete(false, false, monitor);
				}
				monitor.worked(25);
			} catch (CoreException e) {
				ArduinoUiActivator.log(Status.ERROR, "Close project failed", e);
			}
		}

		createdProjects.add(project);
		if (record.description == null) {
			// error case
			record.description = workspace.newProjectDescription(projectName);
			IPath locationPath = new Path(
					record.projectSystemFile.getAbsolutePath());

			// If it is under the root use the default location
			if (Platform.getLocation().isPrefixOf(locationPath)) {
				record.description.setLocation(null);
			} else {
				record.description.setLocation(locationPath);
			}
		} else {
			record.description.setName(projectName);
		}

		// import from file system
		try {
			monitor.beginTask("Creating Projects", 100);
			project.create(record.description, new SubProgressMonitor(monitor,
					30));
			project.open(IResource.BACKGROUND_REFRESH, new SubProgressMonitor(
					monitor, 70));
		} catch (CoreException e) {
		} finally {
			monitor.done();
		}

		return true;
	}

	/**
	 * Method used for test suite.
	 * 
	 * @return TreeViewer the viewer containing all the projects found
	 */
	public TreeViewer getProjectsList() {
		return projectsList;
	}

	/**
	 * Retrieve all the projects in the current workspace.
	 * 
	 * @return IProject[] array of IProject in the current workspace
	 */
	private IProject[] getProjectsInWorkspace() {
		if (wsProjects == null) {
			wsProjects = IDEWorkbenchPlugin.getPluginWorkspace().getRoot()
					.getProjects();
		}
		return wsProjects;
	}

	/**
	 * Get the array of project records that can be imported from the source
	 * workspace or archive, selected by the user. If a project with the same
	 * name exists in both the source workspace and the current workspace, then
	 * the hasConflicts flag would be set on that project record.
	 * 
	 * Method declared public for test suite.
	 * 
	 * @return ProjectRecord[] array of projects that can be imported into the
	 *         workspace
	 */
	public ProjectRecord[] getProjectRecords() {
		List projectRecords = new ArrayList();
		for (int i = 0; i < selectedProjects.length; i++) {
			if (isProjectInWorkspace(selectedProjects[i].getProjectName())) {
				selectedProjects[i].hasConflicts = true;
			}
			projectRecords.add(selectedProjects[i]);
		}
		return (ProjectRecord[]) projectRecords
				.toArray(new ProjectRecord[projectRecords.size()]);
	}

	/**
	 * Determine if there is a directory with the project name in the workspace
	 * path.
	 * 
	 * @param projectName
	 *            the name of the project
	 * @return true if there is a directory with the same name of the imported
	 *         project
	 */
	private boolean isProjectInWorkspacePath(String projectName) {
		final IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IPath wsPath = workspace.getRoot().getLocation();
		IPath localProjectPath = wsPath.append(projectName);
		return localProjectPath.toFile().exists();
	}

	/**
	 * Determine if the project with the given name is in the current workspace.
	 * 
	 * @param projectName
	 *            String the project name to check
	 * @return boolean true if the project with the given name is in this
	 *         workspace
	 */
	private boolean isProjectInWorkspace(String projectName) {
		if (projectName == null) {
			return false;
		}
		IProject[] workspaceProjects = getProjectsInWorkspace();
		for (int i = 0; i < workspaceProjects.length; i++) {
			if (projectName.equals(workspaceProjects[i].getName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Use the dialog store to restore widget values to the values that they
	 * held last time this wizard was used to completion, or alternatively, if
	 * an initial path is specified, use it to select values.
	 * 
	 * Method declared public only for use of tests.
	 */
	public void restoreWidgetValues() {

		// First, check to see if we have resore settings, and
		// take care of the checkbox
		IDialogSettings settings = getDialogSettings();
		if (settings != null) {
			restoreFromHistory(settings, STORE_DIRECTORIES, directoryPathField);
		}

		// Second, check to see if we don't have an initial path,
		// and if we do have restore settings. If so, set the
		// radio selection properly to restore settings

		if (initialPath == null && settings != null) {
			directoryRadioSelected();
		}
		// Third, if we do have an initial path, set the proper
		// path and radio buttons to the initial value. Move
		// cursor to the end of the path so user can see the
		// most relevant part (directory / archive name)
		else if (initialPath != null) {
			boolean dir = new File(initialPath).isDirectory();

			if (dir) {
				directoryPathField.setText(initialPath);
				directoryPathField.setSelection(new Point(initialPath.length(),
						initialPath.length()));
				directoryRadioSelected();
			}
		}
	}

	private void restoreFromHistory(IDialogSettings settings, String key,
			Combo combo) {
		String[] sourceNames = settings.getArray(key);
		if (sourceNames == null) {
			return; // ie.- no values stored, so stop
		}

		for (int i = 0; i < sourceNames.length; i++) {
			combo.add(sourceNames[i]);
		}
	}

	/**
	 * Since Finish was pressed, write widget values to the dialog store so that
	 * they will persist into the next invocation of this wizard page.
	 * 
	 * Method declared public only for use of tests.
	 */
	public void saveWidgetValues() {
		IDialogSettings settings = getDialogSettings();
		if (settings != null) {
			saveInHistory(settings, STORE_DIRECTORIES,
					directoryPathField.getText());
		}
	}

	private void saveInHistory(IDialogSettings settings, String key,
			String value) {
		String[] sourceNames = settings.getArray(key);
		if (sourceNames == null) {
			sourceNames = new String[0];
		}
		sourceNames = addToHistory(sourceNames, value);
		settings.put(key, sourceNames);
	}

	public void handleEvent(Event event) {
	}

	protected boolean allowNewContainerName() {
		return true;
	}

}
