package main.service;

import main.flight.Flight;
import main.flight.FlightRepository;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;

public class FlightText implements FlightRepository
{
  @Override
  public void outputList(List<Flight> flights, Path file)
  {
    try (PrintWriter out = new PrintWriter(Files.newBufferedWriter(file, StandardCharsets.UTF_8))) 
    {
      if (flights != null)
      {
        for (Flight flight : flights)
        {
          out.println(flight.getId() + ";" +
              flight.getDestination() + ";" +
              flight.getFlightNumber() + ";" +
              flight.getDepartureTime() + ";" +
              flight.getArrivalTime() + ";" +
              flight.getSeatCount() + ";"+
              flight.getAirline() + ";");
        }
      }
    } 
    catch (IOException e) 
    {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void outputList(List<Flight> flights, String fileName)
  {
    Path file = Path.of(fileName);
    outputList(flights, file);
  }

  @Override
  public List<Flight> readList(Path file)
  {
      List<Flight> flights = new ArrayList<>();

      try (BufferedReader in = Files.newBufferedReader(file, StandardCharsets.UTF_8)) 
      {
          String line;
          while ((line = in.readLine()) != null) 
          {
              String[] tokens = line.split(";");
              if (tokens.length == 7)
              {
                  Flight train = new Flight(
                      Integer.parseInt(tokens[0]),
                      tokens[1],
                      tokens[2],
                      LocalTime.parse(tokens[3]),
                      LocalTime.parse(tokens[4]),
                      Integer.parseInt(tokens[5]),
                      tokens[6]
                  );
                  flights.add(train);
              }
          }
      } 
      catch (IOException e) 
      {
          throw new RuntimeException(e);
      }

      return flights;
  }

  @Override
  public List<Flight> readList(String fileName)
  {
    Path file = Path.of(fileName);
    return readList(file);
  }
}
