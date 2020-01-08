//******************************************************************************//
//Author: 1625928																//
//Description: Client_Manage_Booking class displays the booking details from	//
//				the 'myBookingsBook' MySQL Database Table. Afterwards, it asks	//
//				user to enter the booking details in order to modify, such as:	//
//				guests number, booking start date, booking end date, the choice	//
//				of catering. Finally, when the user will press the correspondent//
//				button, it will call the method to modify the current booking 	//
//				from the 'Client_Manage_Done' class.							//
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
public class Client_Manage_Booking extends JApplet implements ActionListener
{
	/************************************************ Declaring the variables ************************************************************/
	public boolean isWorking= true;			// Assigning Boolean object representing the value true according to the string
	public String company= "Limited";		// Assigning String object displaying the given value
	private String Status;					// Declaring the current status
	
	// an execution environment that is responsible for adding the technical concerns to the COMPONENTS
	private Container my_container = null;
	private GridLayout gridLayout1 = null;	// instance filed for the grid layout
	private Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
	
	// Instance fields for labels
	private static JLabel my_label;
	private static JLabel num_guests_lb;
	private static JLabel start_date_lb;
	private static JLabel end_date_lb;
	private static JLabel box_lb;
	
	// Instance fields for text fields
	public static JTextField num_guests_text;
	public static JTextField start_date_text;
	public static JTextField end_date_text;
	
	// Instance fields for buttons
	private static JButton manage_bt;
	private static JButton back_btn;
	
	private static Font aFont;					// Font instance field
	private static JPanel client_panel;			// The main Java Panel
	private static JPanel helperPanel;			// The additional Java Panel to sort out the layout
	
	// instance field for drop down menu
	public static JComboBox box = new JComboBox (new String[] {"Yes", "No"});
	
	// Instance fields for the type String
	private static String full_name;			// Full name details taken from the MySQL database
	private static String filename;				// The JDBC URL for filename of the server
	private static String database;				// The JDBC URL for database of the connection for the server
	private static String userName;				// The JDBC URL for user-name for the connection
	private static String thePassword;			// The JDBC URL for the password for the connection
	private static String databaseConn;			// Full JDBC URL connection to the database variable
	private static String logged_user;			// User input details
	private static String avlb_str_dt;			// The start date value taken from MyApartmentsBook table
	private static String avlb_end_dt;			// The end date value taken from MyApartmentsBook table
	private static String max_guest_num;		// The maximum guests number value taken from MyApartmentsBook table
	private static String the_price;			// The apartment price value taken from MyApartmentsBook table
	public static String mdfy_guests;			// User entered value of guests number
	public static String mdfy_strdt;			// User entered value of start date
	public static String mdfy_enddt;			// User entered value of end date
	public static String box_opt;				// User entered value of the catering option
	private static String theStart_Date;		// The start-date taken from myBookingsBook to assign to the text field
	private static String theEnd_Date;			// The end-date taken from myBookingsBook to assign to the text field
	private static String aptm_nm;				// The original apartment name taken from the 'myBookingsBook' table
	
	// Declaring the integers
	private int ttl_prc;
	public static int total_cat_price;
	public static int total_aprt_price;
	public static int total_cat_price2;
	public static int total_aprt_price2;
	private static int num_of_guests;
	private static int num_of_nights;
	private static int aprt_cost;
	
	// Declaring the connection, statement and result-set
	private static Connection aConnection;
	private static Statement stmt = null;
	private static ResultSet rs;					// Result-set of getting full name details
	private static ResultSet Booking_Details;		// Result-set of getting the current booking details
	private static ResultSet Booking_ID;			// Result-set for getting the booking details from the specific booking
	private static ResultSet Apartment_Details;		// Result-set for getting the apartment details
	//=====================================================================================================================================
	
