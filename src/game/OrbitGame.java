package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

/**
 * This class is the main game engine. It is the panel that contains 
 * the game as well as keeping track of game data and updates.
 * @author JustinDowty
 * @author Ted Lange
 * @author Alec Allain
 */
public class OrbitGame extends JPanel {
	private static final long serialVersionUID = 1L;
	/**
	 * Window width of game panel.
	 */
	public static final int WINDOW_WIDTH = 1100;
	/**
	 * Window height of game panel.
	 */
	public static final int WINDOW_HEIGHT = 800;
	/**
	 * Margin from edge of panel to edge of frame.
	 */
	public static final int MARGIN = 400; 
	/**
	 * ArrayList of meteors in game.
	 */
	private ArrayList<Meteor> meteorArray;
	/**
	 * ArrayList of aliens in game.
	 */
	private ArrayList<Alien> alienArray;
	/**
	 * Ship in game.
	 */
	private Ship ship;
	/**
	 * Planet in game.
	 */
	private Planet planet;
	/**
	 * Current key that is pressed.
	 */
	private char currentKey;
	/**
	 * Boolean whether game is in play or not.
	 */
	private boolean playing;
	/**
	 * If ship is hit, hitTime holds the in-game time it occurred.
	 */
	private int hitTime = 0;
	/**
	 * Instance of random class.
	 */
	private Random rand = new Random();
	/**
	 * Array of star locations for background.
	 */
	private int[] starLocations;
	/**
	 * Secondary graphics instance for double buffering.
	 */
	private Graphics dbg;
	/**
	 * Image instance used for double buffering.
	 */
	private Image dbImage;
	
	/**
	 * Constructor resets game, generates stars for background,
	 * and sets size and background of panel.
	 */
	public OrbitGame() {
		reset();
		generateStars();
		this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		this.setBackground(Color.BLACK);
	}
	
	/**
	 * Initializes game components and resets score panel.
	 */
	public void reset() {
		planet = new Planet();
		ship = new Ship();
		meteorArray = new ArrayList<Meteor>();
		alienArray = new ArrayList<Alien>();
		ScorePanel.resetMeteorsDodged();
		ScorePanel.resetAliensKilled();
		playing = true;
		// Adds one meteor to start game off
		meteorArray.add(new Meteor());
		currentKey = 'S';
	}
	
	/**
	 * This method double buffers graphics for smoothness.
	 * @param g Instance of Graphics.
	 */
	public void paint(final Graphics g) {
		dbImage = createImage(WINDOW_WIDTH, WINDOW_HEIGHT);
		dbg = dbImage.getGraphics();
		paintComponent(dbg);
		g.drawImage(dbImage, 0, 0, this);
	}

	/**
	 * This method paints components to panel for game.
	 * @param g Instance of Graphics.
	 */
	public void paintComponent(final Graphics g) {
		super.paintComponent(g);	
		g.setColor(Color.DARK_GRAY);
		for (int i = 0; i < starLocations.length; i += 4) {
			g.fillOval(starLocations[i], 
					starLocations[i + 1], 
					starLocations[i + 2], 
					starLocations[i + 3]);
		}
		planet.paintComponent(g);
		for (Alien alien : alienArray) {
			alien.paintComponent(g);
		}
		for (Meteor meteor : meteorArray) {
			meteor.paintComponent(g);
		}
		ship.paintComponent(g);
		// Drawing small tick showing ship bounds next to planet
		g.setColor(Color.LIGHT_GRAY);
		g.drawLine(410, 900, 410, 730);
	}
	
