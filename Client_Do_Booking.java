//******************************************************************************//
//Author: 1625928																//
//Description: The Client Do Booking class displays ONLY currently available	//
//				apartments with all their details. The client will be able to 	//
//				write the chosen apartment details in order to book the chosen	//
//				apartment. Additionally, confirm and back buttons are included	//
//******************************************************************************//

//importing all the packages in order to run the code
import java.applet.Applet;
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
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

//The following class extends the Java Applet that works like the main window where components are added to create an AppletGUI
//The class also implements ActionListener which is notified whenever you click on the button or menu item
public class Client_Do_Booking extends JApplet implements ActionListener
{
	/************************************************ Declaring the variables ************************************************************/
	public boolean isWorking= true;			// Assigning Boolean object representing the value true according to the string
	public String company= "Limited";		// Assigning String object displaying the given value
	private String Status;					// Declaring the current status
	private Applet my_applet;
	
	// an execution environment that is responsible for adding the technical concerns to the COMPONENTS
	private Container my_container = null;
	private GridLayout gridLayout1 = null;	// instance filed for the grid layout
	private Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
	
	private static Font aFont;													// Font instance field
	private static JPanel client_panel;											// The main Java Panel
	private static JPanel helperPanel;											// The Java Panel for adding additional layout
	public static JComboBox box = new JComboBox (new String[] {"Yes", "No"});	// instance field for drop down menu
	
	// Instance fields for labels
	private static JLabel aprt_name_lb;
	private static JLabel num_guests_lb;
	private static JLabel start_date_lb;
	private static JLabel end_date_lb;
	private static JLabel box_lb;
	private static JLabel my_label;
	
	// Instance fields for text fields
	public static JTextField aprt_name_text;
	public static JTextField num_guests_text;
	public static JTextField start_date_text;
	public static JTextField end_date_text;
	
	// Instance fields for buttons
	private static JButton confirm_bt;
	private static JButton back_btn;
	
	// Instance fields for the type String
	private static String client_id;		// The Client ID number gotten from the MySQL Table
	private static String aprt_name;		// Apartment name taken from myApartmnetsBook Table
	private static String avlb_str_dt;		// Apartment available start-date taken from myApartmnetsBook Table
	private static String avlb_end_dt;		// Apartment available end-date taken from myApartmnetsBook Table
	private static String max_guest_num;	// Apartment maximum guests number taken from myApartmnetsBook Table
	private static String full_name;		// Full name details taken from the MySQL database
	private static String filename;			// The JDBC URL for filename of the server
	private static String database;			// The JDBC URL for database of the connection for the server
	private static String userName;			// The JDBC URL for user-name for the connection
	private static String thePassword;		// The JDBC URL for the password for the connection
	private static String databaseConn;		// Full JDBC URL connection to the database variable
	private static String logged_user;		// User input details
	private static String name_details;		// Client name details
	private static String the_aprt;			// Client input apartment name
	private static String guests_num;		// Client input guests number
	private static String start_date;		// Client input Start-date
	private static String end_date;			// Client input End-date
	private static String record;			// The record for updating the table
	
	private static int client_ID;			// An integer for increasing the Client ID number
	private static int z = 1;				// An integer for showing the number of rows
	
	// Declaring the connection, statement and result-set
	private static Connection aConnection;
	private static Statement stmt = null;
	private static ResultSet rs;					// Result-set of getting full name details
	private static ResultSet avlb_aprtms;			// Result-set of only available apartments
	private static ResultSet compare_tables;		// Result-set of two tables that are compared for non-matching records
	private static ResultSet setting_Client_ID;		// Result-set for setting the client ID number
	private static ResultSet getting_Aprt_records;	// Result-set for getting the record from the myApartmentsBook table
	private static ResultSet Aprt_record;			// Result-set for getting the record from the myBookingsBook table
	private static ResultSet checking_Aprt;			// Result-set for checking if the record exists
	//=====================================================================================================================================
	
