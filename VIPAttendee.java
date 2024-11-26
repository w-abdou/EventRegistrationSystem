public class VIPAttendee extends Attendee {
    private String accessLevel;

    // Constructor
    public VIPAttendee(String name, String email, String accessLevel) {
        super(name, email);
        this.accessLevel = accessLevel;
    }

    // Getter and Setter
    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    @Override
    public String toString() {
        return super.toString() + ", Access Level: " + accessLevel;
    }
}
