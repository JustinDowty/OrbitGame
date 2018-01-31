package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class OrbitGame extends JPanel{
	public static final int WINDOW_WIDTH = 1100;
	public static final int WINDOW_HEIGHT = 800;
	public final static int MARGIN = 400; // The margin from edge of screen to maximum meteor x location
	private ArrayList<Meteor> meteorArray = new ArrayList<Meteor>();
	private static Ship ship = new Ship();	
	private static Planet planet = new Planet();
	private static char currentKey;
	private static boolean playing;
	
	// Used for double buffering graphics
	private Graphics dbg;
	private Image dbImage;
	
	public OrbitGame(){
		// Adds one meteor to start game off
		meteorArray.add(new Meteor());
		this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		this.setBackground(Color.BLACK);
	}
	
	public void paint(Graphics g){
		dbImage = createImage(WINDOW_WIDTH, WINDOW_HEIGHT);
		dbg= dbImage.getGraphics();
		paintComponent(dbg);
		g.drawImage(dbImage, 0, 0, this);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);		
		for(Meteor meteor : meteorArray){
			meteor.paint(g);
		}		
		planet.paintComponent(g);
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
	
	public static void setUpKeyListener(JFrame frame){
		frame.addKeyListener(new KeyListener(){
        	@Override
        	public void keyPressed(KeyEvent e){
        		if(e.getKeyCode() == 39){ // Right arrow
        			currentKey = 'R';
        		}
        		else if(e.getKeyCode() == 37){ // Left arrow
        			currentKey = 'L';
        		}
        		else if (e.getKeyCode()==38){ // Up arrow
        			currentKey = 'U';
        	    }
        	    else if (e.getKeyCode()==40){ // Down arrow
        	        currentKey = 'D';
        	    }
        	    else if (e.getKeyCode()==32){ // Space bar
        	    	ship.initiateBlast();
        	    }
        	}
        	@Override
        	public void keyTyped(KeyEvent e) {        	}
        	@Override
        	public void keyReleased(KeyEvent e) {
        		currentKey = 'S';
        	}
        });
	}
	
	/*
	 * This static method begins game. To call in other class beginning the game
	 * use OrbitGame.beginGame()
	 * This function creates OrbitGame object and frame, starting play
	 */
	public static void beginGame() throws InterruptedException{
		OrbitGame game = new OrbitGame();
        JFrame frame = new JFrame();
        JPanel scorePanel = new ScorePanel();
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(game, BorderLayout.CENTER);
        mainPanel.add(scorePanel, BorderLayout.EAST);
        frame.add(mainPanel);
        setUpKeyListener(frame);
        frame.setVisible(true);
        frame.setSize(WINDOW_WIDTH + 300, WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("ORBIT");
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        int currTime = 0;
        playing = true;
        while (playing) {
            game.update(currTime);
            game.repaint();
            currTime += 10;
            Thread.sleep(10);
        }
	}
	
	/*
	 * Main function is calling beginGame() to begin game for testing
     * Menu class will ball beginGame() and this main function will be
	 * deleted in this case
	 */
	public static void main(String[] args) throws InterruptedException {
        beginGame();
    }	
}
