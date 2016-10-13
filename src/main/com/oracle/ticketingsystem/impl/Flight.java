package main.com.oracle.ticketingsystem.impl;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import main.com.oracle.ticketingsystem.definitions.TransportMode;

class Flight extends TransportMode {
	
	Flight(String source, String destination, String travelTimestamp, int totalSeatsInput, int modeNumber) throws ParseException{
		this.source = source;
		this.destination = destination;
		this.travelTimestamp = new Timestamp(((new SimpleDateFormat("dd/MM/yyyy")).parse(travelTimestamp)).getTime());
		totalSeats = totalSeatsInput;
		this.modeNumber = modeNumber;
	}
	Flight(String source, String destination, Timestamp travelTimestamp, int totalSeatsInput, int modeNumber) throws ParseException{
		this.source = source;
		this.destination = destination;
		this.travelTimestamp = travelTimestamp;
		totalSeats = totalSeatsInput;
		this.modeNumber = modeNumber;
	}
}
