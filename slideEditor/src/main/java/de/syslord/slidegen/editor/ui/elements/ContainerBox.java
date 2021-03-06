package de.syslord.slidegen.editor.ui.elements;

import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.AbsoluteLayout.ComponentPosition;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;

import de.syslord.slidegen.editor.ui.editor.StylableLabel;
import de.syslord.slidegen.editor.util.StreamUtil;

public class ContainerBox extends UiBox {

	private AbsoluteLayout layout;

	public ContainerBox(AbsoluteLayout absoluteLayout) {
		super(absoluteLayout, new UiBoxStyleData());
		this.layout = absoluteLayout;
	}

	public AbsoluteLayout getLayout() {
		return layout;
	}

	@Override
	public String getTreeCaption() {
		return "Container";
	}

	@Override
	public void clearSelection() {
		layout.removeStyleName(SELECTED_STYLE);

		getChildren().stream()
			.forEach(box -> {
				box.clearSelection();
			});
	}

	public UiBox createTextBox(String content, int x, int y, int width, int height) {
		StylableLabel label = new StylableLabel(content);
		label.addStyleName(TEXTBOX_STYLE);

		UiTextBox textbox = new UiTextBox(label);
		textbox.setEditor(editor);

		label.updateStyle(textbox.getUiTextBoxStyleData());

		addToBox(textbox, x, y, width, height);

		outline(label);
		return textbox;
	}

	public ContainerBox createBox(int x, int y, int width, int height) {
		AbsoluteLayout ac = new AbsoluteLayout();
		ac.addStyleName(BOX_STYLE);

		ContainerBox containerBox = new ContainerBox(ac);
		containerBox.setEditor(editor);

		addToBox(containerBox, x, y, width, height);

		outline(ac);
		return containerBox;
	}

	public void moveInsert(UiBox uiBox, int x, int y) {
		addToLayout(uiBox, x, y, uiBox.getWidth(), uiBox.getHeight());
	}

	private void addToBox(UiBox uiBox, float x, float y, int width, int height) {
		addToLayout(uiBox, x, y, width, height);

		editor.getEditorTree().addTreeItem(uiBox, this);
	}

	private void addToLayout(UiBox uiBox, float x, float y, int width, int height) {
		Component component = uiBox.getComponent();

		layout.addComponent(component);

		ComponentPosition pos = layout.new ComponentPosition();
		pos.setTop(y, Unit.PIXELS);
		pos.setLeft(x, Unit.PIXELS);
		layout.setPosition(component, pos);

		component.setWidth(String.valueOf(width) + "px");
		component.setHeight(String.valueOf(height) + "px");
	}

	public List<UiBox> getChildren() {
		return StreamUtil.asStream(layout.iterator())
			.map(c -> (AbstractComponent) c)
			.map(ac -> UiBox.getBoxOf(ac))
			.filter(o -> o.isPresent())
			.map(o -> o.get())
			.collect(Collectors.toList());
	}

	public List<UiBox> getChildrenOrdered() {
		return editor.getEditorTree().getOrderedChildren(this);
	}

}
