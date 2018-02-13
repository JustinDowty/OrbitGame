package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Alien extends JPanel{
	private int health;
	private int xLocation;
	private int yLocation;
	private int initxLocation;
	private int ySpeed;
	private double xSpeed;
	private int blastxLocation;
	private int blastyLocation;
	private boolean firing;
	private int blastSpeed;
	private Dimension size;
	private Random rand = new Random();	
	public ImageIcon i;
	
	public Alien(){
		i = new ImageIcon("alien.png");
		this.health = 2;
		this.setSize();
		reset();
	}
	
	// Sets random x location, resets y to above screen height
	public void setLocation(){
		this.xLocation = rand.nextInt((int) (OrbitGame.WINDOW_WIDTH - OrbitGame.MARGIN - this.size.getWidth())); // Subtracts end margin (room for movement) and meteor size
		this.initxLocation = this.xLocation;
		this.yLocation = rand.nextInt(100);
		if(!firing){
			this.blastxLocation = (int) (this.xLocation + this.size.getWidth()/2);
			this.blastyLocation = (int) (this.yLocation + this.size.getHeight()/2);
		}
	}
	
	// Sets random speed
	//!!! ADD SPEED INCREMENT HERE AND IN RESET
	public void setSpeed(){
		this.ySpeed = rand.nextInt(1) + 4;
		this.xSpeed = 30;
		this.blastSpeed = 2 * this.ySpeed;
	}
	
	// Sets random size
	public void setSize(){
		this.size = new Dimension(rand.nextInt(40) + 50, rand.nextInt(40) + 50);
	}
	
	// Paints alien
	public void paintComponent(Graphics g){
		if(this.firing){
			g.setColor(Color.YELLOW);
			g.fillRect(this.blastxLocation, this.blastyLocation, 20, 20);
		}
		i.paintIcon(this, g, xLocation, yLocation);
		g.setColor(Color.GREEN);
		if(this.health == 2){
			g.fillRect(xLocation + getWidth() + 20, yLocation, 5, 50);
		}
		else if(this.health == 1){
			g.fillRect(xLocation + getWidth() + 20, yLocation, 5, 25);
			g.setColor(Color.RED);
			g.fillRect(xLocation + getWidth() + 20, yLocation + 25, 5, 25);
		}
		else{
			g.setColor(Color.RED);
			g.fillRect(xLocation + getWidth() + 20, yLocation, 5, 50);
		}		
	}
	
	// Resets the meteors position and speed
	public void reset(){
		this.setLocation();
		this.setSpeed();
		Image image = i.getImage(); // transform it
		Image newimg = image.getScaledInstance(this.getWidth(), this.getWidth(),  java.awt.Image.SCALE_SMOOTH); 
		i = new ImageIcon(newimg);
	}
	
	// Updates meteors position, resetting if past the bottom of screen and it is time for a new meteor to spawn
	public void update(int currTime){
		int r = rand.nextInt(200) + 5;
		if(this.yLocation <= OrbitGame.WINDOW_HEIGHT){
			this.yLocation += this.ySpeed; // Moves alien
			if(!this.firing){ // Moves blast
				this.blastyLocation += this.ySpeed;
			}
			else{
				this.blastyLocation += this.blastSpeed;
			}
		}
		if(this.yLocation <= OrbitGame.WINDOW_HEIGHT / 2 && this.xLocation < this.initxLocation + OrbitGame.MARGIN){ // additional 100 to assure edge is covered
			this.xLocation += this.xSpeed; // Moves x direction to enter
			if(!this.firing){ // Moves blast
				this.blastxLocation += this.xSpeed;
			}
			this.xSpeed -= 2 * this.xSpeed / 30;
		}
		else if(currTime % r == 0){
			this.firing = true;
		}
		if(yLocation > OrbitGame.WINDOW_HEIGHT){ 
			this.reset();		// Resets the alien when end is reached
		}
		if(blastyLocation > OrbitGame.WINDOW_HEIGHT){
			this.firing = false; // Resets blast when end is reached
			this.blastxLocation = (int) (this.xLocation + this.size.getWidth()/2);
			this.blastyLocation = (int) (this.yLocation + this.size.getHeight()/2);
		}
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
	
	public int getWidth(){
		return (int) this.size.getWidth();
	}
	public void decreaseHealth(){
		this.health--;
		if(this.health == 0){
			this.reset();
			this.setSize();
			this.health = 2;
			ScorePanel.incrementAliensKilled();
		}
	}
}
