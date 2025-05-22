package main.service;

import main.flight.Flight;
import main.ticket.Ticket;
import main.ticket.TicketRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TicketText implements TicketRepository {

  @Override
  public void outputList(List<Ticket> tickets, Path file) {
    try (PrintWriter out = new PrintWriter(Files.newBufferedWriter(file, StandardCharsets.UTF_8))) {
      if (tickets != null) {
        for (Ticket ticket : tickets) {
          out.println(ticket.getFlightId() + ";" +
                  ticket.getPassengerName() + ";" +
                  ticket.getFlightNumber() + ";" +
                  ticket.getDestination() + ";" +
                  ticket.getAirline());
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void outputList(List<Ticket> tickets, String fileName) {
    Path file = Path.of(fileName);
    outputList(tickets, file);
  }

  @Override
  public List<Ticket> readList(Path file) {
    List<Ticket> tickets = new ArrayList<>();

    try (BufferedReader in = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
      String line;
      while ((line = in.readLine()) != null) {
        String[] tokens = line.split(";");
        if (tokens.length == 5) {
          Ticket ticket = new Ticket(
                  Integer.parseInt(tokens[0]),
                  tokens[1],
                  tokens[2],
                  tokens[3],
                  tokens[4]
          );
          tickets.add(ticket);
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return tickets;
  }

  @Override
  public List<Ticket> readList(String fileName) {
    Path file = Path.of(fileName);
    return readList(file);
  }
}
