import java.util.ArrayList;
import java.util.List;

public class EventAdmin implements Admin {
private static List<Event> events = new ArrayList<>();    
// give event admin username
    @Override
    public List<Event> createEvent(Event event) {
        System.out.println(event.getEventName());
        events.add(event);
        return events;
    }

    @Override
    public void deleteEvent(Event event) {
        events.remove(event);
    }

    @Override
    public void viewAllEvents() {
        System.out.println("hi");
    }

    @Override
    public void viewEventAttendees(Event event) {
        System.out.println("fill these in later");
    }

}