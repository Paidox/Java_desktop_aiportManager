package main.io;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import main.factory.Factory;
import main.ticket.Ticket;
import main.flight.Flight;
import main.service.Service;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ArrayList;


public class Input
{
  @FXML
  private TableView<Flight> flightTable;

  @FXML
  private TableView<Ticket> ticketsTable;

  private final Service service = new Service();
  private final View view = new View();

  public String enterText(String prompt) {
    while (true) {
      TextInputDialog dialog = new TextInputDialog();
      dialog.setTitle("Enter Text");
      dialog.setHeaderText(prompt);
      dialog.setContentText("Please enter text:");

      Optional<String> result = dialog.showAndWait();
      if (result.isPresent()) {
        return result.get().trim();
      } else {
        return "";
      }
    }
  }


  public LocalTime enterTime(String prompt)
  {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    while (true) {
      TextInputDialog dialog = new TextInputDialog();
      dialog.setTitle("Enter Time");
      dialog.setHeaderText(prompt);
      dialog.setContentText("Enter time (HH:mm):");

      Optional<String> result = dialog.showAndWait();
      if (result.isPresent()) {
        try {
          return LocalTime.parse(result.get().trim(), formatter);
        } catch (DateTimeParseException e) {
          showError();
        }
      } else {
        return null;
      }
    }
  }


  public void saveData(List<Flight> flights, List<Ticket> tickets)
  {
    String result = chosenFile("Save data to file");
    if (result == null) return;

    String[] parts = result.split(";");
    if (parts.length != 2) return;

    String fileName = parts[0];
    String dataType = parts[1];

    if (fileName.endsWith(".txt")) 
    {
      if (dataType.equals("Flights")) 
      {
        service.outputListTextFlight(flights, fileName);
      } 
      else if (dataType.equals("Ticket")) 
      {
        service.outputListTextTicket(tickets, fileName);
      }
    } 
    else if (fileName.endsWith(".bin")) 
    {
      if (dataType.equals("Flights")) 
      {
        service.outputListBinFlight(flights, fileName);
      } 
      else if (dataType.equals("Ticket")) 
      {
        service.outputListBinTicket(tickets, fileName);
      }
    }
  }

  public void readData(List<Flight> flights, List<Ticket> tickets) {
    String result = chosenFile("Load data from file");
    if (result == null) return;

    String[] parts = result.split(";");
    if (parts.length != 2) return;

    String fileName = parts[0];
    String dataType = parts[1];

    if (dataType.equals("Flights")) 
    {
      flights.clear();
      if (fileName.endsWith(".txt")) 
      {
        flights.addAll(service.readListTextFlight(flights, fileName));
      } 
      else if (fileName.endsWith(".bin")) 
      {
        flights.addAll(service.readListBinFlight(fileName));
      }
    } else if (dataType.equals("Ticket")) 
    {
      tickets.clear();
      if (fileName.endsWith(".txt")) 
      {
        tickets.addAll(service.readListTextTicket(tickets, fileName));
      } 
      else if (fileName.endsWith(".bin")) 
      {
        tickets.addAll(service.readListBinTicket(fileName));
      }
    }
  }

  public Optional<Ticket> createTicketFromSelection(TableView<Flight> flightTable)
  {
    Flight selectedFlight = flightTable.getSelectionModel().getSelectedItem();

    if (selectedFlight == null) {
      View.showError("Please select a flight from the table.");
      return Optional.empty();
    }

    if (selectedFlight.getSeatCount() <= 0) {
      View.showError("No free seats available on this flight.");
      return Optional.empty();
    }

    Ticket ticket = enterNewTicket(selectedFlight);
    if (ticket == null) return Optional.empty();

    ticket.setFlightId(selectedFlight.getId());
    ticket.setFlightNumber(selectedFlight.getFlightNumber());
    ticket.setDestination(selectedFlight.getDestination());

    selectedFlight.setSeatCount(selectedFlight.getSeatCount() - 1);
    flightTable.refresh();

    return Optional.of(ticket);
  }

  public void showTicketAddedMessage()
  {
    View.showText("Ticket", "Ticket successfully added.");
  }

