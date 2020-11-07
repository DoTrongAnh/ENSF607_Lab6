package server_only;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GameServerGUI {

	
	/**
	 * ServerSocket of the Game Server
	 */
	private ServerSocket serverSocket;
	/**
	 * Thread pool to execute Runnable Game
	 */
	private ExecutorService pool;
	/**
	 * Number of Games to execute before closing pool
	 */
	private int sessions;
	/**
	 * Constructs the server with pool size
	 * @param numGame
	 */
	public GameServerGUI(int numGame) {
		sessions = numGame;
		pool = Executors.newFixedThreadPool(sessions); 
		try {
			serverSocket = new ServerSocket(8880);
			System.out.println("Game Server is now running...");
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}
	/**
	 * Hosts games every two players join
	 */
	public void host() {
			int i = 0;
			//Hosts 10k games in total
			while(i++ < 10000) {
				Socket xSocket;
				try {
					xSocket = serverSocket.accept();
				} catch (IOException e) {
					continue;
				}
				System.out.println("First player joined!");
				Socket oSocket;
				try {
					oSocket = serverSocket.accept();
				} catch (IOException e) {
					continue;
				}
				while(!xSocket.isConnected()) {
					System.out.println("First player left!");
					try {
						xSocket.close();
					} catch (IOException e1) {
					}
					xSocket = oSocket;
					try {
						oSocket = serverSocket.accept();
					} catch (IOException e) {
						continue;
					}
					System.out.println("First player joined!");
				}
				System.out.println("Second player joined!");
				pool.execute(new GameGUI(xSocket, oSocket)); 
				System.out.println("A new Game thread executed!");
			}
		pool.shutdown();
		try {
			pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		GameServerGUI gs = new GameServerGUI(30);//Creates a maximum of 30 parallel games
		gs.host();
	}

}
