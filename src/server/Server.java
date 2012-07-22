package server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import common.remote.BoxOffice;

/**
 * 
 * @author Terri-Anne
 *
 */
public class Server {

	private String boxOffice = "MTL";

	public Server(String boxOffice) {
		super();
		this.boxOffice = boxOffice;
	}
	
	public void process() {
		try {

			Registry registry = LocateRegistry.getRegistry();
			BoxOffice boxOfficeMTL = new BoxOffice(boxOffice);
			System.out.println("boxOfficeMTL.getBoxOffice() " + boxOfficeMTL.getBoxOffice());
			registry.rebind(boxOfficeMTL.getBoxOffice(), boxOfficeMTL);

			System.out.println("The Server " + boxOffice + " is up and running");
		}
		catch (Exception e) {
			System.out.println("The Server " + boxOffice + " failed:\n" + e);
			e.printStackTrace();
		}
	}

	public static void main (String[] args) {

		Server serverMTL = new Server("MTL");
		serverMTL.process();
		
		Server serverNYC = new Server("NYC");
		serverNYC.process();
		
		Server serverTOR = new Server("TOR");
		serverTOR.process();
		
	}
}
