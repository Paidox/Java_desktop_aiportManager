package main.ticket;

import main.repository.Repository;

import java.nio.file.Path;
import java.util.List;

public interface TicketRepository extends Repository<Ticket>
{
  void outputList(List<Ticket> Tickets, Path file);

  void outputList(List<Ticket> Tickets, String fileName);

  List<Ticket> readList(Path file);

  List<Ticket> readList(String fileName);
}
