package ticTacToe;

/**
 * @author John Wolf
 * Date April 2020
 * Course: ICS4U
 * Square.java
 * Responsible for each playable space on the Tic Tac Toe board
 */

import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Square extends StackPane {
	// Important constants for the state of the square
	public static final char X = 'X';
	public static final char O = 'O';
	public static final char BLANK = ' ';
	
	// Custom images for the Tic Tac Toe game pieces
	private static Image imgX;
	private static Image imgO;
	
	// The ImageView which stores the image of the game piece
	private ImageView imgGamePiece = new ImageView();
	
	// The rounded square serving as the background of each square
	public Rectangle gamePieceBG;

	// Stores the state of each square (X, O or blank)
	private char state;

	/** The Square class constructor
	 * @param screenWidth  double, the width of the screen
	 */
	public Square(double screenWidth) {
		// Create the stackPane
		super();
		
		// Load the images in now that we can determine their size, making antialiasing possible
		imgX = new Image("/images/tictactoe_x.png", screenWidth / 12.5, screenWidth / 12.5, true, true);
		imgO = new Image("/images/tictactoe_o.png", screenWidth / 12.5, screenWidth / 12.5, true, true);
		
		// Creates the game piece's background
		createGamePieceBG(screenWidth / 12.5);
		
		// Set the imgGamePiece's formatting options
		imgGamePiece.setFitWidth(screenWidth / 12.5);
		imgGamePiece.setPreserveRatio(true);

		// Defaults to the X image to ensure the Square is the right size
		imgGamePiece.setImage(imgX);
		imgGamePiece.setOpacity(0);

		// Add gamePieceBG and imgGamePiece to the Square
		getChildren().addAll(gamePieceBG, imgGamePiece);

		// Squares default to blank
		state = BLANK;
	}
	
	/**
	 * Updates the private data field value, and sets the appropriate graphic
	 * at the same time.
	 * @param newState  the new state for the square
	 */
	public void playSquare(char newState) {
		// Update state
		state = newState;
		
		// Updates the imgGamePiece accordingly
		switch (state) {
			case X:
				imgGamePiece.setImage(imgX);
				break;
			case O:
				imgGamePiece.setImage(imgO);
				break;
		}
		// Make sure imgGamePiece is completely visible
		imgGamePiece.setOpacity(1);
	}
	
	/**
	 * Shows the semi-opaque image on mouseover
	 * @param newState  the state to preview on the board
	 */
	public void previewSquare(char newState) {
		// Updates the imgGamePiece according to newState
		switch (newState) {
			case X:
				imgGamePiece.setImage(imgX);
				break;
			case O:
				imgGamePiece.setImage(imgO);
				break;
		}		
		imgGamePiece.setOpacity(0.5);
	}
	
	
	/**
	 * Removes the previewed image (used when the mouse exits the Square)
	 */
	public void removeSquarePreview() {
		imgGamePiece.setOpacity(0);
	}
	
	/**
	 * Retrieves the square's state (X, O, BLANK)
	 * @return state  the square's state
	 */
	public char getState() {
		return state;
	}
	
	/**
	 * Creates the boxes used as backgrounds for the game pieces
	 * @param size  the length and width of the box
	 */
	private void createGamePieceBG(double size) {
		// Create a square of the specified size
	    gamePieceBG = new Rectangle(size , size);
	    
	    // Add a shadow
	    gamePieceBG.setEffect(new DropShadow(TicTacToe.GAP, Color.rgb(0, 0, 0, 0.5)));
	    
	    // Round its corners with GAP as the radius
	    gamePieceBG.setArcHeight(3*TicTacToe.GAP);
	    gamePieceBG.setArcWidth(3*TicTacToe.GAP);
	    
	    // Set the fill of the square
	    gamePieceBG.setFill(Color.web("#ccc0b2"));
	}

}
