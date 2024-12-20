import java.util.List;

public interface Admin {
    List<Event> createEvent(Event event);
    List<Event> deleteEvent(Event event);
}