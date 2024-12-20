import java.util.ArrayList;
import java.util.List;

public class EventAdmin implements Admin {
private static List<Event> events = new ArrayList<>();    
// give event admin username
    @Override
    public List<Event> createEvent(Event event) {
        events.add(event);
        return events;
    }

    @Override
    public List<Event> deleteEvent(Event event) {
        System.out.println(event.getEventName());
        events.remove(event);
        return events;
    }
/* 
    @Override
    public void viewAllEvents(List<Event> events) {
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
*/

}