package main.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import main.factory.Factory;
import main.io.Input;
import main.io.View;
import main.service.Service;
import main.flight.Flight;
import main.ticket.Ticket;

import java.time.LocalTime;
import java.util.Optional;

public class Controller
{
  private  final List<Flight> tickets1 = Factory.createFlights();

  private  final List<Ticket> tickets = Factory.createTickets();
  private final View view = new View();
  private final Service service = new Service();

  private final Input input = new Input();

  @FXML
  private ComboBox<String> operationsChoiceBox;

  @FXML
  private TableView<Ticket> ticketsTable;

  @FXML
  private TableColumn<Ticket, String> passengerNameCol, flightNumberCol, destinationCol, airlineCol;

  @FXML
  private TableView<Flight> flightTable;

  @FXML
  private TableColumn<Flight, Integer>  idColumn, seatCount;

  @FXML
  private TableColumn<Flight, String> destination,  flightNumber, departureTime,arrivalTime, airline;


  public void initialize()
  {
    idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    destination.setCellValueFactory(new PropertyValueFactory<>("destination"));
    flightNumber.setCellValueFactory(new PropertyValueFactory<>("flightNumber"));
    departureTime.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDepartureTime().toString()));
    arrivalTime.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getArrivalTime().toString()));
    seatCount.setCellValueFactory(new PropertyValueFactory<>("seatCount"));
    airline.setCellValueFactory(new PropertyValueFactory<>("airline"));
    //flightTable.setItems(FXCollections.observableArrayList(tickets1));
    initializeTicketsTable();
  }

  public void initializeTicketsTable()
  {
    passengerNameCol.setCellValueFactory(new PropertyValueFactory<>("passengerName"));
    flightNumberCol.setCellValueFactory(new PropertyValueFactory<>("flightNumber"));
    destinationCol.setCellValueFactory(new PropertyValueFactory<>("destination"));
    airlineCol.setCellValueFactory(new PropertyValueFactory<>("airline"));
    //ticketsTable.setItems(FXCollections.observableArrayList(tickets));
  }

  public void updateTable(List<Flight> flights)
  {
    flightTable.setItems(FXCollections.observableList(flights));
  }
  public void updateTicketsTable(List<Ticket> tickets)
  {
    ticketsTable.setItems(FXCollections.observableList(tickets));
  }

  public void close()
  {
    Platform.exit();
  }

  @FXML
  public void saveAs() 
  {
    input.saveData(tickets1, tickets);
    updateTable(tickets1);
    updateTicketsTable(tickets);
  }

  @FXML
  public void readFrom()
  {
    input.readData(tickets1, tickets);
    updateTable(tickets1);
    updateTicketsTable(tickets);
  }


  @FXML
  public void showTickets()
  {
    updateTicketsTable(tickets);

    ticketsTable.setVisible(true);
    flightTable.setVisible(false);
  }

  @FXML
  public void showFlights()
  {
    updateTable(tickets1);

    flightTable.setVisible(true);
    ticketsTable.setVisible(false);
  }

  @FXML
  public void addFlight()
  {
    ticketsTable.setVisible(false);
    flightTable.setVisible(true);
    Flight newFlight = input.enterNewFlight(flights);
    if (newFlight == null) return;
    service.addFlights(tickets1, newFlight);
    updateTable(tickets1);
  }

  @FXML
  public void deleteFlight()
  {
    ticketsTable.setVisible(false);
    flightTable.setVisible(true);

    Flight selectedFlight = flightTable.getSelectionModel().getSelectedItem();
    if (selectedFlight == null)
    {
      View.showError("No flight selected.");
      return;
    }

    int flightId = selectedFlight.getId();
    boolean removed = service.removeFlight(tickets1, tickets, flightId);

    if (removed) {
      updateTable(tickets1);
      updateTicketsTable(tickets);
      View.showText("Flight", "Flight successfully deleted.");
    } else {
      View.showError("Failed to delete the flight.");
    }
  }

  @FXML
  public void addTicket()
  {
    ticketsTable.setVisible(false);
    flightTable.setVisible(true);

    Optional<Ticket> optionalTicket = input.createTicketFromSelection(flightTable);
    if (optionalTicket.isEmpty()) return;

    Ticket newTicket = optionalTicket.get();
    service.addTicket(tickets, newTicket);

    updateTicketsTable(tickets);
    input.showTicketAddedMessage();
    updateTable(tickets1);

    ticketsTable.setVisible(true);
    flightTable.setVisible(false);
  }

  @FXML
  public void deleteTicket()
  {
    flightTable.setVisible(false);
    ticketsTable.setVisible(true);

    Ticket selectedTicket = ticketsTable.getSelectionModel().getSelectedItem();
    if (selectedTicket == null)
    {
      View.showError("No ticket selected.");
      return;
    }

    boolean removed = service.removeTicket(tickets, tickets1, selectedTicket);
    if (removed)
    {
      flightTable.refresh();
      updateTicketsTable(tickets);
      updateTable(tickets1);
      view.showText("Ticket", "Ticket successfully deleted.");
    }
    else
    {
      View.showError("Failed to delete the ticket.");
    }
  }

  @FXML
  public void about()
  {
    view.showText("About", """
                -Всё списали? 
                -Нет, не все!
                """);
  }

  public void runOperation()
  {
    String operation = operationsChoiceBox.getValue();
    if (operation == null) return;

    switch (operation) {
      case "1. Find flights by destination" ->
      {
        String city = input.enterText("Enter destination city:");
        List<Flight> result = service.findByDestination(tickets1, city);
        flightTable.setVisible(true);
        ticketsTable.setVisible(false);
        updateTable(result);
      }

      case "2. Find flights after specific time" ->
      {
        LocalTime time = input.enterTime("Enter departure time (HH:mm):");
        List<Flight> result = service.findByTime(tickets1, time);
        flightTable.setVisible(true);
        ticketsTable.setVisible(false);
        updateTable(result);
      }

      case "3. Find ticket by flight number" ->
      {
        String number = input.enterText("Enter flight number:");
        List<Ticket> result = service.findByTicketNumber(tickets, number);
        flightTable.setVisible(false);
        ticketsTable.setVisible(true);
        updateTicketsTable(result);
      }

      case "4. Sort by time and number" ->
      {
        List<Flight> sortedFlights = service.sortByStopsAndNumber(tickets1);
        flightTable.setVisible(true);
        ticketsTable.setVisible(false);
        updateTable(sortedFlights);
      }

      case "5. Check flight by number and airline" ->
      {
        String number = input.enterText("Enter flight number:");
        String air = input.enterText("Enter airlines:");
        Flight found = service.checkFlightByNumberAndAirline(tickets1, number, air);
        flightTable.setVisible(true);
        ticketsTable.setVisible(false);
        if (found != null)
        {
          updateTable(List.of(found));
        } else {
          flightTable.getItems().clear();
        }
      }

      case "6. Sort ticket by airline" ->
      {
        List<Ticket> result = service.sortByAirline(tickets);
        flightTable.setVisible(false);
        ticketsTable.setVisible(true);
        updateTicketsTable(result);
      }

      case "7. Group flights by destination" ->
      {
        Map<String, List<Flight>> grouped = service.getFlightsGroupedByDestinationSortedByNumber(tickets1);
        List<Flight> all = grouped.values().stream().flatMap(List::stream).toList();
        flightTable.setVisible(true);
        ticketsTable.setVisible(false);
        updateTable(all);
      }

      case "8. For destination find the flight with the earliest departure time" -> {
        Map<String, Flight> leastStopFlight = service.getWithLessDepartureTime(tickets1);
        flightTable.setVisible(true);
        ticketsTable.setVisible(false);
        updateTable(new ArrayList<>(leastStopFlight.values()));
      }

      default -> flightTable.getItems().clear();
    }
  }
}
