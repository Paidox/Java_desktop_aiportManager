package main.ticket;

import java.io.Serializable;

public class Ticket implements Serializable
{
  private int flightId;
  private String passengerName;
  private String flightNumber;
  private String destination;
  private String airline;

  public Ticket(int planeId, String passengerName, String flightNumber, String destination, String airline)
  {
    this.flightId = planeId;
    this.passengerName = passengerName;
    this.flightNumber = flightNumber;
    this.destination = destination;
    this.airline = airline;
  }

  public int getFlightId() {
    return flightId;
  }

  public String getPassengerName() {
    return passengerName;
  }

  public String getFlightNumber() {
    return flightNumber;
  }

  public String getDestination() {
    return destination;
  }

  public String getAirline() {
    return airline;
  }

  public void setFlightId(int flightId) {
    this.flightId = flightId;
  }

  public void setPassengerName(String passengerName) {
    this.passengerName = passengerName;
  }

  public void setFlightNumber(String flightNumber) {
    this.flightNumber = flightNumber;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }
  public void setAirline(String airline) {
    this.airline = airline;
  }
}
