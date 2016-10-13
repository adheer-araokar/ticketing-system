package main.com.oracle.ticketingsystem.definitions;

import java.text.ParseException;
import java.util.ArrayList;

public interface TicketingSystemInterface {
	public ArrayList<String> showFlights(String source, String dest, String date) throws ParseException;
	public int checkAvailability(String date, int modeNumber) throws ParseException;
	public int bookTicket(String date, int modeNumber) throws ParseException;
	public void addMode(String date, int modeNumber, String source, String destination, int totalSeats) throws ParseException;
}
