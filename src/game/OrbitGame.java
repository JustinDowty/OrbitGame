package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JPanel;

/**
 * This class is the main game engine. It is the panel that contains 
 * the game as well as keeping track of game data and updates.
 * @author JustinDowty
 * @author Ted Lang
 * @author Alec Allain
 */
public class OrbitGame extends JPanel {
	private static final long serialVersionUID = 1L;
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
	 * Whether or not player is currently fighting a boss.
	 */
	private boolean fightingBoss = false;
	/**
	 * The current boss.
	 */
	private Boss boss;
	/**
	 * The boss number the player is currently on.
	 */
	private int bossCount = 0;
	/**
	 * The last time a boss was defeated.
	 */
	private int bossLastDefeated = 0;
	/**
	 * Toggles incoming enemy warning on and off.
	 */
	private boolean bossMsg = false;
	/**
	 * Current power up object.
	 */
	private PowerUp powerUp;
	/**
	 * Time that power up was activated.
	 */
	private int powerTime = 0;
	/**
	 * Constructor resets game, generates stars for background,
	 * and sets size and background of panel.
	 * @param windowWidth The window width for the game.
	 * @param windowHeight The window height for the game.
	 * @param margin The window margin to left of game.
	 * @param blastType The players current blast type.
	 */
	public OrbitGame(final int windowWidth, 
			final int windowHeight, final int margin,
			final BlastTypes blastType) {
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
		this.margin = margin;
		reset(blastType);
		starLocations = Utils.generateStars(4, windowWidth, windowHeight);
		this.setSize(windowWidth, windowHeight);
		this.setBackground(Color.BLACK);
	}
	
	/**
	 * Initializes game components and resets score panel.
	 * @param blastType The players current blastType.
	 */
	public void reset(final BlastTypes blastType) {
		planet = new Planet(windowHeight);
		ship = new Ship(windowWidth, windowHeight, margin, blastType);
		meteorArray = new ArrayList<Meteor>();
		alienArray = new ArrayList<Alien>();
		playing = true;
		fightingBoss = false;
		boss = null;
		bossCount = 0;
		bossLastDefeated = 0;
		currentKey = 'S';
	}
	
