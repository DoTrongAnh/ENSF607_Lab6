package inlab2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	private BufferedReader socketInput;
	private PrintWriter socketOutput;
	private ServerSocket serverSocket;
	private Socket aSocket;
	
	/**
	 * Construct a Server with Port 8099
	 */
	public Server() {
		try {
			serverSocket = new ServerSocket(8099);
			System.out.println("Server is now running.");
			aSocket = serverSocket.accept();
			socketInput = new BufferedReader(new InputStreamReader(aSocket.getInputStream()));
			socketOutput = new PrintWriter(aSocket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get input from Client.
	 * 
	 * @throws IOException
	 */
	public void getUserInput() throws IOException {
		String line = null;
		while (true) {
			line = socketInput.readLine();
			System.out.println("Received: " + line);
			if (line == null) {
				break;
			}
			String extra = "";
			if (isPalindrome(line.toUpperCase())) {
				extra += " is a Palindrome.";
			} 
			else {
				extra += " is not a Palindrome.";
			}
			socketOutput.println(line + extra);
		}
		
		socketInput.close();
		socketOutput.close();
		serverSocket.close();
	}

	/**
	 * Checks if the String input is a palindrome
	 * @param s String to check if palindrome.
	 * @return boolean indicating whether or not s is a palindrome.
	 */
	private boolean isPalindrome(String s) {
		for(int i = 0; i < s.length()/2; i++)
			if(s.charAt(i) != s.charAt(s.length()-1-i)) return false;
		return true;
	}

	
	public static void main(String[] args)  throws IOException{
		Server theServer = new Server();
		theServer.getUserInput();

	}

}
