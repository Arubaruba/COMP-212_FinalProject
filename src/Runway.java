public class Runway {
    Plane plane;

    public Plane getPlane() {
        return plane;
    }

    void landPlane(Plane plane) {
        this.plane = plane;
    }

    /**
     * The plane has left the runway making it available for others to land on
     */
    void removePlane() {
        plane = null;
    }

    /**
     * @return Can another plan land on this runway
     */
    boolean available() {
        return plane == null;
    }
}
