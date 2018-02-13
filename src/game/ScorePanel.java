package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class ScorePanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * Meteor class determines when meteors dodged is incremented
	 * which is why these variables are static
	 */
	private static int meteorsDodged = 0;
	private static int aliensKilled = 0;
	private static JLabel fireButton;
	private static JLabel meteors = new JLabel("0", SwingConstants.CENTER);
	private static JLabel aliens = new JLabel("0", SwingConstants.CENTER);
	private static JLabel score = new JLabel("0", SwingConstants.CENTER);
	
	public ScorePanel(){		
		this.setPreferredSize(new Dimension(295, OrbitGame.WINDOW_HEIGHT));
		this.setBackground(Color.CYAN);
		Border border = BorderFactory.createLineBorder(Color.DARK_GRAY, 15);
		Border emptyBorder = BorderFactory.createEmptyBorder(0,0,10,0);
		this.setBorder(border);
		JLabel title = new JLabel("ORBIT");
		Font titleFont = new Font("Impact", Font.BOLD, 80);
		title.setFont(titleFont);
		this.add(title);
		
		JLabel meteorsLabel = new JLabel("<html>METEORS<br/>DODGED:</html>");
		Font subTitleFont = new Font("Impact", Font.BOLD, 40);
		meteorsLabel.setFont(subTitleFont);
		meteorsLabel.setBorder(emptyBorder);
		this.add(meteorsLabel);
		
		meteors.setPreferredSize(new Dimension(190, 60));
		Font scoreFont = new Font("Impact", Font.BOLD, 60);
		meteors.setFont(scoreFont);
		meteors.setBorder(emptyBorder);
		this.add(meteors);
		
		JLabel aliensLabel = new JLabel("<html>ALIENS<br/>KILLED:</html>");
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
	
	public JLabel createFireButton(){
		Font fireFont = new Font("Impact", Font.BOLD, 40);
		fireButton = new JLabel("FIRE!");
		fireButton.setFont(fireFont);
		fireButton.setForeground(Color.LIGHT_GRAY);
		return fireButton;
	}

	public static void incrementMeteorsDodged(){
		meteorsDodged++;
		meteors.setText("" + meteorsDodged);
	}
	
	public static void resetMeteorsDodged(){
		meteorsDodged = 0;
		meteors.setText("0");
	}
	
	public static void incrementAliensKilled(){
		aliensKilled++;
		aliens.setText("" + aliensKilled);
	}
	
	public static void resetAliensKilled(){
		aliensKilled = 0;
		aliens.setText("0");
	}
	
	public static void updateScore(){
		int s = meteorsDodged * aliensKilled;
		score.setText("" + s);
	}
	
	public static void setFireButtonColor(Color color){
		fireButton.setForeground(color);
		Border border = BorderFactory.createLineBorder(color, 10);
		fireButton.setBorder(border);
	}

}
