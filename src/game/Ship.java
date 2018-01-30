package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;

public class Ship{
	private int xLocation;
	private int yLocation;
	private int blastxLocation;
	private int blastyLocation;
	private boolean canFire = true;
	private boolean firing; // Tells if blast is firing
	private static final int speed = 15;
	// Change publics to final and add getter method if necessary,
	// if not make variables final
	public int width = 20;
	public int height = 40;
	
	public Ship(){
		setLocation();
	}
	
	public void setLocation(){
		this.xLocation = OrbitGame.WINDOW_WIDTH / 2 + OrbitGame.MARGIN;
		this.yLocation = OrbitGame.WINDOW_HEIGHT - 3 * height;
		this.blastxLocation = xLocation + width/2;
		this.blastyLocation = OrbitGame.WINDOW_HEIGHT - 3*height;
	}
	
	public void paintComponent(Graphics g){
		g.setColor(Color.YELLOW);
		g.fillRect(blastxLocation, blastyLocation, width/2, height); // Blast is 1/2 the width and height of ship
		g.setColor(Color.CYAN);
		g.fillRect(xLocation, yLocation, width, height);
	}
	
	public void moveRight(){
		if(xLocation < OrbitGame.WINDOW_WIDTH - 2*width){ // !CHECK VALUE
			xLocation += speed;
		}
		if(!firing){
			this.blastxLocation = xLocation;
		}
	}
	
	public void moveLeft(){
		if(xLocation > OrbitGame.MARGIN + 10){
			xLocation -= speed;
		}
		if(!firing){
			this.blastxLocation = xLocation;
		}
	}
	
	public void moveDown(){
		if(yLocation < OrbitGame.WINDOW_HEIGHT - 3 * height){
			yLocation += speed;
		}
		if(!firing){
			this.blastyLocation = yLocation;
		}
	}
	
	public void moveUp(){
		if(yLocation > 0){
			yLocation -= speed;
		}
		if(!firing){
			this.blastyLocation = yLocation;
		}
	}
	
	public void initiateBlast(){
		if(this.canFire){
			this.blastyLocation = yLocation; // puts blast at ships position before firing
			this.firing = true; // allowing blast to fly
			this.canFire = false; // so you have to wait to fire again
			ScorePanel.setFireButtonColor(Color.LIGHT_GRAY); // sets fire button to show cannot fire
		}
	}
	
	public void updateBlast(){
		if(this.firing && this.blastyLocation > -20){
			this.blastyLocation -= 10;
		}
		else{
			this.firing = false; // done with blast path
			this.canFire = true; // can fire now
			this.blastyLocation = OrbitGame.WINDOW_HEIGHT; // puts blast off screen
			ScorePanel.setFireButtonColor(Color.RED); // sets fire button to show can fire
		}
	}
	
	public void cancelBlast(){
		this.firing = false;
	}
	
	public int getxLocation(){
		return this.xLocation;
	}
	
	public int getyLocation(){
		return this.yLocation;
	}
	
	public int getBlastxLocation(){
		return this.blastxLocation;
	}
	
	public int getBlastyLocation(){
		return this.blastyLocation;
	}
}
