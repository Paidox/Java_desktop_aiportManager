package main.flight;

import main.ticket.Ticket;

import java.io.Serializable;
import java.time.LocalTime;

public class Flight implements Serializable,  Comparable<Flight>
{
  private int id;
  private String destination; 
  private String flightNumber;
  private LocalTime departureTime;
  private LocalTime arrivalTime;
  private int seatCount;
  private String airline;

  public Flight(int id, String destination, String flightNumber, LocalTime departureTime, LocalTime arrivalTime, int seatCount, String airline)
  {
    this.id = id;
    this.destination = destination;
    this.flightNumber = flightNumber;
    this.departureTime = departureTime;
    this.arrivalTime = arrivalTime;
    this.seatCount = seatCount;
    this.airline = airline;
  }

  public int getId() 
  {
    return id;
  }

  public void setId(int id) 
  {
    this.id = id;
  }

  public String getDestination()
  {
    return destination;
  }

  public void setDestination(String destination)
  {
    this.destination = destination;
  }

  public String getFlightNumber()
  {
    return flightNumber;
  }

  public void setFlightNumber(String flightNumber)
  {
    this.flightNumber = flightNumber;
  }

  public LocalTime getDepartureTime() 
  {
    return departureTime;
  }

  public void setDepartureTime(LocalTime departureTime) 
  {
    this.departureTime = departureTime;
  }

  public LocalTime getArrivalTime() {
    return arrivalTime;
  }

  public void setArrivalTime(LocalTime arrivalTime) {
    this.arrivalTime = arrivalTime;
  }

  public int getSeatCount() 
  {
    return seatCount;
  }

  public void setSeatCount(int seatCount) 
  {
    this.seatCount = seatCount;
  }

  public String getAirline()
  {
    return airline;
  }

  public void setAirline(String airline) {
    this.airline = airline;
  }

  @Override
  public String toString() 
  {
    return String.format(
        "Plane [id = %-1d  destination: %-10s  number: %-8s  departure: %-8s arrival: %-8s  seats: %-3d  airline: %-10s]",
        id, destination, flightNumber, departureTime, seatCount, airline
    );
  }

  @Override
  public int compareTo(Flight other)
  {
    int thisDuration = calculateDurationInMinutes(this.departureTime, this.arrivalTime);
    int otherDuration = calculateDurationInMinutes(other.departureTime, other.arrivalTime);

    if (thisDuration != otherDuration) {
      return Integer.compare(thisDuration, otherDuration);
    }

    return this.flightNumber.compareTo(other.flightNumber);
  }

  private int calculateDurationInMinutes(LocalTime dep, LocalTime arr) {
    int depMinutes = dep.getHour() * 60 + dep.getMinute();
    int arrMinutes = arr.getHour() * 60 + arr.getMinute();

    if (arrMinutes < depMinutes) {
      arrMinutes += 24 * 60;
    }

    return arrMinutes - depMinutes;
  }


  @Override
  public boolean equals(Object o) 
  {
    if (this == o) 
    {
      return true;
    }
    if (o == null || getClass() != o.getClass()) 
    {
      return false;
    }
    Flight plane = (Flight) o;
    return flightNumber.equals(plane.flightNumber) && airline.equals(plane.airline);
  }


}