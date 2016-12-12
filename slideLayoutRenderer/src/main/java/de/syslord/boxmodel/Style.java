package de.syslord.boxmodel;

import java.awt.Color;
import java.awt.Font;
import java.util.Optional;

public interface Style {

	Optional<Font> getFont(String stylename);

	Optional<Color> getColor(String stylename);

}
