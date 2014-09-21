package com.evancarey.game;

import java.util.Random;

/** Contains global variables and functions */
public class Global {

	public static final int WIDTH = 256;
	public static final int HEIGHT = WIDTH / 4 * 3;
	public static final int SCALE = 4;
	public static final String NAME = "Final Project\n\n\nby Evan Carey";
	public static boolean debug = false;
	
	public static Random random = new Random();
	
	public static int randomInt(int i) {
		return random.nextInt(i);
	}
	
	public static int randomInt(int i, int j) {
		return i + random.nextInt(j);
	}
	
	public static double randomDouble(double d0, double d1) {
		return d0 + random.nextDouble() * (d1 - d0);
	}
	
	public static double distance(double x0, double y0, double x1, double y1) {
		double dx = x1 - x0;
		double dy = y1 - y0;
		return Math.sqrt(dx * dx + dy * dy);
	}
	
	public static double angle(double x0, double y0, double x1, double y1) {
		double dx = x1 - x0;
		double dy = y1 - y0;
		return Math.atan2(dy, dx);
	}
	
	public static int multiplyColors(int c0, int c1) {
		int r = (int) ((getR(c0) / 255.0) * (getR(c1) / 255.0) * 255);
		int g = (int) ((getG(c0) / 255.0) * (getG(c1) / 255.0) * 255);
		int b = (int) ((getB(c0) / 255.0) * (getB(c1) / 255.0) * 255);
		return (r << 16) + (g << 8) + b;
	}
	
	public static int addColors(int c0, int c1) {
		int r = Math.min(getR(c0) + getR(c1), 255);
		int g = Math.min(getG(c0) + getG(c1), 255);
		int b = Math.min(getB(c0) + getB(c1), 255);
		return (r << 16) + (g << 8) + b;
	}
	
	public static int getR(int color) {
		return (color & 0xff0000) >> 16;
	}
	
	public static int getG(int color) {
		return (color & 0x00ff00) >> 8;
	}
	
	public static int getB(int color) {
		return (color & 0x0000ff);
	}
	
	public enum Enum_DrawMethod {
		DM_NORMAL, DM_ADDITIVE, DM_MULTIPLY
	}
}