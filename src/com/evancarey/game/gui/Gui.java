package com.evancarey.game.gui;

import java.util.ArrayList;
import java.util.List;

import com.evancarey.game.GameArea;
import com.evancarey.game.Global;
import com.evancarey.game.gfx.Renderer;
import com.evancarey.game.sound.Sound;

/** The Game's GUI */
public class Gui {
	
	// Label colors
	private final int BRIGHT = 0xffffff; // white
	private final int DULL = 0x606060; // gray

	private List<GuiComponent> components;
	//private List<GuiComponent> healthBar;
	
	private GuiComponentRectangle rectanglePanel;
	
	// Start Menu
	private GuiComponent titleLabel;
	private GuiComponent[] labels;
	private GuiComponent startLabel;
	private GuiComponent helpLabel;
	private GuiComponent quitLabel;
	
	private GuiComponent pauseLabel;
	private GuiComponent instructionsLabel;
	
	private GuiComponent backLabel;
	private GuiComponent continueLabel;
	
	private GuiComponent winLabel;
	private GuiComponent loseLabel;
	
	private GuiComponent healthLabel;
	//private GuiComponent healthSprite;
	
	private int menu;
	private int selected;
	
	/**
	 * Constructor to initialize the Gui to the start menu.
	 */
	public Gui() {
		components = new ArrayList<GuiComponent>();
		//healthBar = new ArrayList<GuiComponent>(10);
		
		menu = 0;
		selected = 0;
		
		createComponents();
		addComponents();
	}
	
	/**
	 * Create the Gui components.
	 */
	private void createComponents() {
		
		rectanglePanel = new GuiComponentRectangle(0, 0, Global.WIDTH, Global.HEIGHT).setVisible(true);
		
		// start menu
		titleLabel = new GuiComponentLabel(Global.WIDTH / 2 - 36, Global.HEIGHT / 6, Global.NAME, 0xffff00);
		startLabel = new GuiComponentLabel(Global.WIDTH / 2 - 30, Global.HEIGHT / 4 * 3, "Start Game", BRIGHT);
		helpLabel = new GuiComponentLabel(Global.WIDTH / 2 - 12, Global.HEIGHT / 4 * 3 + 15, "Help", DULL);
		quitLabel = new GuiComponentLabel(Global.WIDTH / 2 - 27, Global.HEIGHT / 4 * 3 + 30, "Quit Game", DULL);
		labels = new GuiComponent[] {startLabel, helpLabel, quitLabel};
		
		pauseLabel = new GuiComponentLabel(Global.WIDTH / 2 - 18, Global.HEIGHT / 2 - 8, "Paused", BRIGHT).setVisible(false);
		
		instructionsLabel = new GuiComponentLabel(Global.WIDTH / 8, Global.HEIGHT / 6, "Controls\n--------\n\nMove   - Arrows\nJump   - Space\nPause  - Esc", BRIGHT).setVisible(false);
		
		backLabel = new GuiComponentLabel(32, Global.HEIGHT - 32, "Press Z to go back", BRIGHT).setVisible(false);
		continueLabel = new GuiComponentLabel(32, Global.HEIGHT - 32, "Press Z to continue", BRIGHT).setVisible(false);

		winLabel = new GuiComponentLabel(Global.WIDTH / 2 - 75, Global.HEIGHT / 2 - 30, "Congratulations! You won!", BRIGHT).setVisible(false);
		loseLabel = new GuiComponentLabel(Global.WIDTH / 2 - 27, Global.HEIGHT / 2 - 30, "You died!", BRIGHT).setVisible(false);
		
		// hp
		healthLabel = new GuiComponentLabel(8, 8, "HP:", 0xffff00).setVisible(false);
//		int xPos = 8 + 6 * 3;
//		for (int i = 0; i < healthBar.size(); i++) {
//			healthSprite = new GuiComponentSprite(xPos, 8, 0, 16 * 3, 10, 6).setVisible(false);
//			healthBar.add(healthSprite);
//			xPos += 10;
//		}
	}
	
	/**
	 * Add the Gui components to the components list.
	 */
	private void addComponents() {
		components.add(rectanglePanel);
		components.add(titleLabel);
		components.add(startLabel);
		components.add(helpLabel);
		components.add(quitLabel);
		components.add(pauseLabel);
		components.add(instructionsLabel);
		components.add(backLabel);
		components.add(continueLabel);
		components.add(winLabel);
		components.add(loseLabel);
		components.add(healthLabel);
//		components.addAll(healthBar);
	}
	
	/**
	 * Update the game area with the current gui selection.
	 * @param area The GameArea that contains the gui
	 */
	public void update(GameArea area) {
		for (int i = 0; i < components.size(); i++) {
			// Not yet implemented
			components.get(i).update(area);
		}

		if (menu != -1) {
			if (menu == 0) { // title screen
				if (area.getInputHandler().KEY_DOWN.isClicked()) {
					changeSelected(1);
				}
				if (area.getInputHandler().KEY_UP.isClicked()) {
					changeSelected(-1);
				}
			} else {
				selected = 0;
			}
			if (area.getInputHandler().KEY_ACTION_1.isClicked() || area.getInputHandler().KEY_ACTION_0.isClicked()) {
				trigger(area);
			}
		} else { // In game
			((GuiComponentLabel) healthLabel).setText("HP:" + area.getLevel().getPlayer().getHP() + "\n$" + area.getLevel().getPlayer().getItems().size());
//			for (int j = 0; j < healthBar.size() /*area.getLevel().getPlayer().getHP()*/; j++) {
//				if (j < area.getLevel().getPlayer().getHP()) {
//					healthBar.get(j).setVisible(true);
//					continue;
//				}
//				healthBar.get(j).setVisible(false);
//				
//			}
		}
	}
	