	/**
	 * Updates game, adding and resetting components at the 
	 * appropriate times. Checks for game interactions via currentKey
	 * and checks for objects interacting.
	 * @param currTime Current time in game.
	 */
	public void update(final int currTime) {
		// Adds a new meteor to game until enough are in game,
		// those 20 get recycled
		if (currTime % 500 == 0 && meteorArray.size() < 6) {
			meteorArray.add(new Meteor());
		}
		if (currTime % 50 == 0 && alienArray.size() < 2) {
			alienArray.add(new Alien());
		}
		for (Meteor meteor : meteorArray) {
			meteor.update();
			if (checkIfShipHit(meteor)) {
				ship.setHealing(true);
				hitTime = currTime;
			}
			checkIfBlastHit(meteor);
		}
		for (Alien alien : alienArray) {
			alien.update(currTime);
			if (checkIfShipHit(alien)) {
				ship.setHealing(true);
				hitTime = currTime;
			}
			checkIfBlastHit(alien);
		}
		if (currTime - hitTime > 1000 && ship.isHealing()) {
			ship.setHealing(false);
		}
		if (currentKey == 'L') {
			ship.moveLeft();
		}
		if (currentKey == 'R') {
			ship.moveRight();
		}
		if (currentKey == 'U') {
			ship.moveUp();
		}
		if (currentKey == 'D') {
			ship.moveDown();
		}
		ship.updateBlast();
		changeFireButtonColor();
		planet.drift(currTime);
		moveStars();
	}
	