	/***************************************************** The init() method *************************************************************/
	public void init()
	{
		Status = "Initializing!";			// Displaying the current status
		setBackground(Color.lightGray);		// Setting the background colour to the light-gray so the border of text area will be visible
		
		// set the grid layout with 1 row and 1 column
		gridLayout1 = new GridLayout(1, 1);
		my_container = new Container();
		
		add(my_container);						// adding the container to the window
		client_panel = new JPanel();		// new JPanel with flow layout
		
		my_container.add(client_panel);
		client_panel.setBorder(loweredetched);
		client_panel.setLayout(new BoxLayout(client_panel, BoxLayout.Y_AXIS));	// Setting the layout of the main panel as a box layout
		client_panel.setSize(1500,1000);
		aFont = new Font("Arial", Font.ITALIC, 16);
		
		helperPanel = new JPanel();
		SpringLayout my_layout = new SpringLayout();							// Setting the layout of the panel as a Spring layout
		helperPanel.setLayout(my_layout);
		
		//Format the label Client using DOM
		StringBuilder buff = new StringBuilder();
		buff.append("<html><table>");
		buff.append(String.format("<tr><td></td><td></td><td></td><td></td><td></td><td>%s</td>"+"</tr>", "CLIENT - " + client_name()));
		buff.append(String.format("<tr><td></td><td></td><td></td><td></td><td></td><td bgcolor=\"#e6ffe6\"; align='left'; border: 0.2px black>%s</td>"+"</tr>", 
				"List of all the `Available` Apartments!"));
		buff.append(String.format("<tr bgcolor=\"white\"><td align='left'; border: 0.2px black>%s</td>", "Nº"));
		buff.append(String.format("<td align='left'; border: 0.2px black>%s</td>", "Apartment name"));
		buff.append(String.format("<td align='left'; border: 0.2px black>%s</td>", "Price per Day"));
		buff.append(String.format("<td align='left'; border: 0.2px black>%s</td>", "Available Start Date"));
		buff.append(String.format("<td align='left'; border: 0.2px black>%s</td>", "Available End Date"));
		buff.append(String.format("<td align='left'; border: 0.2px black>%s</td>", "Maximum number of guests"));
		buff.append(String.format("<td align='left'; border: 0.2px black>%s</td>", "Number of Bedrooms"));
		buff.append(String.format("<td align='left'; border: 0.2px black>%s</td>", "Separate Living Room"));
		buff.append(String.format("<td align='left'; border: 0.2px black>%s</td>"+"</tr>", "Number of Bathrooms"));
			
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
			filename = "jdbc:mysql://ecommsvr5:3306/";
			database = "kanatbeckj_oop";
			userName = "kanatbeckj_aoop";
			thePassword = "1625928";
			databaseConn = filename + database;
			aConnection = DriverManager.getConnection(databaseConn,userName,thePassword);
			
			List<String> myList = new ArrayList<String>();		// New array list for storing the apartment names
			
			stmt = aConnection.createStatement();				// creating the statement
			// Selecting the apartments from 'myLoginsBook' and 'myApartmnetBooks' tables that are not matching, so the client will have ONLY available ones
			compare_tables = stmt.executeQuery("SELECT apartmentName FROM (SELECT apartmentName FROM mybookingsbook UNION ALL SELECT apartmentName FROM myapartmentsbook) "
					+ "tbl GROUP BY apartmentName HAVING count(*) = 1 ORDER BY apartmentName");
			while(compare_tables.next())
			{
				myList.add(compare_tables.getString("apartmentName"));		// storing all the apartments in the array list
			}
			
			// For loop with the array list size
			for(int i = 0; i < myList.size(); i++)
			{
				// Selecting all the 
				avlb_aprtms = stmt.executeQuery("SELECT * FROM myApartmentsBook WHERE apartmentName = '" + myList.get(i) + "'");
				
				// Appends the table with the available apartments records
				while(avlb_aprtms.next())
				{
					buff.append(String.format("<tr bgcolor=\"white\"><td align='left'; border: 1px solid black>%s</td>", z));
					buff.append(String.format("<td align='left'; border: 1px solid black>%s</td>", avlb_aprtms.getString(1)));
					buff.append(String.format("<td align='left'; border: 1px solid black>%s</td>", avlb_aprtms.getString(2)));
					buff.append(String.format("<td align='left'; border: 1px solid black>%s</td>", avlb_aprtms.getString(3)));
					buff.append(String.format("<td align='left'; border: 1px solid black>%s</td>", avlb_aprtms.getString(4)));
					buff.append(String.format("<td align='left'; border: 1px solid black>%s</td>", avlb_aprtms.getString(5)));
					buff.append(String.format("<td align='left'; border: 1px solid black>%s</td>", avlb_aprtms.getString(6)));
					buff.append(String.format("<td align='left'; border: 1px solid black>%s</td>", avlb_aprtms.getString(7)));
					buff.append(String.format("<td align='left'; border: 1px solid black>%s</td>"+"</tr>", avlb_aprtms.getString(8)));
					z++;
				}
			}
		}
		catch (Exception e)		// Catches the exceptions within the code being executed
		{
			e.printStackTrace();
		} //-------------------------------------------- end of try-catch statement ------------------------------
		//***************************************************************************************************************************************//
		buff.append("</table></html>");
		
