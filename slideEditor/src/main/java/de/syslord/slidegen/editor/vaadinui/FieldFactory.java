package de.syslord.slidegen.editor.vaadinui;

import java.util.function.Consumer;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.ui.TextField;

public class FieldFactory {

	public TextField createIntegerAsStringPropertyField(String caption, String content, int minval, int maxVal,
			Consumer<String> c) {
		TextField field = new TextField(caption, content);
		field.setConverter(Integer.class);
		field.setNullSettingAllowed(false);
		field.addValidator(new IntegerRangeValidator("Position außerhalb des Feldes", minval, maxVal));
		field.addValueChangeListener(event -> {
			if (field.isValid()) {
				c.accept(field.getValue());
			}
		});
		return field;
	}

	public TextField createNullableIntegerAsStringPropertyField(String caption, String content, int minval, int maxVal,
			Consumer<String> c) {
		TextField field = createIntegerAsStringPropertyField(caption, content, minval, maxVal, c);

		field.setNullSettingAllowed(true);
		field.setNullRepresentation("--");

		return field;
	}

	public TextField createIntegerPropertyField(String caption, Integer content, int minval, int maxVal, Consumer<Integer> c) {
		TextField field = new TextField(caption, new ObjectProperty<Integer>(content));
		field.setConverter(Integer.class);
		field.setNullSettingAllowed(false);
		field.addValidator(new IntegerRangeValidator("Position außerhalb des Feldes", minval, maxVal));
		field.addValueChangeListener(event -> {
			if (field.isValid()) {
				c.accept((Integer) field.getConvertedValue());
			}
		});
		return field;
	}

	public TextField createNullableIntegerPropertyField(String caption, Integer content, int minval, int maxVal,
			Consumer<Integer> c) {
		TextField field = createIntegerPropertyField(caption, content, minval, maxVal, c);

		field.setNullSettingAllowed(true);
		field.setNullRepresentation("--");

		return field;
	}
}
