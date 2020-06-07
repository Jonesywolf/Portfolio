/**
 * @author John Wolf
 * Date March 2020
 * Course: ICS4U
 * Die.java
 * Responsible for the 2D Dice functionality in HLPlayer.java
 */

package highLowGame;

public class Die {
	// Store the current roll and number of
	// die sides as private class fields
	private int currentRoll;
	private int numDieSides = 6;

	/**
	 * Empty die constructor
	 */
	public Die() {
	}

	/**
	 * The roll method rolls the die, updating the current roll
	 * @return int  a random number from 1 to numDieSides
	 */
	public int roll() {
		currentRoll = (int)(Math.random() * numDieSides) + 1;
		return currentRoll;
	}
	
	/**
	 * Retrieves the current roll
	 * @return int  currentRoll
	 */
	public int getCurrentRoll() {
		return currentRoll;
	}

	/**
	 * Retrieves the number of die sides (method is currently unused)
	 * @return int  numDieSides
	 */
	public int getNumDieSides() {
		return numDieSides;
	}
	
	/**
	 * Sets the number of sides on the die (method is currently unused)
	 * @param numDieSides  the new number of die sides
	 */
	public void setNumDieSides(int numDieSides) {
		this.numDieSides = numDieSides;
	}
}
