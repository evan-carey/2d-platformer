package com.evancarey.game.entities;

import com.evancarey.game.GameArea;
import com.evancarey.game.gfx.Renderer;

/** An entity that represent the end of a level */
public class Goal extends Entity {
	
	/** Not yet implemented, but will eventually be used to animate the goal. */
	private boolean activated;
	
	/**
	 * Constructor to initialize it's position and collision bounds
	 * @param x Its x position
	 * @param y Its y position
	 */
	public Goal(double x, double y) {
		this.x = x;
		this.y = y;
		
		cx0 = 7;
		cy0 = 7;
		cx1 = 8;
		cy1 = 8;
		
		activated = false;
	}

	@Override
	public void update(GameArea gameArea) {
		// Will eventually be used for animation
		if (activated) {
			
		}
	}

	/**
	 * Draw the goal sprite
	 * @param renderer The renderer responsible for drawing the sprite
	 * @param xOffset The relative x position to draw the goal sprite
	 * @param yOffset The relative y position to draw the goal sprite
	 */
	@Override
	public void draw(Renderer renderer, int xOffset, int yOffset) {
		renderer.drawSprite((int) x, (int) y, 0, 64, 16, 16, xOffset, yOffset);
		super.draw(renderer, xOffset, yOffset);
	}

	@Override
	protected void hit(Entity entity, GameArea area) {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Activate the goal, indicating level completion.
	 */
	public void activate() {
		activated = true;
	}

}
