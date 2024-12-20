public class Conference extends Event {
    private int numberOfSpeakers;

    // Constructor
    public Conference(String eventName, String eventDate, String organizer, String category, int capacity, int numberOfSpeakers, String eventType ) {
        super(eventName, eventDate, organizer, category, capacity, numberOfSpeakers, eventType );
        this.numberOfSpeakers = numberOfSpeakers;
    }

    // Getter and Setter
    public int getNumberOfSpeakers() {
        return numberOfSpeakers;
    }

    public void setNumberOfSpeakers(int numberOfSpeakers) {
        this.numberOfSpeakers = numberOfSpeakers;
    }

    @Override
    public int ConferenceDetails() {
        return numberOfSpeakers;
    }


    @Override
    public String toString() {
        return super.toString() + ", Number of Speakers: " + numberOfSpeakers;
    }
}
