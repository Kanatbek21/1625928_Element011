//******************************************************************************//
//Author: 1625928																//
//Description: The Client Hotel Management class displays the options for the	//
//				client to choose. The client needs to write the number of the	//
//				chosen option and press enter. Additionally, it will display	//
//				clients full name details, taken from the myLoginBooks.			//
//******************************************************************************//

//importing all the packages in order to run the code
import java.applet.Applet;
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
import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

//The following class extends the Java Applet that works like the main window where components are added to create an AppletGUI
//The class also implements ActionListener which is notified whenever you click on the button or menu item
public class Client_Hotel_Management extends JApplet implements ActionListener
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
	
	// Instance fields for labels
	private  JLabel my_client_label;
	private static JLabel my_client_opt_label;
	
	// Instance fields for text fields
	private static JTextField my_client_opt_field;

	// Font instance fields
	private static Font aFont;
	private static Font aTextFieldFont;
	
	// Instance fields for the type String
	private static String full_name;		// Full name details taken from the MySQL database
	private static String filename;			// The JDBC URL for filename of the server
	private static String database;			// The JDBC URL for database of the connection for the server
	private static String userName;			// The JDBC URL for user-name for the connection
	private static String thePassword;		// The JDBC URL for the password for the connection
	private static String databaseConn;		// Full JDBC URL connection to the database variable
	private static String logged_user;		// User input details
	private static String Client_Option;	// The chosen option of the client
	
	// Declaring the connection, statement and result-set
	private static Connection aConnection;
	private static Statement stmt = null;
	private static ResultSet rs;			// Result-set of getting full name details
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
		
		// new JPanel with flow layout
		client_panel = new JPanel(new FlowLayout());
		my_container.add(client_panel);			// adding the panel to the container
		add(my_container);						// adding the container to display within the window
		client_panel.setBorder(loweredetched);	// setting the border of the panel
		
		//Format the label Client using DOM
		StringBuilder buff = new StringBuilder();
		buff.append("<html><table>");
		buff.append(String.format("<tr><td align='left'>%s</td>"+"</tr>", "WELCOME TO LUXURY BUNGALOW-STYLE APARTMENTS"));
		buff.append(String.format("<tr><td align='left'>%s</td>"+"</tr>", ""));
		buff.append(String.format("<tr><td align='left'>%s</td>"+"</tr>", "CLIENT - " + client_name()));
		buff.append(String.format("<tr><td align='left'>%s</td>"+"</tr>", ""));
		buff.append(String.format("<tr><td align='left'>%s</td>"+"</tr>", "1. Do Your Booking"));
		buff.append(String.format("<tr><td align='left'>%s</td>"+"</tr>", "2. Manage Your Booking"));
		buff.append(String.format("<tr><td align='left'>%s</td>"+"</tr>", "3. EXIT"));
		buff.append("</table></html>");
		
		// font details
		aFont = new Font("Arial", Font.ITALIC, 24);
		aTextFieldFont = new Font("Arial", Font.BOLD, 18);
		
		my_client_label = new JLabel(buff.toString());		// label for displaying the table
			my_client_label.setForeground(Color.black);		// setting the foreground colour
			my_client_label.setFont(aFont);					// setting the font
			client_panel.add(my_client_label);				// adding the label to the panel
		
		my_client_opt_label = new JLabel("Option: ");		// client options
			my_client_opt_label.setBounds(20, 120, 80, 20);
			my_client_opt_label.setFont(aTextFieldFont);
			my_client_label.add(my_client_opt_label);
		
		my_client_opt_field = new JTextField();				// client options text field
			my_client_opt_field.setBounds(100, 120, 80, 20);
			my_client_opt_field.setFont(aTextFieldFont);
			my_client_label.add(my_client_opt_field);
			my_client_opt_field.addActionListener(this);
		
		repaint();											// A component repaints itself
	} //================================================== End of method init() ==========================================================
	
	/******************************************* The method for performing the actions ***************************************************/
	// The ActionEvent parameter is an Event object that represents an event (a button click)
	public void actionPerformed(ActionEvent event)
	{
		Client_Option = my_client_opt_field.getText();	
		
		// if the client option is 1, then DO the booking
		if(Client_Option.equals("1")) {
			getContentPane().removeAll();
	        Client_View_Apartments page = new Client_View_Apartments();
	        page.init();
	        getContentPane().add(page);
	        getContentPane().validate();
		}
		// if the client option is 2, then view the bookings
		if(Client_Option.equals("2")) {
			getContentPane().removeAll();
			Client_View_Bookings page = new Client_View_Bookings();
	        page.init();
	        getContentPane().add(page);
	        getContentPane().validate();
		}
		// if the client option is 3, then exit and go to login page
		if(Client_Option.equals("3")) {
			getContentPane().removeAll();
			Login page = new Login();
	        page.init();
	        getContentPane().add(page);
	        getContentPane().validate();
		}
	} //================================================ End of method actionPerformed ===================================================
	
	/******************************* The method for displaying full name details of the client *******************************************/
	public String client_name()
	{
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
} // end of class