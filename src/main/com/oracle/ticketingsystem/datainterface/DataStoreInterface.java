package main.com.oracle.ticketingsystem.datainterface;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import main.com.oracle.ticketingsystem.definitions.TransportMode;

public interface DataStoreInterface {
	public void insertData(Timestamp timestamp, TransportMode data);
	public void insertAllData(HashMap<Timestamp, ArrayList<TransportMode>> data);
	public HashMap<Timestamp, ArrayList<TransportMode>> selectAllData();
}
