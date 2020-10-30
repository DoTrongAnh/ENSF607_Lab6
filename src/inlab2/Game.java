package inlab2;

import java.io.*;
import java.net.Socket;

//STUDENTS SHOULD ADD CLASS COMMENTS, METHOD COMMENTS, FIELD COMMENTS 
/**
 * This class keeps track of the state of the game
 * @author anhtr
 *
 */

public class Game implements Constants, Runnable {

	/**
	 * Two sockets to communicate with two players
	 */
	private Socket xSocket, oSocket;
	/**
	 * Reading input of two players
	 */
	private BufferedReader xInput, oInput;
	/**
	 * Speaking to two players separately
	 */
	private PrintWriter xOutput, oOutput;
	private Board theBoard;//Object board on which the game happens
	/**
	 * Name of two players
	 */
	private String xName, oName;
	/**
	 * Constructs the game to handle two sockets
	 */
    public Game(Socket xSocket, Socket oSocket ) {
        theBoard  = new Board();
        this.xSocket = xSocket;
    	this.oSocket = oSocket;
	}

    @Override
	public void run() {
    	try {
			xInput = new BufferedReader(new InputStreamReader(this.xSocket.getInputStream()));
			oInput = new BufferedReader(new InputStreamReader(this.oSocket.getInputStream()));
			xOutput = new PrintWriter(this.xSocket.getOutputStream(), true);
			oOutput = new PrintWriter(this.oSocket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	//Introduction: assigns mark for both players
    	xOutput.println(LETTER_X);
    	oOutput.println(LETTER_O);
		String read = "";
		String write = "O player, wait until X player introduce themselves...";
		try {
			oOutput.println(write);
			xOutput.println("X player, introduce yourself:");
			read = xInput.readLine();
			xName = read.toString();
			oOutput.println("Looks like " + read + " will be your opponent. O player, introduce yourself:");
			read = oInput.readLine();
			oName = read.toString();
			xOutput.println("Looks like " + read + " will be your opponent.");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		//End of Introduction. Game commences.
		try {
			while(true) {
				
				if(theBoard.isFull()) {
					xOutput.println("GAME OVER!");
					oOutput.println("GAME OVER!");
					break;
				}
				int row = 0, col = 0;
				//X turn
				while(true) {
					xOutput.println(xName + ", which row is your next X mark?");
					try {
						read = xInput.readLine();
						row = Integer.parseInt(read);
					} catch (NumberFormatException e) {}
					xOutput.println(xName + ", which colum is your next X mark?");
					try {
						read = xInput.readLine();
						col = Integer.parseInt(read);
					} catch (NumberFormatException e) {}
					if(col < 0 || col > 2 || row < 0 || row > 2 || theBoard.getMark(row, col) != SPACE_CHAR) {
						xOutput.println("Invalid move. Try again.");
						continue;
					}
					else theBoard.addMark(row, col, LETTER_X);
					xOutput.println(theBoard.display());
					oOutput.println(theBoard.display());
					break;
				}
				if(theBoard.xWins() || theBoard.isFull()) {
					xOutput.println("GAME OVER!");
					oOutput.println("GAME OVER!");
					break;
				}
				//O turn
				while(true) {
					oOutput.println(oName + ", which row is your next O mark?");
					try {
						read = oInput.readLine();
						row = Integer.parseInt(read);
					} catch (NumberFormatException e) {}
					oOutput.println(oName + ", which colum is your next O mark?");
					try {
						read = oInput.readLine();
						col = Integer.parseInt(read);
					} catch (NumberFormatException e) {}
					if(col < 0 || col > 2 || row < 0 || row > 2 || theBoard.getMark(row, col) != SPACE_CHAR) {
						oOutput.println("Invalid move. Try again.");
						continue;
					}
					else theBoard.addMark(row, col, LETTER_O);
					oOutput.println(theBoard.display());
					xOutput.println(theBoard.display());
					break;
				}
				if(theBoard.oWins() || theBoard.isFull()) {
					xOutput.println("GAME OVER!");
					oOutput.println("GAME OVER!");
					break;
				}
			}
			
			if(!theBoard.xWins() && !theBoard.oWins()) {
				xOutput.println("It's a draw!");
				oOutput.println("It's a draw!");
			}
			else if(theBoard.xWins()) {
				xOutput.println("You win!");
				oOutput.println("You lose!");
			}
			else {
				oOutput.println("You win!");
				xOutput.println("You lose!");
			}
		}
		 catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			xInput.close();
			xOutput.close();
			oInput.close();
			oOutput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
