package common.remote;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentMap;

import common.local.Show;

/**
 * 
 * @author Terri-Anne
 *
 */
public interface BoxOfficeInterface extends Remote {

	/**
	 * 
	 * @return
	 * @throws RemoteException
	 */
	public String getBoxOffice() throws RemoteException;
	
	/**
	 * 
	 * @return
	 * @throws RemoteException
	 */
	public ConcurrentMap<String, Show> getShowList() throws RemoteException;

	/**
	 * Reserve the required number of tickets (<b>numberOfTickets</b>) for the specified 
	 * show (<b>showID</b>) for the customer (<b>customerID</b>)
	 * @param customerID
	 * @param showID
	 * @param numberOfTickets
	 * @throws Exception
	 */
	public void reserve(int customerID, String showID,
			int numberOfTickets) throws Exception;

	/**
	 * Cancel the specified number of tickets (<b>numberOfTickets</b>) for the show (<b>showID</b>) 
	 * that the customer (<b>customerID</b>) had already reserved. The number of tickets cancelled 
	 * may be smaller than or equal to the number of tickets reserved.
	 * @param customerID
	 * @param showID
	 * @param numberOfTickets
	 * @throws IOException
	 * @throws Exception
	 */
	public abstract void cancel(int customerID, String showID,
			int numberOfTickets) throws IOException, Exception;

	/**
	 * Returns the number of tickets currently available for the specified show (showID).
	 * @param showID
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public abstract int check(String showID) throws IOException, Exception;

}