		my_label = new JLabel(buff.toString());				// new label for client
			my_label.setForeground(Color.black);
			my_label.setBackground(Color.white);
			my_label.setFont(aFont);
			client_panel.add(my_label);
		
		aprt_name_lb = new JLabel("Apartment Name: ");		// label for Apartment Name
			aprt_name_lb.setFont(aFont);
			helperPanel.add(aprt_name_lb);
		aprt_name_text = new JTextField(20);				// Text field for the user input
			aprt_name_text.setFont(aFont);
			helperPanel.add(aprt_name_text);
		
		num_guests_lb = new JLabel("Number of guests: ");	// label for Number of guests
			num_guests_lb.setFont(aFont);
			helperPanel.add(num_guests_lb);
		num_guests_text = new JTextField(20);				// Text field for user input
			num_guests_text.setFont(aFont);
			helperPanel.add(num_guests_text);
		
		start_date_lb = new JLabel("Start date: ");			// label for Start date
			start_date_lb.setFont(aFont);
			helperPanel.add(start_date_lb);
		start_date_text = new JTextField(20);				// Text field for user input
			start_date_text.setFont(aFont);
			helperPanel.add(start_date_text);
		
		end_date_lb = new JLabel("End date: ");				// label for End date
			end_date_lb.setFont(aFont);
			helperPanel.add(end_date_lb);
		end_date_text = new JTextField(20);					// Text field for user input
			end_date_text.setFont(aFont);
			helperPanel.add(end_date_text);
		
		box_lb = new JLabel("Catering: ");					// label for Catering option
			box_lb.setFont(aFont);
			helperPanel.add(box_lb);
		helperPanel.add(box);								// Box option for user selection
			box.setFont(aFont);
			
		confirm_bt = new JButton("Book this Apartment");	// Button for booking the apartment
			confirm_bt.setFont(aFont);
			helperPanel.add(confirm_bt);
			confirm_bt.addActionListener(this);
			
		back_btn = new JButton("Back");						// Button for going to the previous page
			back_btn.setFont(aFont);
			helperPanel.add(back_btn);
			back_btn.addActionListener(this);
			
