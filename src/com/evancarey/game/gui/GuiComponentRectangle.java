package com.evancarey.game.gui;

import com.evancarey.game.gfx.Renderer;
/** A class representing a Rectangular gui component */
public class GuiComponentRectangle extends GuiComponent {

	private int width;
	private int height;

	/**
	 * Construct a new rectangle component.
	 * @param x The x position of the component
	 * @param y The y position of the component
	 * @param width The width of the rectangle
	 * @param height The height of the rectangle
	 */
	public GuiComponentRectangle(int x, int y, int width, int height) {
		super(x, y);
		this.width = width;
		this.height = height;
	}

	/**
	 * Draw the rectangle component.
	 * @param renderer The renderer responsible for drawing the component
	 */
	@Override
	public void draw(Renderer renderer) {
		renderer.drawRect(x, y, width, height, 0x000000, 0, 0, false);
	}

	/**
	 * Set the visibility of the component.
	 * @param visible Whether the component should be visible
	 * @return The component
	 */
	@Override
	public GuiComponentRectangle setVisible(boolean visible) {
		this.visible = visible;
		return this;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
}