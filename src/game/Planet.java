package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

/**
 * This class conatins data for backgroud planet, sets its position as it drifts
 * across screen.
 * @author JustinDowty
 * @author Ted Lang
 * @author Alec Allain
 */
public class Planet {
	/**
	 * Land mass on planet.
	 */
	private Polygon land;
	/**
	 * Color of water.
	 */
	private Color waterColor;
	/** 
	 * Color of land mass.
	 */
	private Color landColor;
	/**
	 * Planet's X location.
	 */
	private int xLocation;
	/**
	 * Planet's Y location.
	 */
	private int yLocation;
	/**
	 * Planet's drift speed.
	 */
	private int driftSpeed;
	
	/**
	 * Constructor sets speed and initial location. 
	 */
	public Planet() {
		this.driftSpeed = 1;
		this.xLocation = -300;
		this.yLocation = OrbitGame.WINDOW_HEIGHT - 400;
		
		int[] xCoords = {0, 10, 20, 100, 180, 360, 400, 50};
		int[] yCoords = {900, 600, 600, 500, 600, 500, 650, 900};
		int points = 8;
		land = new Polygon(xCoords, yCoords, points);	
		this.waterColor = Utils.chooseRandomColor();
		this.landColor = Utils.chooseRandomColor();
		while (waterColor == landColor) {
			this.waterColor = Utils.chooseRandomColor();
		}
	}
	
	/**
	 * Moves planet slowly to the left to simulate drifting away.
	 * @param currTime Current game time.
	 */
	public void drift(final int currTime) {
		if (currTime % 500 == 0) {
			this.xLocation -= this.driftSpeed;
			this.yLocation += this.driftSpeed;
			for (int i = 0; i < land.xpoints.length; i++) {
				land.xpoints[i] -= this.driftSpeed;
				land.ypoints[i] += this.driftSpeed;
			}
		}		
	}
	
	/**
	 * Paints Planet to screen.
	 * @param g Instance of Graphics.
	 */
	public void paintComponent(final Graphics g) {
		g.setColor(waterColor);
		g.fillOval(this.xLocation, this.yLocation, 900, 900);
		g.setColor(landColor);
		g.fillPolygon(land);
	}
	
	/**
	 * Sets the water color.
	 * @param color Color to set.
	 */
	public void setWaterColor(final Color color) {
		this.waterColor = color;
	}
	
	/**
	 * Sets the land color.
	 * @param color Color to set.
	 */
	public void setLandColor(final Color color) {
		this.landColor = color;
	}
}
