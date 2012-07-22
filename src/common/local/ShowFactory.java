package common.local;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 
 * @author Terri-Anne
 *
 */
public class ShowFactory implements Serializable {
	
	private static final long serialVersionUID = -7323196608835294144L;
	private static ShowFactory instance;
	private static final int numberOfTickets = 18;
	private ShowFactory() throws RemoteException {
		
	}
	
	public static ShowFactory getInstance() throws RemoteException {
		if(instance == null)
			instance = new ShowFactory();
		return instance;
	}
	
	private ConcurrentMap<String, Show> createListOfShows(String boxOffice) throws Exception {
		ConcurrentMap<String, Show> list = new ConcurrentHashMap<String, Show>();
		
		Show s1 = new Show(boxOffice, 111, numberOfTickets);
		Show s2 = new Show(boxOffice, 222, numberOfTickets);
		Show s3 = new Show(boxOffice, 333, numberOfTickets);
		
		list.put(s1.getShowID(), s1);
		list.put(s2.getShowID(), s2);
		list.put(s3.getShowID(), s3);
		
		return list;
	}

	public ConcurrentMap<String, Show> getListOfShows(String boxOffice) throws Exception {
		return createListOfShows(boxOffice);
	}

}
