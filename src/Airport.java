public class Airport extends AirTrafficController {

    String name;
    City city;

    int flights[];

    Flight circlingFlights[];

    Runway runways[];

    public Airport(String name, City city, int runwayCount) {
        this.name = name;
        this.city = city;

        runways = new Runway[runwayCount];
    }

    boolean landPlane(Plane plane) {
        for (int i = 0; i < runways.length; i++) {
            if (runways[i] == null) runways[i] = new Runway();
            if (runways[i].available()) {
                runways[i].landPlane(plane);
                return true;
            }
        }
        return false;
    }

    /**
     * behaviour: regist flight with airport
     * @param flightNumber
     */
    void registerFlight(int flightNumber, String origin) {

    }

    /**
     *
     * @param flightNumber
     * @return runway number
     * OR throw error that flight is unregistered
     * OR throw error that no runways are available
     */
    int requestLanding(int flightNumber) {
        return 0;
    }

    /**
     * May throw error that flight was not allowed to land on runway
     */
    void reportLanding(int flightNumber, Runway runway) {

    }

    /**
     * Allow circling flights to land
     * Throw error if plane was not at airport
     * @param flightNumber
     * @param runwayNumber
     */
    void reportTakeoff(int flightNumber, Runway runway) {

    }

    @Override
    public String toString() {
        return name;
    }

    public Runway[] getRunways() {
        return runways;
    }
}
