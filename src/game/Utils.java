package game;

import java.awt.Color;
import java.util.Random;

/*
 * Class contains utilities used in multiple classes
 */
public class Utils {
	public static Color chooseRandomColor(){
		Random rand = new Random();
		int r = rand.nextInt(6);
		Color color = null;
		if(r == 0){
			color = Color.BLUE;
		}
		if(r == 1){
			color = Color.GREEN;
		}
		if(r == 2){
			color = Color.LIGHT_GRAY;
		}
		if(r == 3){
			color = Color.MAGENTA;
		}
		if(r == 4){
			color = Color.PINK;
		}
		if(r == 5){
			color = Color.DARK_GRAY;
		}
		return color;
	}
}
