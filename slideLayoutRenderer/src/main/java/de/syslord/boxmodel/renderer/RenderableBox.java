package de.syslord.boxmodel.renderer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.io.ByteArrayInputStream;

import de.syslord.boxmodel.ImageScaling;
import de.syslord.boxmodel.Margin;
import de.syslord.boxmodel.Padding;

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

	Paint getTextBackgroundColor();

	int getTextBackgroundPadding();

	RenderType getRenderType();

	ByteArrayInputStream getBackgroundImage();

	ImageScaling getBackgroundScaling();

	int getLineThickness();

	int getLineSpacing();

}