		// Setting the location of each component
		client_panel.add(helperPanel);
		my_layout.putConstraint(SpringLayout.WEST, aprt_name_lb, 5, SpringLayout.WEST, helperPanel);
		my_layout.putConstraint(SpringLayout.NORTH, aprt_name_lb, 10, SpringLayout.NORTH, helperPanel);
		my_layout.putConstraint(SpringLayout.NORTH, aprt_name_text, 10, SpringLayout.NORTH, helperPanel);
		my_layout.putConstraint(SpringLayout.WEST, aprt_name_text, 5, SpringLayout.EAST, aprt_name_lb);
		
		my_layout.putConstraint(SpringLayout.WEST, num_guests_lb, 5, SpringLayout.WEST, helperPanel);
		my_layout.putConstraint(SpringLayout.NORTH, num_guests_lb, 30, SpringLayout.NORTH, aprt_name_lb);
		my_layout.putConstraint(SpringLayout.NORTH, num_guests_text, 30, SpringLayout.NORTH, aprt_name_text);
		my_layout.putConstraint(SpringLayout.WEST, num_guests_text, 5, SpringLayout.EAST, num_guests_lb);
		
		my_layout.putConstraint(SpringLayout.WEST, start_date_lb, 5, SpringLayout.WEST, helperPanel);
		my_layout.putConstraint(SpringLayout.NORTH, start_date_lb, 30, SpringLayout.NORTH, num_guests_lb);
		my_layout.putConstraint(SpringLayout.NORTH, start_date_text, 30, SpringLayout.NORTH, num_guests_text);
		my_layout.putConstraint(SpringLayout.WEST, start_date_text, 5, SpringLayout.EAST, start_date_lb);
		
		my_layout.putConstraint(SpringLayout.WEST, end_date_lb, 5, SpringLayout.WEST, helperPanel);
		my_layout.putConstraint(SpringLayout.NORTH, end_date_lb, 30, SpringLayout.NORTH, start_date_lb);
		my_layout.putConstraint(SpringLayout.NORTH, end_date_text, 30, SpringLayout.NORTH, start_date_text);
		my_layout.putConstraint(SpringLayout.WEST, end_date_text, 5, SpringLayout.EAST, end_date_lb);
		
		my_layout.putConstraint(SpringLayout.WEST, box_lb, 5, SpringLayout.WEST, helperPanel);
		my_layout.putConstraint(SpringLayout.NORTH, box_lb, 30, SpringLayout.NORTH, end_date_lb);
		my_layout.putConstraint(SpringLayout.NORTH, box, 30, SpringLayout.NORTH, end_date_text);
		my_layout.putConstraint(SpringLayout.WEST, box, 5, SpringLayout.EAST, box_lb);
		
		my_layout.putConstraint(SpringLayout.WEST, confirm_bt, 5, SpringLayout.WEST, helperPanel);
		my_layout.putConstraint(SpringLayout.NORTH, confirm_bt, 30, SpringLayout.NORTH, box);
		
		my_layout.putConstraint(SpringLayout.WEST, back_btn, 5, SpringLayout.WEST, helperPanel);
		my_layout.putConstraint(SpringLayout.NORTH, back_btn, 30, SpringLayout.NORTH, confirm_bt);
		
