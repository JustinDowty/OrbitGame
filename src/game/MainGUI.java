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
	private static final long serialVersionUID = 1L;
	private JMenuItem restartItem;
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private OrbitGame game;
	public static MainGUI gui;

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
			gui.beginGame();
		}
	}
	
	public void setUpKeyListener(){
		this.addKeyListener(new KeyListener(){
        	@Override
        	public void keyPressed(KeyEvent e){
        		if(e.getKeyCode() == 39){ // Right arrow
        			game.currentKey = 'R';
        		}
        		else if(e.getKeyCode() == 37){ // Left arrow
        			game.currentKey = 'L';
        		}
        		else if (e.getKeyCode()==38){ // Up arrow
        			game.currentKey = 'U';
        	    }
        	    else if (e.getKeyCode()==40){ // Down arrow
        	        game.currentKey = 'D';
        	    }
        	    else if (e.getKeyCode()==32){ // Space bar
        	    	game.ship.initiateBlast();
        	    }
        	}
        	@Override
        	public void keyTyped(KeyEvent e) {        	}
        	@Override
        	public void keyReleased(KeyEvent e) {
        		game.currentKey = 'S';
        	}
        });
	}
	
	// Resets game 
	public void beginGame(){
		Runnable r = new Runnable(){
			@Override
			public void run() {
				game.reset();
				int currTime = 0;
		        while (game.playing) {
		            game.update(currTime);
		            game.repaint();
		            ScorePanel.updateScore();
		            currTime += 10;
					try {
						Thread.sleep(11);
					} catch (Exception e) {
						e.printStackTrace();
					}
		        }
		        lostDialog();
			}			
		};
		Thread t = new Thread(r);
		t.start();
	}
	
	public void lostDialog(){
		Object[] options = {"Let's Go!",
                "No way.",
                "Menu"};
		int n = JOptionPane.showOptionDialog(this,
				"Try Again?",
						"GAME OVER",
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.PLAIN_MESSAGE,
						null,
						options,
						null);
		if(n == JOptionPane.YES_OPTION){
			gui.beginGame();
		}
		else if(n == JOptionPane.NO_OPTION){
			System.exit(0);
		}
		else if(n == JOptionPane.CANCEL_OPTION){
			this.dispose();
			StartMenu m = new StartMenu();
		}
	}
}
