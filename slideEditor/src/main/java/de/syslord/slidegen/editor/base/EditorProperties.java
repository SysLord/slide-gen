package de.syslord.slidegen.editor.base;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.google.gwt.thirdparty.guava.common.collect.Maps;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.syslord.slidegen.editor.baseui.CustomTextField;

public class EditorProperties {

	private GridLayout properties;

	private Consumer<Component> propertiesStyleSetter;

	private Map<Field<?>, Runnable> valueUpdaters = Maps.newHashMap();

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
		valueUpdaters.clear();
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

	public void reReadValues() {
		streamComponents(properties)
			.filter(c -> Layout.class.isAssignableFrom(c.getClass()))
			.flatMap(c -> streamComponents((Layout) c))
			.forEach(component -> tryReReadValues(component));
	}

	private Stream<Component> streamComponents(Layout layout) {
		return StreamSupport.stream(layout.spliterator(), false);
	}

	private void tryReReadValues(Component component) {
		if (valueUpdaters.containsKey(component)) {
			valueUpdaters.get(component).run();
		}
	}

	public <T> void setValueProvider(TextField field, Supplier<T> supplier) {
		if (CustomTextField.class.isAssignableFrom(field.getClass())) {

			CustomTextField customTextField = (CustomTextField) field;

			valueUpdaters.put(customTextField,
					() -> {
						customTextField.setFireOnValueChange(false);
						field.setConvertedValue(supplier.get());
						customTextField.setFireOnValueChange(true);
					});

		} else {
			// fallback
			valueUpdaters.put(field,
					() -> field.setConvertedValue(supplier.get()));
		}
	}

}
