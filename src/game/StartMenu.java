package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JLabel;


/**
 * This class generates the start menu for 
 * the game. 
 * @author Alec Allain
 * @author Justin Dowty
 * @author Ted Lang
 */
public class StartMenu extends JFrame implements ActionListener {
	/**
	 * Width of menu.
	 */
	private int width = 600;
	/**
	 * Height of menu.
	 */
	private int height = 500;
	/**
	 * Start button.
	 */
	private JButton start;
	/**
	 * Stats button.
	 */
	private JButton stats;
	/**
	 * Quit button.
	 */
	private JButton quit;
	/**
	 * Upgrade button.
	 */
	private JButton upgrade;
	/**
	 * High Scores button.
	 */
	private JButton scores;
	/**
	 * Panel for start menu.
	 */
	private JPanel panel;
	
	/**
	 * Title Label.
	 */
	private JLabel orbit;
	
	/**
	 * Current player initials.
	 */
	private String currPlayer;
	/**
	 * Current players total score.
	 */
	private int currPlayerScore = 0;
	/**
	 * Current players total meteors dodged.
	 */
	private int currPlayerMeteors = 0;
	/**
	 * Current players total aliens killed.
	 */
	private int currPlayerAliens = 0;
	/**
	 * The current players blast type.
	 */
	private BlastTypes blastType = BlastTypes.STANDARD;
	/**
	 * Background star locations.
	 */
	private int[] starLocations;
	
	/**
	 * Constructor for class sets up menus and displays frame.
	 */
	public StartMenu() {
		setupMainMenu();
		enterInitialsDialog();
		readStats();
		Utils.starLoop(panel, starLocations, width, height);
	}

	/**
	 * This method sets up the start menu.
	 */
	public void setupMainMenu() {
		starLocations = Utils.generateStars(2, width, height);
		panel = new JPanel() {
			private static final long serialVersionUID = 1L;
			/**
			 * Paints the background of panel.
			 */
			public void paint(final Graphics g) {
				super.paint(g);
				g.setColor(Color.DARK_GRAY);
				for (int i = 0; i < starLocations.length; i += 4) {
					g.fillOval(starLocations[i], 
							starLocations[i + 1], 
							starLocations[i + 2], 
							starLocations[i + 3]);
				}
			}
		};
		
		panel.setLayout(new GridBagLayout());
		GridBagConstraints g = new GridBagConstraints();
		g.gridwidth = GridBagConstraints.REMAINDER;
		g.fill = GridBagConstraints.HORIZONTAL;
		g.gridx = 0;
		
		orbit = new JLabel("ORBIT", JLabel.CENTER);
		orbit.setFont(new Font("Rockwell", Font.BOLD, 100));
		orbit.setForeground(Color.RED);
		
		start = new JButton("Start");
		stats = new JButton("Statistics");
		upgrade = new JButton("Upgrades");
		scores = new JButton("High Scores");
		quit = new JButton("Quit");
		
		start.setBackground(Color.CYAN);
		stats.setBackground(Color.CYAN);
		upgrade.setBackground(Color.GREEN);
		scores.setBackground(Color.GREEN);
		quit.setBackground(Color.RED);
		
		start.setFont(new Font("Bold", Font.BOLD, 18));
		stats.setFont(new Font("Bold", Font.BOLD, 18));
		upgrade.setFont(new Font("Bold", Font.BOLD, 18));
		scores.setFont(new Font("Bold", Font.BOLD, 18));
		quit.setFont(new Font("Bold", Font.BOLD, 18));
		
		start.addActionListener(this);
		stats.addActionListener(this);
		upgrade.addActionListener(this);
		scores.addActionListener(this);
		quit.addActionListener(this);
		
		panel.add(orbit);
		panel.add(start, g);
		panel.add(stats, g);
		panel.add(upgrade, g);
		panel.add(scores, g);
		panel.add(quit, g);
		panel.setBackground(Color.BLACK);
		this.setResizable(false);
		this.setSize(width, height);
		this.add(panel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Orbit");
		this.setVisible(true);
		this.setFocusable(true);
		this.setLocationRelativeTo(null);
	}
	
	/**
	 * This class listens for button presses.
	 * @param e Instance of ActionEvent.
	 */
	public void actionPerformed(final ActionEvent e) {
		if (e.getSource() == start) {
			this.dispose();
			MainGUI gui = new MainGUI(currPlayer, blastType);
			gui.beginGame();
		}		
		if (e.getSource() == stats) {
			new StatsMenu(currPlayer, currPlayerScore,
					currPlayerMeteors, currPlayerAliens);
		}		
		if (e.getSource() == upgrade) {
			new UpgradesMenu(currPlayer, currPlayerAliens, this);
		}
		if (e.getSource() == scores) {
			new HighScoresMenu();
		}
		if (e.getSource() == quit) {
			System.exit(0);
		}
	}
	
	/**
	 * Initiates dialog to prompt user for initials.
	 */
	public void enterInitialsDialog() {
		currPlayer = JOptionPane.showInputDialog("Enter your initials");
		if (currPlayer == null) {
			System.exit(0);
		}
		while (currPlayer.length() != 3 || currPlayer.charAt(0) == ' '
				|| currPlayer.charAt(1) == ' ' || currPlayer.charAt(2) == ' ') {
			JOptionPane.showMessageDialog(this, "Enter your initials in three digits!");
			currPlayer = JOptionPane.showInputDialog("Enter your initials");
			if (currPlayer == null) {
				System.exit(0);
			}
		}
	}
	
	/**
	 * Reads the current players stats from the Stats.txt save file.
	 */
	public void readStats() {
		File file = new File("Stats.txt");
		if (file.exists()) {
			String line = null;
			BufferedReader br;
			try {
				br = new BufferedReader(new FileReader("Stats.txt"));
				while ((line = br.readLine()) != null) {
					// Splits line into fields 
					String[] splitLine = line.split(" ");
					// Checks if this line (save) is the same as current player
					// if so save that lines stats
					if (currPlayer.equals(splitLine[0])) {
						currPlayerScore = Integer.parseInt(splitLine[1]);
						currPlayerMeteors = Integer.parseInt(splitLine[2]);
						currPlayerAliens = Integer.parseInt(splitLine[3]);
						break; // break when found
					}
				}
				br.close();
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(this, "Load file not found.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Set players blast type.
	 * @param blastType Blast type to be set.
	 */
	public void setBlastType(BlastTypes blastType) {
		this.blastType = blastType;
	}

	/**
	 * Main method begins game.
	 * @param args Unused standard parameter for main class.
	 */
	public static void main(final String[] args) {
		new StartMenu();
		
	}	
}
