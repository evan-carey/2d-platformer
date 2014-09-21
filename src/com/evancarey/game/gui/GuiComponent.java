package com.evancarey.game.gui;

import com.evancarey.game.GameArea;
import com.evancarey.game.gfx.Renderer;
/** Abstract class for a component of the game's GUI */
public abstract class GuiComponent {
	
	protected int x;
	protected int y;

	protected boolean visible;

	/**
	 * Construct a gui component.
	 * @param x The absolute x position of the component
	 * @param y The absolute y position of the component
	 */
	public GuiComponent(int x, int y) {
		this.x = x;
		this.y = y;

		visible = true;
	}

	public void update(GameArea area) {
		// will eventually be used for animated gui components
	}

	/**
	 * Draw the gui component.
	 * @param renderer The renderer responsible for drawing the component
	 */
	public abstract void draw(Renderer renderer);

	/**
	 * Set the position of the gui component
	 * @param x The absolute x position for the component
	 * @param y The absolute y position for the component
	 */
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Set the visibility of the component
	 * @param visible Whether this component should be visible
	 * @return The component
	 */
	public GuiComponent setVisible(boolean visible) {
		this.visible = visible;
		return this;
	}

	/**
	 * Check if the component is visible.
	 * @return True if it is visible
	 */
	public boolean isVisible() {
		return visible;
	}
}
