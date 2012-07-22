package common.util.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * 
 * @author Terri-Anne
 *
 */
public class CsvUtil implements Serializable {

	private static final long serialVersionUID = -6862872003506676209L;
	private final String FILE_DIRECTORY = System.getProperty("user.dir") + "/files/";
	private final String SHOW = "SHOW_";
	private final int NUMBER_OF_FIELDS = 2;

	private String csvFile;

	public CsvUtil(String csvFile) throws IOException {
		this.csvFile = csvFile + ".csv";
		FileWriter fileWriter = new FileWriter(FILE_DIRECTORY + SHOW + this.csvFile);
		fileWriter.close();
	}


	/**
	 * 
	 * @param csvFile
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public ConcurrentMap<Integer, Integer> readCSV() throws IOException, Exception {
		
		ConcurrentMap<Integer, Integer> list = new ConcurrentHashMap<Integer, Integer>();
		BufferedReader reader = new BufferedReader(new FileReader(FILE_DIRECTORY + SHOW + csvFile));
		String line;
		
		while (( line = reader.readLine()) != null) {
			
			String[] tokens = line.split(",\\s*");
			
			if (tokens.length != NUMBER_OF_FIELDS)
				throw new Exception("Improperly formatted CSV file: Expected " + NUMBER_OF_FIELDS + " comma-separated records, Found " + tokens.length + ".");
			
			int customerID = Integer.parseInt(tokens[0]);
			int numberOfTickets = Integer.parseInt(tokens[1]);
			list.put(customerID, numberOfTickets);
		}
		
		reader.close();
		return list;   
	}


	/**
	 * 
	 * @param csvFile
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public void writeCSV(ConcurrentMap<Integer, Integer> list) throws IOException, Exception {
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_DIRECTORY + SHOW + csvFile));
		Iterator<Entry<Integer, Integer>> it = list.entrySet().iterator();
		
		while (it.hasNext()) {
			
			ConcurrentMap.Entry<Integer, Integer> pairs = (ConcurrentMap.Entry<Integer, Integer>)it.next();
			String line = (Integer)pairs.getKey() + "," + (Integer)pairs.getValue() + "\n";
			writer.write(line);
			
		}
		
		writer.close();
	}

}