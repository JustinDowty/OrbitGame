package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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
	 * Menu bar.
	 */
	private JMenuBar menu;
	/**
	 * Menu.
	 */
	private JMenu file;
	/**
	 * Menu quit item.
	 */
	private JMenuItem quitItem;
	/**
	 * Menu info item.
	 */
	private JMenuItem infoItem;
	/**
	 * Menu stats item.
	 */
	private JMenuItem statsItem;
	
	/**
	 * Panel for start menu.
	 */
	private JPanel panel;
	
	/**
	 * Frame for pop up dialogs.
	 */
	private JFrame popUp;
	
	/**
	 * Title Label.
	 */
	private JLabel orbit;
	
	/**
	 * Current player initials.
	 */
	private String currPlayer;
	
	/**
	 * Constructor for class sets up menus and displays frame.
	 */
	public StartMenu() {
		setupFileMenu();
		setupMainMenu();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Orbit");
		this.setVisible(true);
		this.setFocusable(true);
		this.setLocationRelativeTo(null);
		enterInitialsDialog();
	}
	
	/**
	 * This method creates the file menu.
	 */
	public void setupFileMenu() {
		menu = new JMenuBar();
		file = new JMenu("File");
		quitItem = new JMenuItem("Quit");
		infoItem = new JMenuItem("Information");
		statsItem = new JMenuItem("Statistics");
		
		infoItem.addActionListener(this);
		statsItem.addActionListener(this);
		quitItem.addActionListener(this);
		
		file.add(infoItem);
		file.add(statsItem);
		file.add(quitItem);
		
		menu.add(file);
		
		setJMenuBar(menu);
	}

	/**
	 * This method sets up the start menu.
	 */
	public void setupMainMenu() {
		panel = new JPanel();
		
		panel.setLayout(new GridBagLayout());
		GridBagConstraints g = new GridBagConstraints();
		g.gridwidth = GridBagConstraints.REMAINDER;
		g.fill = GridBagConstraints.HORIZONTAL;
		g.gridx = 0;
		
		orbit = new JLabel("ORBIT", JLabel.CENTER);
		orbit.setFont(new Font("Rockwell", Font.BOLD, 100));
		orbit.setForeground(Color.RED);
		
		start = new JButton("Start");
		//start.setPreferredSize(new Dimension(25,40));
		stats = new JButton("Statistics");
		upgrade = new JButton("Upgrades");
		quit = new JButton("Quit");
		
		start.setBackground(Color.CYAN);
		stats.setBackground(Color.CYAN);
		upgrade.setBackground(Color.GREEN);
		quit.setBackground(Color.RED);
		
		start.setFont(new Font("Bold", Font.BOLD, 18));
		stats.setFont(new Font("Bold", Font.BOLD, 18));
		upgrade.setFont(new Font("Bold", Font.BOLD, 18));
		quit.setFont(new Font("Bold", Font.BOLD, 18));
		
		start.addActionListener(this);
		stats.addActionListener(this);
		upgrade.addActionListener(this);
		quit.addActionListener(this);
		
		panel.add(orbit);
		panel.add(start, g);
		panel.add(stats, g);
		panel.add(upgrade, g);
		panel.add(quit, g);
		
		
		panel.setBackground(Color.BLACK);
		
		this.setResizable(false);
		this.setSize(600, 500);
		
		this.add(panel);
	}
	
	/**
	 * This class listens for button presses.
	 * @param e Instance of ActionEvent.
	 */
	public void actionPerformed(final ActionEvent e) {
		if (e.getSource() == start) {
			this.dispose();
			MainGUI gui = new MainGUI(currPlayer);
			gui.beginGame();
		}
		
		if (e.getSource() == infoItem) {
			popUp = new JFrame("Information");
			JOptionPane.showMessageDialog(popUp, 
			"The name of the game is Orbit!"
			+ "\n The object of the game is to see"
			+ "\n how far you can "
			+ "go without crashing"
			+ "\n your ship into any meteors!"
			+ "\n Oh and watch out "
			+ "for aliens too...");
		}
		
		if (e.getSource() == stats || e.getSource() == statsItem) {
			popUp = new JFrame("Statistics");
			JOptionPane.showMessageDialog(popUp, 
					"Here is where we put "
					+ "the latest stats!");
		}
		
		if (e.getSource() == upgrade) {
			popUp = new JFrame("Upgrades");
			JOptionPane.showMessageDialog(popUp, 
					"Here is where you will be able"
					+ "\n to upgrade your ship!"
					+ "\n This will be coming "
					+ "in the second release.");
		}
		
		if (e.getSource() == quit || e.getSource() == quitItem) {
			System.exit(1);
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
	 * Main method begins game.
	 * @param args Unused standard parameter for main class.
	 */
	public static void main(final String[] args) {
		new StartMenu();
		
	}	
}
