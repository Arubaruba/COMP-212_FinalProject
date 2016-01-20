import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

public class UIController {

    public ComboBox airportsComboBox, registerFlightToComboBox, registerFlightPlaneComboBox,
            planeActionsFlightComboBox, passengerBoardingFlightComboBox;
    public Label airportLabel;
    public TextField passengerBoardingName;
    public TableView outgoingFlightsTableView;

    // Table Columns - Outgoing Table
    public TableColumn<Flight, String> outgoingFlightStatus;
    public TableColumn<Flight, Integer> outgoingFlightNumber, outgoingFlightPassengers;
    public TableColumn<Flight, Airport> outgoingFlightTo;


    ObservableList<Flight> flights = FXCollections.observableArrayList();
    ObservableList<Airport> airports = FXCollections.observableArrayList();
    int flightIdIncrementer = 0;

    public void registerFlight() {
        if (airportsComboBox.getValue() == null || registerFlightToComboBox.getValue() == null ||
                registerFlightPlaneComboBox.getValue() == null) {
            (new Alert(Alert.AlertType.ERROR, "Please make sure \"Airport\", \"To\" and \"Plane\" combo boxes have selected values")).showAndWait();
        } else if (airportsComboBox.getValue() == registerFlightToComboBox.getValue()) {
            (new Alert(Alert.AlertType.ERROR, "\"Airport\" (the current airport) and \"To\" (the destination) need to be different airports")).showAndWait();
        } else {
            // Add the flight
            flights.add(new Flight(++flightIdIncrementer, (Plane) registerFlightPlaneComboBox.getValue(),
                    (Airport) airportsComboBox.getValue(), (Airport) registerFlightToComboBox.getValue()));

            // Clear inputs
            registerFlightToComboBox.valueProperty().set(null);
            registerFlightPlaneComboBox.valueProperty().set(null);
        }
    }

    public void boardPlane() {
        if (passengerBoardingFlightComboBox.getValue() == null) {
            (new Alert(Alert.AlertType.ERROR, "Please select a flight number")).showAndWait();
        } else if (passengerBoardingName.getText().length() == 0) {
            (new Alert(Alert.AlertType.ERROR, "Please enter a passenger name")).showAndWait();
        } else {
            Flight flight = (Flight) passengerBoardingFlightComboBox.getValue();
            flight.plane.passengers.add(new Passenger(passengerBoardingName.getText()));

            passengerBoardingFlightComboBox.valueProperty().set(null);
            passengerBoardingName.setText("");
        }
    }

    /**
     * Called once the UI is ready
     */
    public void initialize() {
        addMockData();

        // Init UI elements
        airportsComboBox.setItems(airports);
        registerFlightToComboBox.setItems(airports);

        planeActionsFlightComboBox.setItems(flights);
        passengerBoardingFlightComboBox.setItems(flights);

        // TODO filter flights
        outgoingFlightsTableView.setItems(flights);


        airportsComboBox.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                ObservableList<Plane> availablePlanes = FXCollections.observableArrayList();
                Airport selectedAirport = (Airport) newValue;
                for (Runway runway : selectedAirport.getRunways()) {
                    if (runway != null && !runway.available())
                        availablePlanes.add(runway.getPlane());
                }

                registerFlightPlaneComboBox.setItems(availablePlanes);
            }
        });
    }

    /**
     * Fake data used until a real database will theoretically be added
     */
    void addMockData() {
        City london = new City("London", "UK");
        City newYork = new City("New York", "US");

        // London airports
        Airport gatwick = new Airport("Gatwick", london, 4);
        Airport heathrow = new Airport("Heathrow", london, 3);
        london.setAirports(new Airport[]{gatwick, heathrow});

        // New York airports
        Airport stewart = new Airport("Stewart", newYork, 5);
        Airport macArthur = new Airport("MacArthur", newYork, 2);
        newYork.setAirports(new Airport[]{stewart, macArthur});

        airports.addAll(gatwick, heathrow, stewart, macArthur);


        // Add planes to airports
        gatwick.landPlane(new Plane("Boeing 767"));
        gatwick.landPlane(new Plane("Boeing 747"));

        heathrow.landPlane(new Plane("Boeing 767"));
        heathrow.landPlane(new Plane("Boeing Dreamlifter"));

        stewart.landPlane(new Plane("Boeing 767"));
        stewart.landPlane(new Plane("Boeing 747"));

        macArthur.landPlane(new Plane("Boeing 767"));
        macArthur.landPlane(new Plane("Boeing Dreamlifter"));
    }
}
