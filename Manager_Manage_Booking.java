//******************************************************************************//
//Author: 1625928																//
//Description: Manager Manage Booking class displays the user only the specific	//
//				booking with the user entered Booking ID number. Afterwards,	//
//				user will be able to modify the record by entering the column	//
//				name and its value.												//
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
import java.util.concurrent.TimeUnit;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

//The following class extends the Java Applet that works like the main window where components are added to create an AppletGUI
//The class also implements ActionListener which is notified whenever you click on the button or menu item
public class Manager_Manage_Booking extends JApplet implements ActionListener
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
	
	private static JPanel thePanel;		// The Java Panel
	private static JLabel my_label;			// The label for the table
	private static Font aFont;				// The font of the components
	
	// Instance fields for buttons
	private static JButton manage_bt;
	private static JButton remove_bt;
	private static JButton back_bt;
	
	// Declaring the connection, statement and result-set
	private static Connection aConnection;
	private static Statement stmt = null;
	private static ResultSet Getting_Bkg_Rcrds;		// Getting the records from the Bookings book
	private static ResultSet Getting_Price;			// Getting the price record from the Apartments book
	private static ResultSet Modifying_Bk;			// Modifying the records from the Bookings book
	private static ResultSet Removing_Bk;			// Removing the record from the Bookings book
	
	// Instance fields for the type String
	private static String BID;				// The Booking ID Number
	private static String filename;			// The JDBC URL for filename of the server
	private static String database;			// The JDBC URL for database of the connection for the server
	private static String userName;			// The JDBC URL for user-name for the connection
	private static String thePassword;		// The JDBC URL for the password for the connection
	private static String databaseConn;		// Full JDBC URL connection to the database variable
	private static String aCatering;		// The original catering option taken from the Bookings Book table
	private static String Bk_Aprt_Name;		// The original apartment name taken from the Bookings Book table
	private static String Bk_Start_Date;	// The original start date taken from the Bookings Book table
	private static String Bk_End_Date;		// The original end date taken from the Bookings Book table
	private static String Bk_Guests_Num;	// The original guests number taken from the Bookings Book table
	private static String mdfy_record;		// The user input column name
	private static String mdfy_value;		// The user input column value
	private static String price;			// The original price taken from the Apartments Book table
	private static String modify_catering;	// The record to modify the total catering price
	private static String modify_price;		// The record to modify the total apartment price
	private static String modify_record;	// The record to modify
	private static String remove_record;	// The record to delete
	
	// Instantiating the integers
	private static int the_Catering = 0;
	private static int num_of_guests = 0;
	private static int num_of_nights = 0;
	private static int the_price = 0;
	private static int aprt_cost = 0;
	private static int total_cat_price = 0;
	private static int total_aprt_price = 0;
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
		
		// A new panel with a vertical box layout
		add(my_container);
		thePanel = new JPanel();
		my_container.add(thePanel);
		thePanel.setBorder(loweredetched);
		thePanel.setLayout(new BoxLayout(thePanel, BoxLayout.Y_AXIS));
		
		aFont = new Font("Arial", Font.ITALIC, 12);									// The font of all components
		BID = JOptionPane.showInputDialog(this,"Enter the Booking ID Number: ");	// Prompting the user to enter Booking ID number
		
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
			
			stmt = aConnection.createStatement();		// Creating the statement
			
			// Selecting the booking with the specific booking ID number
			Getting_Bkg_Rcrds = stmt.executeQuery("SELECT * FROM myBookingsBook WHERE bookingIDNumber = '" + BID + "'");
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
				
				// Assigning the variables with the table records
				Bk_Aprt_Name = Getting_Bkg_Rcrds.getString(3);
				Bk_Guests_Num = Getting_Bkg_Rcrds.getString(4);
				Bk_Start_Date = Getting_Bkg_Rcrds.getString(5);
				Bk_End_Date = Getting_Bkg_Rcrds.getString(6);
			}
		}
		catch (Exception e)		// Catches the exceptions within the code being executed
		{
			e.printStackTrace();
		} //-------------------------------------------- end of try-catch statement ------------------------------
		//***************************************************************************************************************************************//
		buff.append("</table></html>");
		
		my_label = new JLabel(buff.toString());			// Label for table
			my_label.setForeground(Color.black);
			my_label.setFont(aFont);
			thePanel.add(my_label);
			
		manage_bt = new JButton("Modify the booking");	// Button for modifying the record
			manage_bt.setFont(aFont);
			thePanel.add(manage_bt);
			manage_bt.addActionListener(this);
			
		remove_bt = new JButton("Remove the booking");	// Button for removing the record
			remove_bt.setFont(aFont);
			thePanel.add(remove_bt);
			remove_bt.addActionListener(this);
			
		back_bt = new JButton("Back");					// Button for going to the previous page
			back_bt.setFont(aFont);
			thePanel.add(back_bt);
			back_bt.addActionListener(this);
		
		repaint();		// A component repaints itself
	} //================================================== End of method init() ==========================================================
	
	/******************************************* The method for performing the actions ***************************************************/
	// The ActionEvent parameter is an Event object that represents an event (a button click)
	public void actionPerformed(ActionEvent event)
	{
		/**************************************** If the manage button has been clicked *****************************************/
		if(event.getSource() == manage_bt)
		{
			try
			{
				stmt = aConnection.createStatement();												 // Creating the statement
				mdfy_record = JOptionPane.showInputDialog(this,"Enter the column name to modify: "); // Prompting the user to enter the column name and its value
				
				// Changing the values of the user input, in order to match to the MySQL Database Table
				if(mdfy_record.equals("Full Name Details")) { mdfy_record = "clientNameDetails"; }
				else if(mdfy_record.equals("Apartment Name")) { mdfy_record = "apartmentName"; }
				else if(mdfy_record.equals("Number of Guests")) { mdfy_record = "guestsNumber"; }
				else if(mdfy_record.equals("Booking Start-Date")) { mdfy_record = "bookingStartDate"; }
				else if(mdfy_record.equals("Booking End-Date")) { mdfy_record = "bookingEndDate"; }
				else if(mdfy_record.equals("Number of Bedrooms")) { mdfy_record = "BedroomsNumber"; }
				else if(mdfy_record.equals("Separate Living Room")) { mdfy_record = "separateLivingRoom"; }
				else if(mdfy_record.equals("Number of Bathrooms")) { mdfy_record = "BathroomsNumber"; }
				else if(mdfy_record.equals("Total Catering Price")) { mdfy_record = "totalCateringPrice"; }
				else if(mdfy_record.equals("Total Price")) { JOptionPane.showMessageDialog(this,"You CANNOT modify this column!"); }
				else { JOptionPane.showMessageDialog(this,"Invalid column name!"); }
				
				/*************************************** Getting the price from the Apartments Book Table ****************************************************/
				Getting_Price = stmt.executeQuery("SELECT * FROM myApartmentsBook WHERE apartmentName = '" + Bk_Aprt_Name + "'");
				if(Getting_Price.next())
				{
					price = Getting_Price.getString("price");
				}
				/*=============================================================================================================================================*/
				
				// Selecting the specific booking
				Modifying_Bk = stmt.executeQuery("SELECT * FROM myBookingsBook WHERE bookingIDNumber = '" + BID + "'");
				if(Modifying_Bk.next())
				{
					aCatering = Modifying_Bk.getString("totalCateringPrice");		// Assigning the catering price
					
					// If the column to change is the Total Catering Price, then does the following
					if(mdfy_record.equals("totalCateringPrice"))
					{
						mdfy_value = JOptionPane.showInputDialog(this,"Choose the Catering (Yes/No): ");
						
						/**************** Re-calculating the Catering Price and the Total Price for the Client ***********************/
						num_of_guests = Integer.parseInt(Bk_Guests_Num);
						
						java.text.DateFormat sql_format = new java.text.SimpleDateFormat("yyyy-MM-dd");
						java.util.Date SQLStartDate = sql_format.parse(Bk_Start_Date);
						java.sql.Date str_dt = new java.sql.Date(SQLStartDate.getTime());
						java.util.Date SQLEndDate = sql_format.parse(Bk_End_Date);
						java.sql.Date end_dt = new java.sql.Date(SQLEndDate.getTime());
						
						long date_diff = end_dt.getTime() - str_dt.getTime();
						long dates_num = TimeUnit.DAYS.convert(date_diff, TimeUnit.MILLISECONDS);
						String diff_num = String.valueOf(dates_num);
						num_of_nights = Integer.parseInt(diff_num);
						the_price = Integer.parseInt(price);
						/*===========================================================================================================*/
						
						/*********************************** Checking the Catering Option ********************************************/
						if(mdfy_value.equals("Yes"))
						{
							// Checking if the value is not the same as the original value
							if(mdfy_value.equals(aCatering))
							{
								JOptionPane.showMessageDialog(this,"You Did Not Change Anything!");
							}
							else
							{
								// Calculating the new catering and apartment prices
								the_Catering = 15;
								aprt_cost = the_price * num_of_nights;
								total_cat_price = (the_Catering * num_of_guests) * num_of_nights;
								total_aprt_price = aprt_cost + total_cat_price;
							}
						}
						else
						{
							// Checking if the value is not the same as the original value
							if(mdfy_value.equals(aCatering))
							{
								JOptionPane.showMessageDialog(this,"You Did Not Change Anything!");
							}
							else
							{
								// Calculating the new catering and apartment prices
								the_Catering = 0;
								aprt_cost = the_price * num_of_nights;
								total_cat_price = (the_Catering * num_of_guests) * num_of_nights;
								total_aprt_price = aprt_cost + total_cat_price;
							}
						} /*==========================================================================================================*/
						
						// Modifying the record with the new catering price and apartment price values
						modify_catering = "UPDATE myBookingsBook SET " + mdfy_record + " = '" + total_cat_price + "' WHERE bookingIDNumber = '" + BID + "'";
						stmt.executeUpdate(modify_catering);
						
						modify_price = "UPDATE myBookingsBook SET totalPrice = '" + total_aprt_price + "' WHERE bookingIDNumber = '" + BID + "'";
						stmt.executeUpdate(modify_price);
						JOptionPane.showMessageDialog(this,"The record has been modified!");
						System.exit(0);
					}
					else	// Every other columns, are modified here
					{
						mdfy_value = JOptionPane.showInputDialog(this,"Enter the value: ");
						
						modify_record = "UPDATE myBookingsBook SET " + mdfy_record + " = " + mdfy_value + " WHERE bookingIDNumber = '" + BID + "'";
						stmt.executeUpdate(modify_record);
						JOptionPane.showMessageDialog(this,"The record has been modified!");
						System.exit(0);
					}
				}
				else	// If the record does not exist
				{
					// Displays the following message
					JOptionPane.showMessageDialog(this,"The record with such name DOES NOT EXIST!");
				}
			}
			catch (Exception e)		// Catches the exceptions within the code being executed
			{
				e.printStackTrace();
			} //-------------------------------------------- end of try-catch statement -------------------------------------
		} /*=========================================================================================================================*/
		
		/**************************************** If the remove button has been clicked *****************************************/
		if(event.getSource() == remove_bt)
		{
			try
			{
				stmt = aConnection.createStatement();		// Creating the statement
				
				// Selecting the record with specific booking ID number
				Removing_Bk = stmt.executeQuery("SELECT * FROM myBookingsBook WHERE bookingIDNumber = '" + BID + "'");
				if(Removing_Bk.next())
				{
					// Removing an existing record
					remove_record = "DELETE FROM myBookingsBook WHERE bookingIDNumber = '" + BID + "'";
					// Updating the statement, which removes the record
					stmt.executeUpdate(remove_record);
					
					JOptionPane.showMessageDialog(this,"The record has been removed!");
					System.exit(0);
				}
				else
				{
					JOptionPane.showMessageDialog(this,"The apartment with such name DOES NOT EXIST!");
				}
			}
			catch (Exception e)		// Catches the exceptions within the code being executed
			{
				e.printStackTrace();
			}
		} /*=========================================================================================================================*/
		
		/**************************************** If the back button has been clicked *****************************************/
		if(event.getSource() == back_bt)
		{
			// Goes back to the previous page
			getContentPane().removeAll();
	        Manager_View_Bookings page = new Manager_View_Bookings();
	        page.init();
	        getContentPane().add(page);
	        getContentPane().validate();
		} /*=========================================================================================================================*/
	} //================================================== End of method actionPerformed ==========================================================
}// end class