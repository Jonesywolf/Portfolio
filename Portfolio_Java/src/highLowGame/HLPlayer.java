/**
 * @author John Wolf
 * Date March 2020
 * Course: ICS4U
 * HLPlayer.java
 * Responsible for the core gameplay functionality of the high low game
 */

package highLowGame;

public class HLPlayer {
	// Constants for calling LOW or HIGH
	public final static int LOW = 0;
	public final static int HIGH = 1;
	
	// The player's number of points (starts at 1000)
	private int currentPoints = 1000;
	
	// The number of points risked by the player
	private int pointsRisked = 0;
	
	// Stores the player's call
	private int call;
	
	// Stores the player's current die roll
	private int currentRoll;
	
	// Create a die object for each Die
	private Die die1 = new Die();
	private Die die2 = new Die();

	/**
	 * HLPlayer constructor
	 */
	public HLPlayer () {
		
	}
	
	
	/**
	 * Rolls both dice, calls updateCurrentPoints
	 */
	public void rollDice() {
		currentRoll = die1.roll() + die2.roll();
		updateCurrentPoints(currentRoll, 2);
	}
	
	
	
	/**
	 * Updates the player's points based on their call,
	 * points risked, and dice roll
	 * @param roll  the player's current roll
	 * @param numDice  the number of dice in play
	 */
	public void updateCurrentPoints(int roll, int numDice) {
		// The most common roll is broken in two variables to
		// account for the most common rolls for three dice
		// Default is 2 dice
		int lowestMostCommonRoll = 7;
		int highestMostCommonRoll = 7;
		
		// If more than two dice are being used, update
		// lowestMostCommonRoll and highestMostCommonRoll
		switch (numDice) {
			case 3:
				// Two most common rolls for three dice (10 and 11)
				lowestMostCommonRoll = 10;
				highestMostCommonRoll = 11;
				break;
			case 4:
				// Most common roll for four dice is 14
				lowestMostCommonRoll = 14;
				highestMostCommonRoll = 14;
				break;
		}
		// If the player called low and their roll was less than
		// lowestMostCommonRoll
		if (call == HLPlayer.LOW && roll < lowestMostCommonRoll) {
			// They called the dice roll correctly, double the points risked and add it to
			currentPoints += pointsRisked * 2;
		}
		// If the player called low and their roll was less than
		// lowestMostCommonRoll
		else if (call == HLPlayer.HIGH && roll > highestMostCommonRoll) {
			currentPoints += pointsRisked * 2;
		}
		// Otherwise the player guessed wrong or the most common roll
		// was rolled, either way, they lose the points they risked
		// lowestMostCommonRoll
		else {
			currentPoints -= pointsRisked;
		}
	}
	
	
	/**
	 * Sets the number of points the user is risking
	 * @param pointsRisked  the number of points risked
	 */
	public void riskPoints(int pointsRisked) {
		this.pointsRisked = pointsRisked;
	}
	
	/**
	 * Sets the user's call
	 * @param call  the user's call (either HLPlayer.LOW or HLPlayer.HIGH)
	 */
	public void makeCall(int call) {
		this.call = call;
	}
	
	/**
	 * Sets the user's currentPoints (used only for resetting the game)
	 * @param currentPoints  the user's number of points
	 */
	public void setCurrentPoints(int currentPoints) {
		this.currentPoints = currentPoints;
	}

	/**
	 * Retrieves the user's current roll
	 * @return currentRoll  the user's current roll
	 */
	public int showRoll() {
		return currentRoll;
	}

	/**
	 * Retrieves the user's number of points
	 * @return currentPoints  the user's points
	 */
	public int showPoints() {
		return currentPoints;
	}
}
