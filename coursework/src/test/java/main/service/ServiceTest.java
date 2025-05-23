package main.service;

import main.factory.Factory;
import main.flight.Flight;
import main.ticket.Ticket;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTest
{
  private Service service;

  @BeforeEach
  void setUp()
  {
    service = new Service();
  }

  @AfterEach
  void cleanup() throws Exception
  {
    Files.deleteIfExists(Path.of("testFlights.bin"));
    Files.deleteIfExists(Path.of("testFlights.txt"));
  }

  @Test
  void testAddTicket()
  {
    List<Ticket> tickets = Factory.createTickets();
    Ticket newTicket = Factory.createTicket(3, "Ivan Topchii", "FL789", "Paris", "Bubu");

    service.addTicket(tickets, newTicket);

    assertEquals(4, tickets.size());
  }

  @Test
  void testRemoveFlightById() {
    Service service = new Service();

    List<Flight> flights = new ArrayList<>();
    List<Ticket> tickets = new ArrayList<>();

    Flight flight = new Flight(1, "Kyiv", "FL001",  LocalTime.of(10, 0), LocalTime.of(12, 0), 100, "UIA");

    flights.add(flight);

    Ticket ticket = new Ticket(1, "John Doe", "FL001", "Kyiv", "UIA");
    tickets.add(ticket);

    boolean removed = service.removeFlight(flights, tickets, 1);

    assertTrue(removed);
    assertEquals(0, flights.size());
    assertEquals(0, tickets.size());
  }

  @Test
  void testAddFlight() {
    List<Flight> planes = Factory.createFlights();
    Flight newPlane = Factory.createFlight("Rome", "FL999", "13:00", "17:00", 180, "Alitalia");

    service.addFlights(planes, newPlane);

    assertEquals(10, planes.size());
    assertEquals(newPlane, planes.get(9));
  }

  @Test
  void testRemoveTicket()
  {
    List<Flight> flights = Factory.createFlights();
    List<Ticket> tickets = Factory.createTickets();

    Ticket ticketToRemove = tickets.get(0);

    boolean removed = service.removeTicket(tickets, flights, ticketToRemove);

    assertTrue(removed);
    assertEquals(2, tickets.size());
  }

  @Test
  void testFindByDestination()
  {
    String expected = "Dubai";
    List<Flight> planes = Factory.createFlights();

    List<Flight> result = service.findByDestination(planes, "Dubai");

    for (Flight plane : result)
    {
      Assertions.assertEquals(expected, plane.getDestination());
    }
  }

  @Test
  void testFindByTime()
  {
    List<Flight> planes = Factory.createFlights();

    List<Flight> result = service.findByTime(planes, LocalTime.parse("12:00"));

    List<String> expectedDestinations = new ArrayList<>();
    expectedDestinations.add("Paris");
    expectedDestinations.add("London");
    expectedDestinations.add("Dubai");
    expectedDestinations.add("Dubai");
    expectedDestinations.add("Singapore");
    expectedDestinations.add("Istanbul");

    List<String> actualDestinations = result.stream()
            .map(Flight::getDestination)
            .collect(Collectors.toList());

    Assertions.assertEquals(expectedDestinations.size(), actualDestinations.size());
    Assertions.assertIterableEquals(expectedDestinations, actualDestinations);
  }

  @Test
  void testByNumber()
  {
    List<Ticket> tickets = Factory.createTickets();

    List<Ticket> result = service.findByTicketNumber(tickets, "FL123");

    List<String> expected = new ArrayList<>();
    expected.add("Alice Smith");
    expected.add("Ivan Top");

    List<String> actual = result.stream()
            .map(Ticket::getPassengerName)
            .toList();

    Assertions.assertEquals(expected.size(), actual.size());
    Assertions.assertIterableEquals(expected, actual);
  }

  @Test
  void testSortByStopsAndPlaneNumber()
  {
    List<Flight> planes = Factory.createFlights();

    List<Flight> result = service.sortByStopsAndNumber(planes);

    List<String> expectedDestinations = new ArrayList<>();
    expectedDestinations.add("Paris");
    expectedDestinations.add("London");
    expectedDestinations.add("New York");
    expectedDestinations.add("Istanbul");
    expectedDestinations.add("Dubai");
    expectedDestinations.add("New York");
    expectedDestinations.add("Dubai");
    expectedDestinations.add("Singapore");
    expectedDestinations.add("Tokyo");

    List<String> actualDestinations = result.stream()
            .map(Flight::getDestination)
            .collect(Collectors.toList());

    Assertions.assertIterableEquals(expectedDestinations, actualDestinations);
  }

  @Test
  void testCheckPlaneByNumberAndAirline()
  {
    List<Flight> planes = Factory.createFlights();

    Flight result = service.checkFlightByNumberAndAirline(planes, "FL456","ANA");
    String expected = "Tokyo";

    assertEquals(expected, result.getDestination());
  }

  @Test
  void testSortByAirline()
  {
    List<Ticket> tickets = Factory.createTickets();

    List<Ticket> result = service.sortByAirline(tickets);

    List<String> expected = new ArrayList<>();
    expected.add("Tokyo");
    expected.add("New York");
    expected.add("New York");

    List<String> actual = result.stream()
            .map(Ticket::getDestination)
            .collect(Collectors.toList());

    Assertions.assertEquals(expected.size(), actual.size());
    Assertions.assertIterableEquals(expected, actual);
  }

  @ParameterizedTest
  @CsvSource({
          "Dubai, FL145, FL345",
          "New York, FL123, FL223"
  })
  void testGetPlanesGroupedByDestinationSortedByNumber(String destination, String firstFlight, String lastFlight) {
    List<Flight> planes = Factory.createFlights();

    Map<String, List<Flight>> grouped = service.getFlightsGroupedByDestinationSortedByNumber(planes);
    List<Flight> destinationPlanes = grouped.get(destination);

    List<String> flightNumbers = new ArrayList<>();
    for (Flight f : destinationPlanes) {
      flightNumbers.add(f.getFlightNumber());
    }

    Assertions.assertEquals(firstFlight, flightNumbers.get(0));
    Assertions.assertEquals(lastFlight, flightNumbers.get(flightNumbers.size() - 1));
  }

  @ParameterizedTest
  @CsvSource({
          "Dubai, 17:30",
          "New York, 09:30"
  })
  void testGetWithLessDepartureTime(String destination, String time1)
  {
    List<Flight> flights = Factory.createFlights();

    Map<String, Flight> grouped = service.getWithLessDepartureTime(flights);
    Flight earliestPlane = grouped.get(destination);

    Assertions.assertEquals(time1, earliestPlane.getDepartureTime().toString());
  }

  @Test
  void testOutputAndReadBinFlight() {
    List<Flight> flights = new ArrayList<>();
    flights.add(Factory.createFlight("New York", "AA101", "10:00", "13:00", 100, "AirlineA"));
    flights.add(Factory.createFlight("London", "BB202", "12:30", "10:00", 120, "AirlineB"));

    service.outputListBinFlight(flights, "testFlights.bin");
    List<Flight> readFlights = service.readListBinFlight("testFlights.bin");

    assertNotNull(readFlights);
    assertEquals(flights.size(), readFlights.size());
  }

  @Test
  void testOutputAndReadTextFlight() {
    List<Flight> flights = new ArrayList<>();
    flights.add(Factory.createFlight("New York", "AA101", "10:00", "13:00", 100, "AirlineA"));
    flights.add(Factory.createFlight("London", "BB202", "12:30", "10:00", 120, "AirlineB"));

    service.outputListTextFlight(flights, "testFlights.txt");
    List<Flight> readFlights = service.readListTextFlight(flights, "testFlights.txt");

    assertNotNull(readFlights);
    assertEquals(flights.size(), readFlights.size());
  }

}
