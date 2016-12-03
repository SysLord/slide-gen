package de.syslord.boxmodel;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageDisplayer {

	public static void showImage(BufferedImage render) throws IOException {
		String name = "Y:\\awt.png";
		ImageIO.write(render, "png", new File(name));
		java.awt.Desktop.getDesktop().open(new File(name));
	}
}
