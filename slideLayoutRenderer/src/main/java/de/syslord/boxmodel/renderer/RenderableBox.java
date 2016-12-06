package de.syslord.boxmodel.renderer;

import java.awt.Color;
import java.awt.Font;
import java.io.ByteArrayInputStream;

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

	Color getForegroundColor();

	RenderType getRenderType();

	ByteArrayInputStream getBackgroundImage();

}
