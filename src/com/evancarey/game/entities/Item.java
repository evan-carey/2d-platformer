package com.evancarey.game.entities;

import com.evancarey.game.GameArea;
import com.evancarey.game.gfx.Renderer;
import com.evancarey.game.sound.Sound;

/** Not yet implemented, but will represent an item that the player can interact with */
public class Item extends Entity {

	public Item(double x, double y) {
		this.x = x;
		this.y = y;
		
		cx0 = 6;
		cy0 = 6;
		cx1 = 9;
		cy1 = 9;
	}
	
	@Override
	public void update(GameArea area) {

	}

	@Override
	protected void hit(Entity entity, GameArea area) {
		if (entity instanceof Player) {
			alive = false;
			Sound.coinPickup.play();
		}	
	}

	@Override
	public void draw(Renderer renderer, int xOffset, int yOffset) {
		if (alive) {
			renderer.drawSprite((int) x, (int) y, 0, 32, 16, 16, xOffset, yOffset);
		}
		super.draw(renderer, xOffset, yOffset);
	}

}