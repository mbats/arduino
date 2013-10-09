package fr.obeo.dsl.arduino.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Scanner;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;

import fr.obeo.dsl.arduino.Instruction;
import fr.obeo.dsl.arduino.Project;
import fr.obeo.dsl.arduino.Sketch;
import fr.obeo.dsl.arduino.gen.main.Generate;
import fr.obeo.dsl.arduino.menus.ArduinoUiActivator;
import fr.obeo.dsl.viewpoint.business.api.session.Session;
import fr.obeo.dsl.viewpoint.business.api.session.SessionManager;
import fr.obeo.dsl.viewpoint.ui.business.api.dialect.DialectUIManager;

public class ArduinoServices {
	public void closeOpenedEditors() {
		for (IEditorReference editorRef : PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage()
				.getEditorReferences()) {
			IEditorPart editor = editorRef.getEditor(false);
			DialectUIManager.INSTANCE.closeEditor(editor, true);
		}
	}

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
		return (Project) resource.getContents().get(0);
	}

	public boolean isValidSketch(Sketch sketch) {

		if (sketch != null) {
			Instruction instruction = sketch;
			while (instruction != null && instruction.getNext() != null
					&& !(instruction.getNext() instanceof Sketch)) {
				instruction = instruction.getNext();
			}

			if (instruction != null && instruction.getNext() instanceof Sketch) {
				return true;
			}
		}
		return false;
	}

	public void upload(final Sketch sketch) {
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(PlatformUI
				.getWorkbench().getActiveWorkbenchWindow().getShell());
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
					executeCommand("make", genFolder, null);
					monitor.worked(33);
					monitor.subTask("Upload code");
					executeCommand("make", genFolder, "upload");
					monitor.done();
				}
			});
		} catch (InvocationTargetException e) {
			ArduinoUiActivator.log(Status.ERROR, "Upload failed", e);
		} catch (InterruptedException e) {
			ArduinoUiActivator.log(Status.ERROR, "Upload failed", e);
		}

	}

	private File generateCode(Sketch sketch) {
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
		return genFolder;
	}

	private void executeCommand(String command, File directory, String arg) {
		ProcessBuilder builder;
		if (arg != null) {
			builder = new ProcessBuilder(command, arg);
		} else {
			builder = new ProcessBuilder(command);
		}

		builder.directory(directory);
		Map<String, String> env = builder.environment();
		env.put("ARDUINO_DIR", "/usr/share/arduino");
		env.put("ARDMK_DIR", "/usr/share/arduino");
		env.put("ARDMK_PATH", "/usr/bin");
		env.put("AVR_TOOLS_DIR", "/usr");
		try {
			Process process = builder.start();
			inheritIO(process.getInputStream(), System.out);
			inheritIO(process.getErrorStream(), System.err);
			process.waitFor();
		} catch (IOException e) {
			ArduinoUiActivator.log(Status.ERROR, "Run command " + command
					+ " failed", e);
		} catch (InterruptedException e) {
			ArduinoUiActivator.log(Status.ERROR, "Run command " + command
					+ " failed", e);
		}
	}

	private static void inheritIO(final InputStream src, final PrintStream dest) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Scanner sc = new Scanner(src);
				while (sc.hasNextLine()) {
					dest.println(sc.nextLine());
				}
			}
		}).start();
	}
}
