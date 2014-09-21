package com.evancarey.game.entities;

import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;

import com.evancarey.game.GameArea;
import com.evancarey.game.InputHandler;
import com.evancarey.game.gfx.Renderer;
import com.evancarey.game.level.tiles.Tile;
import com.evancarey.game.sound.Sound;

/** The player entity */
public class Player extends Entity {
	
	/** Spawn location */
	private double spawnX, spawnY;
	/** Player health */
	private int hp;
	/** Timer for player invincibility after being hurt */
	private int invincibilityTime;
	
	/** Items in possesssion */
	private List<Entity> items;
	
	private boolean visible;
	private boolean movable;
	private boolean onGround;
	private boolean canJump;
	
	private int flip;
	private int spriteIndex;
	private double imageIndex;
	
	/**
	 * Construct a new player at position (x, y)
	 * @param x The x position of the player
	 * @param y The y position of the player
	 */
	public Player(double x, double y) {
		this.x = x;
		this.y = y;
		
		// collision bounds
		cx0 = 3;
		cy0 = 0;
		cx1 = 13;
		cy1 = 16;
		
		spawnX = x;
		spawnY = y;
		
		hp = 3; // starting health
		invincibilityTime = 60; //one second
		visible = true;
		
		movable = true;
		onGround = false;
		canJump = false;
		
		flip = 0;
		spriteIndex = 0;
		imageIndex = 0.0;
		
		items = new LinkedList<Entity>();
	}
	
	/**
	 * Update the player entity according to user input.
	 * @param area The GameArea the player is in
	 */
	private void input(GameArea area) {
		InputHandler input = area.getInputHandler();
		
		if(input.KEY_RIGHT.isDown()) { // move right
			dx += 0.2;
		}
		
		if (input.KEY_LEFT.isDown()) { // move left
			dx -= 0.2;
		}
		
		// jumping logic
		if (canJump && input.KEY_ACTION_0.isDown() && onGround) {
			dy = -3.5;
			canJump = false;
		}
		if (!input.KEY_ACTION_0.isDown() && onGround) {
			canJump = true;
		}
		
		// gravity
		if (!onGround) {
			canJump = false;
			dy = Math.min(dy + .125, 4.0);
		}
	}
	
	/**
	 * Update player logic based on input and collisions.
	 */
	public void update(GameArea area) {
		if (!onGround && isOnGround(area)) { // landing after a jump
			Sound.walk.play();
		}
		onGround = isOnGround(area);
		if (movable) {
			input(area);
		}
		move(area);
		dx *= 0.9;
		animate();
		checkEntityCollision(area);
		checkOutside(area);
		
	}
	
	/**
	 * Check if player entity is on the ground.
	 * @param area The GameArea the entity is in
	 * @return True if entity is on the ground (i.e intersecting with a solid tile)
	 */
	private boolean isOnGround(GameArea area) {
		Tile[] tiles = area.getLevel().getTiles();
		for (int i = 0; i < tiles.length; i++) {
			Tile tile = tiles[i];
			if (!tile.isSolid()) {
				continue;
			}
			y += 1;
			Rectangle bounds = getBounds();
			y -= 1;
			Rectangle tileBounds = getTileBounds(tile);
			if (bounds.intersects(tileBounds)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Move the player's collision bounds
	 * @param area The GameArea the player entity is in.
	 * @param direction The direction to move the box (0 for x, 1 for y)
	 */
	@Override
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
					int yDepth = getDepth(bounds, tileBounds, 1);
					y += yDepth;
					if (yDepth < 0) {
						onGround = true;
					}
					dy = 0.0;
				}
			}
		}
	}
	
	/**
	 * Animate the player sprite
	 */
	private void animate() {
		if (onGround) {
			if (Math.abs(dx) < 0.5) { // standing still
				spriteIndex = 0;
				imageIndex = 0.0;
			} else {	// moving
				spriteIndex = 1;
				imageIndex += 0.13; // animation speed
				if (imageIndex % spriteIndex <= .12)
					Sound.walk.play();
				if (imageIndex >= 4.0) {
					imageIndex = 0.0;
				}
				
			}
		} else { // jumping
			if (dy < 0.0) spriteIndex = 5;
			else spriteIndex = 6;
			imageIndex = 0.0;
		}
		
		if (dx > 0.0) flip = 0; // moving right
		if (dx < 0.0) flip = 1; // moving left
		
		if (invincibilityTime > 0) {
			invincibilityTime--;
			if (invincibilityTime % 3 == 0) {
				visible = !visible;	// flicker sprite
			}
		} else {
			visible = true;
		}
	}
	
	/**
	 * Logic for when player hits another entity
	 * @param entity The entity the player is hitting
	 * @param area The GameArea the player is in
	 */
	protected void hit(Entity entity, GameArea area) {
		// hit item
		if (entity instanceof Item) {
			items.add(entity);
			((Item) entity).hit(this, area);
		}
		// hit enemy
		if (entity instanceof Enemy) {
			if (hp > 1) {
				invincibilityTime = 120; // 2 seconds
				hp--; // take damage
			} else {
				alive = false;
			}
		}
		// hit goal
		if (entity instanceof Goal) {
			movable = false;
			area.getLevel().finishLevel(area);
			((Goal) entity).activate();
		}
	}
	
	/**
	 * Check if the player has fallen outside the level.
	 * @param area The GameArea the player is in
	 */
	private void checkOutside(GameArea area) {
		// If the player has fallen outside the level, take damage and reset position.
		if (area.getLevel() != null && y > area.getLevel().getHeight() * 16) {
			hp--;
			if (hp == 0) {
				alive = false;
			} else {
				x = spawnX;
				y = spawnY;
				invincibilityTime = 120; // two seconds
			}
		}
	}
	
	/**
	 * Draw the player sprite.
	 * @param renderer The renderer responsible for drawing the player sprite
	 * @param xOffset The relative x position to draw the player sprite
	 * @param yOffset The relative y position to draw the player sprite
	 */
	public void draw(Renderer renderer, int xOffset, int yOffset) {
		if (visible) {
			renderer.drawSprite((int) x, (int) y, spriteIndex * 16 + (int) imageIndex * 16, flip * 16, 16, 16, xOffset, yOffset);
//			for (Entity item : items) {
//				// draw items behind player
//			}
		}
		super.draw(renderer, xOffset, yOffset);
	}
	
	/**
	 * Set the player's ability to move.
	 * @param movable Whether the player can move or not
	 */
	public void setMovable(boolean movable) {
		this.movable = movable;
	}
	
	/**
	 * Get the player's current health.
	 * @return player's health
	 */
	public int getHP() {
		return hp;
	}
	
	/**
	 * Check if player sprite is flipped (i.e. facing left)
	 * @return True if it's facing left
	 */
	public boolean isFlipped() {
		return flip == 1;
	}
	
	/**
	 * Set the player's ability to jump
	 * @param canJump Whether the player can jump
	 */
	public void setJump(boolean canJump) {
		this.canJump = canJump;
	}
	
	public List<Entity> getItems() {
		return items;
	}
}