  public Ticket enterNewTicket(Flight selectedFlight) {
    if (selectedFlight == null) {
      View.showError("No flight selected.");
      return null;
    }

    Image image = new Image(Objects.requireNonNull(getClass().getResource("/images/logo.jpg")).toExternalForm());
    ImageView imageView = new ImageView(image);
    imageView.setFitWidth(40);
    imageView.setFitHeight(40);

    Dialog<Ticket> dialog = new Dialog<>();
    dialog.setTitle("Add New Ticket");
    dialog.setHeaderText("Enter passenger name only");
    Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
    stage.getIcons().add(image);
    dialog.setGraphic(imageView);

    Label passengerLabel = new Label("Passenger Name:");
    TextField passengerField = new TextField();

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.add(passengerLabel, 0, 0);
    grid.add(passengerField, 1, 0);

    dialog.getDialogPane().setContent(grid);

    ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

    dialog.setResultConverter(button -> {
      if (button == okButtonType) {
        String passengerName = passengerField.getText().trim();
        if (passengerName.isEmpty()) {
          View.showError("Passenger name cannot be empty.");
          return null;
        }

        return Factory.createTicket(
                selectedFlight.getId(),
                passengerName,
                selectedFlight.getFlightNumber(),
                selectedFlight.getDestination(),
                selectedFlight.getAirline()
        );
      }
      return null;
    });

    Optional<Ticket> result = dialog.showAndWait();
    return result.orElse(null);
  }



    public Flight enterNewFlight(List<Flight> flights) {
    Image image = new Image(Objects.requireNonNull(getClass().getResource("/images/logo.jpg")).toExternalForm());
    ImageView imageView = new ImageView(image);
    imageView.setFitWidth(40);
    imageView.setFitHeight(40);

    Dialog<Flight> dialog = new Dialog<>();
    dialog.setTitle("Add New flight");
    dialog.setHeaderText("Enter flight details");

    Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
    stage.getIcons().add(image);
    dialog.setGraphic(imageView);

    Label destLabel = new Label("Destination:");
    Label flightLabel = new Label("Flight Number:");
    Label timeLabel = new Label("Departure Time (HH:mm):");
    Label arrivalLabel = new Label("Arrival Time (HH:mm):");
    Label seatsLabel = new Label("Seat Count:");
    Label airlineLabel = new Label("Airline:");

    TextField destField = new TextField();
    TextField flightField = new TextField();
    TextField timeField = new TextField();
    TextField arrivalTimeField = new TextField();
    TextField seatsField = new TextField();
    TextField airlineField = new TextField();

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);

    grid.add(destLabel, 0, 0);
    grid.add(destField, 1, 0);
    grid.add(flightLabel, 0, 1);
    grid.add(flightField, 1, 1);
    grid.add(timeLabel, 0, 2);
    grid.add(timeField, 1, 2);
    grid.add(arrivalLabel, 0, 3);
    grid.add(arrivalTimeField, 1, 3);
    grid.add(seatsLabel, 0, 4);
    grid.add(seatsField, 1, 4);
    grid.add(airlineLabel, 0, 5);
    grid.add(airlineField, 1, 5);

    dialog.getDialogPane().setContent(grid);

    ButtonType buttonTypeOk = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, ButtonType.CANCEL);

    dialog.setResultConverter(button ->
    {
      if (button == buttonTypeOk) {
        try {
          String destination = destField.getText().trim();
          String flightNumber = flightField.getText().trim();
          String departureTime = timeField.getText().trim();
          String arrivalTime = arrivalTimeField.getText().trim();
          int seatCount = Integer.parseInt(seatsField.getText().trim());
          String airline = airlineField.getText().trim();

          int newId = flights.stream()
                  .mapToInt(Flight::getId)
                  .max()
                  .orElse(0) + 1;

          return new Flight(newId, destination, flightNumber, LocalTime.parse(departureTime), LocalTime.parse(arrivalTime), seatCount, airline);
        } catch (Exception e) {
          view.showError("Invalid input. Please check all fields and use HH:mm format for times.");
          return null;
        }
      }
      return null;
    });

    Optional<Flight> result = dialog.showAndWait();
    return result.orElse(null);
  }

  public String chosenFile(String title)
  {
    Dialog<String> dialog = new Dialog<>();
    dialog.setTitle(title);
    dialog.setHeaderText("Enter the file name and choose file type and data type");

    ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(20, 150, 10, 10));

    TextField fileName = new TextField();
    fileName.setPromptText("File name");

    ComboBox<String> typeChoice = new ComboBox<>();
    typeChoice.getItems().addAll(".txt", ".bin");

    ComboBox<String> dataChoice = new ComboBox<>();
    dataChoice.getItems().addAll("Flights", "Ticket");

    grid.add(new Label("Name:"), 0, 0);
    grid.add(fileName, 1, 0);
    grid.add(new Label("Type:"), 0, 1);
    grid.add(typeChoice, 1, 1);
    grid.add(new Label("Data:"), 0, 2);
    grid.add(dataChoice, 1, 2);

    dialog.getDialogPane().setContent(grid);

    dialog.setResultConverter(button ->
    {
      if (button == okButtonType)
      {
        String name = fileName.getText();
        String type = typeChoice.getValue();
        String data = dataChoice.getValue();
        if (name != null && type != null && data != null) {
          return name + type + ";" + data;
        }
      }
      return null;
    });

    Optional<String> result = dialog.showAndWait();
    return result.orElse(null);
  }


  private void showError()
  {
    System.out.println("Error");
  }

}
