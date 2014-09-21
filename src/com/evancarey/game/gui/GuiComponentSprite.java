package com.evancarey.game.gui;

import com.evancarey.game.gfx.Renderer;

public class GuiComponentSprite extends GuiComponent {

	private int texX;
	private int texY;
	private int texW;
	private int texH;

	public GuiComponentSprite(int x, int y, int texX, int texY, int texW, int texH) {
		super(x, y);
		this.texX = texX;
		this.texY = texY;
		this.texW = texW;
		this.texH = texH;
	}

	public void draw(Renderer renderer) {
		renderer.drawSprite(x, y, texX, texY, texW, texH, 0, 0);
	}
}
