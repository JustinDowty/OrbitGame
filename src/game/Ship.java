package game;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Ship extends JPanel{
	private int xLocation;
	private int yLocation;
	private int blastxLocation;
	private int blastyLocation;
	public boolean canFire = true;
	public boolean firing; // Tells if blast is firing
	private static final int speed = 15;
	// Change publics to final and add getter method if necessary,
	// if not make variables final
	public int width = 40;
	public int height = 45;
	private int health;
	public boolean healing;
	private ImageIcon i;
	
	public Ship(){
		this.health = 5;
		this.healing = false;
		setLocation();
	}
	
	public void setLocation(){
		this.xLocation = OrbitGame.WINDOW_WIDTH / 2 + OrbitGame.MARGIN;
		this.yLocation = OrbitGame.WINDOW_HEIGHT - 3 * this.height;
		this.blastxLocation = this.xLocation + width/2;
		this.blastyLocation = OrbitGame.WINDOW_HEIGHT - 3 * this.height;
	}
	
	public void paintComponent(Graphics g){
		if(this.healing && this.health > 0){
			i = new ImageIcon("shiptransparent.png");
		}
		else{
			i = new ImageIcon("ship.png");
		}
		g.setColor(Color.YELLOW);
		g.fillRect(blastxLocation, blastyLocation, width/2, height); // Blast is 1/2 the width and height of ship
		i.paintIcon(this, g, xLocation, yLocation);
		g.setColor(Color.GREEN);
		g.fillRect(this.xLocation + this.width + 20, this.yLocation + 5, 5, 50);
		g.setColor(Color.RED);
		if(this.health == 4){
			g.fillRect(this.xLocation + this.width + 20, this.yLocation + 5, 5, 10);
		}
		else if(this.health == 3){
			g.fillRect(this.xLocation + this.width + 20, this.yLocation + 5, 5, 20);
		}
		else if(this.health == 2){
			g.fillRect(this.xLocation + this.width + 20, this.yLocation + 5, 5, 30);
		}
		else if(this.health == 1){
			g.fillRect(this.xLocation + this.width + 20, this.yLocation + 5, 5, 40);
		}
		if(this.health == 0){
			g.fillRect(this.xLocation + this.width + 20, this.yLocation + 5, 5, 50);
		}
	}
	
	public void moveRight(){
		if(this.xLocation < OrbitGame.WINDOW_WIDTH - 2*width){ // !CHECK VALUE
			this.xLocation += speed;
		}
		if(!firing){
			this.blastxLocation = this.xLocation;
		}
	}
	
	public void moveLeft(){
		if(this.xLocation > OrbitGame.MARGIN + 10){
			this.xLocation -= speed;
		}
		if(!firing){
			this.blastxLocation = this.xLocation;
		}
	}
	
	public void moveDown(){
		if(this.yLocation < OrbitGame.WINDOW_HEIGHT - 3 * height){
			this.yLocation += speed;
		}
		if(!firing){
			this.blastyLocation = this.yLocation;
		}
	}
	
	public void moveUp(){
		if(this.yLocation > 0){
			this.yLocation -= speed;
		}
		if(!firing){
			this.blastyLocation = this.yLocation;
		}
	}
	
	public void initiateBlast(){
		if(this.canFire){
			this.blastyLocation = this.yLocation; // puts blast at ships position before firing
			this.blastxLocation = this.xLocation;
			this.firing = true; // allowing blast to fly
			this.canFire = false; // so you have to wait to fire again
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
		}
	}
	
	public int decreaseHealth(){
		if(this.health > 0){
			this.health--;
		}
		this.healing = true;
		return this.health;
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
