public class Flight {
    Plane plane;
    Airport origin, destination;
    int number;

    public Flight(int number, Plane plane, Airport origin, Airport destination) {
        this.number = number;
        this.plane = plane;
        this.origin = origin;
        this.destination = destination;
    }

    @Override
    public String toString() {
        return "#" + number;
    }
}
