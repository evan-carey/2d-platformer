package com.evancarey.game.level;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.evancarey.game.Game;
import com.evancarey.game.GameArea;
import com.evancarey.game.Global;
import com.evancarey.game.entities.Enemy;
import com.evancarey.game.entities.Entity;
import com.evancarey.game.entities.Goal;
import com.evancarey.game.entities.Item;
import com.evancarey.game.entities.Player;
import com.evancarey.game.gfx.Renderer;
import com.evancarey.game.gfx.Sprite;
import com.evancarey.game.level.tiles.Tile;

/** Class representing a game level */
public class Level {
	
	private Tile[] tiles;
	private int width;
	private int height;
	
	/** The entities in the level */
	private List<Entity> entities;
	private Player player;
	
	@SuppressWarnings("unused")
	private Goal goal;
	
	private boolean finished;
	
	/** The x position of the camera */
	private int cameraX;
	/** The y position of the camera */
	private int cameraY;

	/**
	 * Construct a new level from an image.
	 * @param imagePath The file path of the level image
	 */
	public Level(String imagePath) {

		entities = new ArrayList<Entity>();
		player = null;
		finished = false;
		cameraX = 0;
		cameraY = 0;
		
		try { // load level from file
			Sprite level = new Sprite(ImageIO.read(Game.class.getResource(imagePath)));
			width = level.getWidth();
			height = level.getHeight();
			tiles = new Tile[width * height];
			
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					int levelPixel = level.getPixel(x, y);
					tiles[x + y * width] = new Tile(x, y, 0, false);
					
					// background (not yet implemented)
					
					// tiles
					if (levelPixel == 0x000000) { // black represents a basic solid dirt tile
						tiles[x + y * width] = new Tile(x, y, 1, true);
					}
					
					// entities
					if (levelPixel == 0x00ff00) { // green represents a player's spawn point
						spawnEntity(new Player(x * 16, y * 16));
					}
					if (levelPixel == 0x0000ff) { // blue represents the goal/end of the level
						spawnEntity(new Goal(x * 16, y * 16));
					}
					
					// enemies and items not yet implemented
					if (levelPixel == 0xff0000) { // red represents an enemy's spawn point
						//spawnEntity(new Enemy(x * 16, y * 16));
					}
					if (levelPixel == 0xffff00) { // yellow represents an item
						spawnEntity(new Item(x * 16, y * 16));
					}
					
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// build the level tiles
		for (int i = 0; i < tiles.length; i++) {
			tiles[i].build(this);
		}
	}
	
	/**
	 * Update the level.
	 * @param area The GameArea containing the level
	 */
	public void update(GameArea area) {
		updateTiles(area);
		updateEntities(area);
		updateCamera(area);
	}
	
	/**
	 * Update the level's tiles.
	 * @param area The GameArea containing the level
	 */
	// Not yet implemented, but will eventually be used for animating tiles
	private void updateTiles(GameArea area) {
		int cx = cameraX / 16;
		int cy = cameraY / 16;
		int cw = Global.WIDTH / 16;
		int ch = Global.HEIGHT / 16;
		
		for (int x = cx - 1; x <= cx + cw; x++) {
			for (int y = cy - 1; y <= cy + ch; y++) {
				if (x < 0 || x >= width || y < 0 || y >= height) {
					return;
				}
				tiles[x + y * width].update(area);
			}
		}
	}
	/**
	 * Update the level's entities.
	 * @param area The GameArea containing the level
	 */
	private void updateEntities(GameArea area) {
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).update(area);
			
			// if entity is not alive, remove it from the list
			if (!entities.get(i).isAlive()) {
				if (entities.get(i) instanceof Player) {
					player = null; // died
				}
				if (entities.get(i) instanceof Enemy) {
					// enemies not yet implemented
				}
				if (entities.get(i) instanceof Item) {
					// items not yet implemented
				}
				entities.remove(i);
				i--;
			}
		}
	}
	
	/**
	 * Update the level's camera (centered on the player)
	 * @param area The GameArea containing the level
	 */
	private void updateCamera(GameArea area) {
		if (player != null) {
			cameraX = (int) player.getX() - Global.WIDTH / 2;
			cameraY = (int) player.getY() - Global.HEIGHT / 2;
		}
	}
	
	/**
	 * Draw the portion of the level currently on screen.
	 * @param renderer The renderer responsible for drawing the level
	 */
	public void draw(Renderer renderer) {
		for (int i = 0; i < tiles.length; i ++) {
			tiles[i].draw(renderer, -cameraX, -cameraY);
		}
		for (int i = 0; i < entities.size(); i ++) {
			entities.get(i).draw(renderer, -cameraX, -cameraY);
		}
	}
	
	/**
	 * Spawn an entity by adding it to the entities list.
	 * @param e The entity to spawn
	 */
	public void spawnEntity(Entity e) {
		if (e instanceof Player) {
			player = (Player) e;
		}
		if (e instanceof Enemy) {
			// enemies not yet implemented
		}
		if (e instanceof Item) {
			// items not yet implemented
		}
		if (e instanceof Goal) {
			goal = (Goal) e;
		}
		entities.add(e);
	}
	
	/**
	 * Finish the level when player reaches the end.
	 * @param area The GameArea containing the level
	 */
	public void finishLevel(GameArea area) {
		finished = true;
		area.winGame(); // because there's currently only one level
	}
	
	/**
	 * Check whether the level is finished.
	 * @return True if the level is finished
	 */
	public boolean isFinished() {
		return finished;
	}
	
	/**
	 * Get the ID of a tile.
	 * @param x The x position of the tile
	 * @param y The y position of the tile
	 * @return The tile's ID
	 */
	public int getTileID(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height) {
			return -1;
		}
		return tiles[x + y * width].getID();
	}
	
	/**
	 * Get the tiles in the level.
	 * @return The array of tiles
	 */
	public Tile[] getTiles() {
		return tiles;
	}
	
	/**
	 * Get the entities in the level.
	 * @return The entities list
	 */
	public List<Entity> getEntities() {
		return entities;
	}
	
	/**
	 * Get the player.
	 * @return The player
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * Get the x position of the camera.
	 * @return The x position of the camera
	 */
	public int getCameraX() {
		return cameraX;
	}

	/**
	 * Get the y position of the camera.
	 * @return The y position of the camera
	 */
	public int getCameraY() {
		return cameraY;
	}
	
	/**
	 * Get the width of the level.
	 * @return The level's width
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Get the height of the level.
	 * @return The level's height
	 */
	public int getHeight() {
		return height;
	}
}