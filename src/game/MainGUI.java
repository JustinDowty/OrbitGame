package game;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class MainGUI extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JMenuItem restartItem;
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private OrbitGame game;

	public MainGUI(){
		game = new OrbitGame();
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        restartItem = new JMenuItem("Restart");
        restartItem.addActionListener(this);
        this.setJMenuBar(menuBar);
        fileMenu.add(restartItem);
        menuBar.add(fileMenu);
        
        JPanel scorePanel = new ScorePanel();
        this.setLayout(new BorderLayout());
        this.add(scorePanel, BorderLayout.EAST);
        this.add(game, BorderLayout.CENTER);
        this.setFocusable(true);
        this.setUpKeyListener();
        this.setSize(OrbitGame.WINDOW_WIDTH + 300, OrbitGame.WINDOW_HEIGHT);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setTitle("ORBIT");
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);        
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == restartItem){	
			game.playing = false;
			//this.beginGame();
			StartMenu m = new StartMenu();
			//this.dispose();			
		}
	}
	
	public void setUpKeyListener(){
		this.addKeyListener(new KeyListener(){
        	@Override
        	public void keyPressed(KeyEvent e){
        		if(e.getKeyCode() == 39){ // Right arrow
        			OrbitGame.currentKey = 'R';
        		}
        		else if(e.getKeyCode() == 37){ // Left arrow
        			OrbitGame.currentKey = 'L';
        		}
        		else if (e.getKeyCode()==38){ // Up arrow
        			OrbitGame.currentKey = 'U';
        	    }
        	    else if (e.getKeyCode()==40){ // Down arrow
        	        OrbitGame.currentKey = 'D';
        	    }
        	    else if (e.getKeyCode()==32){ // Space bar
        	    	OrbitGame.ship.initiateBlast();
        	    }
        	}
        	@Override
        	public void keyTyped(KeyEvent e) {        	}
        	@Override
        	public void keyReleased(KeyEvent e) {
        		OrbitGame.currentKey = 'S';
        	}
        });
	}
	
	// Resets game 
	public void beginGame(){
		ScorePanel.resetMeteorsDodged();
		game.reset();      
		game.setVisible(true);
        int currTime = 0;
        game.playing = true;
        game.repaint();
        while (game.playing) {
            game.update(currTime);
            game.repaint();
            currTime += 10;
			try {
				Thread.sleep(10);
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        /**
         * !!!CHECK HERE!!!
         * If current gui is closed and new gui is opened here, in the exact way it is in the main,
         * it opens just fine. Removing the dialog all together and having it respawn immediately after
         * losing works just fine. But comment out MainGUI gui... and gui.beginGame...
         * and let the StartMenu g pop up run, which calls those same two lines to generate a new game,
         * this is what won't work and is probably necessary to start games from various menus and such
         */
        int c = JOptionPane.showConfirmDialog(this, "Retry?", "GAME OVER", JOptionPane.YES_NO_OPTION);
        if(c == JOptionPane.YES_OPTION){
        	this.dispose();
            //MainGUI gui = new MainGUI();
            //gui.beginGame();
        	StartMenu g = new StartMenu();
        }
        else if(c == JOptionPane.NO_OPTION) {
        	System.exit(0);
        }

	}
	
	
}
