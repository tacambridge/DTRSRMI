package common.local;

/**
 * 
 * @author Terri-Anne
 *
 */
public class NotEnoughTicketsException extends Exception {

	private static final long serialVersionUID = 4106526899280391210L;
	private static final String TABS = "\t\t\t\t\t\t\t\t";
	public NotEnoughTicketsException(int customerID, String showID, int numberOfRemainingTickets, int numberOfRequestedTickets) {
		super("\n" + TABS + "* There are not enough tickets available to fullfill " + customerID + "'s requests for Show " + showID
				+ "\n" + TABS + "* Number of Tickets available: " + numberOfRemainingTickets
				+ "\n" + TABS + "* " + customerID + " requesting: " + numberOfRequestedTickets + "\n");
	}

	public NotEnoughTicketsException(String message) {
		super(message);
	}

}
