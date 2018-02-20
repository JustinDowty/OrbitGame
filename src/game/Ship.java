package game;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * This class defines ship object and contains information on its location 
 * and firing status.
 * @author JustinDowty
 * @author Ted Lange
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
	 * Current blast X location.
	 */
	private int blastxLocation;
	/**
	 * Current blast Y location.
	 */
	private int blastyLocation;
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
	 * Constructor sets health to full, healing to false, and 
	 * resets location.
	 */
	public Ship() {
		this.health = 5;
		this.healing = false;
		setLocation();
	}
	
	/**
	 * Sets shipts location to initial spot on screen.
	 */
	public void setLocation() {
		this.xLocation = OrbitGame.WINDOW_WIDTH / 2 + OrbitGame.MARGIN;
		this.yLocation = OrbitGame.WINDOW_HEIGHT - 3 * this.height;
		this.blastxLocation = this.xLocation + width / 2;
		this.blastyLocation = OrbitGame.WINDOW_HEIGHT - 3 * this.height;
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
		g.setColor(Color.YELLOW);
		g.fillRect(blastxLocation, blastyLocation, width / 2, height);
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
		if (this.xLocation < OrbitGame.WINDOW_WIDTH - 2 * width) {
			this.xLocation += speed;
		}
		if (!firing) {
			this.blastxLocation = this.xLocation;
		}
	}
	
	/**
	 * Moves ship to the left.
	 */
	public void moveLeft() {
		if (this.xLocation > OrbitGame.MARGIN + 10) {
			this.xLocation -= speed;
		}
		if (!firing) {
			this.blastxLocation = this.xLocation;
		}
	}
	
	/**
	 * Moves ship down.
	 */
	public void moveDown() {
		if (this.yLocation < OrbitGame.WINDOW_HEIGHT - 3 * height) {
			this.yLocation += speed;
		}
		if (!firing) {
			this.blastyLocation = this.yLocation;
		}
	}
	
	/**
	 * Moves ship up.
	 */
	public void moveUp() {
		if (this.yLocation > 0) {
			this.yLocation -= speed;
		}
		if (!firing) {
			this.blastyLocation = this.yLocation;
		}
	}
	
	/**
	 * Initiates blast if ship is able to fire.
	 */
	public void initiateBlast() {
		if (this.canFire) {
			// puts blast at ships position before firing
			this.blastyLocation = this.yLocation; 
			this.blastxLocation = this.xLocation;
			this.firing = true; // allowing blast to fly
			this.canFire = false; // must wait to fire again
		}
	}
	
	/**
	 * Updates location of blast if firing.
	 */
	public void updateBlast() {
		if (this.firing && this.blastyLocation > -20) {
			this.blastyLocation -= 10;
		} else {
			this.firing = false; // done with blast path
			this.canFire = true; // can fire now
			 // puts blast off screen
			this.blastyLocation = OrbitGame.WINDOW_HEIGHT;
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
	public void cancelBlast() {
		this.firing = false;
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
	public int getBlastxLocation() {
		return this.blastxLocation;
	}
	
	/**
	 * @return Blast Y location.
	 */
	public int getBlastyLocation() {
		return this.blastyLocation;
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
