package com.evancarey.game.sound;

import java.applet.Applet;
import java.applet.AudioClip;
/** Class for playing the game's audio (still a work in progress) */
public class Sound {

	public static final Sound walk = new Sound("/sounds/walk.wav");
	public static final Sound menuChange = new Sound("/sounds/menu-click.wav");
	public static final Sound coinPickup = new Sound("/sounds/coin-collect.wav");

	private AudioClip clip;

	/**
	 * Construct a new sound object from a sound file.
	 * @param name The path of the sound file
	 */
	private Sound(String name) {
		try {
			clip = Applet.newAudioClip(Sound.class.getResource(name));
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * Play the sound.
	 */
	public void play() {
		try {
			new Thread() {
				public void run() {
					clip.play();
				}
			}.start();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loop the sound.
	 */
	public void loop() {
		try {
			new Thread() {
				public void run() {
					clip.loop();
				}
			}.start();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * Stop playing the sound.
	 */
	public void stop() {
		try {
			new Thread() {
				public void run() {
					clip.stop();
				}
			}.start();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}