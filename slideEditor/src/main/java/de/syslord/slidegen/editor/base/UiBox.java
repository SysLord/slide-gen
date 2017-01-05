package de.syslord.slidegen.editor.base;

import java.util.Optional;

import com.vaadin.data.Property;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.AbsoluteLayout.ComponentPosition;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;

import de.syslord.slidegen.editor.model.UiBoxStyleData;
import de.syslord.slidegen.editor.util.Numbers;

public abstract class UiBox extends UiObject {

	private static final int SAFETY_MARGIN = 2;

	protected static final String SELECTED_STYLE = "selectedLayout";

	protected static final String TEXTBOX_STYLE = "editor-textbox";

	protected static final String BOX_STYLE = "editor-box";

	private UiBoxStyleData uiBoxStyleData;

	private AbstractComponent component;

	protected Editor editor;

	protected UiBox(AbstractComponent component, UiBoxStyleData uiBoxStyleData) {
		this.component = component;
		this.uiBoxStyleData = uiBoxStyleData;
		component.setData(this);
	}

	// This is dangerous in top down recursion, as we may get a parent and produce an endless loop. Use
	// getBoxOf() instead.
	public static UiBox of(Component component) {
		Component c = component;

		while (c != null && !UiBox.getBoxOf(c).isPresent()) {
			c = c.getParent();
		}

		return UiBox.getBoxOf(c)
			.orElseThrow(() -> new RuntimeException("Component outside of editor"));
	}

	// This enables us to skip ui elements that are present in the editor component tree but should be
	// ignored.
	protected static Optional<UiBox> getBoxOf(Component component) {
		AbstractComponent ac = (AbstractComponent) component;
		UiBox data = (UiBox) ac.getData();
		return Optional.ofNullable(data);
	}

	public UiBoxStyleData getUiStyleData() {
		return uiBoxStyleData;
	}

	/*
	 * Using isEditor() in most of the getters is not very object oriented but it makes the special handling
	 * obvious in this class.
	 */
	public boolean isEditor() {
		return false;
	}

	public ContainerBox getContainer() {
		if (ContainerBox.class.isAssignableFrom(this.getClass())) {
			return (ContainerBox) this;
		}
		return getParent();
	}

	public AbstractComponent getComponent() {
		return component;
	}

	public ContainerBox getParent() {
		if (isEditor()) {
			return (ContainerBox) this;
		}
		AbstractComponent parent = (AbstractComponent) component.getParent();
		return (ContainerBox) parent.getData();
	}

	public int getX() {
		if (isEditor()) {
			return 0;
		}
		return getParent().getLayout().getPosition(component).getLeftValue().intValue();
	}

	public int getY() {
		if (isEditor()) {
			return 0;
		}
		return getParent().getLayout().getPosition(component).getTopValue().intValue();
	}

	public int getWidth() {
		return (int) component.getWidth();
	}

	public int getHeight() {
		return (int) component.getHeight();
	}

	public void setWidth(int width) {
		if (isEditor()) {
			// no constraint for editor
			component.setWidth(width, Unit.PIXELS);
			return;
		}

		int safeWidth = Math.max(2, width);

		int space = getParent().getWidth() - getX();
		boolean newWidthFits = space - safeWidth > 0;

		if (newWidthFits) {
			component.setWidth(safeWidth, Unit.PIXELS);
		} else {
			component.setWidth(space, Unit.PIXELS);
		}
	}

	public void setHeight(int height) {
		if (isEditor()) {
			// no constraint for editor
			component.setHeight(height, Unit.PIXELS);
			return;
		}

		int safeHeight = Math.max(2, height);

		int space = getParent().getHeight() - getY();
		boolean newHeightFits = space - safeHeight > 0;

		if (newHeightFits) {
			component.setHeight(safeHeight, Unit.PIXELS);
		} else {
			component.setHeight(space, Unit.PIXELS);
		}
	}

	public void clearSelection() {
		component.removeStyleName(SELECTED_STYLE);
	}

	public void select() {
		component.addStyleName(SELECTED_STYLE);
	}

	protected void outline(Component... c) {
		for (Component x : c) {
			x.addStyleName("outlinedLayout");
		}
	}

	public void remove() {
		if (!isEditor()) {
			ContainerBox parent = getParent();
			AbsoluteLayout containerLayout = parent.getLayout();

			containerLayout.removeComponent(component);
		}
	}

	public void setX(int x, boolean adding) {
		ContainerBox parent = getParent();
		AbsoluteLayout containerLayout = parent.getLayout();

		ComponentPosition oldPosition = containerLayout.getPosition(component);
		ComponentPosition newPosition = containerLayout.new ComponentPosition();

		Float newX = adding ? oldPosition.getLeftValue() + x : (float) x;
		Float newY = oldPosition.getTopValue();

		Float saneX = Numbers.limit(newX, 0, parent.getWidth() - SAFETY_MARGIN);
		Float saneY = Numbers.limit(newY, 0, parent.getHeight() - SAFETY_MARGIN);

		newPosition.setLeft(saneX, Unit.PIXELS);
		newPosition.setTop(saneY, Unit.PIXELS);

		containerLayout.setPosition(component, newPosition);
	}

	public void setY(int y, boolean adding) {
		ContainerBox parent = getParent();
		AbsoluteLayout containerLayout = parent.getLayout();

		ComponentPosition oldPosition = containerLayout.getPosition(component);
		ComponentPosition newPosition = containerLayout.new ComponentPosition();

		Float newX = oldPosition.getLeftValue();
		Float newY = adding ? oldPosition.getTopValue() + y : (float) y;

		Float saneY = Numbers.limit(newY, 0, (int) parent.getLayout().getHeight() - SAFETY_MARGIN);
		Float saneX = Numbers.limit(newX, 0, (int) parent.getLayout().getWidth() - SAFETY_MARGIN);

		newPosition.setLeft(saneX, Unit.PIXELS);
		newPosition.setTop(saneY, Unit.PIXELS);
		containerLayout.setPosition(component, newPosition);
	}

	public void setEditor(Editor editor) {
		this.editor = editor;
	}

	public boolean isA(Class<?> clazz) {
		return clazz.isAssignableFrom(getClass());
	}

	public boolean componentIsA(Class<?> clazz) {
		return clazz.isAssignableFrom(component.getClass());
	}

	@SuppressWarnings("unchecked")
	public <T> T getComponentAs() {
		return (T) component;
	}

	@SuppressWarnings("unchecked")
	public String getValue() {
		if (StylableLabel.class.isAssignableFrom(component.getClass())) {
			return ((StylableLabel) component).getText();
		} else if (Property.class.isAssignableFrom(component.getClass())) {
			return ((Property<String>) component).getValue();
		}
		return "";
	}

	@SuppressWarnings("unchecked")
	public void setValue(String value) {
		if (Property.class.isAssignableFrom(Property.class)) {
			((Property<String>) component).setValue(value);
		}
	}

}
