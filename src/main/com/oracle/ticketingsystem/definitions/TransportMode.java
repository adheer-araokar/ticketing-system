package main.com.oracle.ticketingsystem.definitions;

import java.io.Serializable;
import java.sql.Timestamp;

public abstract class TransportMode implements Serializable {
	private static final long serialVersionUID = 1L;
	protected int totalSeats;
	protected int seatsBookedATM;
	protected String source;
	protected String destination;
	protected Timestamp travelTimestamp;
	protected int modeNumber;
	
	public int checkAvailability(){
		return(totalSeats - seatsBookedATM);
	}
	
	public int bookTicket(){
		if(seatsBookedATM < totalSeats){
			return(++seatsBookedATM);
		}
		else
			return -1;
	}

	public int getTotalSeats() {
		return totalSeats;
	}

	public int getSeatsBookedATM() {
		return seatsBookedATM;
	}

	public String getSource() {
		return source;
	}

	public String getDestination() {
		return destination;
	}

	public Timestamp getTravelTimestamp() {
		return travelTimestamp;
	}

	public int getModeNumber() {
		return modeNumber;
	}
	
}
