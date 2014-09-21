package com.evancarey.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

public class InputHandler implements KeyListener, MouseListener, MouseMotionListener {

	public List<Input> buttons;

	/** Keyboard inputs */
	public Input KEY_RIGHT, KEY_LEFT, KEY_DOWN, KEY_UP, KEY_ACTION_0,
			KEY_ACTION_1, KEY_ACTION_2, KEY_PAUSE, KEY_ESCAPE, KEY_DEBUG;
	
	// Mouse inputs are not yet implemented
	/** Mouse inputs */
	public Input MOUSE_LEFT, MOUSE_RIGHT, MOUSE_MIDDLE;
	/** Mouse position */
	public int x, y;

	// Input inner class
	public class Input {
		private int hold = 0;
		private int presses = 0;

		private boolean clicked = false;
		private boolean down = false;

		/**
		 * Constructor to add new input to input list.
		 */
		public Input() {
			buttons.add(this);
		}

		/**
		 * Update the input's logic
		 */
		public void update() {
			if (hold < presses) {
				hold++;
				clicked = true;
			} else {
				clicked = false;
			}
		}

		/**
		 * Toggle the input.
		 * @param pressed Whether the input is pressed or not
		 */
		public void toggle(boolean pressed) {
			if (pressed != down) {
				down = pressed;
			}
			if (pressed) {
				presses++;
			}
		}

		/**
		 * Return whether the button is clicked
		 * @return True if it's clicked
		 */
		public boolean isClicked() {
			return clicked;
		}

		/**
		 * Return whether the button is down.
		 * @return True if it's down
		 */
		public boolean isDown() {
			return down;
		}
	}

	/**
	 * Constructor to initialize inputs and add the listeners to the game.
	 * @param game The game
	 */
	public InputHandler(Game game) {
		game.addKeyListener(this);
		game.addMouseListener(this);
		game.addMouseMotionListener(this);

		buttons = new ArrayList<Input>();
		
		KEY_RIGHT = new Input();
		KEY_LEFT = new Input();
		KEY_UP = new Input();
		KEY_DOWN = new Input();
		KEY_ACTION_0 = new Input();
		KEY_ACTION_1 = new Input();
		KEY_ACTION_2 = new Input();
		KEY_PAUSE = new Input();
		KEY_ESCAPE = new Input();
		
		KEY_DEBUG = new Input();

		// Not yet implemented
		MOUSE_LEFT = new Input();
		MOUSE_RIGHT = new Input();
		MOUSE_MIDDLE = new Input();

		x = 0;
		y = 0;
	}

	/**
	 * Update the inputs.
	 */
	public void update() {
		for (int i = 0; i < buttons.size(); i++) {
			buttons.get(i).update();
		}
	}

// KeyListener methods
	/**
	 * Toggle the key being pressed.
	 */
	@Override
	public void keyPressed(KeyEvent ke) {
		toggle(ke, true);
	}

	/**
	 * Toggle the key being released.
	 */
	@Override
	public void keyReleased(KeyEvent ke) {
		toggle(ke, false);
	}

	@Override
	public void keyTyped(KeyEvent ke) {
		// not used
	}

// MouseListener methods	
	@Override
	public void mousePressed(MouseEvent me) {
		toggle(me, true);
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		toggle(me, false);
	}
	
	@Override
	public void mouseClicked(MouseEvent me) {
		// not used
	}

	@Override
	public void mouseEntered(MouseEvent me) {
		// not used
	}

	@Override
	public void mouseExited(MouseEvent me) {
		// not used
	}

// MouseMotionListener methods
	@Override
	public void mouseDragged(MouseEvent me) {
		x = me.getX() / Global.SCALE;
		y = me.getY() / Global.SCALE;
	}

	@Override
	public void mouseMoved(MouseEvent me) {
		x = me.getX() / Global.SCALE;
		y = me.getY() / Global.SCALE;
	}

	/**
	 * Toggle the key.
	 * @param ke The event from the keyboard
	 * @param pressed Whether the key is being pressed or released
	 */
	private void toggle(KeyEvent ke, boolean pressed) {
		// Movement
		if (ke.getKeyCode() == KeyEvent.VK_UP || ke.getKeyCode() == KeyEvent.VK_W) {
			KEY_UP.toggle(pressed);
		}
		if (ke.getKeyCode() == KeyEvent.VK_DOWN || ke.getKeyCode() == KeyEvent.VK_S) {
			KEY_DOWN.toggle(pressed);
		}
		if (ke.getKeyCode() == KeyEvent.VK_LEFT || ke.getKeyCode() == KeyEvent.VK_A) {
			KEY_LEFT.toggle(pressed);
		}
		if (ke.getKeyCode() == KeyEvent.VK_RIGHT || ke.getKeyCode() == KeyEvent.VK_D) {
			KEY_RIGHT.toggle(pressed);
		}
		
		// Actions
		if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
			KEY_ACTION_0.toggle(pressed);
		}
		if (ke.getKeyCode() == KeyEvent.VK_SPACE) {
			KEY_ACTION_0.toggle(pressed);
		}
		if (ke.getKeyCode() == KeyEvent.VK_Z) {
			KEY_ACTION_1.toggle(pressed);
		}
		if (ke.getKeyCode() == KeyEvent.VK_X) {
			KEY_ACTION_2.toggle(pressed);
		}
		
		// Pause
		if (ke.getKeyCode() == KeyEvent.VK_P) {
			KEY_PAUSE.toggle(pressed);
		}
		if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
			KEY_PAUSE.toggle(pressed);
		}
		
		// Debug mode
		if (ke.getKeyCode() == KeyEvent.VK_F12){
			KEY_DEBUG.toggle(pressed);
			if (pressed) Global.debug = !Global.debug;
		}
	}

	/**
	 * Toggle the mouse.
	 * @param me The event from the mouse.
	 * @param pressed Whether the mouse button is being pressed or released.
	 */
	private void toggle(MouseEvent me, boolean pressed) {
		if (me.getButton() == MouseEvent.BUTTON1) {
			MOUSE_LEFT.toggle(pressed);
		}
		if (me.getButton() == MouseEvent.BUTTON2) {
			MOUSE_MIDDLE.toggle(pressed);
		}
		if (me.getButton() == MouseEvent.BUTTON3) {
			MOUSE_RIGHT.toggle(pressed);
		}
	}
}