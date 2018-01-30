package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class ScorePanel extends JPanel {
	private static int meteorsDodged = 0;
	private static JLabel fireButton;
	private static JLabel scoreText = new JLabel("0", SwingConstants.CENTER);
	
	public ScorePanel(){		
		this.setPreferredSize(new Dimension(295, OrbitGame.WINDOW_HEIGHT));
		this.setBackground(Color.CYAN);
		Border border = BorderFactory.createLineBorder(Color.DARK_GRAY, 15);
		this.setBorder(border);
		
		JLabel title = new JLabel("ORBIT");
		Font titleFont = new Font("Impact", Font.BOLD, 80);
		title.setFont(titleFont);
		this.add(title);
		
		JLabel scoreTitle = new JLabel("<html>METEORS<br/>DODGED:</html>");
		Font subTitleFont = new Font("Impact", Font.BOLD, 40);
		scoreTitle.setFont(subTitleFont);
		this.add(scoreTitle);
		
		scoreText.setPreferredSize(new Dimension(190, 60));
		Font scoreFont = new Font("Impact", Font.BOLD, 60);
		scoreText.setFont(scoreFont);
		this.add(scoreText);
		
		fireButton = setFireButton();
		this.add(fireButton);
		
	}
	
	public JLabel setFireButton(){
		Font fireFont = new Font("Impact", Font.BOLD, 40);
		fireButton = new JLabel("FIRE!");
		fireButton.setFont(fireFont);
		fireButton.setForeground(Color.LIGHT_GRAY);
		Border border = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 10);
		fireButton.setBorder(border);
		return fireButton;
		}

	public static void incrementMeteorsDodged(){
		meteorsDodged++;
		scoreText.setText("" + meteorsDodged);
	}
	
	public static void setFireButtonColor(Color color){
		fireButton.setForeground(color);
		Border border = BorderFactory.createLineBorder(color, 10);
		fireButton.setBorder(border);
	}

}