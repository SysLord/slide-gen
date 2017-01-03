package de.syslord.slidegen.editor.base;

import java.util.function.Consumer;

import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ColorPickerArea;
import com.vaadin.ui.TextField;

import de.syslord.slidegen.editor.baseui.CustomTextField;

public class PropertyFieldFactory {

	public TextField createIntegerAsStringField(String caption, String content, int minval, int maxVal,
			Consumer<String> c) {
		TextField field = new CustomTextField(caption);
		field.setValue(content);

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

	public TextField createNullableIntegerAsStringField(String caption, String content, int minval, int maxVal,
			Consumer<String> c) {
		TextField field = createIntegerAsStringField(caption, content, minval, maxVal, c);

		field.setNullSettingAllowed(true);
		field.setNullRepresentation("--");

		return field;
	}

	public TextField createIntegerField(String caption, Integer content, int minval, int maxVal, Consumer<Integer> c) {
		TextField field = new CustomTextField(caption);
		field.setConverter(Integer.class);
		field.setNullSettingAllowed(false);
		field.addValidator(new IntegerRangeValidator("Position außerhalb des Feldes", minval, maxVal));

		field.setConvertedValue(content);

		field.addValueChangeListener(event -> {
			if (field.isValid()) {
				c.accept((Integer) field.getConvertedValue());
			}
		});

		return field;
	}

	public TextField createNullableIntegerField(String caption, Integer content, int minval, int maxVal,
			Consumer<Integer> c) {
		TextField field = createIntegerField(caption, content, minval, maxVal, c);

		field.setNullSettingAllowed(true);
		field.setNullRepresentation("--");

		return field;
	}

	public CheckBox createCheckbox(String caption, Boolean content, Consumer<Boolean> c) {
		CheckBox checkBox = new CheckBox(caption, content);
		checkBox.addValueChangeListener(event -> {
			c.accept(checkBox.getValue());
		});
		return checkBox;
	}

	public ColorPickerArea createColorPickerArea(String caption, Color initialColor, Consumer<Color> c) {
		ColorPickerArea colorPickerArea = new ColorPickerArea(caption, initialColor);
		colorPickerArea.addColorChangeListener(event -> {
			Color color = event.getColor();
			c.accept(color);
		});

		return colorPickerArea;
	}
}
