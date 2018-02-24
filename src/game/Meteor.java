package game;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Meteor class holds information for each meteor object, controls
 * their location and movement, and determines size and image.
 * @author JustinDowty
 * @author Ted Lang
 * @author Alec Allain
 */
public class Meteor extends JPanel {
	/**
	 * Meteor's current X location.
	 */
	private int xLocation;
	/**
	 * Meteor's current Y location.
	 */
	private int yLocation;
	/**
	 * Meteor's initial X location.
	 */
	private int initxLocation;
	/**
	 * Meteor's downward speed (Y).
	 */
	private int ySpeed;
	/**
	 * Meteor's right speed (X).
	 */
	private double xSpeed;
	/**
	 * Meteor's size.
	 */
	private Dimension size;
	/**
	 * Instance of random class.
	 */
	private Random rand = new Random();	
	/**
	 * Meteor's ImageIcon.
	 */
	private ImageIcon i;
	
	/**
	 * Constructor initalizes meteor. Sets random image and resets.
	 */
	public Meteor() {
		int r = rand.nextInt(3);
		if (r == 0) {
			i = new ImageIcon("meteor.png");
		} else if (r == 1) {
			i = new ImageIcon("meteor2.png");
		} else {
			i = new ImageIcon("meteor3.png");
		}
		reset();
	}
	
	/**
	 * Sets Meteor's initial location.
	 */
	public void setLocation() {
		// Subtracts end margin (room for movement) and meteor size
		this.xLocation = rand.nextInt((int) (OrbitGame.WINDOW_WIDTH 
				- OrbitGame.MARGIN - size.getWidth()));
		this.initxLocation = xLocation;
		this.yLocation = rand.nextInt(100);
	}
	
	/**
	 * Sets Meteor's speed at random.
	 */
	public void setSpeed() {
		this.ySpeed = rand.nextInt(1) + 4;
		this.xSpeed = 30;
	}
	
	/**
	 * Sets Meteor's size at random.
	 */
	public void setSize() {
		this.size = new Dimension(rand.nextInt(40) + 50, 
			rand.nextInt(40) + 50);
	}
	
	/**
	 * Paints Meteor at correct location.
	 * @param g Instance of graphics.
	 */
	public void paintComponent(final Graphics g) {
		i.paintIcon(this, g, xLocation, yLocation);
	}
	
	/**
	 * Resets Meteor by calling setSize, setLocation, and setSpeed.
	 */
	public void reset() {
		this.setSize();
		this.setLocation();
		this.setSpeed();		
		Image image = i.getImage(); // transform it
		Image newimg = image.getScaledInstance(this.getWidth(), 
				this.getWidth(),  java.awt.Image.SCALE_SMOOTH); 
		i = new ImageIcon(newimg);
	}
	
	/**
	 * Updates the Meteor's location and speed, resets when meteor
	 * has reached end of screen.
	 */
	public void update() {
		if (yLocation <= OrbitGame.WINDOW_HEIGHT) {
			yLocation += ySpeed;
		}
		if (yLocation <= OrbitGame.WINDOW_HEIGHT / 2 
				&& xLocation < initxLocation 
				+ OrbitGame.MARGIN) {
			xLocation += xSpeed;
			xSpeed -= 2 * xSpeed / 30;
		}
		// Increments score panel, resets at random interval
		if (yLocation > OrbitGame.WINDOW_HEIGHT) { 
			ScorePanel.incrementMeteorsDodged();
			this.reset();		
		}
	}
	
	/**
	 * @return Meteor's X location.
	 */
	public int getxLocation() {
		return this.xLocation;
	}
	
	/**
	 * @return Meteor's Y location.
	 */
	public int getyLocation() {
		return this.yLocation;
	}
	
	/**
	 * @return Meteor's current width.
	 */
	public int getWidth() {
		return (int) size.getWidth();
	}
}
