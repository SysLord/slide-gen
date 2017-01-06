package de.syslord.slidegen.editor.ui.editor;

public interface TreeListener<CHILD, PARENT> {

	void setNewParent(CHILD child, PARENT newParent);

}
