//******************************************************************************//
//Author: 1625928																//
//Description: The Client View Bookings class asks the user to enter his/her	//
//				booking ID number in order to display that record from the		//
//				MySQL Database Table 'myBookingsBook'.							//
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

//The following class extends the Java Applet that works like the main window where components are added to create an AppletGUI
//The class also implements ActionListener which is notified whenever you click on the button or menu item
public class Client_View_Bookings extends JApplet implements ActionListener
{
	/************************************************ Declaring the variables ************************************************************/
	public boolean isWorking= true;				// Assigning Boolean object representing the value true according to the string
	public String company= "Limited";			// Assigning String object displaying the given value
	private String Status;						// Declaring the current status
	
	// an execution environment that is responsible for adding the technical concerns to the COMPONENTS
	private Container my_container = null;
	// instance filed for the grid layout
	private GridLayout gridLayout1 = null;
	// instance field for border
	private Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
	
	private static Font aFont;					// Font instance field
	public static JTextField bk_ID_text;		// Instance field for text field
	private static JPanel my_panel;				// The Java Panel
	
	// Instance fields for labels
	private static JLabel bk_ID_num_lb;
	private static JLabel my_label;
	
	// Instance fields for buttons
	private static JButton confirm_bt;
	private static JButton back_btn;
	
	// Instance fields for the type String
	private static String full_name;			// Full name details taken from the MySQL database
	private static String filename;				// The JDBC URL for filename of the server
	private static String database;				// The JDBC URL for database of the connection for the server
	private static String userName;				// The JDBC URL for user-name for the connection
	private static String thePassword;			// The JDBC URL for the password for the connection
	private static String databaseConn;			// Full JDBC URL connection to the database variable
	private static String logged_user;			// User input details
	private String booking_full_name;			// Full name details taken from the myBookingsBook table
	private String login_full_name;				// Full name details taken from the myLoginsBook table
	
	// Declaring the connection, statement and result-set
	private static Connection aConnection;
	private static Statement stmt = null;
	private static ResultSet rs;				// Result-set of getting full name details
	private static ResultSet get_Booking_ID;	// Result-set for getting the record with the given ID
	private static ResultSet get_user_name;		// Result-set for getting the record with the given user-name
	//=====================================================================================================================================
	
