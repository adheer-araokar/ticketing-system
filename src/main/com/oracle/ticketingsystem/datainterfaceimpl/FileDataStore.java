package main.com.oracle.ticketingsystem.datainterfaceimpl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import main.com.oracle.ticketingsystem.datainterface.DataStoreInterface;
import main.com.oracle.ticketingsystem.definitions.TransportMode;

public class FileDataStore implements DataStoreInterface{
	
	String dataStoreFilePath;
	
	public FileDataStore(String path){
		this.dataStoreFilePath = path;
	}
	
	public String getDataStoreFilePath(){
		return this.dataStoreFilePath;
	}
	
	@Override
	public void insertData(Timestamp ts, TransportMode data) {
		OutputStream ops = null;
        ObjectOutputStream objOps = null;
		HashMap<Timestamp, ArrayList<TransportMode>> fileData = this.selectAllData();
		ArrayList<TransportMode> tmal;
		if(fileData == null){
			fileData = new HashMap<Timestamp, ArrayList<TransportMode>>();
			tmal = new ArrayList<TransportMode>();
			tmal.add(data);
			fileData.put(ts, tmal);
		}
		else{
			tmal = fileData.get(ts);
			tmal.add(data);
			fileData.put(ts, tmal);
		}
		
        try {
            ops = new FileOutputStream(this.dataStoreFilePath, false);
            objOps = new ObjectOutputStream(ops);
            objOps.writeObject(fileData);
            objOps.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try{
                if(objOps != null) objOps.close();
            } catch (Exception ex){
                 
            }
        }
	}
	
	@Override
	public void insertAllData(HashMap<Timestamp, ArrayList<TransportMode>> data) {
		OutputStream ops = null;
        ObjectOutputStream objOps = null;
        try {
            ops = new FileOutputStream(this.dataStoreFilePath, false);
            objOps = new ObjectOutputStream(ops);
            objOps.writeObject(data);
            objOps.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try{
                if(objOps != null) objOps.close();
            } catch (Exception ex){
                 
            }
        }
	}

	@Override
	public HashMap<Timestamp, ArrayList<TransportMode>> selectAllData() {
		HashMap<Timestamp, ArrayList<TransportMode>> result = null;
		
		InputStream fileIs = null;
        ObjectInputStream objIs = null;
        try {
            fileIs = new FileInputStream(this.dataStoreFilePath);
            if(fileIs.available() > 0)
            {
            	objIs = new ObjectInputStream(fileIs);
            	result = (HashMap<Timestamp, ArrayList<TransportMode>>) objIs.readObject();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if(objIs != null) objIs.close();
            } catch (Exception ex){
                 
            }
        }
		
		return result;
	}

}
