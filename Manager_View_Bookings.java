//******************************************************************************//
//Author: 1625928																//
//Description: Manager View Bookings class shows all the bookings. It displays	//
//				all the records from the 'myBookingsBook' MySQL Database Table.	//
//				Afterwards, the manager will be able to manage the specific		//
//				booking by pressing the correspondent button. The application	//
//				will prompt the user to enter the Booking ID number in order to	//
//				manage it.														//
//******************************************************************************//

//importing all the packages in order to run the code
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

//The following class extends the Java Applet that works like the main window where components are added to create an AppletGUI
//The class also implements ActionListener which is notified whenever you click on the button or menu item
public class Manager_View_Bookings extends JApplet implements ActionListener
{
	/************************************************ Declaring the variables ************************************************************/
	public boolean isWorking= true;			// Assigning Boolean object representing the value true according to the string
	public String company= "Limited";		// Assigning String object displaying the given value
	private String Status;					// Declaring the current status
	
	// an execution environment that is responsible for adding the technical concerns to the COMPONENTS
	private Container my_container = null;
	// instance filed for the grid layout
	private GridLayout gridLayout1 = null;
	// instance field for border
	private Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
	
	private static JPanel thePanel;			// The Java Panel
	private static JLabel my_label;			// The label for the table
	private static Font aFont;				// The font of the components
	
	// Instance fields for button
	private static JButton manage_bk_bt;
	private static JButton back_bt;
	
	// Declaring the connection, statement and result-set
	private static Connection aConnection;
	private static Statement stmt = null;
	private static ResultSet Getting_Bkg_Rcrds;		// Getting the records from the Apartments book
	
	// Instance fields for the type String
	private static String filename;			// The JDBC URL for filename of the server
	private static String database;			// The JDBC URL for database of the connection for the server
	private static String userName;			// The JDBC URL for user-name for the connection
	private static String thePassword;		// The JDBC URL for the password for the connection
	private static String databaseConn;		// Full JDBC URL connection to the database variable
	//=====================================================================================================================================
	
