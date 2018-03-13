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
	 */
	public Ship(int windowWidth, int windowHeight, int margin, BlastTypes blastType) {
		this.blastType = blastType;
		if (blastType == BlastTypes.STANDARD) {
			blastxLocations = new int[1];
			blastyLocations = new int[1];
			blastsFiring = new boolean[1];
		} else if (blastType == BlastTypes.MULTI_BLAST
				|| blastType == BlastTypes.SIDE_BLASTS) {
			blastxLocations = new int[3];
			blastyLocations = new int[3];
			blastsFiring = new boolean[3];
		}
		for (int i = 0; i < blastsFiring.length; i++) {
			blastsFiring[i] = false;
		}
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
		this.margin = margin;
		this.health = 5;
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
		if (this.healing && this.health > 0) {
			i = new ImageIcon("shiptransparent.png");
		} else {
			i = new ImageIcon("ship.png");
		}
		if (this.blastType == BlastTypes.STANDARD) {
			g.setColor(Color.YELLOW);
		} else if (this.blastType == BlastTypes.SIDE_BLASTS) {
			g.setColor(Color.PINK);
		} else if (this.blastType == BlastTypes.MULTI_BLAST) {
			g.setColor(Color.MAGENTA);
		}		
		for (int i = 0; i < blastxLocations.length; i++) {
			g.fillRect(blastxLocations[i], blastyLocations[i], width / 2, height);
		}
		i.paintIcon(this, g, xLocation, yLocation);
		g.setColor(Color.GREEN);
		g.fillRect(this.xLocation + this.width + 20, 
				this.yLocation + 5, 5, 50);
		g.setColor(Color.RED);
		if (this.health == 4) {
			g.fillRect(this.xLocation + this.width + 20, 
					this.yLocation + 5, 5, 10);
		} else if (this.health == 3) {
			g.fillRect(this.xLocation + this.width + 20, 
					this.yLocation + 5, 5, 20);
		} else if (this.health == 2) {
			g.fillRect(this.xLocation + this.width + 20, 
					this.yLocation + 5, 5, 30);
		} else if (this.health == 1) {
			g.fillRect(this.xLocation + this.width + 20, 
					this.yLocation + 5, 5, 40);
		}
		if (this.health == 0) {
			g.fillRect(this.xLocation + this.width + 20, 
					this.yLocation + 5, 5, 50);
		}
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
		for (int i = 0; i < blastxLocations.length; i++){
			if (this.firing && this.blastyLocations[i] > -20 
					&& blastsFiring[i] == true) {
				allFired = false;
				if (blastType == BlastTypes.SIDE_BLASTS 
						&& i == 0){ //IGNORE MIDDLE BLAST
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
				// puts blast off screen !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				this.blastyLocations[i] = windowHeight + 100;
			}
		}
		if (allFired) {
			this.firing = false; // done with blast path
			this.canFire = true; // can fire now
			for (int j = 0; j < blastsFiring.length; j++) {
				blastsFiring[j] = false;
				// puts blast off screen !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
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
	 */
	public void cancelBlast(int b) {
		blastsFiring[b] = false;
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
}
