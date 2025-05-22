package main.service;

import java.time.LocalTime;
import main.flight.Flight;
import main.flight.FlightRepository;
import main.ticket.Ticket;
import main.ticket.TicketRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Service 
{
  private final FlightRepository repository = new FlightText();
  private final FlightRepository repository1 = new FlightBinary();

  private final TicketRepository repository2 = new TicketText();
  private final TicketRepository repository3 = new TicketBinary();

  public void outputListBinFlight(List<Flight> flights, String fileName)
  {
    repository1.outputList(flights, fileName);
  }

  public List<Flight> readListBinFlight(String fileName)
  {
    return repository1.readList(fileName);
  }

  public void outputListTextFlight(List<Flight> flights, String fileName)
  {
    repository.outputList(flights, fileName);
  }

  public List<Flight> readListTextFlight(List<Flight> flights, String fileName) {return repository.readList(fileName);}

  public void outputListBinTicket(List<Ticket> tickets, String fileName) {repository3.outputList(tickets, fileName);}

  public List<Ticket> readListBinTicket(String fileName) {return repository3.readList(fileName);}

  public void outputListTextTicket(List<Ticket> tickets, String fileName) {repository2.outputList(tickets, fileName);}

  public List<Ticket> readListTextTicket(List<Ticket> tickets, String fileName) {return repository2.readList(fileName);}


  public void addFlights(List<Flight> flights, Flight newFlight)
  {
    flights.add(newFlight);
  }

  public void addTicket(List<Ticket> tickets, Ticket newTicket)
  {
    tickets.add(newTicket);
  }

  public boolean removeFlight(List<Flight> flights, List<Ticket> tickets, int id)
  {
    tickets.removeIf(ticket -> ticket.getFlightId() == id);
    boolean removed = flights.removeIf(flight -> flight.getId() == id);

    if (removed)
    {
      AtomicInteger counter = new AtomicInteger(1);
      flights.forEach(flight -> flight.setId(counter.getAndIncrement()));
    }
    else
    {
      System.out.println("Flight not found");
    }

    return removed;
  }


  public boolean removeTicket(List<Ticket> tickets, List<Flight> flights, Ticket selectedTicket)
  {
    boolean removed = tickets.remove(selectedTicket);
    if (removed) {
      for (Flight flight : flights) {
        if (flight.getId() == selectedTicket.getFlightId()) {
          flight.setSeatCount(flight.getSeatCount() + 1);
          break;
        }
      }
    }
    return removed;
  }


  public List<Flight> findByDestination(List<Flight> flights, String destination) {
    return flights.stream()
            .filter(flight -> flight.getDestination().equals(destination))
            .collect(Collectors.toList());
  }

  public List<Flight> findByTime(List<Flight> flights, LocalTime departureTime)
  {
    return flights.stream()
            .filter(flight -> flight.getDepartureTime().isAfter(departureTime))
            .collect(Collectors.toList());
  }
  
  public List<Ticket> findByTicketNumber(List<Ticket> tickets, String Number)
  {
    return tickets.stream()
            .filter(ticket -> ticket.getFlightNumber().equals(Number))
            .collect(Collectors.toList());

  } 

  //4. Сортування за Найменшою різницею між відправкою та прибуттям. При збігу - за зростанням номерів.
  public List<Flight> sortByStopsAndNumber(List<Flight> flights)
  {
    return flights.stream()
                  .sorted()
                  .toList();
  }

  //5. Перевірити, чи є вказаний рейс у списку. І, якщо є, чи відповідає авіокомпанії
  public Flight checkFlightByNumberAndAirline(List<Flight> flights, String Number, String airline)
  {
    return flights.stream()
                        .filter(flight -> flight.getFlightNumber().equals(Number) && flight.getAirline().equals(airline))
                        .findFirst()
                        .orElse(null);
  }

  //6.Список пунктів призначення, час прямування до яких менше заданого
  public List<Ticket> sortByAirline(List<Ticket> tickets) {
    return tickets.stream()
            .sorted(Comparator.comparing(Ticket::getAirline))
            .collect(Collectors.toList());
  }



  // 1. Для кожного пункту призначення (key) список рейсів (value) в порядку зростання номерів
  public Map<String, List<Flight>> getFlightsGroupedByDestinationSortedByNumber(List<Flight> flights)
  {
    return flights.stream()
                 .collect(Collectors.groupingBy(Flight::getDestination, Collectors.collectingAndThen(Collectors.toList(),
                  list -> 
                  {
                    list.sort(Comparator.comparing(Flight::getFlightNumber));
                    return list;
                  })));
  }

  //2.Для кожного пункту призначення рейс, що має найраніший виліт
  public Map<String, Flight> getWithLessDepartureTime(List<Flight> flights)
  {
    return flights.stream()
                 .collect(Collectors.toMap(Flight::getDestination, flight -> flight,
                         (p1, p2) -> p1.getDepartureTime().isBefore(p2.getDepartureTime()) ? p1 : p2));

  }
}