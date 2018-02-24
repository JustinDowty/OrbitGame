package game;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * JUnit test cases for Orbit game, organized by class
 * @author Justin Dowty
 * @author Ted Lang
 * @author Alec Allain
 */
public class UnitTesting {
	/**
	 * Testing Alien class
	 */
	@Test
	public void alienTests() {
		Alien alien = new Alien();
		for (int i = 0; i < 1000; i++) {
			// Testing set location bounds
			alien.setLocation();
			assertTrue("X LESS THAN 800", alien.getxLocation() < 800);
			assertTrue("X GREATER THAN 0", alien.getxLocation() >= 0);
			// Testing set speed bounds
			alien.setSpeed();
			//assertTrue("X IS NOT 30", alien.getxSpeed();
		}
	}

}
