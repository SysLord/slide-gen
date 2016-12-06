package de.syslord.slidegen.editor.glue;

import java.util.Optional;

import com.vaadin.data.Property;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Component;

// TODO maybe delete. Try to centralize types used in editor, but mostly one needs the specific type anyway
public enum EditorComponents {

	BOX(AbsoluteLayout.class),
	VALUE_HOLDER(Property.class);

	private Class<?> clazz;

	private EditorComponents(Class<?> clazz) {
		this.clazz = clazz;
	}

	public boolean isAssignable(Object object) {
		return this.clazz.isAssignableFrom(object.getClass());
	}

	public static Optional<AbsoluteLayout> getBox(Object object) {
		if (object != null && BOX.isAssignable(object)) {
			AbsoluteLayout box = (AbsoluteLayout) object;
			return Optional.of(box);
		}
		return Optional.empty();
	}

	public static Optional<Component> getField(Object object) {
		if (object != null && VALUE_HOLDER.isAssignable(object)) {
			Component field = (Component) object;
			return Optional.of(field);
		}
		return Optional.empty();
	}
}
