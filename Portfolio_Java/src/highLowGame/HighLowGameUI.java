/**
 * @author John Wolf
 * Date March 2020
 * Course: ICS4U
 * HighLowGameUI.java
 * Responsible for the UI functionality of the High Low Game
 */

package highLowGame;

import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXToggleButton;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;

public class HighLowGameUI extends Application {
	// Formatting constants
	static final int GAP = 15;
	
	// Define colour constants
	static final Color BACKGROUND_COLOUR = Color.web("#121212");
	static final Color PRIMARY_COLOUR = Color.web("#80deea");
	static final Color SECONDARY_COLOUR = Color.web("#ea8c80");
	static final Color WHITE = Color.web("#ffffff");
	
	// Define Label CSS constants
	static final String TITLE_CSS = "-fx-font-family: Oswald; -fx-font-size: 55; -fx-text-fill: white;";
	static final String LABEL_CSS = "-fx-font-family: Oswald; -fx-font-size: 31; -fx-text-fill: white;";	
	static final String SUBTITLE_CSS = "-fx-font-family: Oswald; -fx-font-size: 18; -fx-text-fill: white; -fx-opacity: 0.8;";
	static final String BODY_CSS = "-fx-font-family: Roboto; -fx-font-size: 16; -fx-text-fill: white;";
	
	// The root is a stackPane, so dialogs can be used
	StackPane dialogRoot = new StackPane();
	
	// However, the title and body of the program are
	// contained within a VBox
	VBox root = new VBox(GAP);
	
	// Define the various dialogPanes
	JFXDialog quitDialog;
	JFXDialog rulesDialog;
	JFXDialog gameOverDialog;
	
	// Define and initialize the VBoxes containing the
	// content of each dialogPane
	VBox quitContent = new VBox(GAP);
	VBox rulesContent = new VBox(GAP);
	VBox gameOverContent = new VBox(GAP);
	
	// Define the StackPanes containing each die and 
	// its background
	StackPane diePane1 = new StackPane();
	StackPane diePane2 = new StackPane();
	StackPane diePane3 = new StackPane();
	StackPane diePane4 = new StackPane();
	
	// Create a Die3D object for each die
	Die3D die1 = new Die3D();
	Die3D die2 = new Die3D();
	Die3D die3 = new Die3D();
	Die3D die4 = new Die3D();

	// Labels to store the user's number of points
	Label lblCurrentPoints = new Label();

	// Labels to store the user's latest roll
	Label lblDiceRoll = new Label();

	// Button to roll the dice
	JFXButton btnRollDice = new JFXButton();
	
	// Slider to set the number of dice
	JFXSlider diceSlider;
	
	// Slider to set the number of points risked
	JFXSlider pointsRiskedSlider;
	
	// Toggle button to set the player's call
	JFXToggleButton callToggleButton = new JFXToggleButton();
	
	// Store the dice StackPanes in an array to access them in succession
	StackPane[] diePanes = {diePane1, diePane2, diePane3, diePane4};
	
	// Store the 3D dice objects in an array to access them in succession
	Die3D[] dice3D = {die1, die2, die3, die4};

	// Store the number of Dice
	int numDice = 2;
	
	// Store the number of dice that have been rolled
	int numDiceRolled = 0;
	
	// Create a new HLPlayer object
	HLPlayer player = new HLPlayer();

