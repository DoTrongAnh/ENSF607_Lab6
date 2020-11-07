package server_only;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GameGUI implements Runnable, Constants{

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
	public GameGUI(Socket xSocket, Socket oSocket) {
		theBoard  = new Board();
        this.xSocket = xSocket;
    	this.oSocket = oSocket;
    	try {
			xInput = new BufferedReader(new InputStreamReader(this.xSocket.getInputStream()));
			oInput = new BufferedReader(new InputStreamReader(this.oSocket.getInputStream()));
			xOutput = new PrintWriter(this.xSocket.getOutputStream(), true);
			oOutput = new PrintWriter(this.oSocket.getOutputStream(), true);
			//Introduction: assigns mark for both players
	    	xOutput.println(LETTER_X);
	    	oOutput.println(LETTER_O);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
	}
	
	@Override
	public void run() {
		//Introduction of players
		try {
			String name1 = xInput.readLine();
			String name2 = oInput.readLine();
			xName = name1;
			oName = name2;
			xOutput.println("You are up against " + oName);
			oOutput.println("You are up against " + xName);
			xOutput.println("OK");
			oOutput.println("OK");
		} catch (IOException e) {
			xOutput.println("Connection error. Game terminated.");
			oOutput.println("Connection error. Game terminated.");
			close();
			return;
		}
		//Introduction complete: Game commences
		String read = "";
		try {
			while(true) {
				//X's turn
				oOutput.println(xName + "'s turn.");
				while(true) {
					xOutput.println("Your turn.");
					read = xInput.readLine();
					int row = Integer.parseInt(read);
					read = xInput.readLine();
					int col = Integer.parseInt(read);
					//Check if move is valid - might be redundant
					if(col < 0 || col > 2 || row < 0 || row > 2 || theBoard.getMark(row, col) != SPACE_CHAR) {
						xOutput.println("Invalid move. Try again.");
						continue;
					}
					theBoard.addMark(row, col, LETTER_X);
					oOutput.println(row);
					oOutput.println(col);
					break;
				}
				//Checking win/end condition
				if(theBoard.xWins() || theBoard.isFull()) {
					xOutput.println("GAME OVER");
					oOutput.println("GAME OVER");
					break;
				}
				else {
					xOutput.println("You have made a move.");
					oOutput.println(xName + " has made a move.");
				}
				//O's turn
				xOutput.println(xName + "'s turn.");
				while(true) {
					oOutput.println("Your turn.");
					read = oInput.readLine();
					int row = Integer.parseInt(read);
					read = oInput.readLine();
					int col = Integer.parseInt(read);
					//Check if move is valid - might be redundant
					if(col < 0 || col > 2 || row < 0 || row > 2 || theBoard.getMark(row, col) != SPACE_CHAR) {
						oOutput.println("Invalid move. Try again.");
						continue;
					}
					theBoard.addMark(row, col, LETTER_O);
					xOutput.println(row);
					xOutput.println(col);
					break;
				}
				//Checking win/end condition
				if(theBoard.oWins() || theBoard.isFull()) {
					xOutput.println("GAME OVER");
					oOutput.println("GAME OVER");
					break;
				}
				else {
					oOutput.println("You have made a move.");
					xOutput.println(oName + " has made a move.");
				}
			}//End of game
			//Time to announce result
			if(theBoard.xWins()) {
				xOutput.println("YOU WIN");
				oOutput.println("YOU LOSE");
			}
			else if(theBoard.oWins()) {
				xOutput.println("YOU LOSE");
				oOutput.println("YOU WIN");
			}
			else {
				xOutput.println("IT'S A DRAW");
				oOutput.println("IT'S A DRAW");
			}
		} catch (NumberFormatException e) {
		} catch (IOException e) {
			xOutput.println("Connection error. Game terminated.");
			oOutput.println("Connection error. Game terminated.");
			close();
			return;
		}
		close();
	}
	/**
	 * Closes the readers and writers
	 */
	private void close() {
		try {
			xInput.close();
			xOutput.close();
			oInput.close();
			oOutput.close();
			xSocket.close();
			oSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
