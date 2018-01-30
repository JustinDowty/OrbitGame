package game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Meteor {
	private int xLocation;
	private int yLocation;
	private int initxLocation;
	private int ySpeed;
	private double xSpeed;
	private int size;
	private Random rand = new Random();
	
	public Meteor(){
		reset();
	}
	
	// Sets random x location, resets y to above screen height
	public void setLocation(){
		this.xLocation = rand.nextInt(OrbitGame.WINDOW_WIDTH - OrbitGame.MARGIN - size); // Subtracts end margin (room for movement) and meteor size
		this.initxLocation = xLocation;
		this.yLocation = rand.nextInt(100);
	}
	
	// Sets random speed
	//!!! ADD SPEED INCREMENT HERE AND IN RESET
	public void setSpeed(){
		this.ySpeed = rand.nextInt(10) + 5;
		this.xSpeed = 30;
	}
	
	// Sets random size
	public void setSize(){
		this.size = rand.nextInt(40) + 10;
	}
	
	// Paints meteor
	public void paint(Graphics g){
		g.setColor(Color.RED);
		g.fillRect(xLocation, yLocation, size, size);
	}
	
	// Resets the meteors position, size, and speed
	public void reset(){
		this.setLocation();
		this.setSpeed();
		this.setSize();
	}
	
	// Updates meteors position, resetting if past the bottom of screen and it is time for a new meteor to spawn
	public void update(int currTime){
		if(yLocation <= OrbitGame.WINDOW_HEIGHT){
			yLocation += ySpeed;
		}
		if(yLocation <= OrbitGame.WINDOW_HEIGHT / 2 && xLocation < initxLocation + OrbitGame.MARGIN){ // additional 100 to assure edge is covered
			xLocation += xSpeed;
			xSpeed -= 2 * xSpeed / 30;
		}
		// Resets meteor at random
		int r = rand.nextInt(1000);
		if(yLocation > OrbitGame.WINDOW_HEIGHT && 
				(currTime % 1000 > r && currTime % 1000 < r + 200)){ 
			this.reset();
			ScorePanel.incrementMeteorsDodged();
		}
	}
	
	public int getxLocation(){
		return this.xLocation;
	}
	
	public int getyLocation(){
		return this.yLocation;
	}
	
	public int getSize(){
		return this.size;
	}
}
