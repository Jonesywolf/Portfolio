package ticTacToe;

/**
 * @author John Wolf
 * Date April 2020
 * Course: ICS4U
 * TicTacToe.java
 * Responsible for UI and gameplay functionality of XOTIC
 */

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TicTacToe extends Application {
	// Formatting constants
	public static final int GAP = 15;
	public static double screenWidth;
	public static double screenHeight;

	// Formatting options for the labels
	private static final String LABEL_CSS = "-fx-font-family: 'Roboto Medium'; -fx-font-size: 32; -fx-text-fill: #776c66;";
	private static final String SUBTITLE_CSS = "-fx-font-family: 'Roboto Bold'; -fx-font-size: 28; -fx-text-fill: white;";

	// Displays the game's result
	private Label lblResult;

	// Quit dialog
	private JFXDialog quitDialog;

	// The content of the quit dialog is a VBox
	private VBox quitContent = new VBox(GAP);

	// The root is a StackPane, so dialogs can be used
	private StackPane dialogRoot = new StackPane();

	// The root of the scene
	private VBox root = new VBox(GAP);

	// Displays the Tic Tac Toe game "board"
	private GridPane gameBoard = new GridPane();

	// Button to reset the game
	private JFXButton btnReset = new JFXButton("RESET GAME");

	// Button to toggle the game mode
	private JFXButton btnGameMode = new JFXButton("SINGLEPLAYER EASY");

	// Keeps track of the turns taken, used to determine whose turn it is (X or O)
	// and to determine when a tie has been reached.
	private int turns = 0;

	// Stores whether the game has been won
	private boolean haveWinner = false;

	// Stores game mode
	private boolean isMultiplayer = false;

	// Stores whether AI is X
	public boolean isAIPlayerX = true;
	
	// Stores the game mode
	// 0 is Singleplayer easy
	// 1 is Singleplayer medium
	// 2 is Singleplayer hard
	// 3 is Multiplayer
	private int gameMode = 0;

	// Create a MinimaxAI object
	private MinimaxAI miniAI = new MinimaxAI();

	// Stores the array of squares used on the UI
	private Square[][] box;

	// Line indicating victory
	private Line winLine = new Line();
	private Pane winPane = new Pane(winLine);

	// Game board StackPane houses game board background, game board and win line
	private StackPane gameBoardStack = new StackPane();

	// UI SFX
	private AudioClip sfxPlaceX = new AudioClip(getClass().getResource("/audio/Tab_1.m4a").toString());
	private AudioClip sfxPlaceO = new AudioClip(getClass().getResource("/audio/Tab_2.m4a").toString());
	private AudioClip sfxPreviewPlay = new AudioClip(getClass().getResource("/audio/Button_5_QUIET.m4a").toString());
	private AudioClip sfxButton = new AudioClip(getClass().getResource("/audio/Button_3.m4a").toString());
	private AudioClip sfxWin = new AudioClip(getClass().getResource("/audio/Success_2.m4a").toString());

	@Override
	public void start(Stage myStage) throws Exception {
		// Instantiate Backgrounds based on the UI element's Z elevation:
		Background elevation1Colour = new Background(
				new BackgroundFill(Color.web("#faf8ef"), CornerRadii.EMPTY, Insets.EMPTY));
		Background elevation2Colour = new Background(
				new BackgroundFill(Color.web("#b9ada1"), new CornerRadii(5), Insets.EMPTY));

		// Get screen dimensions
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		screenWidth = primaryScreenBounds.getWidth();
		screenHeight = primaryScreenBounds.getHeight();

		/********* WIN LINE ********/
		// Set the winLine's formatting options
		winLine.setStrokeLineCap(StrokeLineCap.ROUND);
		winLine.setStrokeWidth(40);
		winLine.setEffect(new DropShadow(GAP, Color.rgb(0, 0, 0, 0.5)));

		/********* QUIT DIALOG CONTENT ********/
		// Set the quit dialog pane's content formatting options
		quitContent.setPadding(new Insets(GAP, GAP, GAP, GAP));

		// Create a label to confirm the user's intent to quit and set its formatting
		// options
		Label confirmQuit = new Label("ARE YOU SURE?");
		confirmQuit.setPadding(new Insets(GAP, GAP, GAP, GAP));
		confirmQuit.setStyle(SUBTITLE_CSS);
		confirmQuit.setBackground(elevation2Colour);
		quitContent.getChildren().add(confirmQuit);

		// Create an HBox to store buttons for the quit dialog
		HBox confirmQuitBtnsHBox = new HBox(GAP);
		confirmQuitBtnsHBox.setPadding(new Insets(GAP, GAP, GAP, GAP));

		// Create a button to quit the application and set its formatting options
		JFXButton btnQuit = new JFXButton("QUIT");
		btnQuit.setOnAction(event -> quit());
		btnQuit.setStyle("-fx-background-color: #e35e41;");
		confirmQuitBtnsHBox.getChildren().add(btnQuit);

		// Create a button to close the quit dialog and set its formatting options
		JFXButton btnCloseQuitDialog = new JFXButton("CANCEL");
		btnCloseQuitDialog.setOnAction(event -> closeQuit());
		confirmQuitBtnsHBox.getChildren().add(btnCloseQuitDialog);
		confirmQuitBtnsHBox.setAlignment(Pos.CENTER);
		quitContent.getChildren().add(confirmQuitBtnsHBox);

		// Set the quit dialog pane's content style options
		quitContent.setAlignment(Pos.TOP_CENTER);
		quitContent.setBackground(elevation1Colour);

		/********* ROOT ********/
		// Set the root's formatting options
		root.setPadding(new Insets(GAP, GAP, GAP, GAP));
		root.setAlignment(Pos.TOP_CENTER);
		root.setBackground(elevation1Colour);

		/********* LOGO ********/
		// Set the logo image's formatting options
		ImageView logoImg = new ImageView(
				new Image("/images/TTT_logo_text.png", screenWidth / 8.0, screenWidth / 8.0, true, true));
		logoImg.setPreserveRatio(true);
		root.getChildren().add(logoImg);

		/********* GAME BOARD ********/
		// Set gameBoard formatting options
		gameBoard.setHgap(3 * GAP + 10);
		gameBoard.setVgap(3 * GAP + 10);
		gameBoard.setAlignment(Pos.CENTER);

		gameBoard.setPrefWidth(screenWidth / 3);
		gameBoard.setMaxWidth(screenWidth / 3);

		gameBoard.setPrefHeight(screenWidth / 3);
		gameBoard.setMaxHeight(screenWidth / 3);

		/********* GAME BOARD BACKGROUND ********/
		// Set the game background image's formatting options
		ImageView gameBackgroundImg = new ImageView("/images/tictactoe_bg.png");
		gameBackgroundImg.setFitWidth(screenWidth / 3 - 2 * GAP);
		gameBackgroundImg.setPreserveRatio(true);

		/********* GAME BOARD STACK ********/
		// Set the game board stack's formatting options
		gameBoardStack.setMaxSize(screenWidth / 3, screenWidth / 3);
		gameBoardStack.setPadding(new Insets(GAP, GAP, GAP, GAP));

		gameBoardStack.setBackground(elevation2Colour);
		gameBoardStack.setEffect(new InnerShadow(GAP, Color.rgb(0, 0, 0, 0.5)));
		gameBoardStack.getChildren().addAll(gameBackgroundImg, gameBoard);

		root.getChildren().add(gameBoardStack);

		// Populate the game board with squares
		createSquares();

		/********* OPTION BUTTON HBOX ********/
		// Set the option button Hbox's formatting options
		HBox optionBtnsHBox = new HBox(GAP);
		optionBtnsHBox.setAlignment(Pos.CENTER);
		root.getChildren().add(optionBtnsHBox);

		/********* RESET BUTTON ********/
		// Set the reset button's formatting options
		btnReset.setOnAction(event -> resetGame());
		optionBtnsHBox.getChildren().add(btnReset);

		/********* OPEN QUIT DIALOG BUTTON ********/
		// Set the quit button's formatting options
		JFXButton btnOpenQuitDialog = new JFXButton("QUIT");
		btnOpenQuitDialog.setOnAction(event -> openQuit());
		btnOpenQuitDialog.setStyle("-fx-background-color: #e35e41;");
		optionBtnsHBox.getChildren().add(btnOpenQuitDialog);

		/********* GAME MODE BUTTON ********/
		// Set the gamemode button's formatting options
		btnGameMode.setOnAction(event -> switchGameMode());
		btnGameMode.setStyle("-fx-background-color: #00b34d");
		optionBtnsHBox.getChildren().add(btnGameMode);

		/********* RESULT LABEL ********/
		// Set the result label's formatting options
		lblResult = new Label();
		lblResult.setStyle(LABEL_CSS);
		root.getChildren().add(lblResult);
		GridPane.setHalignment(lblResult, HPos.CENTER);

		/********* GENERATE SCENE AND ADD IT TO THE STAGE ********/
		// Add root to the dialog StackPane
		dialogRoot.getChildren().add(root);
		Scene scene = new Scene(dialogRoot, screenWidth, screenHeight);

		scene.getStylesheets().add("https://fonts.googleapis.com/css?family=Roboto:400,500,700&display=swap");

		// Load the application's CSS
		scene.getStylesheets().add("/CSS/ticTacToe.css");

		myStage.setTitle("XOTIC");
		myStage.setScene(scene);
		myStage.setMaximized(true);
		myStage.getIcons().add(new Image("/images/TTT_logo.png"));
		myStage.show();

		// Prepare singleplayer if necessary
		if (isMultiplayer == false) {
			prepSingleplayer();
		}
	}

	// Mouseover enter event handler
	EventHandler<MouseEvent> enterEventHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent event) {
			// Returns the Square that generate this event
			Square temp = (Square) event.getSource();
			if (temp.getState() == Square.BLANK && haveWinner == false) {
				if (isMultiplayer == false && turns % 2 == 0 && isAIPlayerX == true) {
					// do nothing, it's the AI's turn
				} else if (isMultiplayer == false && turns % 2 != 0 && isAIPlayerX == false) {
					// do nothing, it's the AI's turn
				} else {
					// Play the preview sound effect
					sfxPreviewPlay.play();
					
					// Place the appropriate preview
					if (turns % 2 == 0) {
						temp.previewSquare(Square.X);
					} else {
						temp.previewSquare(Square.O);
					}
				}
			}
		}
	};

	// Mouseover exit event handler
	EventHandler<MouseEvent> exitEventHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent event) {
			// Returns the Square that generate this event
			Square temp = (Square) event.getSource();
			if (temp.getState() == Square.BLANK && haveWinner == false) {
				temp.removeSquarePreview();
			}
		}
	};

	// Mouse click event handler
	EventHandler<MouseEvent> clickEventHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent event) {
			// Returns the Square that generate this event
			Square temp = (Square) event.getSource();

			// Ensures the square is blank, before placing the player's X or O
			if (temp.getState() == Square.BLANK && haveWinner == false) {
				if (isMultiplayer == false && turns % 2 == 0 && isAIPlayerX == true) {
					// do nothing
				} else if (isMultiplayer == false && turns % 2 != 0 && isAIPlayerX == false) {
					// do nothing
				} else {
					if (turns % 2 == 0) {
						sfxPlaceX.play();
						temp.playSquare(Square.X);
					} else {
						sfxPlaceO.play();
						temp.playSquare(Square.O);
					}
					// Increment turns
					turns++;
					// Check if the game's over
					manageTurns();
				}
			}
		}
	};

	public void manageTurns() {
		char[][] charBoard = MinimaxAI.boardToChar(box);
		char winner = miniAI.checkForWinner(charBoard);
		if (winner == 'N' && isMultiplayer == false) {
			int[] bestMove = miniAI.findBestMove(charBoard, isAIPlayerX);
			if (turns % 2 == 0 && isAIPlayerX == true) {
				sfxPlaceX.play();
				box[bestMove[0]][bestMove[1]].playSquare(Square.X);
				// Increment turns
				turns++;
				// Check if the game's over
				manageTurns();
			} else if (isMultiplayer == false && turns % 2 != 0 && isAIPlayerX == false) {
				sfxPlaceO.play();
				box[bestMove[0]][bestMove[1]].playSquare(Square.O);
				// Increment turns
				turns++;
				// Check if the game's over
				manageTurns();
			}
		} else if (winner == 'X') {
			drawWinLine(box[miniAI.startWinRow][miniAI.startWinCol], box[miniAI.endWinRow][miniAI.endWinCol]);
			gameOver("X wins.");
		} else if (winner == 'O') {
			drawWinLine(box[miniAI.startWinRow][miniAI.startWinCol], box[miniAI.endWinRow][miniAI.endWinCol]);
			gameOver("O wins.");
		} else if (winner == 'T') {
			gameOver("Tie.");
		}
	}

	public void prepSingleplayer() {
		// Random seed between 1 and 0
		int firstPlayerSeed = (int) Math.round(Math.random());
		if (firstPlayerSeed == 0) {
			isAIPlayerX = true;
		} else {
			isAIPlayerX = false;
		}
		manageTurns();
	}

	/**
	 * Displays the game's result in lblResult
	 * 
	 * @param gameResult the outcome of the game (X won, O won, Tie)
	 */
	public void gameOver(String gameResult) {
		lblResult.setText("Game over, " + gameResult);
		haveWinner = true;
		sfxWin.play();
	}

	/**
	 * Draws the win line through a satisfying animation
	 * @param startSquare  the Square at the start of the win
	 * @param endSquare  the Square at the end of the win
	 */
	public void drawWinLine(Square startSquare, Square endSquare) {

		// Get the relative bounds of the start and endSquare within the gameBoardStack
		Bounds startSquareInGameBoardStack = getRelativeBounds(startSquare, gameBoardStack);
		Bounds endSquareInGameBoardStack = getRelativeBounds(endSquare, gameBoardStack);
		Point2D n1Center = getCenter(startSquareInGameBoardStack);
		Point2D n2Center = getCenter(endSquareInGameBoardStack);

		// Set the appropriate colour for the win line
		if (turns % 2 == 0) {
			winLine.setStroke(Color.web("#2594af"));
		} else {
			winLine.setStroke(Color.web("#ff6600"));
		}

		// Set the start of the winLine
		winLine.setStartX(n1Center.getX() - GAP);
		winLine.setStartY(n1Center.getY() - GAP);

		Timeline winAnimation = new Timeline();

		// Use the line's properties to make a satisfying win animation
		KeyFrame initialState = new KeyFrame(Duration.ZERO, new KeyValue(winLine.endXProperty(), n1Center.getX() - GAP),
				new KeyValue(winLine.endYProperty(), n1Center.getY() - GAP));
		KeyFrame endState = new KeyFrame(Duration.millis(500),
				new KeyValue(winLine.endXProperty(), n2Center.getX() - GAP),
				new KeyValue(winLine.endYProperty(), n2Center.getY() - GAP));

		winAnimation.getKeyFrames().addAll(initialState, endState);

		// Set the winLine's coordinates, account for the Insets on the gameBoardStack
		gameBoardStack.getChildren().add(winPane);
		winAnimation.play();
	}

	/**
	 * Retrieves the relative bounds of an object to a parent
	 * 
	 * @param node       the node to find the relative Bounds of
	 * @param relativeTo the parent of the node
	 * @return the relative Bounds
	 */
	private Bounds getRelativeBounds(Node node, Node relativeTo) {
		Bounds nodeBoundsInScene = node.localToScene(node.getBoundsInLocal());
		return relativeTo.sceneToLocal(nodeBoundsInScene);
	}

	/**
	 * Retrieves the center of a Bounds object Source:
	 * https://stackoverflow.com/questions/43115807/how-to-draw-line-between-two-nodes-placed-in-different-panes-regions
	 * 
	 * @param b the Bounds to get the center of
	 * @return Point2D containing the center of the Bounds object
	 */
	private Point2D getCenter(Bounds b) {
		return new Point2D(b.getMinX() + b.getWidth() / 2, b.getMinY() + b.getHeight() / 2);
	}

	/**
	 * Resets the game
	 */
	public void resetGame() {
		// Play the button sound effect
		sfxButton.play();

		// Reset the result label
		lblResult.setText("");

		// Reset haveWinner
		haveWinner = false;

		// Reset turns
		turns = 0;

		// Remove winLine
		gameBoardStack.getChildren().remove(winPane);

		// Remove all pre-existing squares
		for (int row = 0; row <= 2; row++) {
			for (int col = 0; col <= 2; col++) {
				gameBoard.getChildren().remove(box[row][col]);
			}
		}

		// Refresh the game UI
		createSquares();

		// Prep singleplayer if necessary
		if (isMultiplayer == false) {
			prepSingleplayer();
		}
	}

	/**
	 * Switches the gameMode from singleplayer easy, medium, hard, ro multiplayer
	 */
	public void switchGameMode() {
		// Play the button sound effect
		sfxButton.play();
		
		// Update the gameMode
		if (gameMode == 3) {
			gameMode = 0;
		}
		else {
			gameMode++;
		}
		
		// Update the game mode button and the game's properties according to the new gameMode
		switch (gameMode) {
			case 0:
				isMultiplayer = false;
				miniAI.setSimplicity(40);
				btnGameMode.setText("SINGLEPLAYER EASY");
				btnGameMode.setStyle("-fx-background-color: #00b34d");
				break;
			case 1:
				isMultiplayer = false;
				miniAI.setSimplicity(20);
				btnGameMode.setText("SINGLEPLAYER MEDIUM");
				btnGameMode.setStyle("-fx-background-color: #b0b300");
				break;
			case 2:
				isMultiplayer = false;
				miniAI.setSimplicity(0);
				btnGameMode.setText("SINGLEPLAYER HARD");
				btnGameMode.setStyle("-fx-background-color: #e35e41;");
				break;
			case 3:
				isMultiplayer = true;
				btnGameMode.setText("MULTIPLAYER");
				btnGameMode.setStyle("-fx-background-color: #8c00ff;");
				break;
		}
	}

	/**
	 * Adds Squares to the gameBoard
	 */
	public void createSquares() {
		box = new Square[3][3];
		// Instantiate each Square in the 2D array, then add each Square to the GridPane
		for (int row = 0; row <= 2; row++) {
			for (int col = 0; col <= 2; col++) {
				box[row][col] = new Square(screenWidth);

				// Call the event handler method and pass a reference to the ActionEvent
				box[row][col].setOnMouseClicked(clickEventHandler);
				box[row][col].setOnMouseEntered(enterEventHandler);
				box[row][col].setOnMouseExited(exitEventHandler);
				gameBoard.add(box[row][col], col, row);
			}
		}
	}

	/**
	 * This method, called by the quit button, opens the quit dialogPane
	 */
	public void openQuit() {
		// Play the button sound effect
		sfxButton.play();

		quitDialog = new JFXDialog(dialogRoot, quitContent, JFXDialog.DialogTransition.CENTER);
		quitDialog.show();
	}

	/**
	 * This method, called by the cancel button in the quit DialogPane, closes the
	 * quit dialogPane
	 */
	public void closeQuit() {
		// Play the button sound effect
		sfxButton.play();

		quitDialog.close();
	}

	/**
	 * This method, called by either the quit button in the quit DialogPane or the
	 * game over DialogPane, closes the program.
	 */
	public void quit() {
		// Play the button sound effect
		sfxButton.play();

		System.exit(0);
	}

	/**
	 * This method launches the UI
	 * 
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
