package game;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

/**
 * This class initiates the main gui frame and holds the components 
 * of the game. The main loop is found in this class to update screen
 * as the game is played.
 * @author JustinDowty
 * @author Ted Lang
 * @author Alec Allain
 */
public class MainGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	/**
	 * Instance of OrbitGame to run game engine.
	 */
	private OrbitGame game;
	/**
	 * Window width of game panel.
	 */
	private static final int WINDOW_WIDTH = 1400;
	/**
	 * Window height of game panel.
	 */
	private static final int WINDOW_HEIGHT = 1100;
	/**
	 * Margin from edge of frame to edge of game.
	 */
	private static final int MARGIN = 400; 
	/**
	 * Score Panel instance.
	 */
	private ScorePanel scorePanel;
	/**
	 * Whether or not game is paused.
	 */
	private boolean paused = false;
	/**
	 * Current user initials.
	 */
	private String currPlayer;
	/**
	 * Players current blast type.
	 */
	private BlastTypes blastType;
	/**
	 * Games audio clip.
	 */
	//private Clip clip;
	
	/**
	 * Constructor initializes new game and adds components to frame.
	 * Takes entered initials in constructor.
	 * @param currPlayer current player initials.
	 * @param blastType current player blast type.
	 */	
	public MainGUI(final String currPlayer, final BlastTypes blastType) {
		this.currPlayer = currPlayer;
		this.blastType = blastType;
		game = new OrbitGame(WINDOW_WIDTH, WINDOW_HEIGHT, MARGIN, blastType);     
        scorePanel = new ScorePanel(WINDOW_HEIGHT);
        this.setLayout(new BorderLayout());
        this.add(scorePanel, BorderLayout.EAST);
        this.add(game, BorderLayout.CENTER);
        this.setFocusable(true);
        this.setUpKeyListener();
        this.setSize(WINDOW_WIDTH + 300, WINDOW_HEIGHT);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setTitle("ORBIT");
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);    
        //clip = Utils.playSound("MainWAV.wav");
	}
	
	/**
	 * Sets up key listener for frame. Keys are used to control game 
	 * interaction.
	 */
	public void setUpKeyListener() {
		this.addKeyListener(new KeyListener() {
        	@Override
        	public void keyPressed(final KeyEvent e) {
        		if (e.getKeyCode() == 32) { // Space bar
        	    	game.getShip().initiateBlast();
        	    } else if (e.getKeyCode() == 80) { // P for paused
        	    	paused = !paused;
        	    } else if (e.getKeyCode() <= 40 && e.getKeyCode() >= 37) {
        	    	game.setKeydown(e.getKeyCode());
        	    }
        	}
        	@Override
        	public void keyTyped(final KeyEvent e) {        	}
        	@Override
        	public void keyReleased(final KeyEvent e) {
        		if (e.getKeyCode() <= 40 && e.getKeyCode() >= 37) {
        	    	game.setKeyup(e.getKeyCode());
        	    }
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
				game.reset(blastType);
				scorePanel.resetMeteorsDodged();
				scorePanel.resetAliensKilled();
				int currTime = 0;
		        while (game.getPlaying()) {
		        	if (paused) {
		        		try {
		        			Thread.sleep(11);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
		        	} else {
		        		game.update(currTime, scorePanel);
			            game.repaint();
			            scorePanel.updateScore();
			            currTime += 10;
						try {
							Thread.sleep(11);
						} catch (Exception e) {
							e.printStackTrace();
						}
		        	}
		        }
		        //clip.stop();
		        addToScores();
		        addToStats();
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
			new StartMenu();
		}
	}
	
	/**
	 * Adds current score to High Scores, High Scores saves user score.
	 */
	public void addToScores() {
		File file = new File("HighScores.txt");
		if (!file.exists()) {
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter("HighScores.txt"));
				bw.write(currPlayer + " " + scorePanel.getCurrentScore()[0] 
						+ "\r\n");
				bw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} else {
			try {
				String line = null;
				Boolean hasWritten = false;
				BufferedWriter bw = new BufferedWriter(new FileWriter("Temp.txt"));
				BufferedReader br = new BufferedReader(new FileReader("HighScores.txt"));
				int currLine = 0;
				while (currLine < 5) {
					line = br.readLine();
					if (line == null) {
						if (!hasWritten) {
							bw.write(currPlayer + " " 
									+ scorePanel.getCurrentScore()[0] + "\r\n");
						}	
						break;
					}
					String[] splitLine = line.split(" ");
					if (!hasWritten && Integer.parseInt(splitLine[1])
							<= scorePanel.getCurrentScore()[0]) {
						bw.write(currPlayer + " " + scorePanel.getCurrentScore()[0] 
								+ "\r\n");	
						hasWritten = true;
					}
					if (currLine < 4 || !hasWritten) {
						bw.write(line + "\r\n");
					}
					currLine++;
				}
				bw.close();
				br.close();
				File oldFile = new File("HighScores.txt");
				oldFile.delete();
				File tempFile = new File("Temp.txt");
				File newFile = new File("HighScores.txt");
				tempFile.renameTo(newFile);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * Adds to current stats, Stats saves user totalScore totalMeteors totalAliens.
	 */
	public void addToStats() {
		File file = new File("Stats.txt");
		if (!file.exists()) {
			try {
				BufferedWriter bw 
					= new BufferedWriter(new FileWriter("Stats.txt"));
				bw.write(currPlayer + " " 
					+ scorePanel.getCurrentScore()[0] 
					+ " " + scorePanel.getCurrentScore()[1] 
					+ " " + scorePanel.getCurrentScore()[2] + "\r\n");
				bw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} else {
			try {
				String line = null;
				Boolean found = false;
				Boolean skip = false;
				BufferedWriter bw = new BufferedWriter(new FileWriter("Temp2.txt"));
				BufferedReader br = new BufferedReader(new FileReader("Stats.txt"));
				while ((line = br.readLine()) != null) {					
					String[] splitLine = line.split(" ");
					if (currPlayer.equals(splitLine[0])) {
						int newTotalScore = scorePanel.getCurrentScore()[0] 
								+ Integer.parseInt(splitLine[1]);
						int newTotalMeteors = scorePanel.getCurrentScore()[1]
								+ Integer.parseInt(splitLine[2]);
						int newTotalAliens = scorePanel.getCurrentScore()[2]
								+ Integer.parseInt(splitLine[3]);
						bw.write(currPlayer + " " + newTotalScore 
								+ " " + newTotalMeteors 
								+ " " + newTotalAliens + "\r\n");	
						found = true;
						skip = true;
					}
					if (!skip) {
						bw.write(line + "\r\n");
					}
					skip = false;
				}
				if (!found) {
					bw.write(currPlayer + " " + scorePanel.getCurrentScore()[0] 
							+ " " + scorePanel.getCurrentScore()[1] 
							+ " " + scorePanel.getCurrentScore()[2] + "\r\n");	
				}
				bw.close();
				br.close();
				File oldFile = new File("Stats.txt");
				oldFile.delete();
				File tempFile = new File("Temp2.txt");
				File newFile = new File("Stats.txt");
				tempFile.renameTo(newFile);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
