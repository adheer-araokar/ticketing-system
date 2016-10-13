package main.com.oracle.ticketingsystem.impl;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import main.com.oracle.ticketingsystem.datainterface.DataStoreInterface;
import main.com.oracle.ticketingsystem.definitions.TicketingSystemInterface;
import main.com.oracle.ticketingsystem.definitions.TransportMode;

public class FlightTecketingSystem implements TicketingSystemInterface {
	
	private static HashMap<Timestamp, ArrayList<TransportMode>> allFlightsByTimestamp;
	private static volatile FlightTecketingSystem instance;
	private static DataStoreInterface dataStore;
	
	private FlightTecketingSystem(){}
	
	public static FlightTecketingSystem getInstance(DataStoreInterface dataStore){
		if (instance == null) {
            synchronized (FlightTecketingSystem.class) {
                if (instance == null) {
                    instance = new FlightTecketingSystem();
                    FlightTecketingSystem.dataStore = dataStore;
                    allFlightsByTimestamp = dataStore.selectAllData();
                    if(allFlightsByTimestamp == null){
                    	allFlightsByTimestamp = new HashMap<Timestamp, ArrayList<TransportMode>>();
                    }
                }
            }
        }
		return instance;
	}
	
	@Override
	public ArrayList<String> showFlights(String source, String destination, String date) throws ParseException {
		// TODO Auto-generated method stub
		ArrayList<String> result = new ArrayList<String>();
		Timestamp travelTimestamp = new Timestamp(((new SimpleDateFormat("dd/MM/yyyy")).parse(date)).getTime());
		ArrayList<TransportMode> flights;
		if(allFlightsByTimestamp.containsKey(travelTimestamp)){
			flights = allFlightsByTimestamp.get(travelTimestamp);
			Flight flight;
			for(int i = 0;i<flights.size();i++){
				flight = (Flight)flights.get(i);
				if(flight.getSource().equalsIgnoreCase(source) && flight.getDestination().equalsIgnoreCase(destination))
					result.add(""+flight.getModeNumber());
			}
		}
		if(result.size() == 0)
			result = null;
		return result;
	}

	@Override
	public int checkAvailability(String date, int flightNumber) throws ParseException {
		int result = -1;
		Timestamp travelTimestamp = new Timestamp(((new SimpleDateFormat("dd/MM/yyyy")).parse(date)).getTime());
		ArrayList<TransportMode> flights;
		if(allFlightsByTimestamp.containsKey(travelTimestamp)){
			flights = allFlightsByTimestamp.get(travelTimestamp);
			Flight flight;
			for(int i = 0;i<flights.size();i++){
				flight = (Flight)flights.get(i);
				if(flight.getModeNumber() == flightNumber)
					result = flight.checkAvailability();
			}
		}
		return result;
	}

	@Override
	public int bookTicket(String date, int flightNumber) throws ParseException{
		TransportMode modeInstance = isFlightExists(new Timestamp(((new SimpleDateFormat("dd/MM/yyyy")).parse(date)).getTime()), flightNumber);
		int seatNum = 0;
		if(modeInstance == null)
			return -1;
		if(modeInstance.checkAvailability() > 0){
			seatNum = modeInstance.bookTicket();
			dataStore.insertAllData(allFlightsByTimestamp);
			return seatNum;
		}
		return 0;
	}
	
	public void addMode(String date, int flightNumber, String source, String destination, int totalSeats) throws ParseException{
		Timestamp travelTimestamp = new Timestamp(((new SimpleDateFormat("dd/MM/yyyy")).parse(date)).getTime());
		ArrayList<TransportMode> flights;
		if(allFlightsByTimestamp.containsKey(travelTimestamp)){
			flights = allFlightsByTimestamp.get(travelTimestamp);
			if(isFlightExists(travelTimestamp, flightNumber) == null)
				flights.add(new Flight(source, destination, travelTimestamp, totalSeats, flightNumber));
			else
				System.out.println("Flight already exists. Not adding.");
		}
		else{
			ArrayList<TransportMode> newFlights = new ArrayList<TransportMode>();
			newFlights.add(new Flight(source, destination, travelTimestamp, totalSeats, flightNumber));
			allFlightsByTimestamp.put(travelTimestamp, newFlights);
		}
		dataStore.insertAllData(allFlightsByTimestamp);
	}
	
	private Flight isFlightExists(Timestamp travelTimestamp, int flightNumber){
		Flight result = null;
		ArrayList<TransportMode> flights;
		if(allFlightsByTimestamp.containsKey(travelTimestamp)){
			flights = allFlightsByTimestamp.get(travelTimestamp);
			Flight flight;
			for(int i = 0;i<flights.size();i++){
				flight = (Flight)flights.get(i);
				if(flight.getModeNumber() == flightNumber)
					return flight;
			}
		}
		return result;
	}

}
