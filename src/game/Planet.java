package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.Random;

public class Planet {
	private Polygon land;
	private Color waterColor;
	private Color landColor;
	private int xLocation;
	private int yLocation;
	private int driftSpeed;
	private static Random rand = new Random();
	
	public Planet(){
		this.driftSpeed = 1;
		this.xLocation = -300;
		this.yLocation = OrbitGame.WINDOW_HEIGHT - 400;
		
		int[] xCoords = {0, 10, 20, 100, 180, 260, 300, 10};
		int[] yCoords = {900, 600, 600, 500, 600, 500, 650, 900};
		int points = 8;
		land = new Polygon(xCoords, yCoords, points);	
		this.waterColor = chooseRandomColor();
		this.landColor = chooseRandomColor();
		while(waterColor == landColor){
			this.waterColor = chooseRandomColor();
		}
	}
	
	public void drift(int currTime){
		if(currTime % 100 == 0){
			this.xLocation -= this.driftSpeed;
			//this.yLocation += this.driftSpeed;
			for(int i = 0; i < land.xpoints.length; i ++){
				land.xpoints[i] -= this.driftSpeed;
				//land.ypoints[i] += this.driftSpeed;
			}
		}		
	}
	
	public void paintComponent(Graphics g){
		g.setColor(waterColor);
		g.fillOval(this.xLocation, this.yLocation, 700, 700);
		g.setColor(landColor);
		g.fillPolygon(land);
	}
	
	public void setWaterColor(Color color){
		this.waterColor = color;
	}
	
	public void setLandColor(Color color){
		this.landColor = color;
	}
	
	public static Color chooseRandomColor(){
		int r = rand.nextInt(7);
		Color color = null;
		if(r == 0){
			color = Color.BLUE;
		}
		if(r == 1){
			color = Color.GREEN;
		}
		if(r == 2){
			color = Color.RED;
		}
		if(r == 3){
			color = Color.MAGENTA;
		}
		if(r == 4){
			color = Color.CYAN;
		}
		if(r == 5){
			color = Color.DARK_GRAY;
		}
		if(r == 6){
			color = Color.PINK;
		}
		if(r == 7){
			color = Color.LIGHT_GRAY;
		}
		return color;
	}
}
