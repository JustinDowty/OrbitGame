package game;

import java.awt.Color;
import java.util.Random;

import javax.swing.JPanel;

/**
 * Class contains utilities used in multiple classes.
 * @author Justin Dowty
 * @author Ted Lang
 * @author Alec Allain
 */
public final class Utils {
	/**
	 * Random class instance.
	 */
	private static Random rand = new Random();
	/**
	 * Constructor is private for utility class.
	 */
	private Utils() { }
	/**
	 * Chooses color at random.
	 * @return Color chosen.
	 */
	public static Color chooseRandomColor() {
		int r = rand.nextInt(6);
		Color color = null;
		if (r == 0) {
			color = Color.BLUE;
		}
		if (r == 1) {
			color = Color.GREEN;
		}
		if (r == 2) {
			color = Color.LIGHT_GRAY;
		}
		if (r == 3) {
			color = Color.MAGENTA;
		}
		if (r == 4) {
			color = Color.PINK;
		}
		if (r == 5) {
			color = Color.DARK_GRAY;
		}
		return color;
	}
	
	/**
	 * Generates background stars at random.
	 * @param density Star density based on screen size.
	 * @param width Width of screen.
	 * @param height Height of screen.
	 * @return Star locations.
	 */
	public static int[] generateStars(final int density,
			final int width, final int height) {
		int d = density * 60;
		int[] starLocations = new int[d];
		for (int i = 0; i < d; i += 4) {
			int size = rand.nextInt(20) + 10;
			starLocations[i] = rand.nextInt(width);
			starLocations[i + 1] = rand.nextInt(height);
			starLocations[i + 2] = size / 2;
			starLocations[i + 3] = size;
		}
		return starLocations;
	}
	
	/**
	 * Moves stars down background.
	 * @param starLocations Locations of stars.
	 * @param width Width of screen.
	 * @param height Height of screen.
	 */
	public static void moveStars(final int[] starLocations, 
			final int width, final int height) {
		for (int i = 0; i < starLocations.length; i += 4) {
			if (starLocations[i + 1] > height + 10) {
				starLocations[i] = rand.nextInt(width);
				starLocations[i + 1] = -5;
			} else {
				starLocations[i + 1] += 2;
			}
		}
	}
	
	/**
	 * Background animation loop.
	 * @param starLocations Star locations.
	 * @param panel JPanel to add loop.
	 * @param width Width of panel.
	 * @param height Height of panel.
	 */
	public static void starLoop(final JPanel panel, 
			final int[] starLocations, final int width, final int height) {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				while (true) {
					Utils.moveStars(starLocations, width, height);
					panel.repaint();
					try {
						Thread.sleep(10);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}			
		};
		Thread t = new Thread(r);
		t.start();
	}
}
