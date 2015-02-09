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
package fr.obeo.dsl.arduino.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.acceleo.common.preference.AcceleoPreferences;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.sirius.business.api.dialect.DialectManager;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;

import fr.obeo.dsl.arduino.Connector;
import fr.obeo.dsl.arduino.Hardware;
import fr.obeo.dsl.arduino.Instruction;
import fr.obeo.dsl.arduino.Module;
import fr.obeo.dsl.arduino.ModuleInstruction;
import fr.obeo.dsl.arduino.Project;
import fr.obeo.dsl.arduino.Sketch;
import fr.obeo.dsl.arduino.build.ArduinoBuilder;
import fr.obeo.dsl.arduino.gen.main.Generate;
import fr.obeo.dsl.arduino.menus.ArduinoUiActivator;
import fr.obeo.dsl.arduino.preferences.ArduinoPreferences;
import fr.obeo.dsl.arduino.preferences.ArduinoSdkDialog;

public class ArduinoServices {
	private ArduinoPreferences preferences = new ArduinoPreferences();

	public IProject getWorkspaceProject() {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot()
				.getProjects();

		if (projects.length > 0) {
			return projects[0];
		}
		return null;
	}

	public Session getSession() {
		Collection<Session> sessions = SessionManager.INSTANCE.getSessions();
		if (sessions.size() > 0) {
			return (Session) sessions.toArray()[0];
		}
		return null;
	}

	public Project getArduinoProject() {
		Session session = getSession();
		if (session == null) {
			return null;
		}
		Collection<Resource> resources = session.getSemanticResources();
		if (resources.size() == 0) {
			return null;
		}
		Resource resource = (Resource) resources.toArray()[0];

		List<EObject> contents = resource.getContents();
		if (contents == null || contents.size() == 0) {
			return null;
		}
		return (Project) contents.get(0);
	}

	public boolean isInvalidSketch(Sketch sketch) {
		return !isValidSketch(sketch);
	}

	public boolean isValidSketch(Sketch sketch) {

		if (sketch != null) {
			Instruction instruction = sketch;
			while (instruction != null && instruction.getNext() != null
					&& !(instruction.getNext() instanceof Sketch)) {
				instruction = instruction.getNext();
			}

			if (instruction != null && instruction.getNext() != null
					&& instruction.getNext() instanceof Sketch) {
				return true;
			}
		}
		return false;
	}

	public void upload(final Sketch sketch) {
		if (preferences.getArduinoSdk() == null
				|| preferences.getArduinoSdk().length() == 0) {
			askUser();
			return;
		}
		final ProgressMonitorDialog dialog = new ProgressMonitorDialog(
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		try {
			dialog.run(true, true, new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) {
					monitor.beginTask("Upload sketch to arduino platform...",
							100);
					monitor.subTask("Generate code");
					File genFolder = generateCode(sketch);
					monitor.worked(33);
					monitor.subTask("Compile code");

					String arduinoSdk = preferences.getArduinoSdk();
					String serialPort = preferences.getArduinoSerialPort();
					String boardTag = sketch.getHardware().getPlatforms()
							.get(0).getName();
					String workingDirectory = genFolder.toString();
					ArduinoBuilder builder = new ArduinoBuilder(arduinoSdk,
							boardTag, workingDirectory, serialPort);
					List<String> libraries = getLibraries(sketch);
					final IStatus compileStatus = builder.compile("Sketch",
							libraries);
					if (compileStatus.getSeverity() != IStatus.OK) {
						Display.getDefault().syncExec(new Runnable() {
							public void run() {
								MessageDialog.openError(
										dialog.getShell(),
										"Compilation Fail",
										"Compilation fail : "
												+ compileStatus.getMessage());
							}
						});
						return;
					}

					monitor.worked(33);
					monitor.subTask("Upload code");
					final IStatus uploadStatus = builder.upload();
					if (uploadStatus.getSeverity() != IStatus.OK) {
						Display.getDefault().syncExec(new Runnable() {
							public void run() {
								MessageDialog.openError(dialog.getShell(),
										"Upload Fail", "Upload fail : "
												+ uploadStatus.getMessage());
							}
						});
					}
					monitor.done();
				}
			});
		} catch (InvocationTargetException e) {
			ArduinoUiActivator.log(Status.ERROR, "Upload failed", e);
		} catch (InterruptedException e) {
			ArduinoUiActivator.log(Status.ERROR, "Upload failed", e);
		}
	}

	protected List<String> getLibraries(Sketch sketch) {
		final List<String> libraries = new ArrayList<String>();
		UnmodifiableIterator<ModuleInstruction> it = Iterators.filter(
				sketch.eAllContents(), ModuleInstruction.class);
		while (it.hasNext()) {
			ModuleInstruction input = it.next();
			String library = input.getModule().getLibrary().getName();
			if (!libraries.contains(library) && !library.equals("none")) {
				libraries.add(library);
			}
		}
		return libraries;
	}

	private void askUser() {
		Shell shell = PlatformUI.getWorkbench().getModalDialogShellProvider()
				.getShell();

		ArduinoSdkDialog dialog = new ArduinoSdkDialog(shell);
		dialog.open();
	}

	private File generateCode(Sketch sketch) {
		boolean oldNotificationsPref = AcceleoPreferences
				.areNotificationsForcedDisabled();
		AcceleoPreferences.switchForceDeactivationNotifications(true);
		IFile file = ResourcesPlugin
				.getWorkspace()
				.getRoot()
				.getFile(
						new Path(sketch.eResource().getURI()
								.toPlatformString(true)));
		IFolder folder = file.getProject().getFolder("code");
		File genFolder = folder.getRawLocation().makeAbsolute().toFile();

		try {
			Generate generator = new Generate(sketch.eResource().getURI(),
					genFolder, new ArrayList<Object>());
			generator.doGenerate(new BasicMonitor());
		} catch (IOException e) {
			ArduinoUiActivator.log(Status.ERROR, "Code generation failed", e);
		}
		AcceleoPreferences
				.switchForceDeactivationNotifications(oldNotificationsPref);

		return genFolder;
	}

	public DRepresentation getDiagram(Session session, String diagramName) {
		Collection<DRepresentation> representations = DialectManager.INSTANCE
				.getAllRepresentations(session);
		DRepresentation diagram = null;
		for (DRepresentation representation : representations) {
			if (diagramName.equals(representation.getName())) {
				diagram = representation;
			}
		}
		return diagram;
	}

	public boolean isProjectOpened() {
		return getWorkspaceProject() != null;
	}

	public boolean isValidSketch() {
		Project project = getArduinoProject();
		if (project == null) {
			return false;
		}
		Sketch sketch = project.getSketch();
		if (sketch == null) {
			return false;
		}
		return isValidSketch(sketch);
	}

	public boolean isValidHardware() {
		Project project = getArduinoProject();
		return !(project == null || project.getHardware() == null
				|| project.getHardware().getPlatforms().size() == 0
				|| project.getHardware().getModules().size() == 0 || getConnectedModules(
					project.getHardware()).size() == 0);
	}

	private List<Module> getConnectedModules(Hardware hardware) {
		List<Module> result = Lists.newArrayList();
		for (Connector connector : hardware.getConnectors()) {
			result.add(connector.getModule());
		}
		return result;
	}
}
