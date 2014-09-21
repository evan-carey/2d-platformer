package com.evancarey.game.level.tiles;

import java.util.Random;

import com.evancarey.game.GameArea;
import com.evancarey.game.Global;
import com.evancarey.game.gfx.Renderer;
import com.evancarey.game.level.Level;

/** Class representing a tile in a level */
public class Tile {
		
	private int x;
	private int y;
	private int id;
	
	/** The x position of the tile on the tile sheet */
	private int tileX;
	/** The y position of the tile on the tile sheet */
	private int tileY;
	
	private boolean solid;
	
	private int grassTile;
	
	/**
	 * Construct a new tile.
	 * @param x The x position of the tile
	 * @param y The y position of the tile
	 * @param id The tile's ID
	 * @param solid Whether the tile is solid
	 */
	public Tile(int x, int y, int id, boolean solid) {
		this.x = x;
		this.y = y;
		
		this.id = id;
		this.solid = solid;
		
		tileX = 0;
		tileY = 0;
		grassTile = -1;
	}
	
	/**
	 * Build the tile for the current level.
	 * The correct tile sprite is chosen based on the surrounding tiles.
	 * For example, if this is dirt tile and its surrounded on all four 
	 * sides by other dirt tiles, it will be an inner dirt tile.
	 * @param level The current level
	 */
	public void build(Level level) {
		tileX = 0;
		tileY = 0;
		grassTile = -1;
		
		// Dirt tile set
		if (id == 1) {	
			// check sides to determine correct tile sprite
			int right = level.getTileID(x + 1, y) == id ? 1 : 0;
			int top = level.getTileID(x, y - 1) == id ? 2 : 0;
			int left = level.getTileID(x - 1, y) == id ? 4 : 0;
			int bottom = level.getTileID(x, y + 1) == id ? 8 : 0;
			int sum = right + top + left + bottom;
			
			tileX = sum % 4;
			tileY = sum >> 2;

			if (top == 0) { // top tile, so it will have grass on it
				grassTile = (new Random()).nextInt(7);
			}
		}
	}
	/**
	 * Update the tile.
	 * @param area The GameArea that contains the tile.
	 */
	public void update(GameArea area) {
		// will eventually be used for animated tiles
	}
	
	/**
	 * Draw the tile.
	 * @param renderer The renderer responsible for drawing the tile.
	 * @param xOffset The relative x position of the tile
	 * @param yOffset The relative y position of the tile
	 */
	public void draw(Renderer renderer, int xOffset, int yOffset) {
		if (x * 16 + xOffset < -16 || x * 16 + xOffset >= Global.WIDTH + 16 || y * 16 + yOffset < -16 || y * 16 + yOffset >= Global.HEIGHT + 16) {
			return;	// Tile not visible on screen -> do not draw
		}
		if (id == 0) {
			return; // null tile
		}
		
		// Dirt tiles
		if (id == 1) {
			renderer.drawTile(x << 4, y << 4, tileX << 4, tileY << 4, 16, 16, xOffset, yOffset);
			if (grassTile != -1) { // draw grass on top of the tile
				renderer.drawTile(x * 16, y * 16 - 3, 64 + grassTile * 16, 13, 16, 3, xOffset, yOffset);
			}
		}
	}
	
	/**
	 * Get the x position of the tile.
	 * @return The x position
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Get the y position of the tile.
	 * @return The y position
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Check if the tile is solid.
	 * @return True if the tile is solid
	 */
	public boolean isSolid() {
		return solid;
	}
	
	/**
	 * Get the tile's ID.
	 * @return the tile's ID
	 */
	public int getID() {
		return id;
	}
}