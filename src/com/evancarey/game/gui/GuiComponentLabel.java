package com.evancarey.game.gui;

import com.evancarey.game.gfx.Renderer;

/** Class for a gui component with text */
public class GuiComponentLabel extends GuiComponent {

	private String text;
	private int color;
	
	/**
	 * Construct a new label.
	 * @param x The absolute x position of the label
	 * @param y The absolute y position of the label
	 * @param text The label's text
	 * @param color The text's color
	 */
	public GuiComponentLabel(int x, int y, String text, int color) {
		super(x, y);
		this.text = text;
		this.color = color;
	}
	
	/**
	 * Draw the label.
	 * @param renderer The renderer responsible for drawing the label's text
	 */
	@Override
	public void draw(Renderer renderer) {
		renderer.drawText(text, x, y, color);
	}

	/**
	 * Set the label's text.
	 * @param text The text for the label
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Set the color for the label's text.
	 * @param color The color for the text
	 */
	public void setColor(int color) {
		this.color = color;
	}
	
	/**
	 * Set the visibility of the label.
	 * @param Whether the label should be visible
	 * @return The label
	 */
	@Override
	public GuiComponentLabel setVisible(boolean visible) {
		this.visible = visible;
		return this;
	}

	/**
	 * Get the label's text.
	 * @return The label's text as a string
	 */
	public String getText() {
		return text;
	}
}