	/***************************************************** The init() method *************************************************************/
	public void init()
	{
		Status = "Initializing!";				// Displaying the current status
		setBackground(Color.lightGray);			// Setting the background colour to the light-gray so the border of text area will be visible
		
		// set the grid layout with 1 row and 1 column
		gridLayout1 = new GridLayout(1, 1);
		my_container = new Container();
		my_container.setLayout(gridLayout1);
		
		add(my_container);
		// new JPanel with flow layout
		my_panel = new JPanel();
		
		my_container.add(my_panel);
		my_panel.setBorder(loweredetched);
		my_panel.setLayout(new BoxLayout(my_panel, BoxLayout.Y_AXIS));	// Setting the layout of the main panel as a box layout
		my_panel.setSize(1500,500);
		aFont = new Font("Arial", Font.ITALIC, 12);
		
		JPanel helperPanel = new JPanel();
		SpringLayout my_layout = new SpringLayout();					// Setting the layout of the additional panel as a Spring layout
		helperPanel.setLayout(my_layout);
		
		//Format the label Client using DOM
		StringBuilder buff = new StringBuilder();
		buff.append("<html><table>");
		buff.append(String.format("<tr><td align='left'>%s</td>"+"</tr>", "CLIENT - " + client_name()));
		buff.append(String.format("<tr><td align='left'>%s</td>"+"</tr>", "------------------------"));
		buff.append("</table></html>");
		
		aFont = new Font("Arial", Font.ITALIC, 12);						// font details
		
		my_label = new JLabel(buff.toString());							// label for displaying the table
			my_label.setForeground(Color.black);
			my_label.setFont(aFont);
			my_panel.add(my_label);
		
		bk_ID_num_lb = new JLabel("Enter the Booking ID Number: ");		// Label for the Text field
			bk_ID_num_lb.setFont(aFont);
			helperPanel.add(bk_ID_num_lb);
			
		bk_ID_text = new JTextField(20);								// The text field
			bk_ID_text.setFont(aFont);
			helperPanel.add(bk_ID_text);
		
		confirm_bt = new JButton("Confirm");							// The confirm button
			confirm_bt.setFont(aFont);
			helperPanel.add(confirm_bt);
			confirm_bt.addActionListener(this);
			
		back_btn = new JButton("Back");									// The back button
			back_btn.setFont(aFont);
			helperPanel.add(back_btn);
			back_btn.addActionListener(this);
			
		// Setting the location of all the components
		my_panel.add(helperPanel);
		
		my_layout.putConstraint(SpringLayout.WEST, bk_ID_num_lb, 5, SpringLayout.WEST, helperPanel);
		my_layout.putConstraint(SpringLayout.NORTH, bk_ID_num_lb, 10, SpringLayout.NORTH, helperPanel);
		my_layout.putConstraint(SpringLayout.NORTH, bk_ID_text, 10, SpringLayout.NORTH, helperPanel);
		my_layout.putConstraint(SpringLayout.WEST, bk_ID_text, 5, SpringLayout.EAST, bk_ID_num_lb);
		
		my_layout.putConstraint(SpringLayout.WEST, confirm_bt, 5, SpringLayout.WEST, helperPanel);
		my_layout.putConstraint(SpringLayout.NORTH, confirm_bt, 30, SpringLayout.NORTH, bk_ID_text);
		
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
			//---------------------------------------- The connection to the JDBC driver -----------------------------
			// The try statement allows you to define a block of code to be tested for errors while it is being executed
			// It will test if there is a connection with the driver
			try
			{
				// Connecting through the external driver, which has been installed to this project
				Class.forName("com.mysql.cj.jdbc.Driver");
			}
			// The catch statement allows you to define a block of code to be executed, if an error occurs in the try block
			// Catches any errors in case if there is no connection with the driver
			catch(ClassNotFoundException x)
			{
				// Prints out the message that it could not find the driver
				System.out.println("Cannot find driver class. Check CLASSPATH");
				return;
			} //-------------------------------------------- end of try-catch statement ------------------------------
			
			//--------------------------------- Connecting to the server and populating the table --------------------
			// This time, assuming it connected to the driver, it will try to connect to the ecommsvr5
			try
			{
				// Declaring the variables to the path, user-name and password
				filename = "jdbc:mysql://ecommsvr5:3306/";
				database = "kanatbeckj_oop";
				userName = "kanatbeckj_aoop";
				thePassword = "1625928";
				databaseConn = filename + database;
				
				// Assigning a connection to the server with the variables stated above
				aConnection = DriverManager.getConnection(databaseConn,userName,thePassword);
				
				stmt = aConnection.createStatement();	// Creating the statement
				// Selecting the specific record from the given booking ID number
				get_Booking_ID = stmt.executeQuery("SELECT * FROM myBookingsBook WHERE bookingIDNumber = '" + bk_ID_text.getText() + "'");
				if(get_Booking_ID.next())
				{
					// Getting the client full name details
					booking_full_name = get_Booking_ID.getString("clientNameDetails");
					
					// Selecting the specific record from the given user-name
					get_user_name = stmt.executeQuery("SELECT * FROM myLoginBooks WHERE userName = '" + Login.logintext.getText() + "'");
					if(get_user_name.next())
					{
						// Getting the client full name details
						login_full_name = get_user_name.getString("fullNameDetails");
						
						// Comparing both name details, if they are matching then goes to the Manage page
						if(booking_full_name.equals(login_full_name))
						{
							getContentPane().removeAll();
							Client_Manage_Booking page = new Client_Manage_Booking();
					        page.init();
					        getContentPane().add(page);
					        getContentPane().validate();
						}
						// Otherwise, displays the correspondent message
						else
						{
							JOptionPane.showMessageDialog(this,"Wrong Booking ID Number!");
						}
					}
				}
				else
				{
					JOptionPane.showMessageDialog(this,"The booking with such ID number DOES NOT EXIST!");
				}
			}			
			catch (Exception e)		// Catches the exceptions within the code being executed
			{
				e.printStackTrace();
			} //-------------------------------------------- end of try-catch statement ------------------------------
		}
		
		// If the back button has been clicked then goes to the previous page
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
}