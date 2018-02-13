package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

public class OrbitGame extends JPanel{
	private static final long serialVersionUID = 1L;
	public static final int WINDOW_WIDTH = 1100;
	public static final int WINDOW_HEIGHT = 800;
	public final static int MARGIN = 400; // The margin from edge of screen to maximum meteor x location
	private ArrayList<Meteor> meteorArray;
	private ArrayList<Alien> alienArray;
	public Ship ship;	
	private Planet planet;
	public char currentKey;
	public boolean playing;
	private int hitTime = 0;
	
	private Random rand = new Random();
	private int[] starLocations;
	
	// Used for double buffering graphics
	private Graphics dbg;
	private Image dbImage;
	
	public OrbitGame(){
		reset();
		generateStars();
		this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		this.setBackground(Color.BLACK);
	}
	
	public void reset(){
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
	
	//Double buffering
	public void paint(Graphics g){
		dbImage = createImage(WINDOW_WIDTH, WINDOW_HEIGHT);
		dbg= dbImage.getGraphics();
		paintComponent(dbg);
		g.drawImage(dbImage, 0, 0, this);
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);	
		g.setColor(Color.DARK_GRAY);
		for(int i = 0; i < starLocations.length; i +=4){
			g.fillOval(starLocations[i], starLocations[i+1], starLocations[i+2], starLocations[i+3]);
		}
		planet.paintComponent(g);
		for(Alien alien : alienArray){
			alien.paintComponent(g);
		}
		for(Meteor meteor : meteorArray){
			meteor.paintComponent(g);
		}
		ship.paintComponent(g);
		// Drawing small tick showing ship bounds next to planet
		g.setColor(Color.LIGHT_GRAY);
		g.drawLine(410, 900, 410, 730);
	}
	
	public void update(int currTime){
		// Adds a new meteor to game incrementally until enough are in game,
		// those 20 get recycled
		if(currTime % 500 == 0 && meteorArray.size() < 10){
			//meteorArray.add(new Meteor());
		}
		if(currTime % 50 == 0 && alienArray.size() < 2){
			alienArray.add(new Alien());
		}
		for(Meteor meteor : meteorArray){
			meteor.update(currTime);
			if(checkIfShipHit(meteor)){
				ship.healing = true;
				hitTime = currTime;
			}
			checkIfBlastHit(meteor);
		}
		for(Alien alien : alienArray) {
			alien.update(currTime);
			if(checkIfShipHit(alien)){
				ship.healing = true;
				hitTime = currTime;
			}
			checkIfBlastHit(alien);
		}
		if(currTime - hitTime > 1000 && ship.healing){
			ship.healing = false;
		}
		if(currentKey == 'L'){
			ship.moveLeft();
		}
		if(currentKey == 'R'){
			ship.moveRight();
		}
		if(currentKey == 'U'){
			ship.moveUp();
		}
		if(currentKey == 'D'){
			ship.moveDown();
		}
		ship.updateBlast();
		changeFireButtonColor();
		planet.drift(currTime);
		moveStars();
	}
	
	// Uses the ship and meteor coordinates to calculate when a meteor hits the ship
	public boolean checkIfShipHit(Meteor meteor){
		int shipXcoordLeft = ship.getxLocation() + 5;
		int shipXcoordRight = ship.getxLocation() + ship.width - 5;
		int meteorXcoordLeft = meteor.getxLocation();
		int meteorXcoordRight = meteor.getxLocation() + meteor.getWidth();
		
		int shipYcoordTop = ship.getyLocation() - 5;
		int shipYcoordBottom = ship.getyLocation() + ship.height;
		int meteorYcoordTop = meteor.getyLocation();
		int meteorYcoordBottom = meteor.getyLocation() + meteor.getWidth() - 15;
		
		if((shipXcoordLeft < meteorXcoordRight && shipXcoordRight > meteorXcoordLeft) &&
				(shipYcoordTop < meteorYcoordBottom && shipYcoordBottom > meteorYcoordTop)
				&& !ship.healing) {
			if(ship.decreaseHealth() == 0){
				playing = false;
			}
			return true;
		}
		return false;
	}
	
