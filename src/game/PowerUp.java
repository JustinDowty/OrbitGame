package game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class PowerUp {
	/**
	 * Falling speed of power up.
	 */
	private int ySpeed = 5;
	/**
	 * X location of power up.
	 */
	private int xLocation;
	/**
	 * Y location of power up.
	 */
	private int yLocation;
	/**
	 * Size of power up.
	 */
	private int size = 30;
	/**
	 * Window width of game panel.
	 */
	private int windowWidth;
	/**
	 * Window height of game panel.
	 */
	private int windowHeight;
	/**
	 * Margin from edge of frame to edge of game.
	 */
	private int margin; 
	/**
	 * Instance of random class.
	 */
	private Random rand = new Random();
	
	/**
	 * Builds the power up object and initializes its position.
	 */
	public PowerUp(final int windowWidth, final int windowHeight, final int margin) {
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
		this.margin = margin;
		this.setLocation();
	}
	
	/**
	 * Paints the power up to screen.
	 */
	public void paintComponent(final Graphics g) {
		g.setColor(Color.PINK);
		g.fillRect(xLocation, yLocation, size, size);
	}
	
	/**
	 * Sets the initial location of the power up.
	 */
	public void setLocation() {
		// Random value from edge of margin to edge of game screen.
		int r = rand.nextInt(windowWidth - margin) + margin;
		this.xLocation = r;
		// Off screen
		this.yLocation = -40;
	}
	
	/**
	 * Moves power up down screen.
	 * @return Status of location, if power up is still on screen.
	 */
	public boolean update() {
		int r = rand.nextInt(15) - rand.nextInt(15);
		this.xLocation += r;
		if (this.xLocation < margin) {
			this.xLocation += 20;
		}
		if (this.xLocation > windowWidth) {
			this.xLocation -= 20;
		}
		this.yLocation += this.ySpeed;
		if (yLocation > windowHeight + 50) {
			return false;
		}
		return true;
	}
	
	/**
	 * @return PowerUp's X location.
	 */
	public int getxLocation() {
		return this.xLocation;
	}
	
	/**
	 * @return PowerUp Y location.
	 */
	public int getyLocation() {
		return this.yLocation;
	}
	
	/**
	 * @return PowerUp's size;
	 */
	public int getSize() {
		return this.size;
	}
}
