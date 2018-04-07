package game;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * This class defines ship object and contains information on its location 
 * and firing status.
 * @author JustinDowty
 * @author Ted Lang
 * @author Alec Allain
 */
public class Ship extends JPanel {
	private static final long serialVersionUID = 1L;
	/**
	 * Current X location.
	 */
	private int xLocation;
	/**
	 * Current Y location.
	 */
	private int yLocation;
	/**
	 * Current blasts X locations.
	 */
	private int[] blastxLocations;
	/**
	 * Current blasts Y locations.
	 */
	private int[] blastyLocations;
	/**
	 * Whether or not a specific blast is still firing.
	 */
	private boolean[] blastsFiring;
	/**
	 * Ships blast type.
	 */
	private BlastTypes blastType;
	/**
	 * Width of blast.
	 */
	private int blastWidth;
	/**
	 * Height of blast.
	 */
	private int blastHeight;
	/**
	 * Whether or not ship can fire.
	 */
	private boolean canFire = true;
	/**
	 * Whether or not ship is firing currently.
	 */
	private boolean firing;
	/**
	 * Speed of ship.
	 */
	private int speed = 15;
	/**
	 * Width of ship.
	 */
	private int width = 40;
	/**
	 * Height of ship.
	 */
	private int height = 45;
	/**
	 * Ship's health.
	 */
	private int health;
	/**
	 * Whether or not ship is healing currently.
	 */
	private boolean healing;
	/**
	 * Whether ship is invincible.
	 */
	private boolean invincible = false;
	/**
	 * Ship's ImageIcon.
	 */
	private ImageIcon i;
	/**
	 * The width of the window for game.
	 */
	private int windowWidth;
	/**
	 * The height of the window for game.
	 */
	private int windowHeight;
	/**
	 * The end margin of the window for game.
	 */
	private int margin;
	
	/**
	 * Constructor sets health to full, healing to false, and 
	 * resets location.
	 * @param windowWidth The window width for the game.
	 * @param windowHeight The window height for the game.
	 * @param margin The window margin to left of game.
	 * @param blastType Current blast type.
	 */
	public Ship(final int windowWidth, final int windowHeight, 
			final int margin, final BlastTypes blastType) {
		this.blastType = blastType;
		if (blastType == BlastTypes.STANDARD
				|| blastType == BlastTypes.LAZER) {
			blastxLocations = new int[1];
			blastyLocations = new int[1];
			blastsFiring = new boolean[1];
			blastWidth = 20;
			blastHeight = 45;
			if (blastType == BlastTypes.LAZER) {
				blastHeight = 400;
			}
		} else if (blastType == BlastTypes.MULTI_BLAST
				|| blastType == BlastTypes.SIDE_BLASTS) {
			blastxLocations = new int[3];
			blastyLocations = new int[3];
			blastsFiring = new boolean[3];
			blastWidth = 20;
			blastHeight = 45;
		}
		for (int i = 0; i < blastsFiring.length; i++) {
			blastsFiring[i] = false;
		}
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
		this.margin = margin;
		this.health = 10;
		this.healing = false;
		setLocation();
	}
	
	/**
	 * Sets ships location to initial spot on screen.
	 */
	public void setLocation() {
		this.xLocation = windowWidth / 2 + margin;
		this.yLocation = windowHeight - 3 * this.height;
		for (int i = 0; i < blastxLocations.length; i++) {
			this.blastyLocations[i] = windowHeight + 100;
		}
	}
	
	/**
	 * Paints the ship and its health bar based on current health.
	 * @param g Instance of Graphics.
	 */
	public void paintComponent(final Graphics g) {
		i = new ImageIcon("ship.png");
		if (this.healing && this.health > 0) {
			i = new ImageIcon("shiptransparent.png");
		}
		if (this.invincible) {
			i = new ImageIcon("shipinvincible.png");
		}
		if (this.blastType == BlastTypes.STANDARD) {
			g.setColor(Color.YELLOW);
		} else if (this.blastType == BlastTypes.SIDE_BLASTS) {
			g.setColor(Color.PINK);
		} else if (this.blastType == BlastTypes.MULTI_BLAST) {
			g.setColor(Color.MAGENTA);
		} else if (this.blastType == BlastTypes.LAZER) {
			g.setColor(Color.RED);
		}
		if (firing) {
			for (int i = 0; i < blastxLocations.length; i++) {
				g.fillOval(blastxLocations[i], blastyLocations[i] - 320, blastWidth, blastHeight);
			}
		}
		i.paintIcon(this, g, xLocation, yLocation);
		g.setColor(Color.RED);
		g.fillRect(this.xLocation + this.width + 20, 
				this.yLocation + 5, 5, 50);
		g.setColor(Color.GREEN);
		g.fillRect(this.xLocation + this.width + 20, 
				//10 - health * 5 is to move bar down incrementally
				this.yLocation + 5 + (10 - this.health) * 5, 5, 
				this.health * 5);
	}
	
	/**
	 * Moves ship to the right.
	 */
	public void moveRight() {
		if (this.xLocation < windowWidth - 2 * width) {
			this.xLocation += speed;
		}
	}
	
