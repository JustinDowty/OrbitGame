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
 * This class generates Controls Menu.
 * @author JustinDowty
 *
 */
public class ControlsMenu extends JFrame {
	private static final long serialVersionUID = 1L;
	/**
	 * Background star locations.
	 */
	private int[] starLocations;

	/**
	 * Builds high scores menu.
	 */
	public ControlsMenu() {
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
		Font font = new Font("Rockwell", Font.BOLD, 40);
		panel.setLayout(layout);
		panel.setBackground(Color.BLACK);
		panel.setBorder(new LineBorder(Color.BLACK, 20));
		JLabel text = new JLabel("<html>CONTROLS:<br>"
				+ "Use arrow keys to navigate.<br>"
				+ "Space bar to fire weapon.<br>"
				+ "P to pause current game.");
		text.setFont(font);
		text.setForeground(Color.RED);
		text.setBorder(
				BorderFactory.createEmptyBorder(20, 0, 20, 0));
		panel.add(text);
		this.add(panel);
		this.pack();
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("High Scores");
		this.setVisible(true);
		this.setFocusable(true);
		this.setLocationRelativeTo(null);
		starLocations = Utils.generateStars(2, panel.getWidth(), panel.getHeight());
		Utils.starLoop(panel, starLocations, panel.getWidth(), panel.getHeight());
	}	
}
