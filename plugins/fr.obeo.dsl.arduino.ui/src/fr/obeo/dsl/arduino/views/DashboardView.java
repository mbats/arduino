package fr.obeo.dsl.arduino.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.ui.business.api.dialect.DialectUIManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.part.ViewPart;

import fr.obeo.dsl.arduino.Project;
import fr.obeo.dsl.arduino.menus.ArduinoUiActivator;
import fr.obeo.dsl.arduino.utils.ArduinoServices;
import fr.obeo.dsl.arduino.wizard.ArduinoExternalProjectImportWizard;
import fr.obeo.dsl.arduino.wizard.ArduinoProjectWizard;

public class DashboardView extends ViewPart implements IPartListener2,
		IResourceChangeListener {
	private static final String SKETCH_MSG = "Open the sketch";
	private static final String SKETCH_INVALID_MSG = "The sketch is invalid. You must define a loop.";
	private static final String UPLOAD_MSG = "Upload the sketch on the arduino board";
	private static final String HARDWARE_MSG = "Open the hardware";
	private static final String HARDWARE_INVALID_MSG = "The hardware is invalid. You must define a platform and connect at least one module.";
	public final static String VIEW_ID = "fr.obeo.dsl.arduino.ui.dashboard";
	private static final String HARDWARE_IMAGE = "icons/128x128/hardware.png";
	private static final String HARDWARE_HOVER_IMAGE = "icons/128x128/hardwareHover.png";
	private static final String HARDWARE_INVALID_IMAGE = "icons/128x128/hardwareInvalid.png";
	private static final String HARDWARE_INVALID_HOVER_IMAGE = "icons/128x128/hardwareInvalidHover.png";
	private static final String SKETCH_IMAGE = "icons/128x128/sketch.png";
	private static final String SKETCH_HOVER_IMAGE = "icons/128x128/sketchHover.png";
	private static final String SKETCH_INVALID_IMAGE = "icons/128x128/sketchInvalid.png";
	private static final String SKETCH_INVALID_HOVER_IMAGE = "icons/128x128/sketchInvalidHover.png";
	private static final String UPLOAD_IMAGE = "icons/128x128/upload.png";
	private static final String UPLOAD_HOVER_IMAGE = "icons/128x128/uploadHover.png";
	private FormToolkit toolkit;
	private ScrolledForm form;
	private List<Image> images = new ArrayList<Image>();
	private ArduinoServices service = new ArduinoServices();
	private ImageHyperlink hardwareLink;
	private ImageHyperlink sketchLink;
	private ImageHyperlink uploadLink;

	public DashboardView() {
	}

	Shell shell;

	@Override
	public void createPartControl(final Composite parent) {

		shell = parent.getShell();

		// Create the form
		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createScrolledForm(parent);
		setFormText();
		GridLayout formLayout = new GridLayout();
		formLayout.numColumns = 3;
		formLayout.horizontalSpacing = 100;
		formLayout.verticalSpacing = 50;
		formLayout.marginWidth = 100;
		formLayout.marginHeight = 100;
		form.getBody().setLayout(formLayout);

		// Create all the hyperlinks
		createNewProjectHyperLink(parent, shell);
		createOpenProjectHyperLink(parent, shell);
		createPreferencesHyperLink(shell);
		createHardwareHyperLink(parent, shell);
		createSketchHyperLink(parent, shell);
		createUploadHyperLink(parent, shell);

		this.getSite().getPage().addPartListener(this);
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}

	private void setFormText() {
		String text = "Arduino Designer - ";
		if (!service.isProjectOpened()) {
			text += "Select or create a project!";
		} else {
			text += "You are working on the project : "
					+ service.getWorkspaceProject().getName() + "!";
		}
		form.setText(text);
	}

	private void createUploadHyperLink(final Composite parent, final Shell shell) {
		uploadLink = createImageHyperlink(form.getBody(), UPLOAD_IMAGE,
				UPLOAD_HOVER_IMAGE, UPLOAD_MSG);
		setUploadLinkEnablement();
		uploadLink.addHyperlinkListener(new HyperlinkAdapter() {
			public void linkActivated(HyperlinkEvent e) {
				upload(shell);
			}
		});
	}

	private void setUploadLinkEnablement() {
		if (service.isValidSketch()) {
			uploadLink.setEnabled(true);
			uploadLink.setToolTipText(UPLOAD_MSG);
		} else {
			uploadLink.setEnabled(false);
		}
	}

	private void createSketchHyperLink(final Composite parent, final Shell shell) {
		sketchLink = createImageHyperlink(form.getBody(), SKETCH_IMAGE,
				SKETCH_HOVER_IMAGE, SKETCH_MSG);
		setSketchLinkEnablement();
		sketchLink.addHyperlinkListener(new HyperlinkAdapter() {
			public void linkActivated(HyperlinkEvent e) {
				Session session = service.getSession();
				DialectUIManager.INSTANCE.openEditor(session,
						service.getDiagram(session, "Sketch"),
						new NullProgressMonitor());
			}
		});
	}

	private void setSketchLinkEnablement() {
		if (service.isValidHardware()) {
			sketchLink.setEnabled(true);
			if (service.isValidSketch()) {
				sketchLink.setToolTipText(SKETCH_MSG);
				sketchLink.setImage(getImage(SKETCH_IMAGE));
				sketchLink.setHoverImage(getImage(SKETCH_HOVER_IMAGE));
			} else {
				sketchLink.setToolTipText(SKETCH_INVALID_MSG);
				sketchLink.setImage(getImage(SKETCH_INVALID_IMAGE));
				sketchLink.setHoverImage(getImage(SKETCH_INVALID_HOVER_IMAGE));
			}
		} else {
			sketchLink.setEnabled(false);
		}
	}

	private void createHardwareHyperLink(final Composite parent,
			final Shell shell) {
		hardwareLink = createImageHyperlink(form.getBody(), HARDWARE_IMAGE,
				HARDWARE_HOVER_IMAGE, HARDWARE_MSG);
		setHardwareLinkEnablement();
		hardwareLink.addHyperlinkListener(new HyperlinkAdapter() {
			public void linkActivated(HyperlinkEvent e) {
				Session session = service.getSession();
				DialectUIManager.INSTANCE.openEditor(session,
						service.getDiagram(session, "Hardware"),
						new NullProgressMonitor());
			}
		});
	}

	private void setHardwareLinkEnablement() {
		if (service.isProjectOpened()) {
			hardwareLink.setEnabled(true);
			if (service.isValidHardware()) {
				hardwareLink.setToolTipText(HARDWARE_MSG);
				hardwareLink.setImage(getImage(HARDWARE_IMAGE));
				hardwareLink.setHoverImage(getImage(HARDWARE_HOVER_IMAGE));
			} else {
				hardwareLink.setImage(getImage(HARDWARE_INVALID_IMAGE));
				hardwareLink
						.setHoverImage(getImage(HARDWARE_INVALID_HOVER_IMAGE));
				hardwareLink.setToolTipText(HARDWARE_INVALID_MSG);
			}
		} else {
			hardwareLink.setEnabled(false);
		}
	}

	private void createPreferencesHyperLink(final Shell shell) {
		ImageHyperlink preferencesLink = createImageHyperlink(form.getBody(),
				"icons/128x128/preferences.png", "icons/128x128/preferencesHover.png",
				"Set the preferences (arduino SDK ,serial port...)");
		preferencesLink.addHyperlinkListener(new HyperlinkAdapter() {
			public void linkActivated(HyperlinkEvent e) {
				preferencesDialog(shell);
			}
		});
	}

	private void createOpenProjectHyperLink(final Composite parent,
			final Shell shell) {
		ImageHyperlink openProjectLink = createImageHyperlink(form.getBody(),
				"icons/128x128/openProject.png", "icons/128x128/openProjectHover.png",
				"Open an existing project");
		openProjectLink.addHyperlinkListener(new HyperlinkAdapter() {
			public void linkActivated(HyperlinkEvent e) {
				openProjectDialog(shell);
			}
		});
	}

	private void createNewProjectHyperLink(final Composite parent,
			final Shell shell) {
		ImageHyperlink newProjectLink = createImageHyperlink(form.getBody(),
				"icons/128x128/newProject.png", "icons/128x128/newProjectHover.png",
				"Create a new project");
		newProjectLink.addHyperlinkListener(new HyperlinkAdapter() {
			public void linkActivated(HyperlinkEvent e) {
				newProjectDialog(shell);
			}
		});
	}

	private void upload(Shell shell) {
		Project project = service.getArduinoProject();
		if (project != null) {
			service.upload(project.getSketch());
		}
	}

	private void refreshForm() {
		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				setFormText();
				setHardwareLinkEnablement();
				setSketchLinkEnablement();
				setUploadLinkEnablement();
				form.getParent().update();

			}
		});

	}

	private ImageHyperlink createImageHyperlink(Composite parent,
			String imagePath, String hoverImagePath, String toolTipText) {
		ImageHyperlink imageLink = toolkit.createImageHyperlink(parent,
				SWT.WRAP);
		Image image = getImage(imagePath);
		imageLink.setImage(image);
		Image hoverImage = getImage(hoverImagePath);
		imageLink.setHoverImage(hoverImage);
		imageLink.setToolTipText(toolTipText);
		return imageLink;
	}

	private Image getImage(String relativePath) {
		return ArduinoUiActivator.getImageDescriptor(relativePath)
				.createImage();
	}

	private void openProjectDialog(Shell shell) {
		WizardDialog wizard = new WizardDialog(shell,
				new ArduinoExternalProjectImportWizard(IDEWorkbenchPlugin
						.getPluginWorkspace().getRoot().getLocation()
						.toOSString()));
		wizard.open();
	}

	private void preferencesDialog(final Composite parent) {
		Shell shell = parent.getShell();
		PreferenceDialog dialog = PreferencesUtil.createPreferenceDialogOn(
				shell, "fr.obeo.dsl.arduino.preferences.ArduinoPreferencePage",
				new String[] {}, null);
		if (dialog != null) {
			dialog.open();
		}
	}

	private void newProjectDialog(final Shell shell) {
		WizardDialog wizard = new WizardDialog(shell,
				new ArduinoProjectWizard());
		wizard.open();
	}

	@Override
	public void setFocus() {
		form.setFocus();
	}

	/**
	 * Disposes the toolkit
	 */
	public void dispose() {
		toolkit.dispose();
		for (Image image : images) {
			image.dispose();
		}
		getSite().getPage().removePartListener(this);
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		super.dispose();
	}

	@Override
	public void partActivated(IWorkbenchPartReference partRef) {

	}

	@Override
	public void partBroughtToTop(IWorkbenchPartReference partRef) {
		refreshForm();
	}

	@Override
	public void partClosed(IWorkbenchPartReference partRef) {

	}

	@Override
	public void partDeactivated(IWorkbenchPartReference partRef) {

	}

	@Override
	public void partOpened(IWorkbenchPartReference partRef) {

	}

	@Override
	public void partHidden(IWorkbenchPartReference partRef) {

	}

	@Override
	public void partVisible(IWorkbenchPartReference partRef) {

	}

	@Override
	public void partInputChanged(IWorkbenchPartReference partRef) {

	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		if (event == null || event.getDelta() == null)
			return;
		try {
			event.getDelta().accept(new IResourceDeltaVisitor() {
				public boolean visit(IResourceDelta delta) throws CoreException {
					if (delta.getKind() == IResourceDelta.OPEN) {
						final IResource resource = delta.getResource();
						if (!(resource instanceof IProject)) {
							return false;
						}
					}
					refreshForm();
					return true;
				}
			});
		} catch (CoreException e) {
			ArduinoUiActivator.log(Status.ERROR, "Refresh dashboard failed", e);
		}
	}
}
