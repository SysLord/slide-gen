package de.syslord.slidegen.editor.base;

import java.util.function.Consumer;
import java.util.stream.Stream;

import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.VerticalLayout;

public class EditorProperties {

	private GridLayout properties;

	private Consumer<Component> propertiesStyleSetter;

	public EditorProperties(Consumer<Component> propertiesStyleSetter) {
		this.propertiesStyleSetter = propertiesStyleSetter;
	}

	public Component createProperties() {
		properties = new GridLayout(10, 1);
		properties.setHideEmptyRowsAndColumns(true);
		properties.setMargin(true);
		properties.setSpacing(true);
		properties.setSizeFull();
		return properties;
	}

	public void clear() {
		properties.removeAllComponents();
	}

	public void addProperty(Component component) {
		propertiesStyleSetter.accept(component);
		properties.addComponent(component);
	}

	public void addProperties(Component... components) {
		Stream.of(components).forEach(c -> propertiesStyleSetter.accept(c));
		properties.addComponent(new VerticalLayout(components));
	}

	public void addProperties(int index, Component... components) {
		Stream.of(components).forEach(c -> propertiesStyleSetter.accept(c));

		VerticalLayout gridComponent = (VerticalLayout) properties.getComponent(index, 0);
		if (gridComponent == null) {
			gridComponent = new VerticalLayout();
			properties.addComponent(gridComponent, index, 0);
		}

		gridComponent.addComponents(components);
	}

}
