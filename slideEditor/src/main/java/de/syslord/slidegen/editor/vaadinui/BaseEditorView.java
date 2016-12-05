package de.syslord.slidegen.editor.vaadinui;

import java.util.Optional;
import java.util.stream.Stream;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.AbsoluteLayout.ComponentPosition;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

import de.syslord.slidegen.editor.model.UiBox;
import de.syslord.slidegen.editor.util.UiUtil;

public abstract class BaseEditorView<T> extends BaseView<T> {

	private static final long serialVersionUID = 1L;

	protected AbsoluteLayout editor;

	private GridLayout props;

	protected abstract void onEditableComponentClicked(Component clickedComponent);

	protected Component createProperties() {
		props = new GridLayout(10, 1);
		props.setHideEmptyRowsAndColumns(true);
		props.setMargin(true);
		props.setSpacing(true);
		props.setSizeFull();
		return props;
	}

	protected void clearProps() {
		props.removeAllComponents();
	}

	protected void addProperty(Component component) {
		setPropertyStyle(component);
		props.addComponent(component);
	}

	protected void addProperties(Component... components) {
		Stream.of(components).forEach(c -> setPropertyStyle(c));
		props.addComponent(new VerticalLayout(components));
	}

	protected void addProperties(int index, Component... components) {
		Stream.of(components).forEach(c -> setPropertyStyle(c));

		VerticalLayout gridComponent = (VerticalLayout) props.getComponent(index, 0);
		if (gridComponent == null) {
			gridComponent = new VerticalLayout();
			props.addComponent(gridComponent, index, 0);
		}

		gridComponent.addComponents(components);
	}

	private void setPropertyStyle(Component component) {
		component.setWidth("100px");
	}

	protected Component createEditor(int width, int height) {
		editor = new AbsoluteLayout();
		editor.setWidth(width + "px");
		editor.setHeight(height + "px");
		editor.addStyleName("editor");

		editor.addLayoutClickListener(event -> onEditorClicked(event));
		return editor;
	}

	protected void onEditorClicked(LayoutClickEvent event) {
		removeComponentSelection(editor);
		clearProps();

		Component clickedComponent = event.getClickedComponent();

		if (clickedComponent != null) {
			clickedComponent.addStyleName("selectedLayout");
			onEditableComponentClicked(clickedComponent);
		}
	}

	private void addToBox(AbsoluteLayout absoluteLayout, Component component, float x, float y, int width, int height) {
		// make sure every box in editor has uiBox as data
		if (getUiBox(component) == null) {
			setUiBox(component, new UiBox());
		}

		absoluteLayout.addComponent(component);

		ComponentPosition pos = absoluteLayout.new ComponentPosition();
		pos.setTop(y, Unit.PIXELS);
		pos.setLeft(x, Unit.PIXELS);
		absoluteLayout.setPosition(component, pos);

		component.setWidth(String.valueOf(width) + "px");
		component.setHeight(String.valueOf(height) + "px");
	}

	protected UiBox getUiBox(Component component) {
		return UiUtil.getUiBox(component);
	}

	protected void setUiBox(Component component, UiBox uiBox) {
		AbstractComponent ac = (AbstractComponent) component;
		ac.setData(uiBox);
	}

	protected Label createTextBox(String content, AbsoluteLayout parentBox, float x, float y, int width, int height) {
		Label textBox = new Label(content);
		textBox.addStyleName("editor-textbox");

		addToBox(parentBox, textBox, x, y, width, height);

		// TODO for now outline everything for debugging
		outline(textBox);
		return textBox;
	}

	protected AbsoluteLayout createBox(AbsoluteLayout parentBox, float x, float y, int width, int height) {
		AbsoluteLayout nest = new AbsoluteLayout();
		nest.addStyleName("editor-box");

		addToBox(parentBox, nest, x, y, width, height);

		// TODO for now outline everything for debugging
		outline(nest);
		return nest;
	}

	protected void outline(Component... c) {
		for (Component x : c) {
			x.addStyleName("outlinedLayout");
		}
	}

	private void removeComponentSelection(Layout layout) {
		layout.iterator().forEachRemaining(x -> {
			x.removeStyleName("selectedLayout");
			maybeGetLayout(x).ifPresent(l -> removeComponentSelection(l));

		});
	}

	private Optional<Layout> maybeGetLayout(Component x) {
		return Layout.class.isAssignableFrom(x.getClass()) ? Optional.of((Layout) x) : Optional.empty();
	}

	protected void setNewAbsoluteX(AbsoluteLayout layout, Component component, int x) {
		ComponentPosition oldPosition = layout.getPosition(component);
		ComponentPosition newPosition = layout.new ComponentPosition();
		newPosition.setTop(oldPosition.getTopValue(), Unit.PIXELS);
		newPosition.setLeft((float) x, Unit.PIXELS);
		layout.setPosition(component, newPosition);
	}

	protected void setNewAbsoluteY(AbsoluteLayout layout, Component component, int y) {
		ComponentPosition oldPosition = layout.getPosition(component);
		ComponentPosition newPosition = layout.new ComponentPosition();
		newPosition.setTop((float) y, Unit.PIXELS);
		newPosition.setLeft(oldPosition.getLeftValue(), Unit.PIXELS);
		layout.setPosition(component, newPosition);
	}

}
