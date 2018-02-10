package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Meteor extends JPanel{
	private int xLocation;
	private int yLocation;
	private int initxLocation;
	private int ySpeed;
	private double xSpeed;
	private Dimension size;
	private Random rand = new Random();
	
	private ImageIcon i;
	
	public Meteor(){
		reset();
	}
	
	// Sets random x location, resets y to above screen height
	public void setLocation(){
		this.xLocation = rand.nextInt((int) (OrbitGame.WINDOW_WIDTH - OrbitGame.MARGIN - size.getWidth())); // Subtracts end margin (room for movement) and meteor size
		this.initxLocation = xLocation;
		this.yLocation = rand.nextInt(100);
	}
	
	// Sets random speed
	//!!! ADD SPEED INCREMENT HERE AND IN RESET
	public void setSpeed(){
		this.ySpeed = rand.nextInt(1) + 4;
		this.xSpeed = 30;
	}
	
	// Sets random size
	public void setSize(){
		this.size = new Dimension(rand.nextInt(40) + 10, rand.nextInt(40) + 10);
	}
	
	// Paints meteor
	public void paintComponent(Graphics g){
		i.paintIcon(this, g, xLocation, yLocation);
	}
	
	// Resets the meteors position, size, and speed
	public void reset(){
		this.setSize();
		this.setLocation();
		this.setSpeed();
		i = new ImageIcon("meteor.png");
		Image image = i.getImage(); // transform it
		Image newimg = image.getScaledInstance(this.getWidth(), this.getWidth(),  java.awt.Image.SCALE_SMOOTH); 
		i = new ImageIcon(newimg);
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
	
	public int getWidth(){
		return (int) size.getWidth();
	}
}
