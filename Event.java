public class Event {
    private String eventName;
    private String eventDate;
    private String organizer;
    private String category; 
    private int capacity;

    // Default Constructor
    public Event() {}

    // Constructor with all attributes
    public Event(String eventName, String eventDate, String organizer, String category, int capacity) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.organizer = organizer;
        this.category = category;
        this.capacity = capacity;
    }

    // Getters and Setters
    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "Event Name: " + eventName +
               ", Date: " + eventDate +
               ", Organizer: " + organizer +
               ", Category: " + category +
               ", Capacity: " + capacity;
    }
}
