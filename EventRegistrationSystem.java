import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;


public class EventRegistrationSystem {
    private static List<Event> events = new ArrayList<>();
    private static Map<Event, List<Attendee>> eventAttendees = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);

    private static String currentUserRole = ""; // Will be set during login
    private static final List<String> VALID_CATEGORIES = Arrays.asList("Tech", "Business", "Health", "Education", "Art");

    private static EventAdmin eventadmin = new EventAdmin();   

    public static void main(String[] args) {
        boolean loggedIn = false;
        FileWrite();
       // AddPrevious();
        while (!loggedIn) {
            loggedIn = login();
        }

        while (true) {
            displayMainMenu();
            int choice = getValidatedChoice(6);
            if (choice == 5) {
                System.out.println("Exiting the system. Goodbye!");
                break;
            } else if (choice == 6) {
                System.out.println("Logging out...");
                main(new String[0]); 
            }
            handleMenuChoice(choice);
        }
    }

    private static boolean login() {
        System.out.println("=== Login Page ===");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try (Scanner fileScanner = new Scanner(new File("login_info.txt"))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String storedUsername = parts[0];
                    String storedPassword = parts[1];
                    String role = parts[2];

                    if (storedUsername.equals(username) && storedPassword.equals(password)) {
                        currentUserRole = role;
                        System.out.println("Login successful! Welcome, " + role + ".");
                        return true;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: login_info.txt file not found.");
        }

        System.out.println("Invalid username or password. Please try again.");
        return false;
    }

    private static void displayMainMenu() {
        System.out.println("\n=========================================");
        System.out.println("Welcome to the Event Registration System!");
        System.out.println("=========================================\n");
        
        if (currentUserRole.equals("admin")) { // Admins menu
            System.out.println("1. Create Event");
            System.out.println("2. Modify/Delete Event");
            System.out.println("3. Register Attendee");
            System.out.println("4. Display Attendee List");
            System.out.println("5. Exit");
            System.out.println("6. Logout");
        } else { // Users menu
            System.out.println("1. Display Events");
            System.out.println("2. Register Attendee");
            System.out.println("3. Exit");
            System.out.println("4. Logout");
        }
        
        System.out.println("=========================");
        System.out.print("Enter your choice: ");
    }
    
    private static int getValidatedChoice(int maxOption) {
        int choice = -1;
        try {
            choice = Integer.parseInt(scanner.nextLine());
            if (choice < 1 || choice > maxOption) throw new Exception();
        } catch (Exception e) {
            System.out.println("Invalid choice. Please enter a number between 1 and " + maxOption + ".");
        }
        return choice;
    }

    private static void handleMenuChoice(int choice) {
        if (currentUserRole.equals("admin")) {
            switch (choice) {
                case 1:
                    createEvent(); 
                    break;
                case 2:
                    modifyOrDeleteEvent(); 
                    break;
                case 3:
                    registerAttendee(); 
                    break;
                case 4:
                    displayAttendeeList(); 
                    break;
                case 5:
                    System.out.println("Exiting the system. Goodbye!"); 
                    System.exit(0); 
                    break;
                case 6:
                    System.out.println("Logging out..."); 
                    main(new String[0]); 
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
        
            }
        } else { // User menu
            switch (choice) {
                case 1:
                    displayEvents(); 
                    break;
                case 2:
                    registerAttendee();
                    break;
                case 3:
                    System.out.println("Exiting the system. Goodbye!"); 
                    System.exit(0); 
                    break;
                case 4:
                    System.out.println("Logging out..."); 
                    main(new String[0]); 
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void createEvent() {
        System.out.println("\n=== Create a New Event ===");
    
        System.out.println("Choose event type:");
        System.out.println("1. Event");
        System.out.println("2. Conference");
        System.out.println("3. Workshop");
    
        int eventTypeChoice = getValidatedChoice(3);
        
        String eventType = "";
        switch (eventTypeChoice) {
            case 1:
                eventType = "Event";
                break;
            case 2:
                eventType = "Conference";
                break;
            case 3:
                eventType = "Workshop";
                break;
            default:
                System.out.println("Invalid choice. Returning to the menu.");
                return;
        }
    
        String name;
        do {
            System.out.print("Enter " +  eventType +" name (letters and spaces only): ");
            name = scanner.nextLine();
            if (!isValidName(name)) {
                System.out.println("Invalid name. Only letters and spaces are allowed.");
            }
        } while (!isValidName(name));
    
        String date;
        do {
            System.out.print("Enter " +  eventType +" date (YYYY-MM-DD): ");
            date = scanner.nextLine();
            if (!isValidDate(date)) {
                System.out.println("Invalid date. Please try again.");
            }
        } while (!isValidDate(date));
    
        System.out.print("Enter organizer: ");
        String organizer = scanner.nextLine();
        while (!isValidName(organizer)) {
            System.out.println("Invalid name. Only letters and spaces allowed.");
            organizer = scanner.nextLine();
        }
        EventOrganizer eveorganizer = new EventOrganizer();
        eveorganizer.setName(organizer);


        System.out.println("Choose a category:");
        for (int i = 0; i < VALID_CATEGORIES.size(); i++) {
            System.out.println((i + 1) + ". " + VALID_CATEGORIES.get(i));
        }
        String category = null;
        do {
            System.out.print("Enter category number: ");
            try {
                int categoryIndex = Integer.parseInt(scanner.nextLine()) - 1;
                if (categoryIndex >= 0 && categoryIndex < VALID_CATEGORIES.size()) {
                    category = VALID_CATEGORIES.get(categoryIndex);
                } else {
                    System.out.println("Invalid choice. Please select a valid category.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        } while (category == null);
    
        int capacity;
        do {
            System.out.print("Enter maximum capacity (0-1000): ");
            try {
                capacity = Integer.parseInt(scanner.nextLine());
                if (!isValidCapacity(capacity)) {
                    System.out.println("Invalid capacity. It must be between 0 and 1000.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Capacity must be a number.");
                capacity = -1;
            }
        } while (!isValidCapacity(capacity));
    
        System.out.print("Enter ticket price: $");
        double ticketPrice = getValidatedDouble(); 
        if (eventType == "Event") {
            Event event = new Event(name, date, organizer, category, capacity, ticketPrice, eventType);
            events = eventadmin.createEvent(event);
            eventAttendees.put(event, new ArrayList<>());
            AddEvent(event);
        }
        else if (eventType == "Conference") {
            int speakers;
            do {
            System.out.print("Enter number of speakers: ");
            try {
                speakers = Integer.parseInt(scanner.nextLine());
                if (!isValidNoOfSpeakers(speakers)) {
                    System.out.println("Invalid number of speakers, must be between 1 and 15");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. number of speakers must be a number.");
                speakers = -1;
            } }
            while (!(isValidNoOfSpeakers(speakers)));
         
            Conference conference = new Conference(name, date, organizer, category, capacity, speakers, eventType);
            events = eventadmin.createEvent(conference);
            eventAttendees.put(conference, new ArrayList<>());
            AddEvent(conference);
            
         }
        else if (eventType == "Workshop") {
            System.out.print("Enter topic: ");
            String topic = scanner.nextLine();
            while (!isValidName(topic)) {
                System.out.println("Invalid name. Only letters and spaces allowed.");
                topic = scanner.nextLine();
            }
            Workshop workshop = new Workshop(name, date, organizer, category, capacity, ticketPrice, topic, eventType);
            events = eventadmin.createEvent(workshop);
            eventAttendees.put(workshop, new ArrayList<>());
            AddEvent(workshop);
            

        }
        System.out.println(eventType + " created successfully !");        
    }

    private static void displayEvents() {
        System.out.println("\n=== Available Events ===");
        if (events.isEmpty()) {
            System.out.println("No events available.");
            return;
        }
        double discount;
        if (currentUserRole.equals("VIP")) {
            discount = 15.0 / 100;
        }
        else if (currentUserRole.equals("Premium")) {
            discount = 30.0 / 100;
        }
        else {
            discount = 0;
        }

        for (Event event : events) {
            String record = EventObjToString(event, discount);  
            System.out.println(record);
        }
        System.out.println();
}
    
    
    private static void modifyOrDeleteEvent() {
        System.out.println("\n=== Modify or Delete Event ===");
        System.out.print("Enter event name: ");
        String name = scanner.nextLine();
        Event event = findEventByName(name);
        if (event == null) {
            System.out.println("Event not found.");
            return;
        }

        int choice;
        do {
            System.out.println("1. Modify Event");
            System.out.println("2. Delete Event");
            System.out.print("Enter your choice: ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice == 1) {
                    modifyEvent(event);
                } else if (choice == 2) {
                    events = eventadmin.deleteEvent(event);
                    eventAttendees.remove(event);
                    System.out.println("Event deleted successfully.");
                } else {
                    System.out.println("Invalid choice. Please enter 1 or 2.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number (1 or 2).");
                choice = -1;
            }
        } while (choice != 1 && choice != 2);
    }

    private static void modifyEvent(Event event) {
        System.out.println("\n=== Modify Event ===");
        System.out.print("Enter new name (leave blank to keep current): ");
        String name = scanner.nextLine();
        if (!name.isEmpty() && isValidName(name)) {
            event.setEventName(name);
        }

        System.out.print("Enter new date (YYYY-MM-DD, leave blank to keep current): ");
        String date = scanner.nextLine();
        if (!date.isEmpty() && isValidDate(date)) {
            event.setEventDate(date);
        }

        System.out.println("Choose a new category (leave blank to keep current):");
        for (int i = 0; i < VALID_CATEGORIES.size(); i++) {
            System.out.println((i + 1) + ". " + VALID_CATEGORIES.get(i));
        }
        String category = null;
        String categoryInput = scanner.nextLine();
        if (!categoryInput.isEmpty()) {
            do {
                try {
                    int categoryIndex = Integer.parseInt(categoryInput) - 1;
                    if (categoryIndex >= 0 && categoryIndex < VALID_CATEGORIES.size()) {
                        category = VALID_CATEGORIES.get(categoryIndex);
                        event.setCategory(category);
                    } else {
                        System.out.println("Invalid choice. Please select a valid category.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
            } while (category == null);
        }

        System.out.print("Enter new capacity (leave blank to keep current): ");
        String capacityInput = scanner.nextLine();
        if (!capacityInput.isEmpty()) {
            try {
                int capacity = Integer.parseInt(capacityInput);
                if (isValidCapacity(capacity)) {
                    event.setCapacity(capacity);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Capacity must be a number.");
            }
        }
        System.out.print("Enter new ticket price (leave blank to keep current): $");
         String priceInput = scanner.nextLine();
         if (!priceInput.isEmpty()) {
            try {
                double ticketPrice = Double.parseDouble(priceInput);
                if (ticketPrice > 0) {
                    event.setTicketPrice(ticketPrice);
                    System.out.println("Ticket price updated to: $" + ticketPrice);
                } else {
                    System.out.println("Invalid input. Ticket price must be greater than 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid price.");
            }
    }

        System.out.println("Event modified successfully.");
    }

    private static void registerAttendee() {
        System.out.print("\nEnter event name to register for: ");
        String eventName = scanner.nextLine();
        Event event = findEventByName(eventName);
        if (event == null) {
            System.out.println("Event not found.");
            return;
        }
        if (eventAttendees.get(event).size() >= event.getCapacity()) {
            System.out.println("Event is full. Registration not possible.");
            return;
        }
    
        System.out.print("Enter attendee name: ");
        String name = scanner.nextLine();
        while (!isValidName(name)) {
            System.out.println("Invalid name. Only letters and spaces allowed.");
            name = scanner.nextLine();
        }
    
        System.out.print("Enter attendee email: ");
        String email = scanner.nextLine();
        while (!isValidEmail(email)) {
            System.out.print("Invalid email format. Please enter again: ");
            email = scanner.nextLine();
        }
        
        
        Attendee attendee = new Attendee(name, email);
        eventAttendees.get(event).add(attendee);
        System.out.println("Attendee registered successfully!");
    }
    
    private static void displayAttendeeList() {
        System.out.print("\nEnter event name to view attendees: ");
        String eventName = scanner.nextLine();
        Event event = findEventByName(eventName);
        if (event == null) {
            System.out.println("Event not found.");
            return;
        }
        List<Attendee> attendees = eventAttendees.get(event);
        if (attendees.isEmpty()) {
            System.out.println("No attendees registered for this event.");
        } else {
            System.out.println("Attendee List for " + eventName + ":");
            for (int i = 0; i < attendees.size(); i++) {
                System.out.println((i + 1) + ". " + attendees.get(i));
            }
        }
    }

    private static Event findEventByName(String name) {
        return events.stream().filter(e -> e.getEventName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    private static boolean isValidDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate enteredDate = LocalDate.parse(date, formatter);
            return !enteredDate.isBefore(LocalDate.now());
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            return false;
        }
    }

    private static boolean isValidName(String name) {
        return name.matches("[a-zA-Z ]+");
    }

    private static boolean isValidCapacity(int capacity) {
        return capacity >= 0 && capacity <= 1000;
    }

    private static boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private static boolean isValidNoOfSpeakers(int speakers) {
        return speakers > 0 && speakers <= 15;
    }
   
    private static double getValidatedDouble() {
        double input = -1;
        while (input <= 0) {
            try {
                input = Double.parseDouble(scanner.nextLine());
                if (input <= 0) {
                    System.out.print("Ticket price must be a positive number. Please enter again: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a valid number for the ticket price: ");
            }
        }
        return input;
    }

    public static String EventObjToString(Event eve, double discount) {
        
        String name = eve.getEventName();
        String date = eve.getEventDate(); 
        String organ = eve.getOrganizer(); 
        String category =  eve.getCategory();
        String cap = String.valueOf(eve.getCapacity());
        String price = String.valueOf(eve.getTicketPrice() - eve.getTicketPrice()*discount) ;
        String type = String.valueOf(eve.getEventType());
        String speakers = "N/A";
        String topic = "N/A";
        if (type == "Conference") {
            speakers = String.valueOf(eve.ConferenceDetails());
        }
        if (type == "Workshop") {
            topic = String.valueOf(eve.WorkshopDetails());
        }
        String str = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s", name, date, organ, category, cap, price, type, topic, speakers);       
        return str;
    }
    public static void AddEvent(Event eve) {
        String str = EventObjToString(eve, 0);
        appendFile("EventsList.txt", str);
    }
    public static void FileWrite() {
       try {
      File EventsList = new File("Events List.txt");
      if (EventsList.createNewFile()) {
        System.out.println("File created: " + EventsList.getName());
      } 
    } catch (IOException e) {
      System.out.println("An error occurred in creating file.");
      e.printStackTrace();
    }
    }

     public static void appendFile(String fileName, String str)
    {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName, true));
            BufferedReader br = new BufferedReader(new FileReader("EventsList.txt"));     
            
            out.write(str + '\n');
            out.close();
        }
 

        catch (IOException e) {
            System.out.println("exception occurred" + e);
        }
    }
    // integrate previously created events into new program trial
    public static void AddPrevious() {
        try (Scanner fileScanner = new Scanner(new File("EventsList.txt"))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 9) {
                    String eventName = parts[0];
                    String eventDate = parts[1];
                    String eventOrganizer = parts[2];
                    String Category = parts[3];
                    int Capacity = Integer.parseInt(parts[4]);
                    String ticketPrice = parts[5];
                    String eventType = parts[6];
                    String topic = parts[7];
                    String speakers = parts[8];

                    double price;
                    if (ticketPrice != "N/A") {
                         price = Double.parseDouble(ticketPrice);
                    }
                    else {
                        price = 0;
                    }

                    int NoOfSpeakers;
                    if (speakers != "N/A") {
                        NoOfSpeakers = Integer.parseInt(speakers);
                    }
                    else {
                        NoOfSpeakers = 0;
                    }


                    if (eventType == "Workshop") {
                        Workshop workshop = new Workshop(eventName, eventDate, eventOrganizer, Category, Capacity, price, eventType, topic);
                        eventadmin.createEvent(workshop);
                        eventAttendees.put(workshop, new ArrayList<>());
                    } 
                    else if (eventType == "Conference") {
                        Conference conference = new Conference(eventName, eventDate, eventOrganizer, Category, Capacity, NoOfSpeakers, eventType);
                        eventadmin.createEvent(conference);
                        eventAttendees.put(conference, new ArrayList<>());
                    }
                    else {
                        Event event = new Event(eventName, eventDate, eventOrganizer, Category, Capacity, price, eventType);
                        eventadmin.createEvent(event);
                        eventAttendees.put(event, new ArrayList<>());
                    }
                    
                    }
                }
            }
            catch (FileNotFoundException e) {
                System.out.println("Error: login_info.txt file not found.");
            }
        } 
        
    }

    //add append file here


