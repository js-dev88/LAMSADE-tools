package com.github.lamsadetools;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageSize {
	double width;
	double height;

	public double getWidth() {
		return width;
	}

	private void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	private void setHeight(double height) {
		this.height = height;
	}

	/**
	 * Calls the class constructor with a default scale
	 *
	 * @param imagePath
	 *            the path to the image
	 * @throws IOException
	 */

	public ImageSize(String imagePath) throws IOException {
		this(imagePath, 1.0);
	}

	/**
	 * Sets this object's width and height according to the width and height of
	 * the image scaled to a certain value
	 *
	 * @param imagePath
	 *            the path of the image
	 * @param scale
	 *            value by which to scale the image
	 * @throws IOException
	 */

	public ImageSize(String imagePath, double scale) throws IOException {
		BufferedImage bimg = ImageIO.read(new File(imagePath));

		setHeight(bimg.getHeight() * scale);
		setWidth(bimg.getWidth() * scale);

	}
}
