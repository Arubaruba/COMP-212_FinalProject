import java.util.ArrayList;
import java.util.List;

public class Plane {

    public enum Status {Ground, Flying, Landing};
    public Flight flight;

    String type; // The make and model of the plane
    public List<Passenger> passengers = new ArrayList<Passenger>();
    public Status status = Status.Ground;

    public Plane(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
