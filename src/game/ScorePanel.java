package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

/**
 * This class initializes score panel and contains methods to update
 * panel based on current score.
 * @author JustinDowty
 * @author Ted Lang
 * @author Alec Allain
 */
public class ScorePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	/**
	 * Current Meteors dodged.
	 */
	private static int meteorsDodged = 0;
	/**
	 * Current Aliens killed.
	 */
	private static int aliensKilled = 0;
	/**
	 * JLabel for fire button.
	 */
	private static JLabel fireButton;
	/**
	 * JLabel for Meteor count.
	 */
	private static JLabel meteors 
		= new JLabel("0", SwingConstants.CENTER);
	/**
	 * JLabel for Alien count.
	 */
	private static JLabel aliens 
		= new JLabel("0", SwingConstants.CENTER);
	/**
	 * JLabel for score count.
	 */
	private static JLabel score 
		= new JLabel("0", SwingConstants.CENTER);
	
	/**
	 * Initializes score panel with labels.
	 */
	public ScorePanel() {		
		this.setPreferredSize(new Dimension(295, 
				OrbitGame.WINDOW_HEIGHT));
		this.setBackground(Color.CYAN);
		Border border 
			= BorderFactory.createLineBorder(Color.DARK_GRAY, 15);
		Border emptyBorder 
			= BorderFactory.createEmptyBorder(0, 0, 10, 0);
		this.setBorder(border);
		JLabel title = new JLabel("ORBIT");
		Font titleFont = new Font("Impact", Font.BOLD, 80);
		title.setFont(titleFont);
		this.add(title);
		
		JLabel meteorsLabel 
			= new JLabel("<html>METEORS<br/>DODGED:</html>");
		Font subTitleFont = new Font("Impact", Font.BOLD, 40);
		meteorsLabel.setFont(subTitleFont);
		meteorsLabel.setBorder(emptyBorder);
		this.add(meteorsLabel);
		
		meteors.setPreferredSize(new Dimension(190, 60));
		Font scoreFont = new Font("Impact", Font.BOLD, 60);
		meteors.setFont(scoreFont);
		meteors.setBorder(emptyBorder);
		this.add(meteors);
		
		JLabel aliensLabel 
			= new JLabel("<html>ALIENS<br/>KILLED:</html>");
		aliensLabel.setFont(subTitleFont);
		aliensLabel.setBorder(emptyBorder);
		this.add(aliensLabel);
		
		aliens.setPreferredSize(new Dimension(190, 60));
		aliens.setFont(scoreFont);
		aliens.setBorder(emptyBorder);
		this.add(aliens);
		
		JLabel scoreLabel = new JLabel("SCORE:");
		scoreLabel.setFont(subTitleFont);
		scoreLabel.setBorder(emptyBorder);
		this.add(scoreLabel);
		
		score.setPreferredSize(new Dimension(190, 60));
		score.setFont(scoreFont);
		score.setBorder(emptyBorder);
		this.add(score);
		
		fireButton = createFireButton();
		this.add(fireButton);
		
	}
	
	/**
	 * Creates fire button.
	 * @return The fire button to be used in panel.
	 */
	public JLabel createFireButton() {
		Font fireFont = new Font("Impact", Font.BOLD, 40);
		fireButton = new JLabel("FIRE!");
		fireButton.setFont(fireFont);
		fireButton.setForeground(Color.LIGHT_GRAY);
		return fireButton;
	}

	/**
	 * Increments number of meteors dodged, updates text field.
	 */
	public static void incrementMeteorsDodged() {
		meteorsDodged++;
		meteors.setText("" + meteorsDodged);
	}
	
	/**
	 * Resets meteors dodged to 0, updates text field.
	 */
	public static void resetMeteorsDodged() {
		meteorsDodged = 0;
		meteors.setText("0");
	}
	
	/**
	 * Increments aliens killed, updates text field.
	 */
	public static void incrementAliensKilled() {
		aliensKilled++;
		aliens.setText("" + aliensKilled);
	}
	
	/**
	 * Resets aliens killed to 0, updates text field.
	 */
	public static void resetAliensKilled() {
		aliensKilled = 0;
		aliens.setText("0");
	}
	
	/**
	 * Updates score text field. Score is meteors dodged times a 
	 * multiplier of aliens killed.
	 */
	public static void updateScore() {
		int s;
		if (aliensKilled == 0) {
			s = meteorsDodged;
		} else {
			s = meteorsDodged * aliensKilled;
		}
		score.setText("" + s);
	}
	
	/**
	 * Sets the color of the fire button.
	 * @param color Color to set button to.
	 */
	public static void setFireButtonColor(final Color color) {
		fireButton.setForeground(color);
		Border border = BorderFactory.createLineBorder(color, 10);
		fireButton.setBorder(border);
	}

}
