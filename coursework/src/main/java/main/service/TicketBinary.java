package main.service;

import main.ticket.Ticket;
import main.ticket.TicketRepository;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class TicketBinary implements TicketRepository
{
  @Override
  public void outputList(List<Ticket> tickets, Path file)
  {
    try(ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(file)))
    {
      out.writeObject(tickets);
    } 
    catch (IOException e) 
    {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void outputList(List<Ticket> tickets, String fileName)
  {
    outputList(tickets, Path.of(fileName));
  }

  @Override
  public List<Ticket> readList(Path file)
  {
    try(ObjectInputStream in = new ObjectInputStream(Files.newInputStream(file)))
    {
      Object o = in.readObject();
      return (List<Ticket>)o;
    } catch (IOException | ClassNotFoundException e)
    {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<Ticket> readList(String fileName)
  {
    return readList(Path.of(fileName));
  }
}