	/**
	 * Change the current element selected.
	 * @param inc The number to increment the selection.
	 */
	public void changeSelected(int inc) {
		selected += inc;
		if (selected < 0) {
			selected += labels.length;
		}
		if (selected > labels.length - 1) {
			selected -= labels.length;
		}
		for (int i = 0; i < labels.length; i++) {
			if (i == selected) {
				((GuiComponentLabel) labels[i]).setColor(BRIGHT);
			} else {
				((GuiComponentLabel) labels[i]).setColor(DULL);
			}
		}
	}
	
	/**
	 * Trigger the selected event.
	 * @param area The GameArea that contains the gui
	 */
	public void trigger(GameArea area) {
		Sound.menuChange.play();
		
		if (menu == 0) { // At the start menu
			switch (selected) {
			case 0: // Start the game
				menu = 2;
				area.startGame();
				rectanglePanel.setVisible(true);
				titleLabel.setVisible(false);
				startLabel.setVisible(false);
				helpLabel.setVisible(false);
				quitLabel.setVisible(false);
				healthLabel.setVisible(false);
//				for (GuiComponent g : healthBar) {
//					g.setVisible(true);
//				}
				
				instructionsLabel.setVisible(true);
				backLabel.setVisible(false);
				continueLabel.setVisible(true);
				break;
			case 1:	// Go to the help menu
				menu = 1;
				rectanglePanel.setVisible(true);
				titleLabel.setVisible(false);
				startLabel.setVisible(false);
				helpLabel.setVisible(false);
				quitLabel.setVisible(false);
				healthLabel.setVisible(false);
//				for (GuiComponent g : healthBar) {
//					g.setVisible(false);
//				}
				
				instructionsLabel.setVisible(true);
				backLabel.setVisible(true);
				continueLabel.setVisible(false);
				
				for (int i = 0; i < labels.length; i++) {
					if (i == 0) {
						((GuiComponentLabel) labels[i]).setColor(BRIGHT);
					} else {
						((GuiComponentLabel) labels[i]).setColor(DULL);
					}
				}
				break;
			case 2:	// Quit the game
				area.endGame();
				break;
			}
		} else if (menu == 1) { // At the help menu
			menu = 0; // Go back to start menu
			rectanglePanel.setVisible(true);
			titleLabel.setVisible(true);
			startLabel.setVisible(true);
			helpLabel.setVisible(true);
			quitLabel.setVisible(true);
			healthLabel.setVisible(false);
//			for (GuiComponent g : healthBar) {
//				g.setVisible(false);
//			}
			
			instructionsLabel.setVisible(false);
			backLabel.setVisible(false);
			continueLabel.setVisible(false);
			
		} else if (menu == 2) { // In game
			menu = -1;
			rectanglePanel.setVisible(false);
			titleLabel.setVisible(false);
			startLabel.setVisible(false);
			helpLabel.setVisible(false);
			quitLabel.setVisible(false);
			healthLabel.setVisible(true);
//			for (int i = 0; i < healthBar.size() /*area.getLevel().getPlayer().getHP()*/; i++) {
//				if (i < area.getLevel().getPlayer().getHP()) {
//					healthBar.get(i).setVisible(true);
//					continue;
//				}
//				healthBar.get(i).setVisible(false);
//				
//			}
			
			instructionsLabel.setVisible(false);
			backLabel.setVisible(false);
			continueLabel.setVisible(false);
			
		} else if (menu == 3) { // At the win or loss screen
			area.restart();
		}
	}
	
	/**
	 * Draw the gui components.
	 * @param renderer The renderer responsible for drawing the gui
	 */
	public void draw(Renderer renderer) {
		for (int i = 0; i < components.size(); i++) {
			if (components.get(i).isVisible()) {
				components.get(i).draw(renderer);
			}
		}
//		for (GuiComponent g : healthBar) {
//			if (g.isVisible()) g.draw(renderer);
//		}
	}
	
	/**
	 * Display win screen when player wins the game.
	 */
	public void win() {
		menu = 3;
		rectanglePanel.setVisible(true);
		winLabel.setVisible(true);
		continueLabel.setVisible(true);
		
		healthLabel.setVisible(false);
//		for (GuiComponent g : healthBar) {
//			g.setVisible(false);
//		}
	}
	
	/**
	 * Display lose screen when player loses the game.
	 */
	public void lose() {
		menu = 3;
		rectanglePanel.setVisible(true);
		loseLabel.setVisible(true);
		continueLabel.setVisible(true);
		
		healthLabel.setVisible(false);
//		for (GuiComponent g : healthBar) {
//			g.setVisible(false);
//		}
	}
	
	/**
	 * Display the pause label if the game is paused.
	 * @param pause Whether the game is paused
	 */
	public void togglePause(boolean pause) {
		pauseLabel.setVisible(pause);
	}
	
	/**
	 * Get the current menu.
	 * @return The menu
	 */
	public int getMenu() {
		return menu;
	}
}
