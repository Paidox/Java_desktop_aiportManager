package main.service;

import main.flight.Flight;
import main.flight.FlightRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FlightBinary implements FlightRepository
{
  @Override
  public void outputList(List<Flight> flights, Path file)
  {
    try(ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(file)))
    {
      out.writeObject(flights);
    } 
    catch (IOException e) 
    {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void outputList(List<Flight> flights, String fileName)
  {
    outputList(flights, Path.of(fileName));
  }

  @Override
  public List<Flight> readList(Path file)
  {
    try(ObjectInputStream in = new ObjectInputStream(Files.newInputStream(file)))
    {
      Object o = in.readObject();
      return (List<Flight>)o;
    } catch (IOException | ClassNotFoundException e)
    {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<Flight> readList(String fileName)
  {
    return readList(Path.of(fileName));
  }
}