	/***************************************************** The init() method *************************************************************/
	public void init()
	{
		Status = "Initializing!";				// Setting the current status
		setBackground(Color.lightGray);			// Setting the background colour to the light-gray so the border of text area will be visible
		
		// set the grid layout with 1 row and 1 column
		gridLayout1 = new GridLayout(1, 1);
		my_container = new Container();
		my_container.setLayout(gridLayout1);
		
		add(my_container);
		client_panel = new JPanel();											// Creating the main Panel
		
		my_container.add(client_panel);
		client_panel.setBorder(loweredetched);
		client_panel.setLayout(new BoxLayout(client_panel, BoxLayout.Y_AXIS));	// Setting the layout of main panel as a vertical box layout
		client_panel.setSize(1500,1500);										// Setting the size of the panel
		aFont = new Font("Arial", Font.ITALIC, 12);
		
		helperPanel = new JPanel();												// Creating the additional Panel
		SpringLayout my_layout = new SpringLayout();
		helperPanel.setLayout(my_layout);										// Setting the layout of additional panel as a Spring layout
		
		//Format the label Client using DOM
		StringBuilder buff = new StringBuilder();
		buff.append("<html><table>");
		buff.append(String.format("<tr><td align='left'>%s</td>"+"</tr>", "CLIENT - " + client_name()));
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
		buff.append(String.format("<td align='left'>%s</td>"+"</tr>", "Total Catering Price"));
			
		/************************************ Appending the table with the MySQL Table values **************************************************/
		//---------------------------------------- The connection to the JDBC driver -----------------------------
		try
		{
			// Connecting through the external driver, which has been installed to this project
			Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch(ClassNotFoundException x)
		{
			System.out.println("Cannot find driver class. Check CLASSPATH");
		} //-------------------------------------------- end of try-catch statement ------------------------------
		
		//--------------------------------- Connecting to the server and populating the table --------------------
		try
		{
			// Declaring the variables to the path, user-name and password
			filename = "jdbc:mysql://ecommsvr5:3306/";
			database = "kanatbeckj_oop";
			userName = "kanatbeckj_aoop";
			thePassword = "1625928";
			databaseConn = filename + database;
			aConnection = DriverManager.getConnection(databaseConn,userName,thePassword);
			
			stmt = aConnection.createStatement();		// Creating the statement
			
			// Selecting the current booking with the user entered booking ID number
			Booking_Details = stmt.executeQuery("SELECT * FROM myBookingsBook WHERE bookingIDNumber = '" + Client_View_Bookings.bk_ID_text.getText() + "'");
			while(Booking_Details.next())
			{
				// Appending the table with the table values
				buff.append(String.format("<td align='left'>%s</td>", Booking_Details.getString(1)));
				buff.append(String.format("<td align='left'>%s</td>", Booking_Details.getString(2)));
				buff.append(String.format("<td align='left'>%s</td>", Booking_Details.getString(3)));
				buff.append(String.format("<td align='left'>%s</td>", Booking_Details.getString(4)));
				buff.append(String.format("<td align='left'>%s</td>", Booking_Details.getString(5)));
				buff.append(String.format("<td align='left'>%s</td>", Booking_Details.getString(6)));
				buff.append(String.format("<td align='left'>%s</td>", Booking_Details.getString(7)));
				buff.append(String.format("<td align='left'>%s</td>", Booking_Details.getString(8)));
				buff.append(String.format("<td align='left'>%s</td>", Booking_Details.getString(9)));
				buff.append(String.format("<td align='left'>%s</td>"+"</tr>", Booking_Details.getString(10)));
				
				// Assigning the variables with the record values
				theStart_Date = Booking_Details.getString(5);
				theEnd_Date = Booking_Details.getString(6);
			}
		}
		catch (Exception e)		// Catches the exceptions within the code being executed
		{
			e.printStackTrace();
		} //-------------------------------------------- end of try-catch statement ------------------------------
		/*******************************************************************************************************************************/
		buff.append("</table></html>");
		
		my_label = new JLabel(buff.toString());				// Label for the table
			my_label.setForeground(Color.black);
			my_label.setFont(aFont);
			client_panel.add(my_label);
			
		num_guests_lb = new JLabel("Number of guests: ");	// Label and the text-field for the Number of guests
			num_guests_lb.setFont(aFont);
			helperPanel.add(num_guests_lb);
		num_guests_text = new JTextField(20);
			num_guests_text.setFont(aFont);
			helperPanel.add(num_guests_text);
		
		start_date_lb = new JLabel("Start date: ");			// Label and the text-field for the Start-date
			start_date_lb.setFont(aFont);
			helperPanel.add(start_date_lb);
		start_date_text = new JTextField(20);
			start_date_text.setText(theStart_Date);
			start_date_text.setFont(aFont);
			helperPanel.add(start_date_text);
		
		end_date_lb = new JLabel("End date: ");				// Label and the text-field for the End-date
			end_date_lb.setFont(aFont);
			helperPanel.add(end_date_lb);
		end_date_text = new JTextField(20);
			end_date_text.setText(theEnd_Date);
			end_date_text.setFont(aFont);
			helperPanel.add(end_date_text);
		
		box_lb = new JLabel("Catering: ");					// Label and the box for the catering option
			box_lb.setFont(aFont);
			helperPanel.add(box_lb);
		helperPanel.add(box);
			box.setFont(aFont);
		
		manage_bt = new JButton("Modify the Booking");		// Manage button that allows the user to modify the booking
			manage_bt.setFont(aFont);
			helperPanel.add(manage_bt);
			manage_bt.addActionListener(this);
			
		back_btn = new JButton("Back");						// Back button that goes back to the previous page
			back_btn.setFont(aFont);
			helperPanel.add(back_btn);
			back_btn.addActionListener(this);
			
		// Setting the location of all the components
		client_panel.add(helperPanel);
		
		my_layout.putConstraint(SpringLayout.WEST, num_guests_lb, 5, SpringLayout.WEST, helperPanel);
		my_layout.putConstraint(SpringLayout.NORTH, num_guests_lb, 30, SpringLayout.NORTH, helperPanel);
		my_layout.putConstraint(SpringLayout.NORTH, num_guests_text, 30, SpringLayout.NORTH, helperPanel);
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
		
		my_layout.putConstraint(SpringLayout.WEST, manage_bt, 5, SpringLayout.WEST, helperPanel);
		my_layout.putConstraint(SpringLayout.NORTH, manage_bt, 30, SpringLayout.NORTH, box);
		
		my_layout.putConstraint(SpringLayout.WEST, back_btn, 5, SpringLayout.WEST, helperPanel);
		my_layout.putConstraint(SpringLayout.NORTH, back_btn, 30, SpringLayout.NORTH, manage_bt);
		
		repaint();		// A component repaints itself
	} //================================================== End of method init() ==========================================================
	
	/******************************************* The method for performing the actions ***************************************************/
	// The ActionEvent parameter is an Event object that represents an event (a button click)
	public void actionPerformed(ActionEvent event)
	{
		// If the manage button has been clicked, then does the following
		if(event.getSource() == manage_bt)
		{
			//---------------------------------- Connecting to the server and modifying the record ---------------------------
			try
			{
				stmt = aConnection.createStatement();		// Creating and assigning the statement
				
				// Assigning the variables taken from the user input values
				mdfy_guests = num_guests_text.getText();
				mdfy_strdt = start_date_text.getText();
				mdfy_enddt = end_date_text.getText();
				box_opt = (String) box.getSelectedItem();
				
				// Selecting the specific booking with the client entered booking ID number
				Booking_ID = stmt.executeQuery("SELECT * FROM myBookingsBook WHERE bookingIDNumber = '" + Client_View_Bookings.bk_ID_text.getText() + "'");
				if(Booking_ID.next())
				{
					aptm_nm = Booking_ID.getString("apartmentName");		// The original apartment name
					
					/*********************************** Getting the specific apartment details to compare with the user input values **************************************/
					Apartment_Details = stmt.executeQuery("SELECT * FROM myApartmentsBook WHERE apartmentName = '" + aptm_nm + "'");
					if(Apartment_Details.next())
					{ 
						avlb_str_dt = Apartment_Details.getString("availableStartDate");
						avlb_end_dt = Apartment_Details.getString("availableEndDate");
						max_guest_num = Apartment_Details.getString("maxGuestsNumber");
						the_price = Apartment_Details.getString("price");
					}
					/*=====================================================================================================================================================*/
					
					/************************************** Re-calculating the Catering Price and the Total Price for the Client *******************************************/
					num_of_guests = Integer.parseInt(mdfy_guests);
					
					java.text.DateFormat sql_format = new java.text.SimpleDateFormat("yyyy-MM-dd");
					java.util.Date SQLStartDate = sql_format.parse(mdfy_strdt);
					java.sql.Date str_dt = new java.sql.Date(SQLStartDate.getTime());
					java.util.Date SQLEndDate = sql_format.parse(mdfy_enddt);
					java.sql.Date end_dt = new java.sql.Date(SQLEndDate.getTime());
					
					long date_diff = end_dt.getTime() - str_dt.getTime();
					long dates_num = TimeUnit.DAYS.convert(date_diff, TimeUnit.MILLISECONDS);
					String diff_num = String.valueOf(dates_num);
					
					num_of_nights = Integer.parseInt(diff_num);
					ttl_prc = Integer.parseInt(the_price);
					aprt_cost = ttl_prc * num_of_nights;
					
					total_cat_price = (15 * num_of_guests) * num_of_nights;
					total_aprt_price = aprt_cost + total_cat_price;
					
					total_cat_price2 = 0;
					total_aprt_price2 = aprt_cost + total_cat_price2;
					/*=====================================================================================================================================================*/
					
					/**************************** Comparing the user values with the original values in order to find the right apartment **********************************/
					if(!mdfy_guests.equals(""))
					{
						if(Integer.parseInt(mdfy_guests)<=Integer.parseInt(max_guest_num))
						{
							if(!mdfy_strdt.equals(""))
							{
								if(mdfy_strdt.equals(avlb_str_dt))
								{
									if(!mdfy_enddt.equals(""))
									{
										if(mdfy_enddt.equals(avlb_end_dt))
										{
											if(!box_opt.equals(""))
											{
												if(box_opt.equals("Yes"))
												{
													// Calling the method to allow 100 users to simultaneously modify the apartment
													Client_Manage_Done.Client_Manage_Connection();
													// Displaying the correspondent message and exiting the application
													JOptionPane.showMessageDialog(this,"Booking has been modified");
													System.exit(0);
												}
												else
												{
													// Calling the method to allow 100 users to simultaneously modify the apartment
													Client_Manage_Done.Client_Manage_Connection();
													// Displaying the correspondent message and exiting the application
													JOptionPane.showMessageDialog(this,"Booking has been modified");
													System.exit(0);
												}
											}
											else
											{
												JOptionPane.showMessageDialog(this,"Choose the Catering");
											}
										}
										else
										{
											JOptionPane.showMessageDialog(this,"End-Date does not match to the Available End-Date!");
										}
									}
									else
									{
										JOptionPane.showMessageDialog(this,"Enter the End-Date");
									}
								}
								else
								{
									JOptionPane.showMessageDialog(this,"Start-Date does not match to the Available Start-Date!");
								}
							}
							else
							{
								JOptionPane.showMessageDialog(this,"Enter the Start-Date");
							}
						}
						else
						{
							JOptionPane.showMessageDialog(this,"The maximum guests number is " + max_guest_num);
						}
					}
					else
					{
						JOptionPane.showMessageDialog(this,"Enter the Guests Number");
					}
					/*=====================================================================================================================================================*/
				}
				else
				{
					JOptionPane.showMessageDialog(this,"Booking does not exist!");
				}
			}
			catch (Exception e)		// Catches the exceptions within the code being executed
			{
				e.printStackTrace();
			} //-------------------------------------------- end of try-catch statement -------------------------------------
		}
		
		// If the back button has been clicked, then goes back to the previous page
		if(event.getSource() == back_btn)
		{
			getContentPane().removeAll();
			Client_View_Bookings page = new Client_View_Bookings();
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