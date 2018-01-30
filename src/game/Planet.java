package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

public class Planet {
	private Polygon land;
	private Color waterColor;
	private Color landColor;
	private int xLocation;
	private int yLocation;
	private int driftSpeed;
	
	public Planet(){
		this.driftSpeed = 1;
		this.xLocation = -300;
		this.yLocation = OrbitGame.WINDOW_HEIGHT - 400;
		
		int[] xCoords = {0, 10, 20, 100, 180, 260, 300, 10};
		int[] yCoords = {900, 600, 600, 500, 600, 500, 650, 900};
		int points = 8;
		land = new Polygon(xCoords, yCoords, points);	
		this.waterColor = Utils.chooseRandomColor();
		this.landColor = Utils.chooseRandomColor();
		while(waterColor == landColor){
			this.waterColor = Utils.chooseRandomColor();
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
}
