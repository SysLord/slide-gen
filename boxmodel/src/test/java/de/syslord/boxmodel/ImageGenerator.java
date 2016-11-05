package de.syslord.boxmodel;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

import javax.imageio.ImageIO;

public class ImageGenerator {

	public void drawimage(Consumer<Graphics> graphics, String file) {
		String pathname = "./" + file + ".png";
		generateImage(graphics, pathname);

		openImage(pathname);
	}

	private void openImage(String pathname) {
		try {
			Desktop.getDesktop().open(new File(pathname));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void generateImage(Consumer<Graphics> drawer, String pathname) {
		BufferedImage i = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB);

		Graphics graphics = i.getGraphics();
		graphics.setColor(Color.white);
		graphics.fillRect(0, 0, 1000, 1000);

		graphics.setColor(Color.black);
		drawer.accept(graphics);

		try {
			ImageIO.write(i, "PNG", new File(pathname));
		} catch (IOException e) {
			e.printStackTrace();
		}

		graphics.dispose();
	}
}
