package main.com.oracle.ticketingsystem.driver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Properties;

import main.com.oracle.ticketingsystem.datainterface.DataStoreInterface;
import main.com.oracle.ticketingsystem.datainterfaceimpl.FileDataStore;
import main.com.oracle.ticketingsystem.definitions.TicketingSystemInterface;
import main.com.oracle.ticketingsystem.impl.FlightTecketingSystem;

public class TicketingSystemDriver {

	public static void main(String[] args) throws IOException, ParseException {
		DataStoreInterface ds = loadProperties();
		TicketingSystemInterface flightSys;
		if(ds == null){
			System.out.println("Unknown Datasource. Exiting...");
			System.exit(1);
		}
		flightSys = FlightTecketingSystem.getInstance(ds);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String option = "";
		System.out.println("Flight Ticketing System!");
		String date;
		int flightNumber;
		String source;
		String destination;
		int totalSeats;
		do{
			System.out.println("Commands :- Add Flight Data - A, Show Flights - S, Check Availability - C, Book Ticket - B, Exit - E");
			System.out.println("Enter command");
			option = br.readLine();
		    switch(option.toUpperCase()) {
		       case "A" :
		    	   System.out.println("Add Flight Selected.");
		    	   System.out.println("Enter Date in dd/MM/yyyy format");
		    	   date = br.readLine();
		    	   System.out.println("Enter FlightNumber (Only Numbers)");
		    	   flightNumber = Integer.parseInt(br.readLine());
		    	   System.out.println("Enter Source Airport");
		    	   source = br.readLine();
		    	   System.out.println("Enter Destination Airport");
		    	   destination = br.readLine();
		    	   System.out.println("Enter Total number of seats (Only Numbers greater than 0)");
		    	   totalSeats = Integer.parseInt(br.readLine());
		    	   flightSys.addMode(date, flightNumber, source, destination, totalSeats);
		           break;
		       case "S" :
		    	   System.out.println("Show Flights Selected.");
		    	   System.out.println("Enter Date in dd/MM/yyyy format");
		    	   date = br.readLine();
		    	   System.out.println("Enter Source Airport");
		    	   source = br.readLine();
		    	   System.out.println("Enter Destination Airport");
		    	   destination = br.readLine();
		    	   ArrayList<String> flights = flightSys.showFlights(source, destination, date);
		    	   if(flights != null){
		    		   System.out.println("List of Flights :- ");
			    	   for (String flight : flights) {
			    	   	   System.out.println(flight);
			    	   }
		    	   }else
		    		   System.out.println("Currently No flights exixt for the given input.");
		    	   break;
		       case "C" :
		    	   System.out.println("Check Flight Availability Selected.");
		    	   System.out.println("Enter Date in dd/MM/yyyy format");
		    	   date = br.readLine();
		    	   System.out.println("Enter FlightNumber (Only Numbers)");
		    	   flightNumber = Integer.parseInt(br.readLine());
		    	   int availableSeats = flightSys.checkAvailability(date, flightNumber);
		    	   if(availableSeats == -1){
		    		   System.out.println("This flight does not exist.");
		    		   break;
		    	   }
		    	   System.out.println("This flight has " + availableSeats + " Seats available.");
		           break;
		       case "B" :
		    	   System.out.println("Book Flight Ticket Selected.");
		    	   System.out.println("Enter Date in dd/MM/yyyy format");
		    	   date = br.readLine();
		    	   System.out.println("Enter FlightNumber (Only Numbers)");
		    	   flightNumber = Integer.parseInt(br.readLine());
		    	   int seatNo = flightSys.bookTicket(date, flightNumber);
		    	   if(seatNo == -1){
		    		   System.out.println("This flight does not exist.");
		    		   break;
		    	   }else if(seatNo == 0){
		    		   System.out.println("This flight is full. Cannot book ticket in this flight.");
		    		   break;
		    	   }
		    	   System.out.println("Your ticket has been booked with the seat no. :- " + seatNo);
		           break;
		       case "E" :
		           System.out.println("Exiting!");
		           break;
		       default :
		           System.out.println("Invalid Option!");
		    }
		}while(!option.equalsIgnoreCase("E"));
		
	}
	
	public static DataStoreInterface loadProperties(){
		DataStoreInterface dataSource = null;
		Properties prop = new Properties();
		InputStream input = null;

		try {
			
	        ClassLoader classLoader = TicketingSystemDriver.class.getClassLoader();
	        File file = new File(classLoader.getResource("config.properties").getFile());
	        input = new FileInputStream(file);

			prop.load(input);
			String dataStoreType = prop.getProperty("dataStoreType");
			if(dataStoreType.equalsIgnoreCase("File")){
				dataSource = new FileDataStore(prop.getProperty("path"));
			}
			else if(dataStoreType.equalsIgnoreCase("DB")){
				prop.getProperty("dbConnectionString");
				prop.getProperty("tableName");
				//Create a DBDataStoreInterface and pass properties to it.
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return dataSource;
	}

}
