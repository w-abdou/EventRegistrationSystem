public class PremiumAttendee extends Attendee {
    private double discount;

    // Constructor
    public PremiumAttendee(String name, String email, double discount) {
        super(name, email);
        this.discount = discount;
    }

    // Getter and Setter
    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return super.toString() + ", Discount: " + discount + "%";
    }
}
