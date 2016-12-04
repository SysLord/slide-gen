package de.syslord.boxmodel.render;

import java.awt.Color;
import java.awt.Font;

public interface RenderableBox {

	String getContent();

	int getX();

	int getY();

	int getWidth();

	int getHeight();

	int getMargin();

	int getPadding();

	// contains padding and margin
	int getContentX();

	int getContentY();

	int getContentWidth();

	int getContentHeight();

	//

	Font getFont();

	boolean isVisible();

	Color getColor();

	RenderType getRenderType();

}
