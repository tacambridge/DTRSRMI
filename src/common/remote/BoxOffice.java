package common.remote;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import common.local.Show;
import common.local.ShowFactory;

/**
 * 
 * @author Terri-Anne
 *
 */
public class BoxOffice extends UnicastRemoteObject implements Serializable, BoxOfficeInterface {

	private static final long serialVersionUID = -4059047206132137196L;
	private ConcurrentMap<String, Show> showList = new ConcurrentHashMap<String, Show>();
	private static final String PREFIX = "BO_";
	private String boxOffice;

	public BoxOffice(String boxOffice) throws Exception {
		super();
		
		if(boxOffice.length() == 3)
			this.boxOffice = boxOffice.toUpperCase();
		else 
			throw new Exception("Invalid box office.");
		
		this.boxOffice = PREFIX + boxOffice;
		this.showList = ShowFactory.getInstance().getListOfShows(boxOffice);
	}

	public String getBoxOffice() throws RemoteException {
		return boxOffice;
	}


	@Override
	public ConcurrentMap<String, Show> getShowList() throws RemoteException {
		return showList;
	}


	@Override
	public void reserve(int customerID, String showID, int numberOfTickets) throws Exception {
		Show show = showList.get(showID);
		if(show != null)
			show.sellTickets(customerID, numberOfTickets);
	}


	@Override
	public void cancel(int customerID, String showID, int numberOfTickets) throws IOException, Exception {
		Show show = showList.get(showID);
		if(show != null)
			show.reimburseTickets(customerID, numberOfTickets);
	}


	@Override
	public int check(String showID) throws IOException, Exception {
		Show show = showList.get(showID);
		if(show != null)
			return show.getNumberOfRemainingTickets();
		else
			return -1; //negative value means show not there 
	}
}
