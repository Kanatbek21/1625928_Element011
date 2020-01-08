//******************************************************************************//
//Author: 1625928																//
//Description: The Client Booking class does apartment costs calculations and	//
//				displays it to the user. It only displays the chosen apartment	//
//				details. After the user confirms the details, it will call the	//
//				class that allows 100 user to book the chosen apartment at the	//
//				same time. If the user does not agree with the price, then the	//
//				user can exit, which goes back to the login page.				//
//******************************************************************************//

//importing all the packages in order to run the code
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;
import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

//The following class extends the Java Applet that works like the main window where components are added to create an AppletGUI
//The class also implements ActionListener which is notified whenever you click on the button or menu item
public class Client_Booking extends JApplet implements ActionListener
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
	
	private static JPanel client_panel;			// The Java Panel
	
	private static Font aFont;					// Font instance field
	private static JLabel my_label;				// Instance field for the label
	private static JButton confirm_bt;			// Instance field for confirm button
	private static JButton exit_bt;				// Instance field for exit button
	
	// Instance fields for the type String
	private static String full_name;			// Full name details taken from the MySQL database
	private static String filename;				// The JDBC URL for filename of the server
	private static String database;				// The JDBC URL for database of the connection for the server
	private static String userName;				// The JDBC URL for user-name for the connection
	private static String thePassword;			// The JDBC URL for the password for the connection
	private static String databaseConnection;	// Full JDBC URL connection to the database variable
	private static String logged_user;			// User input details
	private static String the_aprt_nm;			// Client entered apartment name
	private static String gst_num;				// Client entered number of guests
	private static String box_opt;				// Client chosen box option
	
	// Declaring the integers
	private static int aPrice = 0;				// The price of the chosen apartment
	private static int num_of_nights = 0;		// Number of nights
	private static int aprt_cost = 0;			// The apartment cost per night
	private static int the_cat_price = 0;		// The catering price
	private static int total_cat_price = 0;		// The total catering price
	private static int total_aprt_price = 0;	// The total apartment price
	
	// Declaring the connection, statement and result-set
	private static Connection theConnection = null;
	private static Statement stmt = null;
	private static ResultSet rs;					// Result-set of getting full name details
	private static ResultSet get_Client_name;		// Result-set for getting the full name details of the client
	private static ResultSet Aprt_details;			// Result-set for getting the apartment details
	private static ResultSet display_Chosen_Aprt;	// Result-ser for displaying the chosen apartment details
	//=====================================================================================================================================
	
	/***************************************************** The init() method *************************************************************/
	public void init()
	{
		Status = "Initializing!";			// Displaying the current status
		setBackground(Color.lightGray);		// Setting the background colour to the light-gray so the border of text area will be visible
		
		// set the grid layout with 1 row and 1 column
		gridLayout1 = new GridLayout(1, 1);
		my_container = new Container();
		my_container.setLayout(gridLayout1);
		
		add(my_container);
		// new JPanel with flow layout
		client_panel = new JPanel(new FlowLayout());
		
		my_container.add(client_panel);
		client_panel.setBorder(loweredetched);
		aFont = new Font("Arial", Font.ITALIC, 16);
		
		//Format the label Client using DOM
		StringBuilder buff = new StringBuilder();
		buff.append("<html><table>");
		buff.append(String.format("<tr><td align='left'>%s</td>"+"</tr>", "CLIENT - " + client_name()));
		buff.append(String.format("<tr><td align='left'>%s</td>"+"</tr>", "-----------"));
		buff.append(String.format("<tr><td align='left'>%s</td>", "Client ID"));
		buff.append(String.format("<td align='left'>%s</td>", "Name Details"));
		buff.append(String.format("<td align='left'>%s</td>", "Apartment Name"));
		buff.append(String.format("<td align='left'>%s</td>", "Guests Number"));
		buff.append(String.format("<td align='left'>%s</td>", "Booking Start-Date"));
		buff.append(String.format("<td align='left'>%s</td>", "Booking End-Date"));
		buff.append(String.format("<td align='left'>%s</td>", "Total Catering Price"));
		buff.append(String.format("<td align='left'>%s</td></tr>", "Total Price"));
			
		//************************************** Appending the table with the MySQL table records **************************************************//
		//---------------------------------------- The connection to the JDBC driver -----------------------------
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			try {
				filename = "jdbc:mysql://ecommsvr5:3306/";
				database = "kanatbeckj_oop";
				userName = "kanatbeckj_aoop";
				thePassword = "1625928";
				databaseConnection = filename + database;
				
				theConnection = DriverManager.getConnection(databaseConnection, userName, thePassword);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		catch(ClassNotFoundException x)
		{
			System.out.println("Cannot find driver class. Check CLASSPATH");
		} //-------------------------------------------- end of try-catch statement ------------------------------
		
		//--------------------------------- Connecting to the server and populating the table --------------------
		try
		{
			// Getting and assigning the user input values
			the_aprt_nm = Client_Do_Booking.aprt_name_text.getText();
			gst_num = Client_Do_Booking.num_guests_text.getText();
			box_opt = (String) Client_Do_Booking.box.getSelectedItem();
			
			stmt = theConnection.createStatement();			// Creating the statement
			logged_user = Login.logintext.getText();		// Getting the user entered user-name
			// Selecting the specific client with the chosen user-name
			get_Client_name = stmt.executeQuery("SELECT * FROM myLoginBooks WHERE userName = '" + logged_user + "'");
			if(get_Client_name.next())
			{
				// Getting the client's full name details
				full_name = get_Client_name.getString("fullNameDetails");
			}
			/*********************** Doing the catering and total price calculations *****************************************/
			// Getting the client input details
			String guests_num = Client_Do_Booking.num_guests_text.getText();
			String start_date = Client_Do_Booking.start_date_text.getText();
			String end_date = Client_Do_Booking.end_date_text.getText();
			
			int num_of_guests = Integer.parseInt(guests_num);		// Parsing the String as an integer
			
			// Converting the start-date and end-date to the SQL format date
			java.text.DateFormat sql_format = new java.text.SimpleDateFormat("yyyy-MM-dd");
			java.util.Date SQLStartDate = sql_format.parse(start_date);
			java.sql.Date str_dt = new java.sql.Date(SQLStartDate.getTime());
			java.util.Date SQLEndDate = sql_format.parse(end_date);
			java.sql.Date end_dt = new java.sql.Date(SQLEndDate.getTime());
			
			// Calculating the number of nights
			long date_diff = end_dt.getTime() - str_dt.getTime();
			long dates_num = TimeUnit.DAYS.convert(date_diff, TimeUnit.MILLISECONDS);
			String diff_num = String.valueOf(dates_num);
			num_of_nights = Integer.parseInt(diff_num);
			/*****************************************************************************************************************/
			// Selecting the specific apartment
			Aprt_details = stmt.executeQuery("SELECT * FROM myApartmentsBook WHERE apartmentName = '" + the_aprt_nm + "'");
			if(Aprt_details.next())
			{
				// Calculating the apartment cose
				String the_price = Aprt_details.getString("price");
				aPrice = Integer.parseInt(the_price);
				aprt_cost = aPrice * num_of_nights;
			}
			
			// If the catering has been chosen
			if(box_opt.equals("Yes"))
			{
				// Calculating the catering price
				the_cat_price = 15;
				total_cat_price = (the_cat_price * num_of_guests) * num_of_nights;
			}
			else
			{
				total_cat_price = 0;
			}
			
			// Selecting only the apartment with the user entered details
			display_Chosen_Aprt = stmt.executeQuery("SELECT * FROM myClientsBook WHERE theApartmentName = '" + the_aprt_nm + "' and clientNameDetails = '"
												+ full_name + "' and guestsNumber = '" + gst_num + "'");
			while(display_Chosen_Aprt.next())
			{
				// Appending the table with the table records
				buff.append(String.format("<tr><td align='left'>%s</td>", display_Chosen_Aprt.getString(1)));
				buff.append(String.format("<td align='left'>%s</td>", display_Chosen_Aprt.getString(2)));
				buff.append(String.format("<td align='left'>%s</td>", display_Chosen_Aprt.getString(3)));
				buff.append(String.format("<td align='left'>%s</td>", display_Chosen_Aprt.getString(4)));
				buff.append(String.format("<td align='left'>%s</td>", display_Chosen_Aprt.getString(5)));
				buff.append(String.format("<td align='left'>%s</td>", display_Chosen_Aprt.getString(6)));
			}
			
			total_aprt_price = aprt_cost + total_cat_price;	// Calculating the total apartment price
			
			buff.append(String.format("<td align='left'>%s</td>", total_cat_price));
			buff.append(String.format("<td align='left'>%s</td>"+"</tr>", total_aprt_price));
		}
		catch (Exception e)		// Catches the exceptions within the code being executed
		{
			e.printStackTrace();
		} //-------------------------------------------- end of try-catch statement ------------------------------
		//***************************************************************************************************************************************//
		
		buff.append(String.format("<tr><td align='left'>%s</td>"+"</tr>", ""));
		buff.append(String.format("<tr><td align='left'>%s</td>"+"</tr>", ""));
		buff.append(String.format("<tr><td align='left'>%s</td>"+"</tr>", ""));
		buff.append(String.format("<tr><td align='left'>%s</td>"+"</tr>", ""));
		buff.append(String.format("<tr><td align='left'>%s</td>"+"</tr>", ""));
		buff.append("</table></html>");
		
		my_label = new JLabel(buff.toString());		// new label for client
			my_label.setForeground(Color.black);
			my_label.setFont(aFont);
			client_panel.add(my_label);
		
		confirm_bt = new JButton("Confirm");		// Button for booking the apartment
			confirm_bt.setFont(aFont);
			my_label.add(confirm_bt);
			confirm_bt.setBounds(20, 140, 100, 20);
			confirm_bt.addActionListener(this);
			
		exit_bt = new JButton("Exit");				// Button for exiting
			exit_bt.setFont(aFont);
			exit_bt.setBounds(150, 140, 80, 20);
			my_label.add(exit_bt);
			exit_bt.addActionListener(this);
		
		repaint();		// A component repaints itself
	} //================================================== End of method init() ==========================================================
	
	/******************************************* The method for performing the actions ***************************************************/
	// The ActionEvent parameter is an Event object that represents an event (a button click)
	public void actionPerformed(ActionEvent event)
	{
		// if the confirm button has been clicked, then calls the booking class
		if(event.getSource() == confirm_bt)
		{
			// Class the multi-threading class
			Client_Booking_Done aClientBooking = new Client_Booking_Done();
			aClientBooking.Client_Booking_Connection();
			
			// Displays the message if the booking has been successful or not
			if(Client_Booking_Done.counter > 0)
			{
				// Displays the message if the booking has been successful with the booking ID number
				JOptionPane.showMessageDialog(this,"Thank you for Booking this Apartment! Your Booking ID number is " + Client_Booking_Done.theBookID);
			}
			else
			{
				// Displays the message if the apartment has been already booked
				JOptionPane.showMessageDialog(this,"SORRY - The chosen Apartment already booked!");
			}
			System.exit(0);		// Exits from the application
		}
		// If an exit button has been clicked, then goes to the login page
		if (event.getSource() == exit_bt)
		{
			getContentPane().removeAll();
			Login page = new Login();
	        page.init();
	        getContentPane().add(page);
	        getContentPane().validate();
		}
	}
	
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
			databaseConnection = filename + database;
			theConnection = DriverManager.getConnection(databaseConnection,userName,thePassword);
			
			logged_user = Login.logintext.getText();	// Getting the user-name details
			
			stmt = theConnection.createStatement();		// creating the statement
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
}