package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 * Stats menu displays the current users game stats.
 * @author Justin Dowty
 */
public class StatsMenu extends JFrame {
	/**
	 * The current player.
	 */
	private String player;
	/**
	 * The current players total points.
	 */
	private int score;
	/**
	 * The current players total meteors dodged.
	 */
	private int meteors;
	/**
	 * The current players total aliens killed.
	 */
	private int aliens;
	/**
	 * Background star locations.
	 */
	private int[] starLocations;

	/**
	 * Constructor builds stats menu.
	 * @param player Current player.
	 * @param score Player's total score.
	 * @param meteors Player's meteors dodged.
	 * @param aliens Player's aliens killed.
	 */
	public StatsMenu(final String player, final int score, 
			final int meteors, final int aliens) {
		this.player = player;
		this.score = score;
		this.meteors = meteors;
		this.aliens = aliens;
		setUpMenu();
	}
	
	/**
	 * Sets up the Stats Menu.
	 */
	public void setUpMenu() {
		JPanel panel = new JPanel() {
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
		GridLayout layout = new GridLayout(0, 1);
		panel.setBorder(new LineBorder(Color.BLACK, 20));
		panel.setLayout(layout);
		Font font = new Font("Rockwell", Font.BOLD, 40);
		panel.setBackground(Color.BLACK);
		
		JLabel titleLabel 
			= new JLabel("" + player + "'s STATS:");
		titleLabel.setFont(font);
		titleLabel.setBorder(
				BorderFactory.createEmptyBorder(20, 0, 20, 0));
		titleLabel.setForeground(Color.CYAN);
		panel.add(titleLabel);
		
		JLabel scoreLabel 
			= new JLabel("Total Score: " + score);
		scoreLabel.setFont(font);
		scoreLabel.setForeground(Color.CYAN);
		panel.add(scoreLabel);
		
		JLabel meteorsLabel
			= new JLabel("Meteors Dodged: " + meteors);
		meteorsLabel.setFont(font);
		meteorsLabel.setForeground(Color.CYAN);
		panel.add(meteorsLabel);
		
		JLabel aliensLabel
			= new JLabel("Aliens Killed: " + aliens);
		aliensLabel.setFont(font);
		aliensLabel.setForeground(Color.CYAN);
		panel.add(aliensLabel);
		
		this.add(panel);
		this.pack();
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("Stats");
		this.setVisible(true);
		this.setFocusable(true);
		this.setLocationRelativeTo(null);
		starLocations = Utils.generateStars(2, panel.getWidth(), panel.getHeight());
		Utils.starLoop(panel, starLocations, panel.getWidth(), panel.getHeight());
	}
}