	@Override
	public void start(Stage myStage) throws Exception {
		// Instantiate Background fills based on the UI element's
		// Z elevation:
		// https://material.io/design/environment/elevation.html#elevation
		BackgroundFill elevation1Colour = new BackgroundFill(BACKGROUND_COLOUR, CornerRadii.EMPTY, Insets.EMPTY);
		BackgroundFill elevation2Colour = new BackgroundFill(Color.rgb(255, 255, 255, 0.05), new CornerRadii(GAP), Insets.EMPTY);
		
		// Set the root VBox's padding
		root.setPadding(new Insets(GAP, GAP, GAP, GAP));
		
		// Set the body's HBox's formatting options
		HBox body = new HBox(GAP);
		body.setPadding(new Insets(GAP, GAP, GAP, GAP));
		
		// Set the leftSide's GridPane formatting options
		GridPane leftSide = new GridPane();
		leftSide.setHgap(GAP);
		leftSide.setVgap(GAP);
		leftSide.setPadding(new Insets(GAP, GAP, GAP, GAP));

		// Set the rightSide's VBox formatting options
		VBox rightSide = new VBox(GAP);
		rightSide.setPadding(new Insets(GAP, GAP, GAP, GAP));
		
		
		/********* QUIT DIALOG CONTENT ********/
		// Set the quit dialog pane's content formatting options
		quitContent.setPadding(new Insets(GAP, GAP, GAP, GAP));
		
		// Create a label to confirm the user's intent to quit and set its formatting options
		Label confirmQuit = new Label("ARE YOU SURE YOU WANT TO QUIT?");
		confirmQuit.setPadding(new Insets(GAP, GAP, GAP, GAP));
		confirmQuit.setStyle(SUBTITLE_CSS);
		confirmQuit.setBackground(new Background(elevation2Colour));
		quitContent.getChildren().add(confirmQuit);
		
		// Create an HBox to store buttons for the quit dialog
		HBox confirmQuitBtnsHBox = new HBox(GAP);
		confirmQuitBtnsHBox.setPadding(new Insets(GAP, GAP, GAP, GAP));

		// Create a button to quit the application and set its formatting options
		JFXButton btnQuit = new JFXButton("QUIT");
		btnQuit.setOnAction(event -> quit());
		btnQuit.setRipplerFill(WHITE);
		confirmQuitBtnsHBox.getChildren().add(btnQuit);
		
		// Create a button to close the quit dialog and set its formatting options
		JFXButton btnCloseQuitDialog = new JFXButton("CANCEL");
		btnCloseQuitDialog.setOnAction(event -> closeQuit());
		btnCloseQuitDialog.setRipplerFill(WHITE);
		btnCloseQuitDialog.setStyle("-fx-background-color: #ffbdaf;");
		confirmQuitBtnsHBox.getChildren().add(btnCloseQuitDialog);
		confirmQuitBtnsHBox.setAlignment(Pos.TOP_CENTER);
		quitContent.getChildren().add(confirmQuitBtnsHBox);
		
		// Set the quit dialog pane's content style options
		quitContent.setAlignment(Pos.TOP_CENTER);
		quitContent.setBackground(new Background(elevation1Colour));
		
		/********* RULES DIALOG CONTENT ********/
		// Set the rule dialog pane's content formatting options
		rulesContent.setPadding(new Insets(GAP, GAP, GAP, GAP));
		
		// Create a label for a title for the rules dialog and set its formatting options
		Label rulesTitle = new Label("RULES");
		rulesTitle.setPadding(new Insets(GAP, GAP, GAP, GAP));
		rulesTitle.setStyle(SUBTITLE_CSS);
		rulesTitle.setBackground(new Background(elevation2Colour));
		rulesContent.getChildren().add(rulesTitle);
		
		// Create a label to store the rules of the game and set its formatting options
		Label rulesTxt = new Label(
				"This is a gambling game. So for my sake, please don't win. \n"
				+ "How to play: \n "
				+ "You start with 1000 points and then place your bet by risking \n"
				+ "some or all of your points by calling whether the next dice roll \n"
				+ "will be higher or lower than the most common roll(s) for that \n"
				+ "number of dice: \n"
				+ "2 Dice Most Common Roll: 7 \n"
				+ "3 Dice Most Common Roll: 10 or 11 \n"
				+ "4 Dice Most Common Roll: 14 \n"
				+ "If you're wrong, you lose the points you risked. If you're \n"
				+ "right, you gain double the points you risked. If you roll \n"
				+ "the most common roll. You still lose the points you risked. \n"
				+ "Good luck. Remember, you're only trying to win for your ego. \n"
				+ "I actually need the money.");
		rulesTxt.setPadding(new Insets(GAP, GAP, GAP, GAP));
		rulesTxt.setStyle(BODY_CSS);
		rulesTxt.setBackground(new Background(elevation2Colour));
		rulesContent.getChildren().add(rulesTxt);
		
		// Create a button to close the rules dialog and set its formatting options
		JFXButton btnCloseRulesDialog = new JFXButton("CLOSE");
		btnCloseRulesDialog.setOnAction(event -> closeRules());
		btnCloseRulesDialog.setRipplerFill(WHITE);
		btnCloseRulesDialog.setStyle("-fx-background-color: #ffbdaf;");
		rulesContent.getChildren().add(btnCloseRulesDialog);
		
		// Set the rules dialog pane's content style options
		rulesContent.setAlignment(Pos.TOP_CENTER);
		rulesContent.setBackground(new Background(elevation1Colour));
		
		/********* GAME OVER DIALOG CONTENT ********/
		// Set the game over dialog pane's content formatting options
		gameOverContent.setPadding(new Insets(GAP, GAP, GAP, GAP));
		
		// Create a label for a title for the game over dialog and set its formatting options
		Label lblGameOver = new Label("GAME OVER. YOU LOSE. POVERTY AWAITS.");
		lblGameOver.setPadding(new Insets(GAP, GAP, GAP, GAP));
		lblGameOver.setStyle(SUBTITLE_CSS);
		lblGameOver.setBackground(new Background(elevation2Colour));
		gameOverContent.getChildren().add(lblGameOver);
		
		// Create an HBox to store buttons for quit dialog
		HBox gameOverBtnsHBox = new HBox(GAP);
		gameOverBtnsHBox.setPadding(new Insets(GAP, GAP, GAP, GAP));

		// Create a button to quit the application and set its formatting options
		JFXButton btnGameOverQuit = new JFXButton("QUIT");
		btnGameOverQuit.setOnAction(event -> quit());
		btnGameOverQuit.setRipplerFill(WHITE);
		gameOverBtnsHBox.getChildren().add(btnGameOverQuit);
		
		// Create a button play again and set its formatting options
		JFXButton btnPlayAgain = new JFXButton("PLAY AGAIN");
		btnPlayAgain.setOnAction(event -> playAgain());
		btnPlayAgain.setRipplerFill(WHITE);
		btnPlayAgain.setStyle("-fx-background-color: #ffbdaf;");
		gameOverBtnsHBox.getChildren().add(btnPlayAgain);
		
		// Set the button HBox's alignment
		gameOverBtnsHBox.setAlignment(Pos.TOP_CENTER);
		
		gameOverContent.getChildren().add(gameOverBtnsHBox);
		gameOverContent.setAlignment(Pos.TOP_CENTER);
		gameOverContent.setBackground(new Background(elevation1Colour));
		
		
		/********* TITLE ********/
		// Set the Title's formatting options
		Label lblTitle = new Label("High Low Game");
		lblTitle.setStyle(TITLE_CSS);
		root.getChildren().add(lblTitle);

		
		/********* DICE UI CONTENT (LEFT SIDE OF UI) ********/
		// Set Die 1's UI Content and formatting options
		InnerShadow innerShadow = new InnerShadow();

        Rectangle die1PlaceHolder = createBox(100);
        die1PlaceHolder.setEffect(innerShadow);

        diePane1.getChildren().addAll(die1PlaceHolder, die1.dieScene);
        leftSide.add(diePane1, 0, 0);
        
        // Set Die 2's UI Content and formatting options
		Rectangle die2PlaceHolder = createBox(100);
		die2PlaceHolder.setEffect(innerShadow);

		diePane2.getChildren().addAll(die2PlaceHolder, die2.dieScene);
		leftSide.add(diePane2, 1, 0);
		 
		// Set Die 3's UI Content and formatting options
		Rectangle die3PlaceHolder = createBox(100);
		die3PlaceHolder.setEffect(innerShadow);

		diePane3.getChildren().add(die3PlaceHolder);
		leftSide.add(diePane3, 0, 1);
		 
		// Set Die 4's UI Content and formatting options
		Rectangle die4PlaceHolder = createBox(100);
		die4PlaceHolder.setEffect(innerShadow);

		diePane4.getChildren().add(die4PlaceHolder);
		leftSide.add(diePane4, 1, 1);
        
		/********* RULES AND QUIT BUTTONS (LEFT SIDE OF UI) ********/
		// Create an HBox to store the rules and quit buttons
		HBox rulesQuitHBox = new HBox(GAP);

		// Set the rules button's formatting options
		JFXButton btnRules = new JFXButton("HOW TO PLAY");
		btnRules.setOnAction(event -> openRules());
		btnRules.setRipplerFill(WHITE);
		btnRules.setStyle("-fx-background-color: #ffbdaf;");
		rulesQuitHBox.getChildren().add(btnRules);

		// Set the quit button's formatting options
		JFXButton btnOpenQuitDialog = new JFXButton("QUIT");
		btnOpenQuitDialog.setOnAction(event -> openQuit());
		btnOpenQuitDialog.setRipplerFill(WHITE);
		rulesQuitHBox.getChildren().add(btnOpenQuitDialog);
		
		leftSide.add(rulesQuitHBox, 0, 2, 2, 1); // pos(0, 2), colspan = 2

		/********* DICE ROLL LABEL (LEFT SIDE OF UI) ********/
		// Set the dice roll label's formatting options
		lblDiceRoll.setStyle(LABEL_CSS);
		leftSide.add(lblDiceRoll, 0, 3, 2, 1); // pos(0, 3), colspan = 2

		/********* GAME OPTIONS TITLE (RIGHT SIDE OF UI) ********/
		// Set the options label's formatting options
		Label lblOptions = new Label("Options:");
		lblOptions.setStyle(LABEL_CSS);
		rightSide.getChildren().add(lblOptions);
		
		/********* NUMBER OF DICE SLIDER (RIGHT SIDE OF UI) ********/
		// Set the number of dice label's formatting options
		Label lblNumDice = new Label("Number of Dice:");
		lblNumDice.setStyle(SUBTITLE_CSS);
		rightSide.getChildren().add(lblNumDice);

		// Initialize the number of dice slider
		diceSlider = new JFXSlider(2, 4, 2);
		
		// Add a listener to the number of dice slider to update the
		// number of dice in play when its state is changed
		diceSlider.valueProperty().addListener( 
	             new ChangeListener<Number>() {   
	                 public void changed(ObservableValue <? extends Number >  
	                           observable, Number oldValue, Number newValue) 
	                 { 
	                	 numDice = newValue.intValue();
	                	 switch (numDice) {
	                	 	case 2:
								// Remove die 3 and 4
	                	 		 diePane3.getChildren().remove(die3.dieScene);
	                	 		 diePane4.getChildren().remove(die4.dieScene);
	   	                		 break;
	                	 	case 3:
								// Remove and then add die 3 to prevent duplicate children errors
								diePane3.getChildren().remove(die3.dieScene);
	                	 		diePane3.getChildren().add(die3.dieScene);
								 
								// Remove die 4
	                	 		diePane4.getChildren().remove(die4.dieScene);
	   	                		break;
	                	 	case 4:
								// Remove and then add die 4 to prevent duplicate children errors
								diePane4.getChildren().remove(die4.dieScene);
	   	                		diePane4.getChildren().add(die4.dieScene);
	   	                		break;
	                	 }
	                 } 
	             });
		rightSide.getChildren().add(diceSlider);
		
		/********* NUMBER OF POINTS LABEL (RIGHT SIDE OF UI) ********/
		// Set the current points label's formatting options
		lblCurrentPoints.setText("Current Points: 1000");
		lblCurrentPoints.setStyle(SUBTITLE_CSS);
		rightSide.getChildren().add(lblCurrentPoints);
		
		/********* POITNS RISKED SLIDER (RIGHT SIDE OF UI) ********/
		// Set the points risked label's formatting options
		Label lblPointsRisked = new Label("Points to risk:");
		lblPointsRisked.setStyle(SUBTITLE_CSS);
		rightSide.getChildren().add(lblPointsRisked);

		// Initialize the points risked slider
		pointsRiskedSlider = new JFXSlider(1, player.showPoints(), 500);
		
		// The number of points risked defaults to 500
		player.riskPoints(500);
		
		// Add a listener to the points risked slider to update the
		// player's points risked when its state is changed
		pointsRiskedSlider.valueProperty().addListener(
	             new ChangeListener<Number>() {   
	                 public void changed(ObservableValue <? extends Number >  
	                           observable, Number oldValue, Number newValue) 
	                 { 
	                	 player.riskPoints(newValue.intValue());
	                 } 
	             });
		rightSide.getChildren().add(pointsRiskedSlider);
		
		
		/********* CALL TOGGLE BUTTON (RIGHT SIDE OF UI) ********/
		// Set the call label's formatting options
		Label lblCall = new Label("Call:");
		lblCall.setStyle(SUBTITLE_CSS);
		rightSide.getChildren().add(lblCall);
		
		// Create an HBox to store the call toggle button and
		// its accompanying labels, set its formatting options
		HBox callToggleButtonHBox = new HBox(GAP);
		callToggleButtonHBox.setAlignment(Pos.CENTER_LEFT);	
		
		// Set the low call label's formatting options
		Label lowCall = new Label("LOW");	
		lowCall.setStyle(SUBTITLE_CSS + "-fx-opacity: 1;");	
		callToggleButtonHBox.getChildren().add(lowCall);	
			
		callToggleButtonHBox.getChildren().add(callToggleButton);	
			
		// The call toggle button defaults to low call	
		player.makeCall(HLPlayer.LOW);
		
		// Add a listener to the call toggle button to update the
		// player's call whenever its state is changed
		callToggleButton.selectedProperty().addListener(new ChangeListener<Boolean>() {	
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {	
					boolean toggled = Boolean.valueOf(newValue);	
					if (toggled == true) {	
						// Make a high call	
						player.makeCall(HLPlayer.HIGH);	
					}	
					else {	
						// Make a low call	
						player.makeCall(HLPlayer.LOW);	
					}	
				}	
		    });	
		
		// Set the high call label's formatting options
		Label highCall = new Label("HIGH");	
		highCall.setStyle(SUBTITLE_CSS + "-fx-opacity: 1;");
		callToggleButtonHBox.getChildren().add(highCall);

		rightSide.getChildren().add(callToggleButtonHBox);
		
		/********* ROLL DICE BUTTON (RIGHT SIDE OF UI) ********/
		// Set the roll dice button's formatting options
		btnRollDice.setText("ROLL DICE");
		btnRollDice.setRipplerFill(WHITE);
		btnRollDice.setOnAction(event -> rollDice());
		rightSide.getChildren().add(btnRollDice);
		
		// Set HBox body style options and add content
		body.setBackground(new Background(elevation2Colour));
		body.getChildren().add(leftSide);
		body.getChildren().add(rightSide);		
		
		// Set VBox root style options and add content
		root.setBackground(new Background(elevation1Colour));
		root.getChildren().add(body);
		
		// Add root to the dialog StackPane
		dialogRoot.getChildren().add(root);
		
		// Set the dice to random faces before displaying them to the user:
		for (int dieNum = 0; dieNum < 4; dieNum++) {	
			Timeline fullDiceRoll = dice3D[dieNum].rollDie(10);
			fullDiceRoll.play();
		}
		
		/********* GENERATE SCENE AND ADD IT TO THE STAGE ********/
		Scene scene = new Scene(dialogRoot);
		// For some reason, both fonts won't load together when sourced form one URL
		scene.getStylesheets().add("https://fonts.googleapis.com/css?family=Oswald:400,500&display=swap");
		scene.getStylesheets().add("https://fonts.googleapis.com/css?family=Roboto:400,500&display=swap");
		
		// Load the application's CSS
		scene.getStylesheets().add("/CSS/highLowGame.css");
		myStage.setTitle("High Low Game");
		myStage.setScene(scene);
		myStage.show();
	}
	
