package de.syslord.slidegen.uiedit.vaadinui;

import java.util.Optional;
import java.util.function.Consumer;

import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.AbsoluteLayout.ComponentPosition;
import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public abstract class BaseEditorView<T> extends BaseView<T> {

	private static final long serialVersionUID = 1L;

	protected AbsoluteLayout editor;

	private VerticalLayout props;

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		// Only with navigator, otherwise use attach()
		// presenter.onViewEntered();
	}

	@Override
	public void detach() {
		super.detach();
	}

	protected abstract void onEditableComponentClicked(Component clickedComponent);

	protected Component createProperties() {
		props = new VerticalLayout();
		props.setMargin(true);
		props.setSpacing(true);
		props.setSizeFull();
		return props;
	}

	protected void clearProps() {
		props.removeAllComponents();
	}

	protected void addProperty(Component component) {
		props.addComponent(component);
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

	protected void addToBox(AbsoluteLayout absoluteLayout, Component component, float x, float y, int width, int height) {
		absoluteLayout.addComponent(component);
		ComponentPosition pos = absoluteLayout.new ComponentPosition();
		pos.setTop(y, Unit.PIXELS);
		pos.setLeft(x, Unit.PIXELS);
		absoluteLayout.setPosition(component, pos);
		component.setWidth(String.valueOf(width) + "px");
		component.setHeight(String.valueOf(height) + "px");
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

	protected TextField createStringPropertyField(String caption, String content, int minval, int maxVal, Consumer<String> c) {
		TextField field = new TextField(caption, content);
		field.setConverter(Integer.class);
		field.addValidator(new IntegerRangeValidator("Position außerhalb des Feldes", minval, maxVal));
		field.addValueChangeListener(event -> {
			if (field.isValid()) {
				c.accept(field.getValue());
			}
		});
		return field;
	}

	protected TextField createIntegerPropertyField(String caption, Integer content, int minval, int maxVal, Consumer<Integer> c) {
		TextField field = new TextField(caption, String.valueOf(content));
		field.setConverter(Integer.class);
		field.addValidator(new IntegerRangeValidator("Position außerhalb des Feldes", minval, maxVal));
		field.addValueChangeListener(event -> {
			if (field.isValid()) {
				c.accept((Integer) field.getConvertedValue());
			}
		});
		return field;
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
