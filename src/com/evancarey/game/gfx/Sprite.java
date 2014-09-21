package com.evancarey.game.gfx;

import java.awt.image.BufferedImage;

/** Class for a sprite image */
public class Sprite {

	private int width;
	private int height;
	private int[] pixels;

	/**
	 * Construct a sprite from a given image
	 * @param image The image that contains the sprite
	 */
	public Sprite(BufferedImage image) {
		width = image.getWidth();
		height = image.getHeight();
		pixels = new int[width * height];
		image.getRGB(0, 0, width, height, pixels, 0, width);

		// set pixels with mask
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] &= 0xffffff;
		}
	}

	/**
	 * Get the width of the sprite.
	 * @return The width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Get the height of the sprite.
	 * @return The height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Get a pixel from the sprite image.
	 * @param x The x position of the pixel
	 * @param y The y position of the pixel
	 * @return The pixel at position (x,y)
	 */
	public int getPixel(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height) {
			return 0x000000;
		}
		return pixels[x + y * width];
	}

	/**
	 * Get the sprite's pixels.
	 * @return An array of the sprite's pixels
	 */
	public int[] getPixels() {
		return pixels;
	}
}
