package main.flight;

import main.repository.Repository;

import java.nio.file.Path;
import java.util.List;

public interface FlightRepository extends Repository<Flight>
 {
  void outputList(List<Flight> flights, Path file);

  void outputList(List<Flight> flights, String fileName);

  List<Flight> readList(Path file);

  List<Flight> readList(String fileName);
}