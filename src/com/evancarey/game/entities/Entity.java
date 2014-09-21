package com.evancarey.game.entities;

import java.awt.Rectangle;
import java.util.List;

import com.evancarey.game.GameArea;
import com.evancarey.game.Global;
import com.evancarey.game.gfx.Renderer;
import com.evancarey.game.level.tiles.Tile;

public class Entity {
	
	// position
	protected double x, y;
	// movement
	protected double dx, dy;
	// collision
	protected int cx0, cy0, cx1, cy1;
	
	protected boolean alive;
	
	/**
	 * Construct the entity.
	 */
	public Entity() {
		x = 0.0;
		y = 0.0;
		dx = 0.0;
		dy = 0.0;
		cx0 = 0;
		cy0 = 0;
		cx1 = 0;
		cy1 = 0;
		
		alive = true;
	}
	
	/**
	 * Update the entity's logic.
	 * @param area The GameArea the entity is in
	 */
	public void update(GameArea area) {
		
	}
	
	/**
	 * Draw the entity.
	 * @param renderer The renderer responsible for drawing the entity's sprite
	 * @param xOffset The relative x position to draw the entity's sprite
	 * @param yOffset The relative y position to draw the entity's sprite
	 */
	public void draw(Renderer renderer, int xOffset, int yOffset) {
		if (Global.debug) {
			renderer.drawRect((int)x + cx0, (int)y + cy0, cx1 - cx0, cy1 - cy0, 0xff0000, xOffset, yOffset, true);
		}
	}
	
	/**
	 * Move the entity.
	 * @param area The GameArea the entity is in.
	 */
	public void move(GameArea area) {
		if (dx != 0.0) {
			x += dx;
			moveContact(area, 0);
		}
		if (dy != 0.0) {
			y += dy;
			moveContact(area, 1);
		}
	}
	
	/**
	 * Move the entity's collision bounds
	 * @param area The GameArea the entity is in.
	 * @param direction The direction to move the box (0 for x, 1 for y)
	 */
	protected void moveContact(GameArea area, int direction) {
		Tile[] tiles = area.getLevel().getTiles();
		for (int i = 0; i < tiles.length; i++) {
			Tile tile = tiles[i];
			if (!tile.isSolid()) {
				continue;
			}
			Rectangle bounds = getBounds();
			Rectangle tileBounds = getTileBounds(tile);
			
			if (bounds.intersects(tileBounds)) {
				if (direction == 0) {
					x += getDepth(bounds, tileBounds, 0);
					dx = 0.0;
				}
				if (direction == 1) {
					y += getDepth(bounds, tileBounds, 1);
					dy = 0.0;
				}
			}
			
		}
	}
	
	/**
	 * Get the entity's bounds
	 * @return The entity's collision bounds
	 */
	protected Rectangle getBounds() {
		return new Rectangle((int) x + cx0, (int) y + cy0, cx1 - cx0, cy1 - cy0);
	}
	
	/**
	 * Get the tile's bounds
	 * @param tile The tile being tested
	 * @return The tile's bounds
	 */
	protected Rectangle getTileBounds(Tile tile) {
		return new Rectangle(tile.getX() << 4, tile.getY() << 4, 16, 16);
	}
	
	/**
	 * Get the distance between two bounds
	 * @param rect0 The first rectangle bounds
	 * @param rect1 The second rectangle bounds
	 * @param direction The direction being tested (0 for x, 1 for y)
	 * @return The distance between them
	 */
	protected int getDepth(Rectangle rect0, Rectangle rect1, int direction) {
		int half0 = 0;
		int half1 = 0;
		int center0 = 0;
		int center1 = 0;
		if (direction == 0) {
			half0 = rect0.width / 2;
			half1 = rect1.width / 2;
			center0 = rect0.x + half0;
			center1 = rect1.x + half1;
		}
		if (direction == 1) {
			half0 = rect0.height / 2;
			half1 = rect1.height / 2;
			center0 = rect0.y + half0;
			center1 = rect1.y + half1;
		}
		int distance = center0 - center1;
		int minDistance = half0 + half1;
		if (Math.abs(distance) > minDistance) {
			return 0;
		}
		return distance > 0 ? minDistance - distance : -minDistance - distance;
	}
	
	/**
	 * Check for entity collision.
	 * @param area The GameArea the entity is in.
	 */
	protected void checkEntityCollision(GameArea area) {
		List<Entity> entities = area.getLevel().getEntities();
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			if (entity == null) {
				continue;
			}
			if (entity == this) {
				continue;
			}
			if (!entity.isAlive()) {
				continue;
			}

			if (entity.intersects(x + cx0, y + cy0, x + cx1, y + cy1)) {
				hit(entity, area);
			}
		}
	}

	/**
	 * Called when there is a collision between this entity and another.
	 * @param entity The other entity
	 * @param area The GameArea the entities are in
	 */
	protected void hit(Entity entity, GameArea area) {
		
	}
	
	/**
	 * Get the entity's x position
	 * @return The x position
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * Get the entity's y position.
	 * @return The y position
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Check if the entity is alive.
	 * @return True if it's alive
	 */
	public boolean isAlive() {
		return alive;
	}
	
	/**
	 * Check collision using position and collision bounds.
	 * @param x0 The left bound of x
	 * @param y0 The top bound of y
	 * @param x1 The right bound of x
	 * @param y1 The right bound of y
	 * @return False if there is not collision, true if there is
	 */
	public boolean intersects(double x0, double y0, double x1, double y1) {
		if (x1 <= x + cx0 || y1 <= y + cy0 || x0 >= x + cx1 || y0 >= y + cy1) {
			return false;
		}
		return true;
	}
}
