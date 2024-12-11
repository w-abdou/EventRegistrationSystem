public class Attendee {
    private String name;
    private String email;

    // Constructor
    public Attendee(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Attendee{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
