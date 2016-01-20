import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.util.ArrayList;

public class UIController {

    public ComboBox airportsComboBox, registerFlightToComboBox, registerFlightPlaneComboBox,
            planeActionsFlightComboBox, passengerBoardingFlightComboBox;
    public Label airportLabel;
    public TextField passengerBoardingName;
    public TableView outgoingFlightsTableView,
            incomingFlightsTableView;

    // Table Columns - Outgoing Table
    public TableColumn<Flight, String> outgoingFlightStatus, outgoingFlightNumber, outgoingFlightPassengers,
            outgoingFlightTo;

    // Table Columns - Incoming Table
    public TableColumn<Flight, String> incomingFlightStatus, incomingFlightNumber, incomingFlightPassengers,
            incomingFlightFrom;


    ObservableList<Flight> flights = FXCollections.observableArrayList();
    ObservableList<Airport> airports = FXCollections.observableArrayList();
    int flightIdIncrementer = 0;

    Flight getSelectedFlight() {
        if (planeActionsFlightComboBox.getValue() == null) {
            (new Alert(Alert.AlertType.ERROR, "Please select a flight number")).showAndWait();
            return null;
        } else {
            return (Flight) planeActionsFlightComboBox.getValue();
        }
    }

    public void planeTakeoff() {
        Flight flight = getSelectedFlight();
        if (flight != null) {
            if (flight.plane.status != Plane.Status.Ground) {
                (new Alert(Alert.AlertType.ERROR, "Plane must be on ground to take off")).showAndWait();
            } else {
                flight.origin.takeOff(flight.plane);
                flight.plane.status = Plane.Status.Flying;
                updateFlightData();
            }
        }
    }

    public void planeRequestLanding() {
        Flight flight = getSelectedFlight();
        if (flight != null) {
            if (flight.plane.status != Plane.Status.Flying) {
                (new Alert(Alert.AlertType.ERROR, "Plane must be flying to request to land")).showAndWait();
            } else {
                if (!flight.destination.reserveRunway(flight.plane)) {
                    (new Alert(Alert.AlertType.ERROR, "No runway currently available")).showAndWait();
                } else {
                    flight.plane.status = Plane.Status.Landing;
                    updateFlightData();
                }
            }
        }
    }

    public void planeLand() {
        Flight flight = getSelectedFlight();
        if (flight != null) {
            if (flight.plane.status != Plane.Status.Landing) {
                (new Alert(Alert.AlertType.ERROR, "Plane must be landing to land")).showAndWait();
            } else {
                flight.plane.status = Plane.Status.Ground;
                // Remove passengers from plane
                flight.plane.passengers = new ArrayList<Passenger>();
                updateFlightData();
            }
        }
    }


    public void registerFlight() {
        if (airportsComboBox.getValue() == null || registerFlightToComboBox.getValue() == null ||
                registerFlightPlaneComboBox.getValue() == null) {
            (new Alert(Alert.AlertType.ERROR, "Please make sure \"Airport\", \"To\" and \"Plane\" combo boxes have selected values")).showAndWait();
        } else if (airportsComboBox.getValue() == registerFlightToComboBox.getValue()) {
            (new Alert(Alert.AlertType.ERROR, "\"Airport\" (the current airport) and \"To\" (the destination) need to be different airports")).showAndWait();
        } else {
            Plane plane = (Plane) registerFlightPlaneComboBox.getValue();
            Flight flight = new Flight(++flightIdIncrementer, plane,
                    (Airport) airportsComboBox.getValue(), (Airport) registerFlightToComboBox.getValue());
            plane.flight = flight;

            // Add the flight
            flights.add(flight);

            updateAvailablePlanes();

            // Clear inputs
            registerFlightToComboBox.valueProperty().set(null);
            registerFlightPlaneComboBox.valueProperty().set(null);
        }
    }

    public void updateAvailablePlanes() {
        ObservableList<Plane> availablePlanes = FXCollections.observableArrayList();
        Airport selectedAirport = (Airport) airportsComboBox.getValue();
        for (Runway runway : selectedAirport.getRunways()) {
            if (runway != null && !runway.available() && runway.getPlane().flight == null)
                availablePlanes.add(runway.getPlane());
        }

        registerFlightPlaneComboBox.setItems(availablePlanes);
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

            updateFlightData();
            updateFlightTableFilters();
        }
    }

    public void updateFlightTableFilters() {
        outgoingFlightsTableView.setItems(new FilteredList<Flight>(flights, flight -> flight.origin == airportsComboBox.getValue()));
        incomingFlightsTableView.setItems(new FilteredList<Flight>(flights, flight -> flight.destination == airportsComboBox.getValue()));
    }

    /**
     * A little hack that forces the observable flight objects to update
     */
    void updateFlightData() {
        if (flights.size() > 0) {
            Flight f1 = flights.get(flights.size() - 1);
            flights.remove(flights.size() - 1);
            flights.add(f1);
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

        updateFlightTableFilters();
        outgoingFlightNumber.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<String>(cellData.getValue().number + ""));
        outgoingFlightTo.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<String>(cellData.getValue().destination + ""));
        outgoingFlightStatus.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<String>(cellData.getValue().plane.status + ""));
        outgoingFlightPassengers.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<String>(cellData.getValue().plane.passengers.size() + ""));


        incomingFlightNumber.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<String>(cellData.getValue().number + ""));
        incomingFlightFrom.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<String>(cellData.getValue().origin + ""));
        incomingFlightStatus.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<String>(cellData.getValue().plane.status + ""));
        incomingFlightPassengers.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<String>(cellData.getValue().plane.passengers.size() + ""));


        airportsComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateAvailablePlanes();
            updateFlightTableFilters();
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
        Plane plane1 = new Plane("Boeing 767");
        gatwick.reserveRunway(plane1);
        gatwick.reserveRunway(new Plane("Boeing 747"));

        heathrow.reserveRunway(new Plane("Boeing 767"));
        heathrow.reserveRunway(new Plane("Boeing Dreamlifter"));

        stewart.reserveRunway(new Plane("Boeing 767"));
        stewart.reserveRunway(new Plane("Boeing 747"));

        macArthur.reserveRunway(new Plane("Boeing 767"));
        macArthur.reserveRunway(new Plane("Boeing Dreamlifter"));

        flights.add(new Flight(++flightIdIncrementer, plane1, stewart, gatwick));
    }
}
