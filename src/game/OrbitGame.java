package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import javax.swing.JPanel;

public class OrbitGame extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int WINDOW_WIDTH = 1100;
	public static final int WINDOW_HEIGHT = 800;
	public final static int MARGIN = 400; // The margin from edge of screen to maximum meteor x location
	private ArrayList<Meteor> meteorArray;
	public static Ship ship;	
	private static Planet planet;
	public static char currentKey;
	public boolean playing;
	
	// Used for double buffering graphics
	private Graphics dbg;
	private Image dbImage;
	
	public OrbitGame(){
		reset();
		this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		this.setBackground(Color.BLACK);
	}
	
	public void reset(){
		planet = new Planet();
		ship = new Ship();
		meteorArray = new ArrayList<Meteor>();
		ScorePanel.resetMeteorsDodged();
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
		planet.paintComponent(g);
		for(Meteor meteor : meteorArray){
			meteor.paint(g);
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
			meteorArray.add(new Meteor());
		}
		for(Meteor meteor : meteorArray){
			meteor.update(currTime);
			checkIfShipHit(meteor);		
			checkIfBlastHit(meteor);
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
		planet.drift(currTime);
	}
	
	// Uses the ship and meteor coordinates to calculate when a meteor hits the ship
	public void checkIfShipHit(Meteor meteor){
		int shipXcoordLeft = ship.getxLocation();
		int shipXcoordRight = ship.getxLocation() + ship.width;
		int meteorXcoordLeft = meteor.getxLocation();
		int meteorXcoordRight = meteor.getxLocation() + meteor.getSize();
		
		int shipYcoordTop = ship.getyLocation();
		int shipYcoordBottom = ship.getyLocation() + ship.height;
		int meteorYcoordTop = meteor.getyLocation();
		int meteorYcoordBottom = meteor.getyLocation() + meteor.getSize();
		
		if((shipXcoordLeft < meteorXcoordRight && shipXcoordRight > meteorXcoordLeft) &&
				(shipYcoordTop < meteorYcoordBottom && shipYcoordBottom > meteorYcoordTop)) {
			playing = false;
		}
	}
	
	public void checkIfBlastHit(Meteor meteor){
		int blastXcoordLeft = ship.getBlastxLocation();
		int blastXcoordRight = ship.getBlastxLocation() + ship.width/2;
		int meteorXcoordLeft = meteor.getxLocation();
		int meteorXcoordRight = meteor.getxLocation() + meteor.getSize();
		
		int blastYcoordTop = ship.getBlastyLocation();
		int blastYcoordBottom = ship.getBlastyLocation() + ship.height/2;
		int meteorYcoordTop = meteor.getyLocation();
		int meteorYcoordBottom = meteor.getyLocation() + meteor.getSize();
		
		if((blastXcoordLeft < meteorXcoordRight && blastXcoordRight > meteorXcoordLeft) &&
				(blastYcoordTop < meteorYcoordBottom && blastYcoordBottom > meteorYcoordTop)) {
			meteor.reset();
			ship.cancelBlast();
			ScorePanel.setFireButtonColor(Color.RED);
		}
	}
	
	
}
