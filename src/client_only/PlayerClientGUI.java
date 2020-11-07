package client_only;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.GridLayout;
import java.awt.TextArea;

import javax.swing.AbstractAction;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;

import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.CompoundBorder;
import java.awt.Color;
import javax.swing.JScrollPane;

public class PlayerClientGUI implements Constants{

	/**
	 * Writer to communicate Client input
	 */
	private PrintWriter socketOut;
	/**
	 * Socket to speak to Game
	 */
	private Socket aSocket;
	/**
	 * Reading Game input
	 */
	private BufferedReader socketIn;
	/**
	 * Player's and opponent's mark
	 */
	private char mark, opponent;
	/**
	 * If it's the player's turn
	 */
	private boolean isTurn;
	//GUI components
	private JFrame frame;
	private final JPanel panel = new JPanel();
	private final JPanel panel_1 = new JPanel();
	private final JPanel panel_2 = new JPanel();
	private final JButton button2 = new JButton("");
	private final JButton button3 = new JButton("");
	private final JButton button1 = new JButton("");
	private final JTextField textField = new JTextField();
	private final JLabel lblNewLabel = new JLabel("Player Name:");
	private final JButton button4 = new JButton("");
	private final JButton button5 = new JButton("");
	private final JButton button6 = new JButton("");
	private final JButton button7 = new JButton("");
	private final JButton button8 = new JButton("");
	private final JButton button9 = new JButton("");
	private final JLabel lblNewLabel_1 = new JLabel("Messages:");
	private final TextArea textArea = new TextArea();
	private InetAddress serverAddress;
	private int portNumber;
	private boolean gameStarted;
	private final JScrollPane scrollBar = new JScrollPane(textArea);
	/**
	 * Launch the application.
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException {
		
		PlayerClientGUI window = new PlayerClientGUI("dtaticktactoe.ddns.net", 8880);
		Thread t = new Thread() {
			public void run() {
				window.open();
				window.introduce();
				window.play();
				window.close();
			}
		};
		t.start();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window.initialize();
					window.frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws UnknownHostException 
	 */
	public PlayerClientGUI(String serverName, int portNumber) throws UnknownHostException {
		isTurn = false;
		gameStarted = false;
		//initialize();
		//frame.setVisible(true);
		this.serverAddress = InetAddress.getByName(serverName);
		this.portNumber = portNumber;
	}
	
	/**
	 * Have Socket opening be in different thread than Swing thread
	 */
	public void open() {
		try {
			aSocket = new Socket(serverAddress, portNumber);
			socketIn = new BufferedReader(new InputStreamReader(aSocket.getInputStream()));
			socketOut = new PrintWriter(aSocket.getOutputStream(), true);
			String read = socketIn.readLine();
			this.mark = read.charAt(0);
			if(this.mark == LETTER_O) opponent = LETTER_X;
			else opponent = LETTER_O;
		} catch (UnknownHostException e) {
			System.out.println("No or invalid IP adress.");
		} catch (IOException e) {
			System.out.println("Connection failed.");
		}
		frame.setTitle("Tic-tac-toe - Player " + this.mark);
	}
	/**
	 * Introduces player to the Game
	 */
	public void introduce() {
		//After inputting name in textField, waits for Game's response
		while(true) {
			String read = "";
			try {
				read = socketIn.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			textArea.append(read + "\n");
			if(read.contentEquals("OK")) break;		
		}
	}
	
	public void play() {
		String read = "";
		try {
			while(true) {
				read = socketIn.readLine();//Whose turn is it? Both player receive this
				textArea.append(read + "\n");
				if(read.equals("Your turn.")) isTurn = true;
				//Time to press a button, sending two lines to Game
				//In case an exception happens from Game thread
				else if(read.equals("Connection error. Game terminated.")) {
					textArea.append(read + "\n");
					break;
				}
				//If not your turn, wait for response from Game on opponent's move
				else {
					String row = socketIn.readLine();//Only one player receives these
					if(row.equals("Connection error. Game terminated.")) {
						textArea.append(row + "\n");
						break;
					}
					String col = socketIn.readLine();
					switch(row) {
					case "0": switch(col) {
					case "0": button1.setText(Character.toString(opponent));break;
					case "1": button2.setText(Character.toString(opponent));break;
					default: button3.setText(Character.toString(opponent));break;
					} break;
					case "1": switch(col) {
					case "0": button4.setText(Character.toString(opponent));break;
					case "1": button5.setText(Character.toString(opponent));break;
					default: button6.setText(Character.toString(opponent));break;
					}break;
					default: switch(col) {
					case "0": button7.setText(Character.toString(opponent));break;
					case "1": button8.setText(Character.toString(opponent));break;
					default: button9.setText(Character.toString(opponent));break;
					}break;
					}
				}
				//End of turn
				read = socketIn.readLine();
				textArea.append(read + "\n");//Both players receive this
				if(read.equals("GAME OVER") || read.equals("Connection error. Game terminated.")) break;//End of game
			}
			//Time to announce the winner
			read = socketIn.readLine();
			if(read != null)
			textArea.append(read + "\n");
		} catch (IOException e) {
			return;
		}
		
	}

	/**
	 * Closes the game thread
	 */
	public void close() {
		try {
			socketIn.close();
			socketOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		scrollBar.setVisible(false);
		scrollBar.setAutoscrolls(true);
		scrollBar.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollBar.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		textArea.setEditable(false);
		textArea.setRows(22);
		textField.setColumns(10);
		frame = new JFrame();
		frame.setForeground(Color.LIGHT_GRAY);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel.setBorder(new CompoundBorder());
		panel.setLayout(new GridLayout(3, 3, 0, 0));
		button1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int col = 0, row = 0;
				String bMark = button1.getText();
				if(!bMark.isEmpty() || !isTurn) return;
				button1.setText(Character.toString(mark));
				socketOut.println(row);
				socketOut.println(col);
				isTurn = false;
			}
		});
		
