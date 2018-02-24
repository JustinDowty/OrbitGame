package game;

import java.awt.Color;
import java.util.Random;

/**
 * Class contains utilities used in multiple classes.
 * @author Justin Dowty
 * @author Ted Lang
 * @author Alec Allain
 */
public final class Utils {
	/**
	 * Constructor is private for utility class.
	 */
	private Utils() { }
	/**
	 * Chooses color at random.
	 * @return Color chosen.
	 */
	public static Color chooseRandomColor() {
		Random rand = new Random();
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
}
