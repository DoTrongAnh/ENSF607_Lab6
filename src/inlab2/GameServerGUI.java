package inlab2;

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
			serverSocket = new ServerSocket(8090);
			System.out.println("Game Server is now running...");
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}
	/**
	 * Hosts games every two players join
	 */
	public void host() {
		try {		
			int i = 0;
			while(i++ < sessions) {
				Socket xSocket = serverSocket.accept();
				Socket oSocket = serverSocket.accept();
				pool.execute(new GameGUI(xSocket, oSocket)); 
			}
		} catch (IOException e1) {
			e1.printStackTrace();
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
		GameServerGUI gs = new GameServerGUI(3);//Creates a maximum of 3 games before closing server
		gs.host();
	}

}