		panel.add(button1);
		button2.setFont(new Font("Tahoma", Font.PLAIN, 20));
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int col = 1, row = 0;
				String bMark = button2.getText();
				if(!bMark.isEmpty() || !isTurn) return;
				button2.setText(Character.toString(mark));
				socketOut.println(row);
				socketOut.println(col);
				isTurn = false;
			}
		});
		
		panel.add(button2);
		button3.setFont(new Font("Tahoma", Font.PLAIN, 20));
		button3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int col = 2, row = 0;
				String bMark = button3.getText();
				if(!bMark.isEmpty() || !isTurn) return;
				button3.setText(Character.toString(mark));
				socketOut.println(row);
				socketOut.println(col);
				isTurn = false;
			}
		});
		
		panel.add(button3);
		button4.setFont(new Font("Tahoma", Font.PLAIN, 20));
		button4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int col = 0, row = 1;
				String bMark = button4.getText();
				if(!bMark.isEmpty() || !isTurn) return;
				button4.setText(Character.toString(mark));
				socketOut.println(row);
				socketOut.println(col);
				isTurn = false;
			}
		});
		
		panel.add(button4);
		button5.setFont(new Font("Tahoma", Font.PLAIN, 20));
		button5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int col = 1, row = 1;
				String bMark = button5.getText();
				if(!bMark.isEmpty() || !isTurn) return;
				button5.setText(Character.toString(mark));
				socketOut.println(row);
				socketOut.println(col);
				isTurn = false;
			}
		});
		
		panel.add(button5);
		button6.setFont(new Font("Tahoma", Font.PLAIN, 20));
		button6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int col = 2, row = 1;
				String bMark = button6.getText();
				if(!bMark.isEmpty() || !isTurn) return;
				button6.setText(Character.toString(mark));
				socketOut.println(row);
				socketOut.println(col);
				isTurn = false;
			}
		});
		
		panel.add(button6);
		button7.setFont(new Font("Tahoma", Font.PLAIN, 20));
		button7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int col = 0, row = 2;
				String bMark = button7.getText();
				if(!bMark.isEmpty() || !isTurn) return;
				button7.setText(Character.toString(mark));
				socketOut.println(row);
				socketOut.println(col);
				isTurn = false;
			}
		});
		
		panel.add(button7);
		button8.setFont(new Font("Tahoma", Font.PLAIN, 20));
		button8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int col = 1, row = 2;
				String bMark = button8.getText();
				if(!bMark.isEmpty() || !isTurn) return;
				button8.setText(Character.toString(mark));
				socketOut.println(row);
				socketOut.println(col);
				isTurn = false;
			}
		});
		
		panel.add(button8);
		button9.setFont(new Font("Tahoma", Font.PLAIN, 20));
		button9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int col = 2, row = 2;
				String bMark = button9.getText();
				if(!bMark.isEmpty() || !isTurn) return;
				button9.setText(Character.toString(mark));
				socketOut.println(row);
				socketOut.println(col);
				isTurn = false;
			}
		});
		
		panel.add(button9);
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(23)
					.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textField, GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(5)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGap(6)
							.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 14, Short.MAX_VALUE))
						.addComponent(textField))
					.addContainerGap())
		);
		textField.addActionListener(new AbstractAction() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				if(gameStarted) return;
				String name = textField.getText();
				socketOut.println(name);
				textField.setEditable(false);
				gameStarted = true;
			}
			
		});
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_1.setLayout(gl_panel_1);
		panel_2.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_2.createSequentialGroup()
							.addComponent(textArea, GroupLayout.PREFERRED_SIZE, 210, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(scrollBar, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel_2.createSequentialGroup()
					.addGap(8)
					.addComponent(lblNewLabel_1, GroupLayout.DEFAULT_SIZE, 18, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.TRAILING)
						.addComponent(scrollBar, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
						.addComponent(textArea, GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE))
					.addContainerGap())
		);
		panel_2.setLayout(gl_panel_2);
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(5)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addComponent(panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addGap(18)
					.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(5)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(panel, GroupLayout.PREFERRED_SIZE, 219, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)))
					.addContainerGap())
		);
		frame.getContentPane().setLayout(groupLayout);
		frame.pack();
		frame.addComponentListener(new ComponentAdapter() {
		      private int oldWidth = 0;
		      private int oldHeight = 0;

		      @Override
		      public void componentResized(ComponentEvent e) {
		        oldWidth = frame.getWidth();
		        oldHeight = frame.getHeight();
		      }

		      @Override
		      public void componentMoved(ComponentEvent e) {
		          if (frame.getWidth() != oldWidth || frame.getHeight() != oldHeight) {
		        	  frame.invalidate();
		        	  frame.validate();
		        	  frame.repaint();
		          }
		          oldWidth = frame.getWidth();
		          oldHeight = frame.getHeight();
		      }
		    });
	}
}
