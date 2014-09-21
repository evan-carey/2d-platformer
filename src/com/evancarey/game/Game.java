package com.evancarey.game;
/**
 * CS113 Final Project
 * @author Evan Carey
 * 
 * The basic skeleton of a platforming game.
 * It's unfinished, so some of the classes and methods are not yet implemented.
 */
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.evancarey.game.gfx.Renderer;
/**
 * The main class.
 */
public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	
	private static JFrame frame;
	
	public boolean running;
	public Thread thread;
	
	private BufferedImage image;
	private int[] pixels;
	
	private GameArea area;
	private InputHandler inputHandler;
	private Renderer renderer;
	
	/**
	 * Constructor to initialize game elements.
	 */
	public Game() {
		running = false;
		thread = new Thread(this);
		image = new BufferedImage(Global.WIDTH, Global.HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		
		inputHandler = new InputHandler(this);
		area = new GameArea(this);
		renderer = new Renderer();		
	}

	/**
	 * Start the game's thread and allow it to run.
	 */
	public synchronized void start() {
		running = true;
		thread.start();
	}
	
	/**
	 * Stop running the game.
	 */
	public synchronized void stop() {
		running = false;
	}
	
	/**
	 * Run the game.
	 * There is no limit on how often the game is rendered, but updates
	 * are limited to 60 times per second (so it plays at constant speed
	 * irrelevant of machine's power).
	 */
	@Override
	public void run() {
		long lastTime = System.nanoTime(); // current time in nanoseconds
		long timer = System.currentTimeMillis(); // current time in milliseconds
		
		double ns = 1000000000.0 / 60.0; // nanoseconds per tick
		double unprocessed = 0.0;
		
		int ticks = 0;
		int frames = 0;
		
		while (running) {
			long now = System.nanoTime();
			unprocessed += (now - lastTime) / ns;
			lastTime = now;
			
			while (unprocessed >= 1.0) {
				update(); // Update game logic
				ticks++;
				unprocessed -= 1.0;
			}
						
			render(); // Render game to screen
			frames++;
			
			if (System.currentTimeMillis() - timer >= 1000) {
				System.out.println(ticks + " ticks, " + frames + " fps");
				frames = 0;
				ticks = 0;
				timer += 1000;
			}
		}
		stop();
	}
	
	/**
	 * Update the game's logic.
	 */
	public void update() {
		inputHandler.update();
		area.update();
		if (inputHandler.KEY_ESCAPE.isClicked()) { // quit game
			stop();
			System.exit(0);
		}
	}
	
	/**
	 * Draw game image on screen.
	 */
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3); // triple-buffering
			requestFocus();
			return;
		}

		area.draw(renderer);
		
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = renderer.getPixel(i);
		}
		
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, Global.WIDTH * Global.SCALE, Global.HEIGHT * Global.SCALE, null);
		g.dispose();
		bs.show();
	}
	
	/**
	 * Get the game's input handler
	 * @return The input handler
	 */
	public InputHandler getInputHandler() {
		return inputHandler;
	}

	/**
	 * Main method.
	 * Initializes the JFrame that contains the game and starts the game.
	 * @param args Unused
	 */
	public static void main(String[] args) {
		Game game = new Game();
		Dimension size = new Dimension(Global.WIDTH * Global.SCALE - 10 , Global.HEIGHT * Global.SCALE - 10);

		game.setMinimumSize(size);
		game.setMaximumSize(size);
		game.setPreferredSize(size);

		frame = new JFrame(Global.NAME);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		frame.add(game, BorderLayout.CENTER);
		frame.pack();

		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		// set frame to middle of screen
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(screenSize.width / 2 - frame.getWidth() / 2,
					screenSize.height / 2 - frame.getHeight() / 2);
		
		game.start();
	}
}