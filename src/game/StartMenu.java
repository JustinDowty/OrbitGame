package game;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

public class StartMenu extends JFrame implements ActionListener{

	public StartMenu(){
		JButton button = new JButton("START");
		button.addActionListener(this);
		
		this.setLayout(new BorderLayout());
        this.setFocusable(true);
        this.setSize(300, 300);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setTitle("ORBIT");
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.add(button);
        this.setVisible(true);   
        
        
    }
	
	public void actionPerformed(ActionEvent e){
		this.dispose();
		MainGUI g = new MainGUI();
		g.beginGame();
	}
	
	public static void main(String[] args){
		MainGUI gui = new MainGUI();
		gui.beginGame();
	}
	
}
