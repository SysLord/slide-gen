package de.syslord.slidegen.editor.base;

import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.AbsoluteLayout.ComponentPosition;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

import de.syslord.slidegen.editor.util.StreamUtil;

public class ContainerBox extends UiBox {

	private AbsoluteLayout layout;

	public ContainerBox(AbsoluteLayout absoluteLayout) {
		super(absoluteLayout);
		this.layout = absoluteLayout;
	}

	public AbsoluteLayout getLayout() {
		return layout;
	}

	@Override
	public void removeComponentSelection() {
		getChildren().stream()
			.forEach(box -> {
				layout.removeStyleName(SELECTED_STYLE);
				box.removeComponentSelection();
			});
	}

	public UiBox createTextBox(String content, int x, int y, int width, int height) {
		Label label = new Label(content);
		label.addStyleName(TEXTBOX_STYLE);

		UiBox textbox = new UiBox(label);
		textbox.setEditor(editor);

		addToBox(label, x, y, width, height);

		// TODO debugging
		outline(label);
		return textbox;
	}

	public ContainerBox createBox(int x, int y, int width, int height) {
		AbsoluteLayout ac = new AbsoluteLayout();
		ac.addStyleName(BOX_STYLE);

		ContainerBox containerBox = new ContainerBox(ac);
		containerBox.setEditor(editor);

		addToBox(ac, x, y, width, height);

		// TODO debugging
		outline(ac);
		return containerBox;
	}

	// TODO works on component rather than uibox?
	private void addToBox(Component component, float x, float y, int width, int height) {
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
			.map(ac -> (UiBox) ac.getData())
			.collect(Collectors.toList());
	}

}