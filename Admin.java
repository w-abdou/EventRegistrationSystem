import java.util.List;

public interface Admin {
    List<Event> createEvent(Event event);
    void deleteEvent(Event event);
    void viewAllEvents();
    void viewEventAttendees(Event event);
}