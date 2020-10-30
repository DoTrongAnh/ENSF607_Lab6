package inlab2;
/**
 * This class keeps track of the states of the tick-tack-toe board
 * @author anhtr
 *
 */

//STUDENTS SHOULD ADD CLASS COMMENTS, METHOD COMMENTS, FIELD COMMENTS 


public class Board implements Constants {
	private char theBoard[][];//The double array storing marks
	private int markCount;//Current mark count on the board (max 9)

	/**
	 * Construct a 3x3 blank board
	 */
	public Board() {
		markCount = 0;
		theBoard = new char[3][];
		for (int i = 0; i < 3; i++) {
			theBoard[i] = new char[3];
			for (int j = 0; j < 3; j++)
				theBoard[i][j] = SPACE_CHAR;
		}
	}
	
	/**
	 * Returns the mark X or O is at board location
	 * @param row
	 * Row index
	 * @param col
	 * Column index
	 * @return mark on the row and column
	 */
	public char getMark(int row, int col) {
		return theBoard[row][col];
	}

	/**
	 * Checks if the board is completely filled
	 * @return if the board is full
	 */
	public boolean isFull() {
		return markCount == 9;
	}

	/**
	 * Checks if X player wins
	 * @return if X wins
	 */
	public boolean xWins() {
		if (checkWinner(LETTER_X) == 1)
			return true;
		else
			return false;
	}

	/**
	 * Checks if O player wins
	 * @return if O wins
	 */
	public boolean oWins() {
		if (checkWinner(LETTER_O) == 1)
			return true;
		else
			return false;
	}
	
	/**
	 * Display the board's current state onto terminal
	 */
	public String display() {
		String s = displayColumnHeaders();//1st line
		s += addHyphens();//2nd line
		for (int row = 0; row < 3; row++) {
			s += addSpaces();//3rd, 7th, 11th line
			s += "    row " + row + ' ';//4th, 8th, 12th line
			for (int col = 0; col < 3; col++)
				s += "|  " + getMark(row, col) + "  ";
			s += "|\n";
			s += addSpaces();//5th, 9th, 13th line
			s += addHyphens();//6th, 10th, 14th line
		}
		return s;
	}

	/**
	 * Adds the input mark on the board of input position
	 * @param row
	 * Row index
	 * @param col
	 * Column index
	 * @param mark
	 * Mark to be added to row and column
	 */
	public void addMark(int row, int col, char mark) {
		
		theBoard[row][col] = mark;
		markCount++;
	}

	/**
	 * Clears the board of all marks
	 */
	public void clear() {
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				theBoard[i][j] = SPACE_CHAR;
		markCount = 0;
	}

	/**
	 * Determines whether or not player with mark wins
	 * @param mark
	 * Mark to check
	 * @return 0 if player with the mark does not win and 1 otherwise
	 */
	int checkWinner(char mark) {
		int row, col;
		int result = 0;

		for (row = 0; result == 0 && row < 3; row++) {
			int row_result = 1;
			for (col = 0; row_result == 1 && col < 3; col++)
				if (theBoard[row][col] != mark)
					row_result = 0;
			if (row_result != 0)
				result = 1;
		}

		
		for (col = 0; result == 0 && col < 3; col++) {
			int col_result = 1;
			for (row = 0; col_result != 0 && row < 3; row++)
				if (theBoard[row][col] != mark)
					col_result = 0;
			if (col_result != 0)
				result = 1;
		}

		if (result == 0) {
			int diag1Result = 1;
			for (row = 0; diag1Result != 0 && row < 3; row++)
				if (theBoard[row][row] != mark)
					diag1Result = 0;
			if (diag1Result != 0)
				result = 1;
		}
		if (result == 0) {
			int diag2Result = 1;
			for (row = 0; diag2Result != 0 && row < 3; row++)
				if (theBoard[row][3 - 1 - row] != mark)
					diag2Result = 0;
			if (diag2Result != 0)
				result = 1;
		}
		return result;
	}

	/**
	 * Displays column headers
	 */
	String displayColumnHeaders() {
		String s = "          ";
		for (int j = 0; j < 3; j++)
			s += "|col " + j;
		return s + "\n";
	}

	/**
	 * Adds hyphen between rows
	 */
	String addHyphens() {
		String s = "          ";
		for (int j = 0; j < 3; j++)
			 s += "+-----";
		return s + "+\n";
	}

	/**
	 * Adds spacing between columns
	 */
	String addSpaces() {
		String s = "          ";
		for (int j = 0; j < 3; j++)
			s += "|     ";
		return s + "|\n";
	}
}
