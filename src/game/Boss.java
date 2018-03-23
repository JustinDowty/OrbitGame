package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Random;

import javax.swing.ImageIcon;

/**
 * Boss class holds all infromation for bosses position,
 * blasts, and how it is painted to screen.
 * @author Justin Dowty
 *
 */
public class Boss {
	/**
	 * Boss's current health.
	 */
	private int health;
	/**
	 * Boss's current X Location.
	 */
	private int xLocation;
	/**
	 * Boss's current Y Location.
	 */
	private int yLocation;
	/**
	 * Whether or not boss is firing.
	 */
	private boolean firing;
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
	 * Current image icon for Boss.
	 */
	private ImageIcon i;
	/**
	 * Size of boss.
	 */
	private Dimension size;
	/**
	 * Random number generator.
	 */
	private Random rand = new Random();
	/**
	 * Blast X locations.
	 */
	private int[] blastxLocations = new int[3];
	/**
	 * Blast Y locations.
	 */
	private int[] blastyLocations = new int[3];
	/**
	 * Size of blasts.
	 */
	private int blastSize = 20;
	/**
	 * Speed of blasts.
	 */
	private int blastSpeed = 5;
	/**
	 * Whether the ship is moving left.
	 */
	private boolean movingLeft = false;
	/**
	 * Whether the ship is moving right.
	 */
	private boolean movingRight = true;
	/**
	 * Moving speed.
	 */
	private int movingSpeed = 4;
	/**
	 * Initializes boss object.
	 * @param windowWidth width of game window.
	 * @param windowHeight height of game window.
	 * @param margin margin for panel of game window.
	 * @param bossCount current boss count, determines
	 * health multiplier.
	 */
	public Boss(final int windowWidth, final int windowHeight, 
			final int margin, final int bossCount) {
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
		this.margin = margin;
		this.health = 10 + 5 * bossCount;
		this.size = new Dimension(400, 200);
		this.setLocation();
	}
	
	/**
	 * Sets the bosses location.
	 */
	public void setLocation() {
		// Sets X location to center the boss in screen
		this.xLocation = (int) ((windowWidth - margin) / 2
				- this.getWidth() / 2 + margin);
		this.yLocation = 40;
		// Sets blast locations within boss
		blastxLocations[0] = xLocation + 40;
		blastxLocations[1] = xLocation + getWidth() / 2
				- blastSize / 2;
		blastxLocations[2] = xLocation + getWidth()
				- blastSize - 40;
		for (int i = 0; i < 3; i++) {
			blastyLocations[i] = yLocation + getHeight()
				- blastSize - 20;
		}
	}
	/**
	 * Paints the boss and blasts.
	 * @param g Graphics instance.
	 */
	public void paintComponent(final Graphics g) {
		g.setColor(Color.CYAN);
		for (int i = 0; i < 3; i++) {
			g.fillRect(blastxLocations[i], blastyLocations[i], 
					blastSize, blastSize);
		}
		g.setColor(Color.GRAY);
		g.fillRect(xLocation, yLocation,
				this.getWidth(), this.getHeight());
	}
	/**
	 * Updates the boss's positions based on current game time.
	 * @param currTime current time in game.
	 */
	public void update(final int currTime) {
		int r = rand.nextInt(200) + 5;
		if (currTime % r == 0) {
			this.firing = true;
		}
		if (this.firing) {
			for (int i = 0; i < 3; i++) {
				blastyLocations[i] += blastSpeed;
			}
		}
		// Resets blasts
		if (blastyLocations[0] > windowHeight + blastSize) {
			// Sets blast locations within boss
			blastxLocations[0] = xLocation + 40;
			blastxLocations[1] = xLocation + getWidth() / 2
					- blastSize / 2;
			blastxLocations[2] = xLocation + getWidth()
					- blastSize - 40;
			for (int i = 0; i < 3; i++) {
				blastyLocations[i] = yLocation + getHeight()
					- blastSize - 20;
			}
			this.firing = false;
		}
		if (movingRight) {
			this.xLocation += movingSpeed;
			// moves blasts if not firing
			if (!firing) {
				for (int i = 0; i < 3; i++) {
					this.blastxLocations[i] += movingSpeed;
				}
			}
			if (this.xLocation + getWidth() > windowWidth) {
				movingRight = false;
				movingLeft = true;
			}
		}
		if (movingLeft) {
			this.xLocation -= movingSpeed;
			// moves blasts if not firing
			if (!firing) {
				for (int i = 0; i < 3; i++) {
					this.blastxLocations[i] -= movingSpeed;
				}
			}
			if (this.xLocation < margin) {
				movingRight = true;
				movingLeft = false;
			}
		}
	}
	/**
	 * @return Boss's width.
	 */
	public int getWidth() {
		return (int) size.getWidth();
	}
	/**
	 * @return Boss's height.
	 */
	public int getHeight() {
		return (int) size.getHeight();
	}
	/**
	 * @return Boss's X location.
	 */
	public int getxLocation() {
		return this.xLocation;
	}
	/**
	 * @return Boss's Y location.
	 */
	public int getyLocation() {
		return this.yLocation;
	}
	/**
	 * @return Boss's health.
	 */
	public int getHealth() {
		return this.health;
	}
	/**
	 * Decreases boss's health by 1.
	 */
	public void decreaseHealth() {
		this.health--;
	}
	/**
	 * @return Blast X locations.
	 */
	public int[] getBlastxLocations() {
		return this.blastxLocations;
	}
	/**
	 * @return Blast Y locations.
	 */
	public int[] getBlastyLocations() {
		return this.blastyLocations;
	}
	/**
	 * @return Blast size.
	 */
	public int getBlastSize() {
		return this.blastSize;
	}
}
