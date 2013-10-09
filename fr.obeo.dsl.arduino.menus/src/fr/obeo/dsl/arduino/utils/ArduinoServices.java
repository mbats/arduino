package fr.obeo.dsl.arduino.utils;

import java.util.Collection;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;

import fr.obeo.dsl.arduino.Instruction;
import fr.obeo.dsl.arduino.Project;
import fr.obeo.dsl.arduino.Sketch;
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
			Instruction instruction = sketch.getNext();
			while (instruction != null
					&& instruction.getNext() instanceof Sketch) {
				instruction = instruction.getNext();
			}

			if (instruction instanceof Sketch) {
				return true;
			}
		}
		return false;
	}
}
