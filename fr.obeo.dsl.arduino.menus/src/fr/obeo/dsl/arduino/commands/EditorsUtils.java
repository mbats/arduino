package fr.obeo.dsl.arduino.commands;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;

import fr.obeo.dsl.viewpoint.ui.business.api.dialect.DialectUIManager;

public class EditorsUtils {
	public static void closeOpenedEditors() {
		for (IEditorReference editorRef : PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage()
				.getEditorReferences()) {
			IEditorPart editor = editorRef.getEditor(false);
			DialectUIManager.INSTANCE.closeEditor(editor, true);
		}
	}
}