	/***************************************************** The init() method *************************************************************/
	public void init()
	{
		Status = "Initializing!";			// Setting the current status
		setBackground(Color.lightGray);		// Setting the background colour to the light-gray so the border of text area will be visible
		
		// set the grid layout with 1 row and 1 column
		gridLayout1 = new GridLayout(1, 1);
		my_container = new Container();
		my_container.setLayout(gridLayout1);
		
		// new JPanel with a vertical box layout
		add(my_container);
		thePanel = new JPanel();
		my_container.add(thePanel);
		thePanel.setBorder(loweredetched);
		thePanel.setLayout(new BoxLayout(thePanel, BoxLayout.Y_AXIS));
		aFont = new Font("Arial", Font.ITALIC, 12);
		
		//Format the label Client using DOM
		StringBuilder buff = new StringBuilder();
		buff.append("<html><table>");
		buff.append(String.format("<tr><td align='left'>%s</td>"+"</tr>", "MANAGER - " + Login.logintext.getText()));
		buff.append(String.format("<tr><td align='left'>%s</td>"+"</tr>", "-----------"));
		buff.append(String.format("<tr><td align='left'>%s</td>", "Booking ID Number"));
		buff.append(String.format("<td align='left'>%s</td>", "Full Name Details"));
		buff.append(String.format("<td align='left'>%s</td>", "Apartment Name"));
		buff.append(String.format("<td align='left'>%s</td>", "Number of Guests"));
		buff.append(String.format("<td align='left'>%s</td>", "Booking Start-Date"));
		buff.append(String.format("<td align='left'>%s</td>", "Booking End-Date"));
		buff.append(String.format("<td align='left'>%s</td>", "Number of Bedrooms"));
		buff.append(String.format("<td align='left'>%s</td>", "Separate Living Room"));
		buff.append(String.format("<td align='left'>%s</td>", "Number of Bathrooms"));
		buff.append(String.format("<td align='left'>%s</td>", "Total Catering Price"));
		buff.append(String.format("<td align='left'>%s</td>"+"</tr>", "Total Price"));
			
		//************************************** Appending the table with the MySQL table records **************************************************//
		//---------------------------------------- The connection to the JDBC driver -----------------------------
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch(ClassNotFoundException x)
		{
			System.out.println("Cannot find driver class. Check CLASSPATH");
		} //-------------------------------------------- end of try-catch statement ------------------------------
		
		//--------------------------------- Connecting to the server and populating the table --------------------
		try
		{
			// Assigning the values for connecting to the Driver
			filename = "jdbc:mysql://ecommsvr5:3306/";
			database = "kanatbeckj_oop";
			userName = "kanatbeckj_aoop";
			thePassword = "1625928";
			databaseConn = filename + database;
			aConnection = DriverManager.getConnection(databaseConn,userName,thePassword);
			
			stmt = aConnection.createStatement();			// Creating the statement
			
			// Selecting all the records from the Bookings Book table
			Getting_Bkg_Rcrds = stmt.executeQuery("SELECT * FROM myBookingsBook");
			while(Getting_Bkg_Rcrds.next())
			{
				buff.append(String.format("<tr><td align='left'>%s</td>", Getting_Bkg_Rcrds.getString(1)));
				buff.append(String.format("<td align='left'>%s</td>", Getting_Bkg_Rcrds.getString(2)));
				buff.append(String.format("<td align='left'>%s</td>", Getting_Bkg_Rcrds.getString(3)));
				buff.append(String.format("<td align='left'>%s</td>", Getting_Bkg_Rcrds.getString(4)));
				buff.append(String.format("<td align='left'>%s</td>", Getting_Bkg_Rcrds.getString(5)));
				buff.append(String.format("<td align='left'>%s</td>", Getting_Bkg_Rcrds.getString(6)));
				buff.append(String.format("<td align='left'>%s</td>", Getting_Bkg_Rcrds.getString(7)));
				buff.append(String.format("<td align='left'>%s</td>", Getting_Bkg_Rcrds.getString(8)));
				buff.append(String.format("<td align='left'>%s</td>", Getting_Bkg_Rcrds.getString(9)));
				buff.append(String.format("<td align='left'>%s</td>", Getting_Bkg_Rcrds.getString(10)));
				buff.append(String.format("<td align='left'>%s</td>"+"</tr>", Getting_Bkg_Rcrds.getString(11)));
			}
		}
		catch (Exception e)		// Catches the exceptions within the code being executed
		{
			e.printStackTrace();
		} //-------------------------------------------- end of try-catch statement ------------------------------
		//***************************************************************************************************************************************//
		buff.append("</table></html>");
		
		my_label = new JLabel(buff.toString());				// Label for table
			my_label.setForeground(Color.black);
			my_label.setFont(aFont);
			thePanel.add(my_label);
		
		manage_bk_bt = new JButton("Select the Booking");	// Button for managing the record
			manage_bk_bt.setFont(aFont);
			thePanel.add(manage_bk_bt);
			manage_bk_bt.addActionListener(this);
			
		back_bt = new JButton("Back");						// Button for going to the previous page
			back_bt.setSize(140,30);
			back_bt.setFont(aFont);
			thePanel.add(back_bt);
			back_bt.addActionListener(this);
		
		repaint();											// A component repaints itself
	} //================================================== End of method init() ==========================================================
	
	/******************************************* The method for performing the actions ***************************************************/
	// The ActionEvent parameter is an Event object that represents an event (a button click)
	public void actionPerformed(ActionEvent event)
	{
		// If the manage button has been clicked
		if(event.getSource() == manage_bk_bt)
		{
			// Goes to the Manager Manage Booking page
			getContentPane().removeAll();
	        Manager_Manage_Booking page = new Manager_Manage_Booking();
	        page.init();
	        getContentPane().add(page);
	        getContentPane().validate();
		}
		
		// If the back button has been clicked
		if(event.getSource() == back_bt)
		{
			// Goes back to the previous page
			getContentPane().removeAll();
	        Manager_View_Tables page = new Manager_View_Tables();
	        page.init();
	        getContentPane().add(page);
	        getContentPane().validate();
		}
	} //================================================== End of method actionPerformed ==========================================================
}// end class