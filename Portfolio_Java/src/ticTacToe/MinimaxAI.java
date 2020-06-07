package ticTacToe;

import java.util.Random;

/**
 * @author John Wolf
 * Date April 2020
 * Course: ICS4U
 * Minimax.java
 * Responsible for the AI player functionality in TicTacToe.java
 * Based on this article on the minimax algorithm:
 * https://www.geeksforgeeks.org/minimax-algorithm-in-game-theory-set-3-tic-tac-toe-ai-finding-optimal-move/?ref=lbp
 */

public class MinimaxAI {
	// Stores the coordinates of the win, used to draw the win line on the GUI
	public int startWinRow;
	public int startWinCol;
	public int endWinRow;
	public int endWinCol;
	
	// Determines how easy the AI is to play against
	// 40 is easy
	// 20 is medium
	// 0 is impossible
	private int simplicity = 40;

	/**
	 * The MiniMaxAI constructor
	 */
	public MinimaxAI() {
	}

	/**
	 * Converts the Square box[][] to a char[][] representation using getState()
	 * @param box  the game's Square box[][]
	 * @return  char[][] representation of the Square box[][]
	 */
	public static char[][] boardToChar(Square[][] box) {
		char[][] charBoard = new char[3][3];
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				charBoard[row][col] = box[row][col].getState();
			}
		}
		return charBoard;
	}

	/**
	 * A Minimax algorithm implementation which determines all possible outcomes from a given Tic Tac Toe board state
	 * @param board  char[][] containing the current state of the board
	 * @param depth  how many turns it would take to reach this state
	 * @param isMax  stores whether the current simulated player is the maximizer (X) or the minimizer (O)
	 * @return the score of the sequence of events, accommodating
	 */
	public int minimax(char[][] board, int depth, boolean isMax) {
		int score = 0;
		// Check if the game's over
		char winner = checkForWinner(board);
		
		// If it is, set the score accordingly
		if (winner == 'X') {
			score = 10 - depth;
		} else if (winner == 'O') {
			score = -10 + depth;
		} else if (winner == 'T') {
			score = 0;
		} else {
			// Otherwise continue make moves
			// If this is the maximizer's (X's) move
			if (isMax) {
				// Set the best score to a large negative number to start
				int bestScore = -1000;

				// Traverse all squares
				for (int y = 0; y < 3; y++) {
					for (int x = 0; x < 3; x++) {
						// Check if cell is empty
						if (board[y][x] == Square.BLANK) {
							// Make the move
							board[y][x] = Square.X;

							// Call minimax recursively and choose the maximum value
							bestScore = Math.max(bestScore, minimax(board, depth + 1, !isMax));

							// Undo the move
							board[y][x] = Square.BLANK;
						}
					}
				}
				// Update the bestScore
				score = bestScore;
			}

			// If this is the minimizer's (O's) move
			else {
				// Set the best score to a large positive number to start
				int bestScore = 1000;

				// Traverse all squares
				for (int y = 0; y < 3; y++) {
					for (int x = 0; x < 3; x++) {
						// Check if cell is empty
						if (board[y][x] == Square.BLANK) {
							// Make the move
							board[y][x] = Square.O;

							// Call minimax recursively and choose
							// the minimum value
							bestScore = Math.min(bestScore, minimax(board, depth + 1, !isMax));

							// Undo the move
							board[y][x] = Square.BLANK;
						}
					}
				}
				// Update the bestScore
				score = bestScore;
			}
		}
		return score;
	}

	/**
	 * Determines the AI's next move
	 * @param board  the Square[][] box in char[][] form
	 * @param isAIPlayerX  determines whether the AI is playing as X or O
	 * @return  int[] storing the row and column of the AI's best move
	 */
	public int[] findBestMove(char board[][], boolean isAIPlayerX) {
		// Stores the best possible score for the AI
		int bestScore;
		
		if (isAIPlayerX == true) {
			// Set it to a large negative value
			bestScore = -1000;
		}
		else {
			// Set it to a large positive value
			bestScore = 1000;
		}
		
		// Stores the best move at {row, col}
		int[] bestMove = {-1, -1};

		// Traverse all cells, evaluate minimax function
		// for all empty cells. And return the cell
		// with the optimal value (maximum for X, minimum for O).
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				// Check if cell is empty
				if (board[y][x] == Square.BLANK) {
					
					// Stores the score for moving to this cell
					int moveScore;
					if (isAIPlayerX == true) {
						// Make the move
						board[y][x] = Square.X;
						moveScore = minimax(board, 0, false);
					}
					else {
						// Make the move
						board[y][x] = Square.O;
						moveScore = minimax(board, 0, true);
					}

					// Undo the move
					board[y][x] = Square.BLANK;

					// Generate a random int from 0 to simplicity
					Random rand = new Random();
				    int randomNum = rand.nextInt(simplicity + 1);

					// X tries to maximize its score, so the best score is the highest one
					if (moveScore > bestScore && isAIPlayerX == true) {
						// If the absolute difference between the simplicity and the randomNum is less than 10,
						// or the best move has not yet been set (avoids unset moves), update the bestMove and bestScore
						if (Math.abs(simplicity - randomNum) < 10 || bestMove[0] == -1) {
							// Store the move's coordinates with the highest score
							bestMove[0] = y;
							bestMove[1] = x;
							bestScore = moveScore;
						}
					}
					// O tries to minimize its score, so the best score is the lowest one
					else if (moveScore < bestScore && isAIPlayerX == false) {
						// If the absolute difference between the simplicity and the randomNum is less than 10,
						// or the best move has not yet been set (avoids unset moves), update the bestMove and bestScore
						if (Math.abs(simplicity - randomNum) < 10 || bestMove[0] == -1) {
							// Store the move's coordinates with the lowest score
							bestMove[0] = y;
							bestMove[1] = x;
							bestScore = moveScore;
						}
					}
				}
			}
		}
		return bestMove;
	}

	/**
	 * Determines whether the game is over yet
	 * @param board  the Square[][] box in char[][] form
	 * @return  char 'N' indicates game isn't over
	 * 				 'T' indicates a tie
	 * 				 'X' indicates X won
	 * 				 'O' indicates O won
	 */
	public char checkForWinner(char board[][]) {
		char winner = 'N';
		// Check for a winner by row
		for (int row = 0; row < 3; row++) {
			if (board[row][0] == board[row][1] && board[row][1] == board[row][2]) {

				if (board[row][0] == Square.X) {
					winner = 'X';
					startWinRow = row;
					startWinCol = 0;
					endWinRow = row;
					endWinCol = 2;
				} else if (board[row][0] == Square.O) {
					winner = 'O';
					startWinRow = row;
					startWinCol = 0;
					endWinRow = row;
					endWinCol = 2;
				}
			}
		}
		// Check for a winner by column
		for (int col = 0; col < 3; col++) {
			if (board[0][col] == board[1][col] && board[1][col] == board[2][col]) {

				if (board[0][col] == Square.X) {
					winner = 'X';
					
					// Set the start and end of win coordinates
					startWinRow = 0;
					startWinCol = col;
					endWinRow = 2;
					endWinCol = col;
				} else if (board[0][col] == Square.O) {
					winner = 'O';
					
					// Set the start and end of win coordinates
					startWinRow = 0;
					startWinCol = col;
					endWinRow = 2;
					endWinCol = col;
				}
			}
		}

		// Check for a winner diagonally (top left to bottom right)
		if (board[0][0] == board[1][1] && board[1][1] == board[2][2]) {

			if (board[1][1] == Square.X) {
				winner = 'X';
				
				// Set the start and end of win coordinates
				startWinRow = 0;
				startWinCol = 0;
				endWinRow = 2;
				endWinCol = 2;
			} else if (board[1][1] == Square.O) {
				winner = 'O';
				
				// Set the start and end of win coordinates
				startWinRow = 0;
				startWinCol = 0;
				endWinRow = 2;
				endWinCol = 2;
			}
		}
		// Check for a winner diagonally (top right to bottom left)
		else if (board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
			if (board[1][1] == Square.X) {
				winner = 'X';
				
				// Set the start and end of win coordinates
				startWinRow = 0;
				startWinCol = 2;
				endWinRow = 2;
				endWinCol = 0;
			} else if (board[1][1] == Square.O) {
				winner = 'O';
				
				// Set the start and end of win coordinates
				startWinRow = 0;
				startWinCol = 2;
				endWinRow = 2;
				endWinCol = 0;
			}
		}

		// If it's the last turn and there hasn't been a winner
		if (isBoardFull(board) == true && winner == 'N') {
			winner = 'T';
		}
		return winner;
	}

	/**
	 * Determines whether there are blank spaces still on the board
	 * @param board  the Square[][] box in char[][] form
	 * @return  boolean, false means there are still free squares, true means there are no more free spaces
	 */
	private static boolean isBoardFull(char[][] board) {
		boolean boardFull = true;
		// Cycle through the board, if a blank space is found, set boardFull to false
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				if (board[y][x] == Square.BLANK) {
					boardFull = false;
				}
			}
		}
		return boardFull;		
	}
	
	/**
	 * Sets the AI's simplicity value, higher is easier
	 * @param simplicity
	 */
	public void setSimplicity(int newSimplicity) {
		simplicity = newSimplicity;
	}

}