	/**
	 * This method double buffers graphics for smoothness.
	 * @param g Instance of Graphics.
	 */
	public void paint(final Graphics g) {
		dbImage = createImage(windowWidth, windowHeight);
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
		g.setColor(Color.LIGHT_GRAY);
		Font font = new Font("Impact", Font.BOLD, 40);
		g.setFont(font);
		if (bossMsg) {
			g.drawString("INCOMING ENEMY", 600, 200);
		}
		planet.paintComponent(g);
		for (Alien alien : alienArray) {
			alien.paintComponent(g);
		}
		for (Meteor meteor : meteorArray) {
			meteor.paintComponent(g);
		}
		if (boss != null) {
			boss.paintComponent(g);
		}
		if (powerUp != null) {
			powerUp.paintComponent(g);
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
	 * @param scorePanel Scorepanel to be updated.
	 */
	public void update(final int currTime, final ScorePanel scorePanel) {
		// Adds a new meteor to game until enough are in game,
		// those get recycled unless in boss fight
		if (currTime % 500 == 0 && meteorArray.size() < 6 && !fightingBoss) {
			meteorArray.add(new Meteor(windowWidth, windowHeight, margin));
		}
		if (currTime % 50 == 0 && alienArray.size() < 2 && !fightingBoss) {
			alienArray.add(new Alien(windowWidth, windowHeight, margin));
		}
		if (currTime - bossLastDefeated > 10000 && !fightingBoss) {
			if (currTime % 1000 == 0) {
				bossMsg = !bossMsg;
			}
		}
		if (currTime - bossLastDefeated > 15000 && !fightingBoss) {
			bossMsg = false;
			fightingBoss = true;
			// clears all meteors but 2
			alienArray.subList(1, alienArray.size()).clear();
			meteorArray.subList(2, meteorArray.size()).clear();
			boss = new Boss(windowWidth, windowHeight, 
					margin, bossCount);
		}
		if (fightingBoss) {
			boss.update(currTime);
			checkIfBlastHit(boss);
			if (checkIfShipHit(boss)) {
				ship.setHealing(true);
				hitTime = currTime;
			}
			if (boss.getHealth() < 1) {
				boss = null;
				fightingBoss = false;
				bossCount++;
				bossLastDefeated = currTime;
				scorePanel.incrementBossKilled();
			}
		}
		if (currTime % 20000 == 0) {
			powerUp = new PowerUp(windowWidth, windowHeight, margin);
		}
		if (powerUp != null) {
			if (checkPowerUp(powerUp)) {
				ship.setInvincible(true);
				powerTime = currTime;
				powerUp = null;
			} else if (!powerUp.update()) {
				powerUp = null;
			}
		}
		for (Meteor meteor : meteorArray) {
			meteor.update();
			if (checkIfShipHit(meteor)) {
				ship.setHealing(true);
				hitTime = currTime;
			}
			// Increments score panel, resets at random interval
			if (meteor.getyLocation() > windowHeight) { 
				scorePanel.incrementMeteorsDodged();
				meteor.reset();		
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
			if (alien.getHealth() < 1) {
				alien.reset();
				alien.resetHealth();
				alien.setSize();
				scorePanel.incrementAliensKilled();
			}
		}
		if (currTime - hitTime > 1000 && ship.isHealing()) {
			ship.setHealing(false);
		}
		if (currTime - powerTime > 4000 && ship.isInvincible()) {
			ship.setInvincible(false);
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
		Utils.moveStars(starLocations, windowWidth, windowHeight);
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
				&& !ship.isHealing() && !ship.isInvincible()) {
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
				&& !ship.isHealing() && !ship.isInvincible()) {
			if (ship.decreaseHealth() == 0) {
				playing = false;
			}
			return true;
		}
		if ((shipXcoordLeft < blastXcoordRight 
				&& shipXcoordRight > blastXcoordLeft) 
				&& (shipYcoordTop < blastYcoordBottom 
					&& shipYcoordBottom > blastYcoordTop)
				&& !ship.isHealing() && !ship.isInvincible()) {
			if (ship.decreaseHealth() == 0) {
				playing = false;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Calculates if ship is hit, calls methods to update game accordingly.
	 * @return Whether or not ship has been hit.
	 * @param boss Boss to be checked.
	 */
	public boolean checkIfShipHit(final Boss boss) {
		for (int i = 0; i < boss.getBlastxLocations().length; i++) {
			int shipXcoordLeft = ship.getxLocation() + 5;
			int shipXcoordRight = ship.getxLocation() + ship.getWidth() - 5;
			int blastXcoordLeft = boss.getBlastxLocations()[i];
			int blastXcoordRight = boss.getBlastxLocations()[i] 
					+ boss.getBlastSize();
			
			int shipYcoordTop = ship.getyLocation() - 5;
			int shipYcoordBottom = ship.getyLocation() + ship.getHeight();
			int blastYcoordTop = boss.getBlastyLocations()[i];
			int blastYcoordBottom = boss.getBlastyLocations()[i] 
					+ boss.getBlastSize();
			
			if ((shipXcoordLeft < blastXcoordRight 
					&& shipXcoordRight > blastXcoordLeft) 
					&& (shipYcoordTop < blastYcoordBottom 
						&& shipYcoordBottom > blastYcoordTop)
					&& !ship.isHealing() && !ship.isInvincible()) {
				if (ship.decreaseHealth() == 0) {
					playing = false;
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Calculates if meteor is hit, updates game accordingly.
	 * @param meteor Meteor being checked for interaction.
	 */
	public void checkIfBlastHit(final Meteor meteor) {
		for (int i = 0; i < ship.getBlastxLocations().length; i++) {
			int blastXcoordLeft = ship.getBlastxLocations()[i];
			int blastXcoordRight = ship.getBlastxLocations()[i] 
					+ ship.getBlastWidth();
			int meteorXcoordLeft = meteor.getxLocation();
			int meteorXcoordRight = meteor.getxLocation() 
					+ meteor.getWidth();
			
			int blastYcoordTop = ship.getBlastyLocations()[i];
			int blastYcoordBottom = ship.getBlastyLocations()[i] 
					+ ship.getBlastHeight();
			int meteorYcoordTop = meteor.getyLocation();
			int meteorYcoordBottom = meteor.getyLocation() 
					+ meteor.getWidth();
			
			if ((blastXcoordLeft < meteorXcoordRight 
					&& blastXcoordRight > meteorXcoordLeft) 
					&& (blastYcoordTop < meteorYcoordBottom 
						&& blastYcoordBottom 
						> meteorYcoordTop)) {
				meteor.reset();
				ship.cancelBlast(i, false);
				ScorePanel.setFireButtonColor(Color.RED);
			}
		}
	}
	
	/**
	 * Calculates if alien is hit, calls methods to update game accordingly.
	 * @param alien Alien being checked for interaction.
	 */
	public void checkIfBlastHit(final Alien alien) {
		for (int i = 0; i < ship.getBlastxLocations().length; i++) {
			int blastXcoordLeft = ship.getBlastxLocations()[i];
			int blastXcoordRight = ship.getBlastxLocations()[i] 
					+ ship.getBlastWidth();
			int alienXcoordLeft = alien.getxLocation();
			int alienXcoordRight = alien.getxLocation() + alien.getWidth();
			
			int blastYcoordTop = ship.getBlastyLocations()[i];
			int blastYcoordBottom = ship.getBlastyLocations()[i] 
					+ ship.getBlastHeight();
			int alienYcoordTop = alien.getyLocation();
			int alienYcoordBottom = alien.getyLocation() + alien.getWidth();
			
			if ((blastXcoordLeft < alienXcoordRight 
					&& blastXcoordRight > alienXcoordLeft) 
					&& (blastYcoordTop < alienYcoordBottom 
							&& blastYcoordBottom 
							> alienYcoordTop)
					&& ship.isFiring()) {
				ship.cancelBlast(i, false);
				ScorePanel.setFireButtonColor(Color.RED);
				alien.decreaseHealth();
			}
		}
	}
	/**
	 * Checks if blast hits a boss.
	 * @param boss Boss being checked.
	 */
	public void checkIfBlastHit(final Boss boss) {
		for (int i = 0; i < ship.getBlastxLocations().length; i++) {
			int blastXcoordLeft = ship.getBlastxLocations()[i];
			int blastXcoordRight = ship.getBlastxLocations()[i] 
					+ ship.getBlastWidth();
			int bossXcoordLeft = boss.getxLocation();
			int bossXcoordRight = boss.getxLocation() + boss.getWidth();
			
			int blastYcoordTop = ship.getBlastyLocations()[i];
			int blastYcoordBottom = ship.getBlastyLocations()[i] 
					+ ship.getBlastHeight();
			int alienYcoordTop = boss.getyLocation();
			int alienYcoordBottom = boss.getyLocation() + boss.getHeight();
			
			if ((blastXcoordLeft < bossXcoordRight 
					&& blastXcoordRight > bossXcoordLeft) 
					&& (blastYcoordTop < alienYcoordBottom 
							&& blastYcoordBottom 
							> alienYcoordTop)
					&& ship.isFiring()) {
				ship.cancelBlast(i, true);
				ScorePanel.setFireButtonColor(Color.RED);
				// Decreases boss's health by 3 if lazer blast
				if (ship.getBlastType() == BlastTypes.LAZER) {
					for (int j = 0; j < 3; j++) {
						boss.decreaseHealth();
					}
				} else {
					boss.decreaseHealth();
				}
			}
		}
	}
	/**
	 * Checks if ship hits power up.
	 * @param powerUp Power up to be checked.
	 * @return whether power up was hit.
	 */
	public boolean checkPowerUp(final PowerUp powerUp) {
		int shipXcoordLeft = ship.getxLocation();
		int shipXcoordRight = ship.getxLocation() + ship.getWidth();
		int powerUpXcoordLeft = powerUp.getxLocation();
		int powerUpXcoordRight = powerUp.getxLocation() + powerUp.getSize();
		
		int shipYcoordTop = ship.getyLocation();
		int shipYcoordBottom = ship.getyLocation() + ship.getHeight();
		int powerUpYcoordTop = powerUp.getyLocation();
		int powerUpYcoordBottom = powerUp.getyLocation() 
				+ powerUp.getSize();
		
		if ((shipXcoordLeft < powerUpXcoordRight 
				&& shipXcoordRight > powerUpXcoordLeft) 
				&& (shipYcoordTop < powerUpYcoordBottom 
					&& shipYcoordBottom > powerUpYcoordTop)) {
			return true;
		}
		return false;
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
	 * @return Whether game is being played currently.
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
}
