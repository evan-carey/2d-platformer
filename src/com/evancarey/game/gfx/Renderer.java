package com.evancarey.game.gfx;

import java.io.IOException;

import javax.imageio.ImageIO;

import com.evancarey.game.Game;
import com.evancarey.game.Global;
import com.evancarey.game.Global.Enum_DrawMethod;

/** The class responsible for rendering the game's objects */
public class Renderer {

	private int w;
	private int h;
	private int[] pixels;
	
	private Sprite font;
	private Sprite tiles;
	private Sprite sprites;
	/** Proper background is not yet implemented */
	//private Sprite background;
	
	private Enum_DrawMethod drawMode;
	
	public Renderer() {
		w = Global.WIDTH;
		h = Global.HEIGHT;
		pixels = new int[w * h];
		
		// Load sprites
		try {
			font = new Sprite(ImageIO.read(Game.class.getResource("/font.png")));
			tiles = new Sprite(ImageIO.read(Game.class.getResource("/tiles.png")));
			sprites = new Sprite(ImageIO.read(Game.class.getResource("/sprites.png")));
			//background = new Sprite(ImageIO.read(Game.class.getResource("/background.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		drawMode = Enum_DrawMethod.DM_NORMAL;
	}
	
	/**
	 * Clear the screen by setting all images to background color
	 * @param color The background color
	 */
	public void clearScreen(int color) {
		for (int i = 0; i < pixels.length; i++) {
			//pixels[i] = background.getPixel(i % background.getWidth(), i / background.getHeight());
			pixels[i] = color;
		}
	}
	
	/**
	 * Draw a single pixel.
	 * @param x The x position of the pixel to draw
	 * @param y The y position of the pixel to draw
	 * @param color The pixel color
	 */
	public void drawPixel(int x, int y, int color) {
		if (x < 0 || x >= w || y < 0 || y >= h) {
			return;
		}
		if (drawMode == Enum_DrawMethod.DM_NORMAL) 
			pixels[x + y * w] = color;
		if (drawMode == Enum_DrawMethod.DM_ADDITIVE)
			pixels[x + y * w] = Global.addColors(pixels[x + y * w], color);
		if (drawMode == Enum_DrawMethod.DM_MULTIPLY)
			pixels[x + y * w] = Global.multiplyColors(pixels[x + y * w], color);
	}
	
	/**
	 * Draw a tile from the tile sheet.
	 * @param x The absolute x position of the tile
	 * @param y The absolute y position of the tile
	 * @param texX The x position of the tile on the texture sheet
	 * @param texY The y position of the tile on the texture sheet
	 * @param texWidth The width of the tile
	 * @param texHeight The height of the tile
	 * @param xOffset The relative x position of the tile
	 * @param yOffset The relative y position of the tile
	 */
	public void drawTile(int x, int y, int texX, int texY, int texWidth, int texHeight, int xOffset, int yOffset) {
		x += xOffset;
		y += yOffset;
		for (int xa = 0; xa < texWidth; xa++) {
			for (int ya = 0; ya < texHeight; ya++) {
				int color = tiles.getPixel(texX + xa, texY + ya);
				if (color != 0xff00ff && color != 0x77007f) {
					drawPixel(x + xa, y + ya, color);
				}
			}
		}
	}
	
	/**
	 * Draw a sprite from the sprite sheet.
	 * @param x The absolute x position of the sprite
	 * @param y The absolute y position of the sprite
	 * @param texX The x position of the sprite on the sprite sheet
	 * @param texY The y position of the sprite on the sprite sheet
	 * @param texWidth The width of the sprite
	 * @param texHeight The height of the sprite
	 * @param xOffset The relative x position of the sprite
	 * @param yOffset The relative y position of the sprite
	 */
	public void drawSprite(int x, int y, int texX, int texY, int texWidth, int texHeight, int xOffset, int yOffset) {
		x += xOffset;
		y += yOffset;
		for (int xa = 0; xa < texWidth; xa++) {
			for (int ya = 0; ya < texHeight; ya++) {
				int color = sprites.getPixel(texX + xa, texY + ya);
				if (color != 0xff00ff && color != 0x77007f) {
					drawPixel(x + xa, y + ya, color);
				}
			}
		}
	}
	
	/**
	 * Draw text from the font sheet.
	 * @param text The text to draw
	 * @param x The absolute x position to draw the text
	 * @param y The absolute y position to draw the text
	 * @param color The color to draw the text
	 */
	public void drawText(String text, int x, int y, int color) {
		if (text == "" || text == null) {
			return;
		}
		int xTop = x;
		int yTop = y;
		int tileX = 0;
		int tileY = 0;
		for (int i = 0; i < text.length(); i++) {
			
			int nextChar = text.charAt(i);
			
			tileX = nextChar % 16;
			tileY = nextChar / 16;
			
			drawChar(xTop, yTop, tileX, tileY, color);
			
			xTop += 6; // character width
			if (text.charAt(i) == '\n') {
				xTop = x;
				yTop += 6; // character height
			}
		}
	}
	
	/**
	 * Draw a character from the font sheet.
	 * @param x The absolute x position to draw the character
	 * @param y The absolute y position to draw the character
	 * @param tileX The x position of the character on the font sheet
	 * @param tileY They y position of the character on the font sheet
	 * @param color The color to draw the character
	 */
	public void drawChar(int x, int y, int tileX, int tileY, int color) {
		for (int xa = 0; xa < 5; xa++) {
			for (int ya = 0; ya < 5; ya++) {
				int pixel = font.getPixel(tileX * 6 + xa, tileY * 6 + ya);
				if (pixel != 0x000000 && pixel != 0xff00ff && pixel != 0x77007f) {
					drawPixel(x + xa, y + ya, color);
				}
			}
		}
	}
	
	/**
	 * Draw a rectangle.
	 * @param x The absolute x position to draw the rectangle
	 * @param y The absolute y position to draw the rectangle
	 * @param width The width of the rectangle
	 * @param height The height of the rectangle
	 * @param color The color of the rectangle
	 * @param xOffset The relative position of the rectangle
	 * @param yOffset The relative position of the rectangle
	 * @param border Whether the rectangle is filled or an outline
	 */
	public void drawRect(int x, int y, int width, int height, int color, int xOffset, int yOffset, boolean border) {
		x += xOffset;
		y += yOffset;
		
		if (!border) { // fill rectangle
			for (int xa = 0; xa < width; xa++) {
				for (int ya = 0; ya < height; ya++) {
					drawPixel(x + xa, y + ya, color);
				}
			}
		} else {	// empty rectangle
			for (int xa = 0; xa <= width; xa++) {
				drawPixel(x + xa, y, color);
				drawPixel(x + xa, y + height, color);
			}
			for (int ya = 0; ya < height; ya++) {
				drawPixel(x, y + ya, color);
				drawPixel(x + width, y + ya, color);
			}
		}
	}
	
	public void setDrawMode(Enum_DrawMethod drawMode) {
		this.drawMode = drawMode;
	}
	
	/**
	 * Get a pixel from the pixels array
	 * @param i The index of the pixel
	 * @return The pixel at index i
	 */
	public int getPixel(int i) {
		if (i < 0 || i >= pixels.length) {
			return 0x000000;
		}
		return pixels[i];
	}
}
