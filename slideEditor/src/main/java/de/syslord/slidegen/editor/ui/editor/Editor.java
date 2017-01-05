package de.syslord.slidegen.editor.ui.editor;

import java.io.ByteArrayInputStream;

import org.springframework.util.Assert;

import com.vaadin.server.StreamResource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Image;

import de.syslord.slidegen.editor.ui.elements.ContainerBox;
import de.syslord.slidegen.editor.ui.elements.UiBox;
import de.syslord.slidegen.editor.util.ResourceUtil;

/*
 * Editor should not be a ContainerBox but contain one. Editor and ContainerBox should implement the
 * same interface 'Container' or something. Editor should be an observer of all Containers to
 * observe changes and act upon them. For example updating the tree.
 */
public class Editor extends ContainerBox {

	private EditorTree editorTree;

	public Editor(AbsoluteLayout absoluteLayout, TreeListener<UiBox, ContainerBox> treeListener) {
		super(absoluteLayout);
		editor = this;

		editorTree = new EditorTree(treeListener);
		editorTree.addTreeItem(this);
	}

	public EditorTree getEditorTree() {
		return editorTree;
	}

	@Override
	public boolean isEditor() {
		return true;
	}

	public void setBackdropImage(String backgroundImageResource) {
		getUiStyleData().setImage(ResourceUtil.getResourceAsStream(backgroundImageResource));

		StreamResource source = new StreamResource(() -> {
			// Stream needs to be created here as a page refresh will try to reread the resource which will
			// fail even with in.reset()
			ByteArrayInputStream in = ResourceUtil.getResourceAsStream(backgroundImageResource);
			Assert.notNull(in);

			return in;
		}, "backdrop.png");
		Image image = new Image("", source);
		image.setSizeFull();

		// The background must be an image component but it will be ignored without a UiBox as data.
		getLayout().addComponent(image);
	}

}
