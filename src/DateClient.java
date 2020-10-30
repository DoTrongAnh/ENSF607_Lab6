import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class DateClient {

	/**
	 * PrintWriter to read output stream of aSocket
	 */
	private PrintWriter socketOut;
	/**
	 * Socket to communicate with Server
	 */
	private Socket aSocket;
	/**
	 * BufferedReader to read user input
	 */
	private BufferedReader stdIn;
	/**
	 * BufferedReader to read input Stream of aSocket
	 */
	private BufferedReader socketIn;
	/**
	 * Constructs a DateClient with parameters for aSocket
	 * @param serverName Name of the Server
	 * @param portNumber Port Number of ServerSocket
	 */
	public DateClient(String serverName, int portNumber) {
		try {
			aSocket = new Socket(serverName, portNumber);
			stdIn = new BufferedReader(new InputStreamReader(System.in));
			socketIn = new BufferedReader(new InputStreamReader(aSocket.getInputStream()));
			socketOut = new PrintWriter(aSocket.getOutputStream(), true);
		} catch (UnknownHostException e) {
			System.err.println("ERROR: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}
	/**
	 * Talks to the Server
	 */
	public void converse() {
		String speak = "";
		String listen = "";
		while (true) {
			System.out.println("Please select an option (DATE/TIME):");
			try {
				speak = stdIn.readLine();
				socketOut.println(speak);
				if (!speak.equals("QUIT")) {
					listen = socketIn.readLine();
					System.out.println(listen);
				}
				else break;
			} catch (IOException e) {
				System.err.println("ERROR: " + e.getMessage());
			} 
		}
		try {
			stdIn.close();
			socketIn.close();
			socketOut.close();
		} catch (IOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		DateClient dc = new DateClient("localhost", 9090);
		dc.converse();

	}

}
