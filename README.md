Author: Do Trong Anh

# ENSF607 Lab6 Assignment

## Exercise 1: File usage
- `src/Client.java`: given to student as-is, serves as a simple Client class.
- `src/inlab2/Server.java`: created to serve as a simple Server to Client class to response checks for Palindromes.

## Exercise 2: File usage
- `src/inlab2/DateServer.java`: given to student as-is, serves as a simple Server to response current date/time as requested by a client.
- `src/DateClient.java`: created to request a date or time response from DateServer.

## Exercise 3: File usage
- `src/MusicRecord.java`: ressembles a data unit containing song name, composer, year of release and price.
- `src/WriteRecord.java`: modified to read the music record information in a text file and save that info as serialized MusicRecord objects into a `.ser` file.
- `someSongs.txt`: given to student as-is, to be read from by WriteRecord.
- `src/ReadRecord.java`: modified to read serialized MusicRecords from the `.ser` file and print them onto console.
- `mySongs.ser`: serialized MusicRecords are saved here by WriteRecord, and read from by ReadRecord.

## Exercise 4: File usage
- `src/PlayerClient.java`: created to communicate between client-sided player and server-sided game of tick-tac-toe.
- `src/Constants.java`: interface defining `char` as marks: X, O or blank.
- `src/inlab2/Constants.java`: identitcal to the one in the client-side.
- `src/inlab2/GameServer.java`: created as server port PlayerClients connect to, which triggers the execution of a Game thread with every two players connected via a thread pool. The demonstration in main performs a maximum of 3 Game threads before the pool shuts down.
- `src/inlab2/Game.java`: modified to implement `Runnable` as handler of two sockets, effectively communicating between two players.
- `src/inlab2/Board.java`: depended on by Game to keep track of the status of the game and determine the outcome, wand when the game ends.
 
## Exercise 5: File usage
- `src/PlayerClientGUI.java`: same role as PlayerClient, but with Swing GUI components and a separate Swing thread to handle said components.
- `src/Constants.java`: interface defining `char` as marks: X, O or blank.
- `src/inlab2/Constants.java`: identitcal to the one in the client-side. 
- `src/inlab2/GameServerGUI.java`: same as GameServer but its pool executes GameGUI threads instead.
- `src/inlab2/GameGUI.java`: same as Game, but modified to properly receive responses from client-sided button presses.
- `src/inlab2/Board.java`: depended on by GameGUI to keep track of the status of the game and determine the outcome, wand when the game ends.

## Bonus:
- `src/server_only/`: contains the model to run on the server machine (the developer's PC).
- `src/client_only/`: contains the model to run on the client machine (the tester's PC). Please run PlayerClientGUI.java to start connection with server's domain.