	// returns true if ship is hit
	public boolean checkIfShipHit(Alien alien){
		int shipXcoordLeft = ship.getxLocation() + 5;
		int shipXcoordRight = ship.getxLocation() + ship.width - 5;
		int alienXcoordLeft = alien.getxLocation();
		int alienXcoordRight = alien.getxLocation() + alien.getWidth();
		int blastXcoordLeft = alien.getBlastxLocation();
		int blastXcoordRight = alien.getBlastxLocation() + 20;
		
		int shipYcoordTop = ship.getyLocation() + 15;
		int shipYcoordBottom = ship.getyLocation() + ship.height;
		int alienYcoordTop = alien.getyLocation();
		int alienYcoordBottom = alien.getyLocation() + alien.getWidth() - 15;
		int blastYcoordTop = alien.getBlastyLocation();
		int blastYcoordBottom = alien.getBlastyLocation() + 20;
		
		if((shipXcoordLeft < alienXcoordRight && shipXcoordRight > alienXcoordLeft) &&
				(shipYcoordTop < alienYcoordBottom && shipYcoordBottom > alienYcoordTop)
				&& !ship.healing) {
			if(ship.decreaseHealth() == 0){
				playing = false;
			}
			return true;
		}
		
		if((shipXcoordLeft < blastXcoordRight && shipXcoordRight > blastXcoordLeft) &&
				(shipYcoordTop < blastYcoordBottom && shipYcoordBottom > blastYcoordTop)
				&& !ship.healing) {
			if(ship.decreaseHealth() == 0){
				playing = false;
			}
			return true;
		}
		return false;
	}
	
	public void checkIfBlastHit(Meteor meteor){
		int blastXcoordLeft = ship.getBlastxLocation();
		int blastXcoordRight = ship.getBlastxLocation() + ship.width/2;
		int meteorXcoordLeft = meteor.getxLocation();
		int meteorXcoordRight = meteor.getxLocation() + meteor.getWidth();
		
		int blastYcoordTop = ship.getBlastyLocation();
		int blastYcoordBottom = ship.getBlastyLocation() + ship.height/2;
		int meteorYcoordTop = meteor.getyLocation();
		int meteorYcoordBottom = meteor.getyLocation() + meteor.getWidth();
		
		if((blastXcoordLeft < meteorXcoordRight && blastXcoordRight > meteorXcoordLeft) &&
				(blastYcoordTop < meteorYcoordBottom && blastYcoordBottom > meteorYcoordTop)) {
			meteor.reset();
			ship.cancelBlast();
			ScorePanel.setFireButtonColor(Color.RED);
		}
	}
	
	public void checkIfBlastHit(Alien alien){
		int blastXcoordLeft = ship.getBlastxLocation();
		int blastXcoordRight = ship.getBlastxLocation() + ship.width/2;
		int alienXcoordLeft = alien.getxLocation();
		int alienXcoordRight = alien.getxLocation() + alien.getWidth();
		
		int blastYcoordTop = ship.getBlastyLocation();
		int blastYcoordBottom = ship.getBlastyLocation() + ship.height/2;
		int alienYcoordTop = alien.getyLocation();
		int alienYcoordBottom = alien.getyLocation() + alien.getWidth();
		
		if((blastXcoordLeft < alienXcoordRight && blastXcoordRight > alienXcoordLeft) &&
				(blastYcoordTop < alienYcoordBottom && blastYcoordBottom > alienYcoordTop)) {
			ship.cancelBlast();
			ScorePanel.setFireButtonColor(Color.RED);
			alien.decreaseHealth();
		}
	}
	
	public void changeFireButtonColor(){
		if(ship.firing){
			ScorePanel.setFireButtonColor(Color.LIGHT_GRAY);
		}
		else{
			ScorePanel.setFireButtonColor(Color.RED);
		}
	}
	
	public void generateStars(){
		starLocations = new int[240];
		for(int i = 0; i < 240; i +=4){
			int size = rand.nextInt(20) + 10;
			starLocations[i] = rand.nextInt(1100);
			starLocations[i+1] = rand.nextInt(800);
			starLocations[i+2] = size/2;
			starLocations[i+3] = size;
		}
	}
	
	public void moveStars(){
		for(int i = 0; i < starLocations.length; i+=4){
			if(starLocations[i+1] > 805){
				starLocations[i] = rand.nextInt(1100);
				starLocations[i+1] = -5;
			}
			else{
				starLocations[i+1] += 2;
			}
		}
	}
}
