import java.util.*;

public class EventRegistrationSystem {
    private static List<Event> events = new ArrayList<>();
    private static Map<Event, List<Attendee>> eventAttendees = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);

    // User roles: "admin" or "user"
    private static String currentUserRole = "admin"; // Change to "user" if needed.

    public static void main(String[] args) {
        while (true) {
            displayMainMenu();
            int choice = getValidatedChoice(5);
            if (choice == 5) {
                System.out.println("Exiting the system. Goodbye!");
                break;
            }
            handleMenuChoice(choice);
        }
    }

    private static void displayMainMenu() {
        System.out.println("\n=========================");
        System.out.println("Welcome to the Event Registration System!");
        System.out.println("=========================");
        if (currentUserRole.equals("admin")) {
            System.out.println("1. Create Event");
            System.out.println("2. Modify/Delete Event");
        } else {
            System.out.println("1. View Events");
        }
        System.out.println("3. Register Attendee");
        System.out.println("4. Display Attendee List");
        System.out.println("5. Exit");
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
        switch (choice) {
            case 1:
                if (currentUserRole.equals("admin")) createEvent();
                else displayEvents();
                break;
            case 2:
                if (currentUserRole.equals("admin")) modifyOrDeleteEvent();
                else System.out.println("Invalid choice for user role.");
                break;
            case 3:
                registerAttendee();
                break;
            case 4:
                displayAttendeeList();
                break;
            default:
                System.out.println("Invalid choice. Try again.");
        }
    }

    private static void createEvent() {
        System.out.println("\n=== Create a New Event ===");
        System.out.print("Enter event name: ");
        String name = scanner.nextLine();
        System.out.print("Enter event date (YYYY-MM-DD): ");
        String date = scanner.nextLine();
        while (!isValidDate(date)) {
            System.out.print("Invalid date format. Please enter again (YYYY-MM-DD): ");
            date = scanner.nextLine();
        }
        System.out.print("Enter organizer: ");
        String organizer = scanner.nextLine();
        System.out.print("Enter event category (e.g., Tech, Business): ");
        String category = scanner.nextLine();
        System.out.print("Enter maximum capacity: ");
        int capacity = Integer.parseInt(scanner.nextLine());

        Event event = new Event(name, date, organizer, category, capacity);
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
        System.out.println("1. Modify Event");
        System.out.println("2. Delete Event");
        System.out.print("Enter your choice: ");
        int choice = Integer.parseInt(scanner.nextLine());
        if (choice == 1) {
            modifyEvent(event);
        } else if (choice == 2) {
            events.remove(event);
            eventAttendees.remove(event);
            System.out.println("Event deleted successfully.");
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private static void modifyEvent(Event event) {
        System.out.println("\n=== Modify Event ===");
        System.out.print("Enter new name (leave blank to keep current): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) event.setEventName(name);

        System.out.print("Enter new date (YYYY-MM-DD, leave blank to keep current): ");
        String date = scanner.nextLine();
        if (!date.isEmpty()) {
            while (!isValidDate(date)) {
                System.out.print("Invalid date format. Please enter again (YYYY-MM-DD): ");
                date = scanner.nextLine();
            }
            event.setEventDate(date);
        }

        System.out.print("Enter new category (leave blank to keep current): ");
        String category = scanner.nextLine();
        if (!category.isEmpty()) event.setCategory(category);

        System.out.print("Enter new maximum capacity (leave blank to keep current): ");
        String capacityInput = scanner.nextLine();
        if (!capacityInput.isEmpty()) event.setCapacity(Integer.parseInt(capacityInput));

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
        return date.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    private static boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}