package de.syslord.boxmodel.renderer;

import java.awt.Color;
import java.awt.Font;
import java.io.ByteArrayInputStream;

import de.syslord.boxmodel.Margin;
import de.syslord.boxmodel.Padding;
import de.syslord.boxmodel.ImageScaling;

public interface RenderableBox {

	String getContent();

	int getX();

	int getY();

	int getWidth();

	int getHeight();

	Margin getMargin();

	Padding getPadding();

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

	ByteArrayInputStream getBackgroundImage();

	ImageScaling getBackgroundScaling();

	int getLineThickness();

}
