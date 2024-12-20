import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class filetest {
    static Event eve = new Event("Burning Man", "2025-02-32", "Loser Loser", "Fashion", 999, 15.9, "Event");
        public static void main(String[] args) {
          String name = eve.getEventName();
          String date = eve.getEventDate(); 
          String organ = eve.getOrganizer(); 
          String category =  eve.getCategory();
          String cap = String.valueOf(eve.getCapacity());
          String price = "$" + String.valueOf(eve.getTicketPrice());
          String type = String.valueOf(eve.getEventType());
         // String str = name + "         | " + date +  "  | " + organ +  "       | " + category +  "      | " + cap + "       | " + price + "         | " + type;
        
          /* 
            
            */
            
            
            String str = String.format("%-20s | %5s | %-15s | %-5s | %5s | %5s | %10s \r", "burning man", date, "Elizabeth Berk", category, 30, price, type);
            String str2 = String.format("%-20s | %5s | %-15s | %-8s | %8s | %12s | %-10s \r", name, date, organ, category, cap, price, type);

            //System.out.println(hi);
            CreateFile();
            appendFile("test.txt", str2);
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
               out.write("Event Name  | Date       | Organizer       | Category | Capacity | Ticket Price | Event Type   \n");
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