	/**
	 * Moves ship to the left.
	 */
	public void moveLeft() {
		if (this.xLocation > margin + 10) {
			this.xLocation -= speed;
		}
	}
	
	/**
	 * Moves ship down.
	 */
	public void moveDown() {
		if (this.yLocation < windowHeight - 3 * height) {
			this.yLocation += speed;
		}
	}
	
	/**
	 * Moves ship up.
	 */
	public void moveUp() {
		// Top Bounds
		if (this.yLocation > 250) {
			this.yLocation -= speed;
		}
	}
	
	/**
	 * Initiates blast if ship is able to fire.
	 */
	public void initiateBlast() {
		if (this.canFire) {
			// puts blast at ships position before firing
			for (int i = 0; i < blastxLocations.length; i++) {
				this.blastxLocations[i] = this.xLocation;
				this.blastyLocations[i] = this.yLocation;
				blastsFiring[i] = true;
			}
			this.firing = true; // allowing blast to fly
			this.canFire = false; // must wait to fire again
		}
	}
	
	/**
	 * Updates location of blast if firing.
	 */
	public void updateBlast() {
		boolean allFired = true;
		for (int i = 0; i < blastxLocations.length; i++) {
			if (this.firing && this.blastyLocations[i] > -20 
					&& blastsFiring[i]) {
				allFired = false;
				if (blastType == BlastTypes.SIDE_BLASTS 
						&& i == 0) { //IGNORE MIDDLE BLAST
					blastsFiring[i] = false;
				} else {
					this.blastyLocations[i] -= 10;
				}
				if (i > 0 && (blastType == BlastTypes.MULTI_BLAST
						|| blastType == BlastTypes.SIDE_BLASTS)) {
					this.blastxLocations[1] += 5;
					this.blastxLocations[2] -= 5;
				}
			} else {
				blastsFiring[i] = false;
				// puts blast off screen
				this.blastyLocations[i] = windowHeight + 100;
			}
		}
		if (allFired) {
			this.firing = false; // done with firing
			this.canFire = true; // can fire now
			for (int j = 0; j < blastsFiring.length; j++) {
				blastsFiring[j] = false;
				// puts blast off screen
				this.blastyLocations[j] = windowHeight + 100;
			}
		}
	}
	
	/**
	 * Decreases ships health.
	 * @return Ship's updated health.
	 */
	public int decreaseHealth() {
		if (this.health > 0) {
			this.health--;
		}
		this.healing = true;
		return this.health;
	}
	
	/**
	 * Cancels blast by setting firing to false.
	 * @param isBoss Determines if hit is to a boss.
	 * @param b Index of blast to be cancelled.
	 */
	public void cancelBlast(final int b, final boolean isBoss) {
		blastsFiring[b] = false;
		// LAZER does not get canceled until end
		if (blastType == BlastTypes.LAZER && !isBoss) {
			blastsFiring[b] = true;
		}
		boolean allFired = true;
		for (int i = 0; i < blastsFiring.length; i++) {
			if (blastsFiring[i]) {
				allFired = false;
			}
		}
		if (allFired) {
			this.firing = false;
		}
	}
	
	/**
	 * @return Ship's X location.
	 */
	public int getxLocation() {
		return this.xLocation;
	}
	
	/**
	 * @return Ship's Y location.
	 */
	public int getyLocation() {
		return this.yLocation;
	}
	
	/**
	 * @return Blast X location.
	 */
	public int[] getBlastxLocations() {
		return this.blastxLocations;
	}
	
	/**
	 * @return Blast Y location.
	 */
	public int[] getBlastyLocations() {
		return this.blastyLocations;
	}
	
	/**
	 * @return Ship's width.
	 */
	public int getWidth() {
		return this.width;
	}
	
	/**
	 * @return Ship's height.
	 */
	public int getHeight() {
		return this.height;
	}
	
	/**
	 * @return Blasts width.
	 */
	public int getBlastWidth() {
		return this.blastWidth;
	}
	
	/**
	 * @return Blasts height.
	 */
	public int getBlastHeight() {
		return this.blastHeight;
	}
	
	/**
	 * @return Blast type.
	 */
	public BlastTypes getBlastType() {
		return this.blastType;
	}
	
	/**
	 * @return Whether or not ship is firing.
	 */
	public boolean isFiring() {
		return this.firing;
	}
	
	/**
	 * @return Whether or not ship is healing.
	 */
	public boolean isHealing() {
		return this.healing;
	}
	
	/**
	 * Sets ship's healing status.
	 * @param healing Whether or not ship is healing.
	 */
	public void setHealing(final boolean healing) {
		this.healing = healing;
	}
	
	/**
	 * Sets ship's invincible status.
	 * @param invincible Whether or not ship is invincible.
	 */
	public void setInvincible(final boolean invincible) {
		this.invincible = invincible;
	}
	
	/**
	 * @return Ship's invincibility status.
	 */
	public boolean isInvincible() {
		return this.invincible;
	}
}
