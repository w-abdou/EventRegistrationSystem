import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class filetest {
    static Event eve = new Event("eve", "2025-02-02", "farah", "Tech", 30, 20, "Conference");
        public static void main(String[] args) {
          String name = eve.getEventName();
          String date = eve.getEventDate(); 
          String organ = eve.getOrganizer(); 
          String type =  eve.getCategory();
          String cap = String.valueOf(eve.getCapacity());
          String str = name + "         | " + date +  "  | " + organ +  "       | " + type +  "      | " + cap;

            CreateFile();
            appendFile("test.txt", str);
    }

    public static void CreateFile() {
       try {
      File test = new File("test.txt");
      if (test.createNewFile()) {
        System.out.println("File created: " + test.getName());
      } else {
        System.out.println("File already exists.");
      }
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
    }

    public static void appendFile(String fileName, String str)
    {
        // Try block to check for exceptions
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName, true));
            // Open given file in append mode by creating an
            // object of BufferedWriter class
            BufferedReader br = new BufferedReader(new FileReader("test.txt"));     
            if (br.readLine() == null) {
               out.write("Event Name  | Date        | Organizer   | Category  | Capacity  \n");
            }
            // Writing on output stream
            out.write(str + '\n');
            // Closing the connection
            out.close();
        }
 
        // Catch block to handle the exceptions
        catch (IOException e) {
 
            // Display message when exception occurs
            System.out.println("exception occurred" + e);
        }
    }

}
