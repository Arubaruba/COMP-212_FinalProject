public class Airport extends AirTrafficController {

    String name;
    City city;

    Runway runways[];

    public Airport(String name, City city, int runwayCount) {
        this.name = name;
        this.city = city;

        runways = new Runway[runwayCount];
    }

    boolean reserveRunway(Plane plane) {
        for (int i = 0; i < runways.length; i++) {
            if (runways[i] == null) runways[i] = new Runway();
            if (runways[i].available()) {
                runways[i].landPlane(plane);
                return true;
            }
        }
        return false;
    }

    void takeOff(Plane plane) {
        for (Runway runway : runways) {
            if (runway != null && runway.plane == plane)
                runway.removePlane();
        }
    }

    @Override
    public String toString() {
        return name;
    }

    public Runway[] getRunways() {
        return runways;
    }
}
