//******************************************************************************//
//Author: 1625928																//
//Description: The Client View Apartments will show all the Apartments for the	//
//				client so that he/she can book it. It will obtain all the data	//
//				from the 'myApartmentsBook' MySQL Database table. Additionally,	//
//				it has the book and back buttons.								//
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
public class Client_View_Apartments extends JApplet implements ActionListener
{
	/************************************************ Declaring the variables ************************************************************/
	public boolean isWorking= true;			// Assigning Boolean object representing the value true according to the string
	public String company= "Limited";		// Assigning String object displaying the given value
	private String Status;					// Declaring the current status
	
	// an execution environment that is responsible for adding the technical concerns to the COMPONENTS
	private Container my_container = null;
	private GridLayout gridLayout1 = null;	// instance filed for the grid layout
	private Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
	private static JPanel client_panel;		// The Java Panel
	
	private static JLabel my_label;			// Instance field for the label
	private static Font aFont;				// Font instance field
	
	// Instance fields for buttons
	private JButton book_btn;
	private JButton back_btn;
	
	// Instance fields for the type String
	private static String full_name;		// Full name details taken from the MySQL database
	private static String filename;			// The JDBC URL for filename of the server
	private static String database;			// The JDBC URL for database of the connection for the server
	private static String userName;			// The JDBC URL for user-name for the connection
	private static String thePassword;		// The JDBC URL for the password for the connection
	private static String databaseConn;		// Full JDBC URL connection to the database variable
	private static String logged_user;		// User input details
	
	private static int z = 1;				// An integer for showing the number of rows
	
	// Declaring the connection, statement and result-set
	private static Connection aConnection;
	private static Statement stmt = null;
	private static ResultSet rs;
	//=====================================================================================================================================
	
	/***************************************************** The init() method *************************************************************/
	public void init()
	{
		Status = "Initializing!";			// Displaying the current status
		setBackground(Color.lightGray);		// Setting the background colour to the light-gray so the border of text area will be visible
		
		// set the grid layout with 2 rows and 1 column
		gridLayout1 = new GridLayout(1, 1);
		my_container = new Container();
		my_container.setLayout(gridLayout1);
		
		JPanel client_panel = new JPanel();										// new JPanel with flow layout
		client_panel.setLayout(new BoxLayout(client_panel, BoxLayout.Y_AXIS));	// setting the layout of the panel as a box layout with Vertical axis
		my_container.add(client_panel);
		add(my_container);
		client_panel.setBorder(loweredetched);
		
		//Format the label Client using DOM
		StringBuilder buff = new StringBuilder();
		buff.append("<html><table>");
		buff.append(String.format("<tr><td></td><td></td><td></td><td></td><td></td><td align='left'; border: 0.2px black>%s</td>"+"</tr>", "CLIENT - " + client_name()));
		buff.append(String.format("<tr><td align='left'>%s</td>", "Nº"));
		buff.append(String.format("<td align='left'>%s</td>", "Apartment name"));
		buff.append(String.format("<td align='left'>%s</td>", "Price"));
		buff.append(String.format("<td align='left'>%s</td>", "Available Start Date"));
		buff.append(String.format("<td align='left'>%s</td>", "Available End Date"));
		buff.append(String.format("<td align='left'>%s</td>", "Maximum number of guests"));
		buff.append(String.format("<td align='left'>%s</td>", "Number of Bedrooms"));
		buff.append(String.format("<td align='left'>%s</td>", "Separate Living Room"));
		buff.append(String.format("<td align='left'>%s</td>"+"</tr>", "Number of Bathrooms"));
			
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
			
			stmt = aConnection.createStatement();	// creating the statement
			// Selecting all the records from the Apartments book table
			rs = stmt.executeQuery("SELECT * FROM myApartmentsBook");
			while(rs.next())
			{
				// Displaying all the records inside the table
				buff.append(String.format("<tr><td align='left'>%s</td>", z));
				buff.append(String.format("<td align='left'>%s</td>", rs.getString(1)));
				buff.append(String.format("<td align='left'>%s</td>", rs.getString(2)));
				buff.append(String.format("<td align='left'>%s</td>", rs.getString(3)));
				buff.append(String.format("<td align='left'>%s</td>", rs.getString(4)));
				buff.append(String.format("<td align='left'>%s</td>", rs.getString(5)));
				buff.append(String.format("<td align='left'>%s</td>", rs.getString(6)));
				buff.append(String.format("<td align='left'>%s</td>", rs.getString(7)));
				buff.append(String.format("<td align='left'>%s</td>"+"</tr>", rs.getString(8)));
				z++;	// Incrementing the number of rows
			}
		}
		catch (Exception e)		// Catches the exceptions within the code being executed
		{
			e.printStackTrace();
		} //-------------------------------------------- end of try-catch statement ------------------------------
		//***************************************************************************************************************************************//
		
		buff.append(String.format("<tr><td align='left'>%s</td>"+"</tr>", ""));
		buff.append("</table></html>");
		
		aFont = new Font("Arial", Font.ITALIC, 12);		// font details
		
		my_label = new JLabel(buff.toString());			// adding the table to the label
			my_label.setForeground(Color.black);
			my_label.setFont(aFont);
			client_panel.add(my_label);
		
		book_btn = new JButton("Book an apartment");	// button for booking the apartment
			book_btn.setFont(aFont);
			client_panel.add(book_btn);
			book_btn.addActionListener(this);
			
		back_btn = new JButton("Back");					// button for going back
			back_btn.setFont(aFont);
			client_panel.add(back_btn);
			back_btn.addActionListener(this);
		
		repaint();										// A component repaints itself
	} //================================================== End of method init() ==========================================================
	
	/******************************************* The method for performing the actions ***************************************************/
	// The ActionEvent parameter is an Event object that represents an event (a button click)
	public void actionPerformed(ActionEvent event)
	{
		// if the book button has been clicked, then displays the booking window
		if(event.getSource() == book_btn)
		{
			getContentPane().removeAll();
			Client_Do_Booking page = new Client_Do_Booking();
	        page.init();
	        getContentPane().add(page);
	        getContentPane().validate();
		}
		// if the back button has been clicked, then goes back to the previous page
		if(event.getSource() == back_btn)
		{
			getContentPane().removeAll();
	        Client_Hotel_Management page = new Client_Hotel_Management();
	        page.init();
	        getContentPane().add(page);
	        getContentPane().validate();
		}
	} //================================================ End of method actionPerformed ===================================================
	
	/******************************* The method for displaying full name details of the client *******************************************/
	public String client_name()
	{
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
			
			logged_user = Login.logintext.getText();	// Getting the user-name details
			
			stmt = aConnection.createStatement();		// creating the statement
			// Selecting all the records from the following table with the chosen user-name
			rs = stmt.executeQuery("SELECT * FROM myLoginBooks WHERE userName = '" + logged_user + "'");
			
			if(rs.next())
			{
				// Getting the full name details from that record
				full_name = rs.getString("fullNameDetails");
			}
		}
		catch (Exception e)		// Catches the exceptions within the code being executed
		{
			e.printStackTrace();
		} //-------------------------------------------- end of try-catch statement ------------------------------
		return full_name;	// returning full name to display in this page
	} //================================================== End of method =======================================================================
}// end of class