	/**
	 * Calculates if ship is hit, calls methods to update game accordingly.
	 * @param meteor Meteor being checked for interaction.
	 * @return Whether or not ship has been hit.
	 */
	public boolean checkIfShipHit(final Meteor meteor) {
		int shipXcoordLeft = ship.getxLocation() + 5;
		int shipXcoordRight = ship.getxLocation() + ship.getWidth() - 5;
		int meteorXcoordLeft = meteor.getxLocation();
		int meteorXcoordRight = meteor.getxLocation() 
				+ meteor.getWidth();
		
		int shipYcoordTop = ship.getyLocation() - 5;
		int shipYcoordBottom = ship.getyLocation() + ship.getHeight();
		int meteorYcoordTop = meteor.getyLocation();
		int meteorYcoordBottom = meteor.getyLocation() 
				+ meteor.getWidth() - 15;
		
		if ((shipXcoordLeft < meteorXcoordRight 
				&& shipXcoordRight > meteorXcoordLeft) 
				&& (shipYcoordTop < meteorYcoordBottom 
					&& shipYcoordBottom > meteorYcoordTop)
				&& !ship.isHealing()) {
			if (ship.decreaseHealth() == 0) {
				playing = false;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Calculates if ship is hit, calls methods to update game accordingly.
	 * @param alien Alien being checked for interaction.
	 * @return Whether or not ship has been hit.
	 */
	public boolean checkIfShipHit(final Alien alien) {
		int shipXcoordLeft = ship.getxLocation() + 5;
		int shipXcoordRight = ship.getxLocation() + ship.getWidth() - 5;
		int alienXcoordLeft = alien.getxLocation();
		int alienXcoordRight = alien.getxLocation() + alien.getWidth();
		int blastXcoordLeft = alien.getBlastxLocation();
		int blastXcoordRight = alien.getBlastxLocation() + 20;
		
		int shipYcoordTop = ship.getyLocation() + 15;
		int shipYcoordBottom = ship.getyLocation() + ship.getHeight();
		int alienYcoordTop = alien.getyLocation();
		int alienYcoordBottom = alien.getyLocation() 
				+ alien.getWidth() - 15;
		int blastYcoordTop = alien.getBlastyLocation();
		int blastYcoordBottom = alien.getBlastyLocation() + 20;
		
		if ((shipXcoordLeft < alienXcoordRight 
				&& shipXcoordRight > alienXcoordLeft) 
				&& (shipYcoordTop < alienYcoordBottom 
					&& shipYcoordBottom > alienYcoordTop)
				&& !ship.isHealing()) {
			if (ship.decreaseHealth() == 0) {
				playing = false;
			}
			return true;
		}
		
		if ((shipXcoordLeft < blastXcoordRight 
				&& shipXcoordRight > blastXcoordLeft) 
				&& (shipYcoordTop < blastYcoordBottom 
					&& shipYcoordBottom > blastYcoordTop)
				&& !ship.isHealing()) {
			if (ship.decreaseHealth() == 0) {
				playing = false;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Calculates if meteor is hit, updates game accordingly.
	 * @param meteor Meteor being checked for interaction.
	 */
	public void checkIfBlastHit(final Meteor meteor) {
		int blastXcoordLeft = ship.getBlastxLocation();
		int blastXcoordRight = ship.getBlastxLocation() 
				+ ship.getWidth() / 2;
		int meteorXcoordLeft = meteor.getxLocation();
		int meteorXcoordRight = meteor.getxLocation() 
				+ meteor.getWidth();
		
		int blastYcoordTop = ship.getBlastyLocation();
		int blastYcoordBottom = ship.getBlastyLocation() 
				+ ship.getHeight() / 2;
		int meteorYcoordTop = meteor.getyLocation();
		int meteorYcoordBottom = meteor.getyLocation() 
				+ meteor.getWidth();
		
		if ((blastXcoordLeft < meteorXcoordRight 
				&& blastXcoordRight > meteorXcoordLeft) 
				&& (blastYcoordTop < meteorYcoordBottom 
					&& blastYcoordBottom 
					> meteorYcoordTop)) {
			meteor.reset();
			ship.cancelBlast();
			ScorePanel.setFireButtonColor(Color.RED);
		}
	}
	
	/**
	 * Calculates if alien is hit, calls methods to update game accordingly.
	 * @param alien Alien being checked for interaction.
	 */
	public void checkIfBlastHit(final Alien alien) {
		int blastXcoordLeft = ship.getBlastxLocation();
		int blastXcoordRight = ship.getBlastxLocation() 
				+ ship.getWidth() / 2;
		int alienXcoordLeft = alien.getxLocation();
		int alienXcoordRight = alien.getxLocation() + alien.getWidth();
		
		int blastYcoordTop = ship.getBlastyLocation();
		int blastYcoordBottom = ship.getBlastyLocation() 
				+ ship.getHeight() / 2;
		int alienYcoordTop = alien.getyLocation();
		int alienYcoordBottom = alien.getyLocation() + alien.getWidth();
		
		if ((blastXcoordLeft < alienXcoordRight 
				&& blastXcoordRight > alienXcoordLeft) 
				&& (blastYcoordTop < alienYcoordBottom 
						&& blastYcoordBottom 
						> alienYcoordTop)) {
			ship.cancelBlast();
			ScorePanel.setFireButtonColor(Color.RED);
			alien.decreaseHealth();
		}
	}
	
	/**
	 * Sets current key pressed.
	 * @param key Character of key that is pressed.
	 */
	public void setCurrentKey(final char key) {
		this.currentKey = key;
	}
	
	/**
	 * @return The game's current ship object.
	 */
	public Ship getShip() {
		return this.ship;
	}
	
	/**
	 * Sets games status, playing or not.
	 * @param playing Whether or not game is currently running.
	 */
	public void setPlaying(final boolean playing) {
		this.playing = playing;
	}
	
	/**
	 * @return Whther game is being played currently.
	 */
	public boolean getPlaying() {
		return this.playing;
	}
	
	/**
	 * Changes the color of score panel if ship is firing. Color changes
	 * to gray if blast is active (meaning ship cannot fire yet) and
	 * to red if ship is ready to blast.
	 */
	public void changeFireButtonColor() {
		if (ship.isFiring()) {
			ScorePanel.setFireButtonColor(Color.LIGHT_GRAY);
		} else {
			ScorePanel.setFireButtonColor(Color.RED);
		}
	}
	
	/**
	 * Generates background stars at random.
	 */
	public void generateStars() {
		starLocations = new int[240];
		for (int i = 0; i < 240; i += 4) {
			int size = rand.nextInt(20) + 10;
			starLocations[i] = rand.nextInt(1100);
			starLocations[i + 1] = rand.nextInt(800);
			starLocations[i + 2] = size / 2;
			starLocations[i + 3] = size;
		}
	}
	
	/**
	 * Moves stars down background.
	 */
	public void moveStars() {
		for (int i = 0; i < starLocations.length; i += 4) {
			if (starLocations[i + 1] > 805) {
				starLocations[i] = rand.nextInt(1100);
				starLocations[i + 1] = -5;
			} else {
				starLocations[i + 1] += 2;
			}
		}
	}
}