	/**
	 * This method launches the UI
	 * @param args command-line arguments
	 */ 
	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * Rolls the 3D Dice, one at a time.
	 * Called when the roll dice button is clicked
	 */
	public void rollDice() {
		// Disable sliders and buttons durign the animation
		btnRollDice.setDisable(true);
		diceSlider.setDisable(true);
		pointsRiskedSlider.setDisable(true);
		callToggleButton.setDisable(true);	
			
		// Play the audio clip for the dice roll	
		AudioClip rollDiceSFX = new AudioClip(getClass().getResource("/audio/roll_dice_SFX.mp3").toString());   	
		rollDiceSFX.play();  	
		
		// Index through all the 3D Dice
		for (int dieNum = 0; dieNum < numDice; dieNum++) {
			// Each individual rotation starts out at 250 ms, before slowing down
			int rotationDuration = 250;
			
			// Each dice roll animation consists a timeline with keyframes
			Timeline fullDiceRoll = dice3D[dieNum].rollDie(rotationDuration);
			
			// When the animation finishes it calls tallyDiceRolls()
			fullDiceRoll.setOnFinished(event -> tallyDiceRolls());
			
			// Play the animation
			fullDiceRoll.play();
		}
	}	
	
	/**
	 * Creates the boxes used as backgrounds for the 3D dice
	 * @param size  the length and width of the box
	 * @return Rectangle  a square with rounded edges of the specified size
	 */
	public Rectangle createBox(double size) {
		// Create a square of the specified size
	    Rectangle r = new Rectangle(size, size);
	    
	    // Round its corners with GAP as the radius
	    r.setArcHeight(GAP);
	    r.setArcWidth(GAP);
	    
	    // Set the fill of the square as the BACKGROUND_COLOUR
	    r.setFill(BACKGROUND_COLOUR);
	    
	    // Return the Rectangle object
	    return r;
	}
	
