package common.local;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import common.util.file.CsvUtil;


/**
 * 
 * @author Terri-Anne
 *
 */
public class Show implements Serializable {

	private static final long serialVersionUID = 4814581410836345296L;
	private String boxOffice;
	private String showID;
	private int showNumber;
	private int numberOfTickets;
	private int numberOfRemainingTickets;
	private ConcurrentMap<Integer, Integer> customerList = new ConcurrentHashMap<Integer, Integer>();
	private CsvUtil csvUtil;

	public Show(String boxOffice, int showNumber, int numberOfTickets) throws Exception {
		super();
		setBoxOffice(boxOffice);
		setShowNumber(showNumber);
		setNumberOfTickets(numberOfTickets);
		String showID = getShowID();
		this.csvUtil = new CsvUtil(showID);
		csvUtil.writeCSV(customerList);
	}

	public String getShowID() {
		if(boxOffice != null && showNumber < 1000 && showNumber > 99)
			showID = boxOffice + showNumber;
		return showID;
	}

	public void setShowID(String showID) {
		this.showID = showID;
	}

	public String getBoxOffice() {
		return boxOffice;
	}

	public void setBoxOffice(String boxOffice) throws Exception {
		if(boxOffice.length() == 3)
			this.boxOffice = boxOffice.toUpperCase();
		else 
			throw new Exception("Invalid box office.");
	}

	public int getShowNumber() {
		return showNumber;
	}

	public void setShowNumber(int showNumber) throws Exception {
		if(showNumber < 1000 && showNumber > 99)
			this.showNumber = showNumber;
		else 
			throw new Exception("Invalid box office.");
	}

	public int getNumberOfTickets() {
		return numberOfTickets;
	}

	public void setNumberOfTickets(int numberOfTickets) {
		numberOfRemainingTickets = numberOfTickets;
		this.numberOfTickets = numberOfTickets;
	}

	public int getNumberOfRemainingTickets() {
		return numberOfRemainingTickets;
	}

	public void setNumberOfRemainingTickets(int numberOfRemainingTickets) {
		this.numberOfRemainingTickets = numberOfRemainingTickets;
	}

	public ConcurrentMap<Integer, Integer> getCustomerList() {
		return customerList;
	}

	public void setCustomerList(ConcurrentMap<Integer, Integer> customerList) {
		this.customerList = customerList;
	}

	public synchronized void sellTickets(int customerID, int numberOfTickets) throws IOException, NotEnoughTicketsException, Exception {
		readFile(customerID);
		if(this.numberOfRemainingTickets >= numberOfTickets) {
			if(Integer.toString(customerID).length() != 6) 
				throw new Exception("Invalid customer ID.");

			int currentNumberOfTickets = 0;
			if(customerList.containsKey(customerID)) {
				currentNumberOfTickets = customerList.get(customerID);
			}
			
			customerList.put(customerID, currentNumberOfTickets + numberOfTickets);
			this.numberOfRemainingTickets -= numberOfTickets;

		}
		else
			throw new NotEnoughTicketsException(customerID, showID, numberOfRemainingTickets, numberOfTickets);
		
		writeToFile(customerID);
	}

	public synchronized void reimburseTickets(int customerID, int numberOfTickets) throws IOException, Exception {
		readFile(customerID);
		if(customerList.containsKey(customerID)) {
			int currentNumberOfTickets = customerList.get(customerID);

			//prevent over reimbursement
			if(numberOfTickets > currentNumberOfTickets)
				numberOfTickets = currentNumberOfTickets;

			customerList.put(customerID, currentNumberOfTickets - numberOfTickets);
			this.numberOfRemainingTickets += numberOfTickets;
		}
		
		writeToFile(customerID);
	}

	public synchronized void printShowRecords() {
		System.out.println("Show ID: " + getShowID() + ", Total Tickets = " 
				+ getNumberOfTickets() + ", Remaining Tickets = " + getNumberOfRemainingTickets());
	}

	@Override
	public String toString() {
		String string = getShowID() + "," + numberOfTickets + "," + numberOfRemainingTickets + "\n";
		return string;
	}

	private synchronized void writeToFile(int customerID) throws IOException, Exception {
		csvUtil.writeCSV(customerList);
		notifyAll();
	}

	private synchronized void readFile(int customerID) throws IOException, Exception {
		customerList.putAll(csvUtil.readCSV());
		notifyAll();
	}

}
