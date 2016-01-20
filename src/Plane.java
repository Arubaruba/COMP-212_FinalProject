import java.util.ArrayList;
import java.util.List;

public class Plane {
    String type; // The make and model of the plane
    public List<Passenger> passengers = new ArrayList<Passenger>();

    public Plane(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