	/**
	 * This method, called by the quit button, opens the quit dialogPane
	 */
	public void openQuit() {
		quitDialog = new JFXDialog(dialogRoot, quitContent, JFXDialog.DialogTransition.CENTER);
		quitDialog.show();
	}
	
	/**
	 * This method, called by the cancel button in the quit
	 * DialogPane, closes the quit dialogPane
	 */
	public void closeQuit() {
		quitDialog.close();
	}
	
	
	/**
	 * This method, called by either the quit button in
	 * the quit DialogPane or the game over DialogPane,
	 * closes the program.
	 */
	public void quit() {
		System.exit(0);
	}
	
	/**
	 * This method, called by tallyDiceRolls() if the user
	 * lost the game, shows the game over DialogPane to the
	 * user.
	 */
	public void gameOver() {
		gameOverDialog = new JFXDialog(dialogRoot, gameOverContent, JFXDialog.DialogTransition.BOTTOM);
		gameOverDialog.show();
	}
	
	/**
	 * This method, called by the play again button
	 * in the game over dialog, resets the UI so that
	 * another game can be played
	 */
	public void playAgain() {
		// Reset the UI for the new game
		player.setCurrentPoints(1000);
		lblDiceRoll.setText("");
		pointsRiskedSlider.setMax(player.showPoints());
		pointsRiskedSlider.setValue(500);
		lblCurrentPoints.setText("Current Points: " + player.showPoints());
		
		// Close the game over dialog
		gameOverDialog.close();

		// Re-enable sliders and buttons
		btnRollDice.setDisable(false);
		diceSlider.setDisable(false);
		pointsRiskedSlider.setDisable(false);
		callToggleButton.setDisable(false);	
	}
	