		repaint();		// A component repaints itself
	} //================================================== End of method init() ==========================================================
	
	/******************************************* The method for performing the actions ***************************************************/
	// The ActionEvent parameter is an Event object that represents an event (a button click)
	public void actionPerformed(ActionEvent event)
	{
		// if the confirm button has been clicked, do the following
		if(event.getSource() == confirm_bt)
		{
			//--------------------------------- Connecting to the server and populating the table --------------------
			// This time, assuming it connected to the driver, it will try to connect to the ecommsvr5
			try
			{
				// Selecting all the records from the following table
				setting_Client_ID = stmt.executeQuery("SELECT * FROM myClientsBook");
				// If the records are empty, then assigns the first value as 1
				if(setting_Client_ID.next() == false)
				{
					client_ID = 1;
				}
				// Otherwise, gets the value of last ID number and increments it
				else
				{
					do
					{
						client_id = setting_Client_ID.getString("clientID");
						client_ID = Integer.parseInt(client_id);
					}
					while(setting_Client_ID.next() == true);
					client_ID++;
				}
				
				// Selecting all the records from the following table with the chosen apartment name
				getting_Aprt_records = stmt.executeQuery("SELECT * FROM myApartmentsBook WHERE apartmentName = '" + aprt_name_text.getText() + "'");
				if(getting_Aprt_records.next())
				{
					// Getting and assigning all the records from the table
					aprt_name = getting_Aprt_records.getString("apartmentName");
					avlb_str_dt = getting_Aprt_records.getString("availableStartDate");
					avlb_end_dt = getting_Aprt_records.getString("availableEndDate");
					max_guest_num = getting_Aprt_records.getString("maxGuestsNumber");
					
					// Selecting the specific apartment
					Aprt_record = stmt.executeQuery("SELECT * FROM myBookingsBook WHERE apartmentName = '" + aprt_name + "'");
					// Getting and assigning the user input values
					name_details = client_name();
					the_aprt = aprt_name_text.getText();
					guests_num = num_guests_text.getText();
					start_date = start_date_text.getText();
					end_date = end_date_text.getText();
					
					// Comparing the client entered value with the original maximum guests number value
					if(Integer.parseInt(guests_num) <= Integer.parseInt(max_guest_num))
					{
						// Comparing the start date
						if(start_date.equals(avlb_str_dt))
						{
							// Comparing the end date
							if(end_date.equals(avlb_end_dt))
							{
								// Checking if the records exists
								if(Aprt_record.next() == false)
								{
									// Selecting the records from Clients book table with the specific values
									checking_Aprt = stmt.executeQuery("SELECT * FROM myClientsBook WHERE theApartmentName = '" + the_aprt + "' and clientNameDetails = '"
													+ client_name() + "' and guestsNumber = '" + guests_num + "'");
									// If the record does not exist, then does the following
									if(checking_Aprt.next() == false)
									{
										record = "INSERT INTO myClientsBook (clientID, clientNameDetails, theApartmentName, guestsNumber, bookingStartDate, bookingEndDate) "
												+ "VALUES ('" + client_ID + "','" + full_name + "','" + the_aprt + "','" + guests_num + "','" + start_date + "','" + end_date + "')";
										// Updating the statement, which inputs the record created above
										stmt.executeUpdate(record);
										
										// Afterwards, goes to the following page
										getContentPane().removeAll();
										Client_Booking page = new Client_Booking();
								        page.init();
								        getContentPane().add(page);
								        getContentPane().validate();
									}
									// If the record already exists, then just goes to the next page
									else
									{
										getContentPane().removeAll();
										Client_Booking page = new Client_Booking();
								        page.init();
								        getContentPane().add(page);
								        getContentPane().validate();
									}
								}
								// If the record already exists, then displays the following message
								else
								{
									do	// It does the following
									{
										JOptionPane.showMessageDialog(this,"The chosen Apartment already booked!");
									} while (Aprt_record.next());
								}  // end of if-else statement
							}
							else
							{
								JOptionPane.showMessageDialog(this,"Wrong End-Date!");
							}
						}
						else
						{
							JOptionPane.showMessageDialog(this,"Wrong Start-Date!");
						}
					}
					else
					{
						JOptionPane.showMessageDialog(this,"Maximum number of guests = " + max_guest_num);
					}
				}
				else
				{
					JOptionPane.showMessageDialog(this,"Wrong Apartment Name!");
				}
			}
			catch (Exception e)		// Catches the exceptions within the code being executed
			{
				e.printStackTrace();
			} //-------------------------------------------- end of try-catch statement ------------------------------
		}
		// if the back button has been clicked, then goes back to the previous page
		if(event.getSource() == back_btn)
		{
			getContentPane().removeAll();
	        Client_View_Apartments page = new Client_View_Apartments();
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
}