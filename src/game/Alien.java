package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Alien class holds information for each alien object, controls
 * their location and movement, and determines size and image.
 * @author JustinDowty
 * @author Ted Lang
 * @author Alec Allain
 */
public class Alien extends JPanel {
	/**
	 * Alien's health.
	 */
	private int health;
	/**
	 * Alien's current X location on screen.
	 */
	private int xLocation;
	/**
	 * Alien's current Y location on screen.
	 */
	private int yLocation;
	/**
	 * Alien's initial X location.
	 */
	private int initxLocation;
	/**
	 * Alien's speed downward (Y).
	 */
	private int ySpeed;
	/**
	 * Alien's speed right (X).
	 */
	private double xSpeed;
	/**
	 * Alien's blast X location.
	 */
	private int blastxLocation;
	/**
	 * Alien's blast Y location.
	 */
	private int blastyLocation;
	/**
	 * Whether or not Alien is currently firing.
	 */
	private boolean firing;
	/**
	 * Alien's blast speed (downward).
	 */
	private int blastSpeed;
	/**
	 * Alien's size.
	 */
	private Dimension size;
	/**
	 * Instance of random class.
	 */
	private Random rand = new Random();
	/**
	 * Alien's ImageIcon.
	 */
	private ImageIcon i;
	/**
	 * Window width of game panel.
	 */
	private int windowWidth;
	/**
	 * Window height of game panel.
	 */
	private int windowHeight;
	/**
	 * Margin from edge of panel to edge of frame.
	 */
	private int margin; 
	
	/**
	 * Constructor sets ImageIcon i, sets health to full, and resets alien
	 * via setSize() and reset() functions.
	 */
	public Alien(int windowWidth, int windowHeight, int margin) {
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
		this.margin = margin;
		i = new ImageIcon("alien.png");
		this.health = 2;
		this.setSize();
		reset();
	}
	
	/**
	 * Sets random x location, resets y to above screen height.
	 */
	public void setLocation() {
		// Subtracts end margin (room for movement) and meteor size
		this.xLocation = rand.nextInt((int) 
				(windowWidth - margin 
						- this.size.getWidth()));
		this.initxLocation = this.xLocation;
		this.yLocation = rand.nextInt(100);
		if (!firing) {
			this.blastxLocation = (int) 
					(this.xLocation 
					+ this.size.getWidth() / 2);
			this.blastyLocation = (int) 
					(this.yLocation 
					+ this.size.getHeight() / 2);
		}
	}
	
	/**
	 * Sets random speed for alien, x and y, and blast speed.
	 */
	public void setSpeed() {
		this.ySpeed = rand.nextInt(1) + 4;
		this.xSpeed = 30;
		this.blastSpeed = 2 * this.ySpeed;
	}
	
	/**
	 * Sets random size.
	 */
	public void setSize() {
		this.size = new Dimension(rand.nextInt(40) + 50, 
				rand.nextInt(40) + 50);
	}
	
	/**
	 * Paints alien to screen based on location. Also paints health bar
	 * next to alien based on current health.
	 * @param g Instance of graphics.
	 */
	public void paintComponent(final Graphics g) {
		if (this.firing) {
			g.setColor(Color.YELLOW);
			g.fillRect(this.blastxLocation, 
					this.blastyLocation, 20, 20);
		}
		i.paintIcon(this, g, xLocation, yLocation);
		g.setColor(Color.GREEN);
		if (this.health == 2) {
			g.fillRect(xLocation + getWidth() + 20, 
					yLocation, 5, 50);
		} else if (this.health == 1) {
			g.fillRect(xLocation + getWidth() + 20, 
					yLocation, 5, 25);
			g.setColor(Color.RED);
			g.fillRect(xLocation + getWidth() + 20, 
					yLocation + 25, 5, 25);
		} else {
			g.setColor(Color.RED);
			g.fillRect(xLocation + getWidth() + 20, 
					yLocation, 5, 50);
		}		
	}
	
	/**
	 * Resets the alien's position and speed.
	 */
	public void reset() {
		this.setLocation();
		this.setSpeed();
		Image image = i.getImage(); // transform it
		Image newimg = image.getScaledInstance(this.getWidth(), 
				this.getWidth(),  java.awt.Image.SCALE_SMOOTH); 
		i = new ImageIcon(newimg);
	}
	
	/**
	 * Updates alien's position, resetting if past the 
	 * bottom of screen and it is time for a new meteor to spawn.
	 * @param currTime Uses current time in game to determine when
	 * 					resetting blast is appropriate.
	 */
	public void update(final int currTime) {
		int r = rand.nextInt(200) + 5;
		if (this.yLocation <= windowHeight) {
			this.yLocation += this.ySpeed; // Moves alien
			if (!this.firing) { // Moves blast
				this.blastyLocation += this.ySpeed;
			} else {
				this.blastyLocation += this.blastSpeed;
			}
		}
		 // additional 100 to assure edge is covered
		if (this.yLocation <= windowHeight / 2 
				&& this.xLocation 
				< this.initxLocation + margin) {
			this.xLocation += this.xSpeed; // Moves x direction
			if (!this.firing) { // Moves blast
				this.blastxLocation += this.xSpeed;
			}
			this.xSpeed -= 2 * this.xSpeed / 30;
		} else if (currTime % r == 0) {
			this.firing = true;
		}
		if (yLocation > windowHeight) { 
			this.reset(); // Resets the alien when end is reached
		}
		if (blastyLocation > windowHeight) {
			this.firing = false; // Resets blast when end is reached
			this.blastxLocation = (int) (this.xLocation 
					+ this.size.getWidth() / 2);
			this.blastyLocation = (int) (this.yLocation 
					+ this.size.getHeight() / 2);
		}
	}
	
	/**
	 * @return Alien's X location.
	 */
	public int getxLocation() {
		return this.xLocation;
	}
	
	/**
	 * @return Alien's Y location.
	 */
	public int getyLocation() {
		return this.yLocation;
	}
	
	/**
	 * @return Alien's blast X location.
	 */
	public int getBlastxLocation() {
		return this.blastxLocation;
	}
	
	/**
	 * @return Alien's blast Y location.
	 */
	public int getBlastyLocation() {
		return this.blastyLocation;
	}
	
	/**
	 * @return Alien's width.
	 */
	public int getWidth() {
		return (int) this.size.getWidth();
	}
	
	/**
	 * @return Alien's x speed.
	 */
	public int getxSpeed() {
		return (int) this.xSpeed;
	}
	
	/**
	 * @return Alien's health.
	 */
	public int getHealth(){
		return this.health;
	}
	
	/**
	 * Decreases alien's health by 1, resets alien if destroyed.
	 */
	public void decreaseHealth() {
		this.health--;
	}
	
	/**
	 * Resets alien's health to 2 when killed.
	 */
	public void resetHealth(){
		this.health = 2;
	}
}
