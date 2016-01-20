public class City {
    String name, countryCode;

    // There are numerous cities with two, or more airports
    // see: https://en.wikipedia.org/wiki/List_of_cities_with_more_than_one_airport
    Airport airports[];

    public City(String countryCode, String name) {
        this.countryCode = countryCode;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Airport[] getAirports() {
        return airports;
    }

    public void setAirports(Airport[] airports) {
        this.airports = airports;
    }
}
