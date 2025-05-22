package main.factory;

import main.ticket.Ticket;
import main.flight.Flight;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Factory
{
  private static int id = 0;

  public static Ticket createTicket(int planeId, String passengerName, String flightNumber, String destination, String airline)
  {
    return new Ticket(planeId, passengerName, flightNumber, destination,airline);
  }
  public static Flight createFlight(String destination, String trainNumber, String departureTime, String arrivalTime, int seatCount, String airline)
  {
    return new Flight(++id, destination, trainNumber, LocalTime.parse(departureTime),LocalTime.parse(arrivalTime), seatCount, airline);
  }

  public static List<Ticket> createTickets() {
    List<Ticket> tickets = new ArrayList<>();
    tickets.add(createTicket(1, "Alice Smith", "FL123", "New York", "Delta Air Lines"));
    tickets.add(createTicket(2, "Bob Johnson", "FL456", "Tokyo", "ANA"));
    tickets.add(createTicket(1, "Ivan Top", "FL123", "New York", "Delta Air Lines"));
    return tickets;
  }


  public static List<main.flight.Flight> createFlights() {
    List<main.flight.Flight> flights = new ArrayList<>();
    flights.add(createFlight("New York", "FL223", "10:30", "14:15", 299, "Delta Air Lines"));
    flights.add(createFlight("Tokyo", "FL456", "12:00", "21:30", 149, "ANA"));
    flights.add(createFlight("Paris", "FL789", "14:00", "15:35", 200, "Air France"));
    flights.add(createFlight("London", "FL012", "16:45", "20:00", 250, "British Airways"));
    flights.add(createFlight("Dubai", "FL345", "18:30", "23:30", 100, "Emirates"));
    flights.add(createFlight("Dubai", "FL145", "17:30", "22:00", 100, "Emirates"));
    flights.add(createFlight("New York", "FL123", "09:30", "14:15", 300, "American Air Lines"));
    flights.add(createFlight("Singapore", "FL678", "20:15", "04:00", 180, "Singapore Airlines"));
    flights.add(createFlight("Istanbul", "FL878", "23:20", "03:45", 200, "Turkish Airlines"));
    return flights;
  }
}