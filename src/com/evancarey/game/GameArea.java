package com.evancarey.game;

import com.evancarey.game.gfx.Renderer;
import com.evancarey.game.gui.Gui;
import com.evancarey.game.level.Level;

public class GameArea {

	private Game game;
	private Gui gui;
	
	private boolean paused;
	
	private Level level;
	
	/** Background has not been implemented yet. */
	//private int backgroundX, backgroundY;
	
	/**
	 * Constructor to initialize the game area and present the GUI (title screen).
	 * @param game The current game
	 */
	public GameArea(Game game) {
		this.game = game;
		
		paused = false;
		level = null;
		gui = new Gui();
		
		//backgroundX = Global.WIDTH / 2;
		//backgroundY = Global.HEIGHT / 2;
	}
	
	/**
	 * Start the game by initializing the level.
	 */
	public void startGame() {
		level = new Level("/levels/test_level.png");
	}
	
	/**
	 * If the player wins, set the level to null and display the win screen.
	 */
	public void winGame() {
		level = null;
		gui.win();
	}

	/**
	 * If the player loses, set the level to null and display the lose screen.
	 */
	public void loseGame() {
		level = null;
		gui.lose();
	}
	
	/**
	 * End the game.
	 */
	public void endGame() {
		game.stop();
		System.exit(0);
	}
	
	/**
	 * Restart the game (after win or loss)
	 */
	public void restart() {
		level = null;
		gui = null;
		gui = new Gui();
		
		//backgroundX = Global.WIDTH / 2;
		//backgroundY = Global.HEIGHT / 2;
	}
	
	/**
	 * Update game logic.
	 */
	public void update() {
		if (getInputHandler().KEY_PAUSE.isClicked() && gui.getMenu() == -1) {
			paused = !paused;
			gui.togglePause(paused);
		}
		
		if (level != null) {
			if (!paused) {
				level.update(this);
			}
			if (level != null && level.getPlayer() != null) {
				//backgroundX = (int) level.getPlayer().getX() - level.getCameraX();
				//backgroundY = (int) level.getPlayer().getY() - level.getCameraY();
			}
			
			if (level != null && level.isFinished()) {
				winGame();
				return;
			}
			
			if (level != null && level.getPlayer() == null) {
				loseGame();
				return;
			}
		} else {
			//backgroundX = Global.WIDTH / 2;
			//backgroundY = Global.HEIGHT / 2;
		}
		if (gui != null) {
			gui.update(this);
		}
	}
	
	/**
	 * Render the game area.
	 * @param renderer Object responsible for drawing game images
	 */
	public void draw(Renderer renderer) {
//		renderer.clearScreen(backgroundX, backgroundY);
		renderer.clearScreen(0x9bcefd);
		if (level != null) {
			level.draw(renderer);
		}
		if (gui != null) {
			gui.draw(renderer);
		}
	}
	
	/**
	 * Returns the game's input handler.
	 * @return The game's input handler.
	 */
	public InputHandler getInputHandler() {
		return game.getInputHandler();
	}
	
	/**
	 * Get the game's current level.
	 * @return The current level
	 */
	public Level getLevel() {
		return level;
	}
	
	/**
	 * Get the game's GUI.
	 * @return The GUI
	 */
	public Gui getGui() {
		return gui;
	}
}