	/**
	 * This method called by the rules button,
	 * opens the rules DialogPane
	 */
	public void openRules() {
		rulesDialog = new JFXDialog(dialogRoot, rulesContent, JFXDialog.DialogTransition.TOP);
		rulesDialog.show();
	}

	/**
	 * This method, called by the close button
	 * in the rules dialog, closes the rules dialog.
	 */
	public void closeRules() {
		rulesDialog.close();
	}
	
	/**
	 * This method tallies the dice rolls, and updates
	 * the player's points after all the dice have 
	 * finished rolling. It is called every single time
	 * a dice roll animation finishes and updates a counter
	 * to keep track of how many dice have finished rolling.
	 */
	public void tallyDiceRolls() {
		// Update the number of dice have been rolled
		numDiceRolled++;
		
		// Check to see if all the dice currently in play\
		// have been rolled
		if (numDiceRolled == numDice) {
			// Once all the dice have been rolled, reset the
			// numDiceRolled counter
			numDiceRolled = 0;
			
			// Tally all the dice rolls and store them in roll
			int roll = 0;
			for (int dieNum = 0 ; dieNum < numDice; dieNum++) { 
				roll += dice3D[dieNum].getCurrentRoll();
			}
			
			// Update the player's points based on the dice roll
			player.updateCurrentPoints(roll, numDice);
			
			// The points risked sldier's max must be updated so
			// that the user can risk all of the points they 
			// currently have.
			pointsRiskedSlider.setMax(player.showPoints());
			
			// Update the current points and dice roll labels
			lblCurrentPoints.setText("Current Points: " + player.showPoints());
			lblDiceRoll.setText("You rolled: " + roll);
			
			if (player.showPoints() == 0) {
				// If the player, ran out of points, show the game over dialog
				gameOver();
			}
			else {
				// Enable sliders and buttons if the game isn't over
				btnRollDice.setDisable(false);
				diceSlider.setDisable(false);
				pointsRiskedSlider.setDisable(false);
				callToggleButton.setDisable(false);	
			}
		}
	}
}
