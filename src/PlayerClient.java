import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class PlayerClient implements Constants{

	/**
	 * Writer to communicate Client input
	 */
	private PrintWriter socketOut;
	/**
	 * Socket to speak to Game
	 */
	private Socket aSocket;
	/**
	 * Reading user input
	 */
	private BufferedReader stdIn;
	/**
	 * Reading Game input
	 */
	private BufferedReader socketIn;
	/**
	 * Player's mark
	 */
	private char mark;
	/**
	 * Constructs PlayerClient with server name and port number
	 * @param serverName
	 * @param portNumber
	 */
	public PlayerClient(String serverName, int portNumber) {
		try {
			aSocket = new Socket(serverName, portNumber);
			stdIn = new BufferedReader(new InputStreamReader(System.in));
			socketIn = new BufferedReader(new InputStreamReader(aSocket.getInputStream()));
			socketOut = new PrintWriter(aSocket.getOutputStream(), true);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Inputs player's name to the Game
	 * @throws IOException
	 */
	public void introduce() throws IOException {
		System.out.println("Awaiting another player....");
		String read = socketIn.readLine();//Get told what the mark is
		System.out.println(read);
		System.out.println("A new game has been created.");
		mark = read.charAt(0);
		if(mark == LETTER_O) {
			read = socketIn.readLine();//Wait while X player introduces first
			System.out.println(read);
		}
		read = socketIn.readLine();//Introduce yourself
		System.out.println(read);
		read = stdIn.readLine();//Input your name
		socketOut.println(read);
		if(mark == LETTER_X) {
			read = socketIn.readLine();
			System.out.println(read);//Your opponent name
		}
	}
	/**
	 * Where player make a move
	 */
	public void play() {
		String read = "";
		if(mark == LETTER_O) {
			for(int i = 0; i < 15; i++) {
				try {
					read = socketIn.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println(read);
			}
		}
		while(true) {
			//Your turn
			try {
				
				read = socketIn.readLine();
				System.out.println(read);//Ask for row input OR announce a game over
				if(read.contentEquals("GAME OVER!")) break;
				int row, col;
				while (true) {
					read = stdIn.readLine();//Input row number
					try {
						row = Integer.parseInt(read);
					} catch (NumberFormatException e) {
						System.err.println("Invalid input, try again!");
						continue;
					}
					break;
				}
				socketOut.println(row);//Communicate row number
				read = socketIn.readLine();
				System.out.println(read);//Ask for col number
				while (true) {
					read = stdIn.readLine();//Input col number
					try {
						col = Integer.parseInt(read);
					} catch (NumberFormatException e) {
						System.err.println("Invalid input, try again!");
						continue;
					}
					break;
				}
				socketOut.println(col);//Communicate col number
				read = socketIn.readLine();
				System.out.println(read);//Print board OR announce invalid move.
				if(!read.equalsIgnoreCase("Invalid move. Try again."))
					for(int i = 1; i < 15; i++) {
						read = socketIn.readLine();
						System.out.println(read);
					}
				else continue;
				read = socketIn.readLine();
				System.out.println(read);
				if(read.contentEquals("GAME OVER!")) break;
				//Opponent's turn
				for(int i = 1; i < 15; i++) {
					read = socketIn.readLine();
					System.out.println(read);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}		
		}
		try {
			read = socketIn.readLine();
			System.out.println(read);//Announce the outcome
			stdIn.close();
			socketIn.close();
			socketOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException{
		PlayerClient pc = new PlayerClient("localhost", 8081);
		pc.introduce();
		pc.play();

	}

}
