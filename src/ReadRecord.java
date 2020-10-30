/** 
 * Started by M. Moussavi
 * March 2015
 * Completed by: Do Trong Anh
 */

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ReadRecord {
    
    private ObjectInputStream input;
    
    /**
     *  opens an ObjectInputStream using a FileInputStream
     */
    
    private void readObjectsFromFile(String name)
    {
        MusicRecord record ;
        
        try
        {
            input = new ObjectInputStream(new FileInputStream( name ) );
        }
        catch ( IOException ioException )
        {
            System.err.println( "Error opening file." );
        }
        
        /* The following loop is supposed to use readObject method of
         * ObjectInputSteam to read a MusicRecord object from a binary file that
         * contains several reords.
         * Loop should terminate when an EOFException is thrown.
         */
        
        try
        {
            while ( true )
            {
                
                
                // TO BE COMPLETED BY THE STUDENTS
                record = (MusicRecord)input.readObject();
                if(record == null) break;
                System.out.println(record.getYear());
                System.out.println(record.getSongName());
                System.out.println(record.getSingerName());
                System.out.println(record.getPurchasePrice());
                System.out.println("-------------------");
           
            }   // END OF WHILE
        }
        catch(IOException e) {
        	System.err.println("ERROR: " + e.getMessage());
        }
        catch(ClassNotFoundException e) {
        	System.err.println("ERROR: " + e.getMessage());
        }
                // ADD NECESSARY catch CLAUSES HERE

    }           // END OF METHOD 
    
    
    public static void main(String [] args)
    {
        ReadRecord d = new ReadRecord();
        d.readObjectsFromFile("mySongs.ser");
    }
}