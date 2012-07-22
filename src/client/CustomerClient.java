package client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;

import common.local.NotEnoughTicketsException;
import common.local.Show;
import common.remote.BoxOfficeInterface;

/**
 * 
 * @author Terri-Anne
 *
 */
public class CustomerClient extends Thread {

	private int customerID;
	private static final String host = "localhost";
	private static final int port = 1099;
	public final Registry registry = LocateRegistry.getRegistry(host, port);

	public CustomerClient(int customerID) throws Exception {
		if(Integer.toString(customerID).length() == 6)
			this.customerID = customerID;
		else 
			throw new Exception("Invalid customer ID.");
	}

	public int getCustomerID() {
		return customerID;
	}

	public void process() {
		process("MTL");
		process("NYC");
		process("TOR");
	}

	/**
	 * Simulates concurrent user entry
	 * @param boxOfficeName
	 */
	public void process(String boxOfficeName) {

		try {

			String remoteBoxOffice = "BO_" + boxOfficeName;
			BoxOfficeInterface boxOffice = (BoxOfficeInterface) registry.lookup(remoteBoxOffice);

			printBoxOfficeRecords(customerID, boxOffice);

			int numberOfRequestedTickets = 2;
			int numberOfCanceledTickets = 1;
			String tabs = "\t\t\t\t\t\t\t\t";

			try {
				boxOffice.reserve(customerID, boxOfficeName + "111", 2);
				System.out.println(tabs + "Show ID: " + boxOfficeName + "111" + ", CustomerID = " + customerID + ", Requested Tickets = " + numberOfRequestedTickets);
			} catch (NotEnoughTicketsException e) {
				System.out.println(e.getMessage());
			} 

			boxOffice.cancel(customerID, boxOfficeName + "111", 1);
			System.out.println(tabs + "Show ID: " + boxOfficeName + "111" + ", CustomerID = " + customerID + ", Canceling Tickets = " + numberOfCanceledTickets);

			try {			
				boxOffice.reserve(customerID, boxOfficeName + "222", 2);
				System.out.println(tabs + "Show ID: " + boxOfficeName + "222" + ", CustomerID = " + customerID + ", Requested Tickets = " + numberOfRequestedTickets);
			} catch (NotEnoughTicketsException e) {
				System.out.println(e.getMessage());
			} 

			boxOffice.cancel(customerID, boxOfficeName + "222", 1);
			System.out.println(tabs + "Show ID: " + boxOfficeName + "222" + ", CustomerID = " + customerID + ", Canceling Tickets = " + numberOfCanceledTickets);

			try {			
				boxOffice.reserve(customerID, boxOfficeName + "333", 2);
				System.out.println(tabs + "Show ID: " + boxOfficeName + "333" + ", CustomerID = " + customerID + ", Requested Tickets = " + numberOfRequestedTickets);
			} catch (NotEnoughTicketsException e) {
				System.out.println(e.getMessage());
			} 

			boxOffice.cancel(customerID, boxOfficeName + "333", 1);
			System.out.println(tabs + "Show ID: " + boxOfficeName + "333" + ", CustomerID = " + customerID + ", Canceling Tickets = " + numberOfCanceledTickets);


		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println("The Client " + customerID + " failed:\n" + e);
			e.printStackTrace();
		}

	}

	public static synchronized void printBoxOfficeRecords(int customerID, BoxOfficeInterface boxOffice) throws RemoteException {
		System.out.println("\n=======================");
		System.out.println("Customer ID: " + customerID);
		System.out.println("Box Office Shows");
		System.out.println("=======================\n");		
		ConcurrentMap<String, Show> showList = boxOffice.getShowList();
		Iterator<Entry<String, Show>> it = showList.entrySet().iterator();
		while (it.hasNext()) {
			ConcurrentMap.Entry<String, Show> pairs = (ConcurrentMap.Entry<String, Show>)it.next();
			Show show = pairs.getValue();
			show.printShowRecords();
		}
	}

	public void run() {		
		process();
	}

	public static void main (String[] args) {

		try {

			CustomerClient c1 = new CustomerClient(111111);
			c1.start();

			CustomerClient c2 = new CustomerClient(222222);
			c2.start();

			CustomerClient c3 = new CustomerClient(333333);
			c3.start();

			CustomerClient c4 = new CustomerClient(444444);
			c4.start();

			CustomerClient c5 = new CustomerClient(555555);
			c5.start();

			c1.join();
			c2.join();
			c3.join();
			c4.join();
			c5.join();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
