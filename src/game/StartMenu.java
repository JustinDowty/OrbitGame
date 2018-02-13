package game;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * This class simulates the start menu for 
 * the game Orbit
 * 
 * @author Alec Allain
 * @author Justin Dowty
 * @author Ted Lang
 */
public class StartMenu extends JFrame implements ActionListener{

	/** main menu buttons */
	private JButton start;
	private JButton stats;
	private JButton quit;
	
	/** file menu items */
	private JMenuBar menu;
	private JMenu file;
	private JMenuItem quitItem;
	private JMenuItem infoItem;
	private JMenuItem statsItem;
	
	/** creates new Orbit game */
	private MainGUI games;
	
	/** creates button layout */
	private JPanel panel;
	
	/** */
	private JFrame popUp;
	
	/**
	 * Constructor for class
	 */
	public StartMenu() {
		setupFileMenu();
		setupMainMenu();
		setupBackground();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Orbit");
		this.setVisible(true);
		this.setFocusable(true);
		this.setLocationRelativeTo(null);
	}
	
	/**
	 * This method creates the file menu
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
	 * This class sets up the start menu
	 */
	public void setupMainMenu() {
		start = new JButton("Start");
		stats = new JButton("Statistics");
		quit = new JButton("Quit");
		
		start.addActionListener(this);
		stats.addActionListener(this);
		quit.addActionListener(this);
		
		panel = new JPanel();
		
		panel.add(start);
		panel.add(stats);
		panel.add(quit);
		
		this.setResizable(false);
		this.setSize(600,500);
		
		this.add(panel);
	}
	
	/**
	 * This method sets up the colored background
	 */
	public void setupBackground() {
		
	}
	
	/**
	 * This class listens for button presses
	 */
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == start) {
			this.dispose();
			MainGUI.gui = new MainGUI();
			MainGUI.gui.beginGame();
		}
		
		if (e.getSource() == infoItem) {
			popUp = new JFrame("Information");
			JOptionPane.showMessageDialog(popUp, "The name of the game is Orbit!\n The object of the game is to see\n how far you can go without crashing\n your ship into any meteors!\n Oh and watch out for aliens too...");
		}
		
		if (e.getSource() == stats || e.getSource() == statsItem) {
			popUp = new JFrame("Statistics");
			JOptionPane.showMessageDialog(popUp,"Here is where we put the latest stats!");
		}
		
		if (e.getSource() == quit || e.getSource() == quitItem) {
			System.exit(1);
		}
	}
	
	/**
	 * Main method for class
	 */
	public static void main(String[] args){
		StartMenu startUp = new StartMenu();
		
	}
	
}
