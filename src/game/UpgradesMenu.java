package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 * This menu has the upgrades options for the players ship.
 * @author JustinDowty
 */
public class UpgradesMenu extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	/**
	 * Current player.
	 */
	private String player;
	/**
	 * Player's total aliens killed.
	 */
	private int aliens;
	/**
	 * JLabel showing side blast option.
	 */
	private JButton sideBlast;
	/**
	 * JLabel showing multi blast option.
	 */
	private JButton multiBlast;
	/**
	 * JLabel showing lazer option.
	 */
	private JButton lazer;
	/**
	 * Locations of background stars.
	 */
	private int[] starLocations;
	/**
	 * Instance of current start menu.
	 */
	private StartMenu menu;
	
	/**
	 * Builds upgrades menu.
	 * @param player Curent player.
	 * @param aliens Player's alien's killed.
	 */
	public UpgradesMenu(final String player, final int aliens,
			StartMenu menu) {
		this.player = player;
		this.aliens = aliens;
		this.menu = menu;
		setUpMenu();
	}
	
	/**
	 * Builds the upgrades menu.
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
		Font titleFont = new Font("Rockwell", Font.BOLD, 40);
		panel.setBackground(Color.BLACK);
		Font smallFont = new Font("Rockwell", Font.PLAIN, 30);
		
		JLabel titleLabel
			= new JLabel("UPGRADES:");
		titleLabel.setFont(titleFont);
		titleLabel.setForeground(Color.GREEN);
		panel.add(titleLabel);
		
		JLabel infoLabel
			= new JLabel("<HTML>Upgrades are earned<br/>by killing aliens.<br/>"
					+ player + " has killed " + aliens + "<br/><br/>");
		infoLabel.setFont(smallFont);
		infoLabel.setForeground(Color.GREEN);
		panel.add(infoLabel);
		
		ImageIcon i = new ImageIcon("sideblast.png");
		sideBlast = new JButton();
		sideBlast.setIcon(i);
		sideBlast.setBorderPainted(false);
		sideBlast.setContentAreaFilled(false);
		sideBlast.addActionListener(this);
		panel.add(sideBlast);
		
		i = new ImageIcon("multiblast.png");
		multiBlast = new JButton();
		multiBlast.setIcon(i);
		multiBlast.setBorderPainted(false);
		multiBlast.setContentAreaFilled(false);
		multiBlast.addActionListener(this);
		panel.add(multiBlast);
		
		i = new ImageIcon("lazer.png");
		lazer = new JButton();
		lazer.setIcon(i);
		lazer.setBorderPainted(false);
		lazer.setContentAreaFilled(false);
		lazer.addActionListener(this);
		panel.add(lazer);
		
		this.add(panel);
		this.pack();
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("Upgrades");
		this.setVisible(true);
		this.setFocusable(true);
		this.setLocationRelativeTo(null);		
		starLocations = Utils.generateStars(2, panel.getWidth(), panel.getHeight());
		Utils.starLoop(panel, starLocations, panel.getWidth(), panel.getHeight());
	}

	/**
	 * Controls the blast type buttons to set the blast type
	 * then close the window.
	 * @param e ActionEvent instance.
	 */
	@Override
	public void actionPerformed(final ActionEvent e) {
		if (e.getSource() == sideBlast) {
			if (aliens > 20) {
				menu.setBlastType(BlastTypes.SIDE_BLASTS);
				this.dispose();
			} else {
				JOptionPane.showMessageDialog(this,
						"Need to kill 20 aliens to unlock.");
			}
		}
		if (e.getSource() == multiBlast) {
			if (aliens > 50) {
				menu.setBlastType(BlastTypes.MULTI_BLAST);
				this.dispose();
			} else {
				JOptionPane.showMessageDialog(this,
						"Need to kill 50 aliens to unlock.");
			}
		}
		if (e.getSource() == lazer) {
			if (aliens > 100) {
				menu.setBlastType(BlastTypes.LAZER);
				this.dispose();
			} else {
				JOptionPane.showMessageDialog(this,
						"Need to kill 100 aliens to unlock.");
			}
		}
	}
}
