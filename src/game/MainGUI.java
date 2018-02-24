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

/**
 * This class initiates the main gui frame and holds the components 
 * of the game. The main loop is found in this class to update screen
 * as the game is played.
 * @author JustinDowty
 * @author Ted Lang
 * @author Alec Allain
 */
public class MainGUI extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	/**
	 * Instance of OrbitGame to run game engine.
	 */
	private OrbitGame game;

	/**
	 * Constructor initializes new game and adds components to frame.
	 */
	public MainGUI() {
		game = new OrbitGame();     
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
	
	/**
	 * Sets up key listener for frame. Keys are used to control game 
	 * interaction.
	 */
	public void setUpKeyListener() {
		this.addKeyListener(new KeyListener() {
        	@Override
        	public void keyPressed(final KeyEvent e) {
        		if (e.getKeyCode() == 39) { // Right arrow
        			game.setCurrentKey('R');
        		} else if (e.getKeyCode() == 37) { // Left arrow
        			game.setCurrentKey('L');
        		} else if (e.getKeyCode() == 38) { // Up arrow
        			game.setCurrentKey('U');
        	    } else if (e.getKeyCode() == 40) { // Down arrow
        	        game.setCurrentKey('D');
        	    } else if (e.getKeyCode() == 32) { // Space bar
        	    	game.getShip().initiateBlast();
        	    }
        	}
        	@Override
        	public void keyTyped(final KeyEvent e) {        	}
        	@Override
        	public void keyReleased(final KeyEvent e) {
        		game.setCurrentKey('S');
        	}
        });
	}
	
	/**
	 * Begins the loop to run game. Game is updated and repainted
	 * every 11ms. The lostDialog is called when game ends and loop 
	 * is broken.
	 */
	public void beginGame() {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				game.reset();
				int currTime = 0;
		        while (game.getPlaying()) {
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
	
	/**
	 * Called when game ends. Prompts user to retry, view menu, or exit.
	 */
	public void lostDialog() {
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
		if (n == JOptionPane.YES_OPTION) {
			beginGame();
		} else if (n == JOptionPane.NO_OPTION) {
			System.exit(0);
		} else if (n == JOptionPane.CANCEL_OPTION) {
			this.dispose();
			StartMenu m = new StartMenu();
		}
	}
}
