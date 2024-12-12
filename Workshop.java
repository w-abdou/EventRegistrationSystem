public class Workshop extends Event {
    private String topic;

    // Constructor
    public Workshop(String eventName, String eventDate, String organizer, String category, int capacity, String topic, String eventType) {
        super(eventName, eventDate, organizer, category, capacity, ticketPrice, eventType);
        this.topic = topic;
    }

    // Getter and Setter
    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public String toString() {
        return super.toString() + ", Topic: " + topic;
    }
}
