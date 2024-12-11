import java.io.File;
import java.io.FileNotFoundException;
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

    public static void main(String[] args) {
        boolean loggedIn = false;

        // Login process
        while (!loggedIn) {
            loggedIn = login();
        }

        // Main system loop
        while (true) {
            displayMainMenu();
            int choice = getValidatedChoice(6);
            if (choice == 5) {
                System.out.println("Exiting the system. Goodbye!");
                break;
            } else if (choice == 6) {
                System.out.println("Logging out...");
                main(new String[0]); // Restart the program for login
                return;
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
                    createEvent(); // Admin: Create Event
                    break;
                case 2:
                    modifyOrDeleteEvent(); // Admin: Modify/Delete Event
                    break;
                case 3:
                    registerAttendee(); // Admin: Register Attendee
                    break;
                case 4:
                    displayAttendeeList(); // Admin: Display Attendee List
                    break;
                case 5:
                    System.out.println("Exiting the system. Goodbye!"); // Admin: Exit
                    System.exit(0); // Exit the application
                    break;
                case 6:
                    System.out.println("Logging out..."); // Admin: Logout
                    main(new String[0]); // Restart the program for login
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
        
            }
        } else { // User menu
            switch (choice) {
                case 1:
                    displayEvents(); // User: Display Events
                    break;
                case 2:
                    registerAttendee(); // User: Register Attendee
                    break;
                case 3:
                    System.out.println("Exiting the system. Goodbye!"); // User: Exit
                    System.exit(0); // Exit the application
                    break;
                case 4:
                    System.out.println("Logging out..."); // User: Logout
                    main(new String[0]); // Restart the program for login
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void createEvent() {
        System.out.println("\n=== Create a New Event ===");
    
        String name;
        do {
            System.out.print("Enter event name (letters and spaces only): ");
            name = scanner.nextLine();
            if (!isValidName(name)) {
                System.out.println("Invalid name. Only letters and spaces are allowed.");
            }
        } while (!isValidName(name));
    
        String date;
        do {
            System.out.print("Enter event date (YYYY-MM-DD): ");
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
    
        double ticketPrice;
        do {
            System.out.print("Enter ticket price: $");
            try {
                ticketPrice = Double.parseDouble(scanner.nextLine());
                if (ticketPrice <= 0) {
                    System.out.println("Invalid price. The ticket price must be greater than 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid price.");
                ticketPrice = -1;
            }
        } while (ticketPrice <= 0);
    
        Event event = new Event (name, date, organizer, category, capacity, ticketPrice);
                events.add(event);
                eventAttendees.put(event, new ArrayList<>());
                System.out.println("Event created successfully!");
            }
        
            private static void displayEvents() {
        System.out.println("\n=== Available Events ===");
        if (events.isEmpty()) {
            System.out.println("No events available.");
            return;
        }
        for (Event event : events) {
            System.out.println(event);
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
                    events.remove(event);
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

        System.out.println("\nChoose attendee type:");
        System.out.println("1. Normal Attendee");
        System.out.println("2. VIP Attendee");
        System.out.println("3. Premium Attendee");
    
        int attendeeTypeChoice = getValidatedChoice(3);
        Attendee attendee = null;
    
        switch (attendeeTypeChoice) {
            case 1:
                attendee = new Attendee(name, email); 
                break;
            case 2:
                // VIP Attendee gets a fixed 15% discount
                attendee = new VIPAttendee(name, email, "Full"); 
            case 3:
                // Premium Attendee gets a fixed 30% discount
                attendee = new PremiumAttendee(name, email, 30.0); 
                break;
            default:
                System.out.println("Invalid choice. Returning to the menu.");
                return;
        }
    
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

}
