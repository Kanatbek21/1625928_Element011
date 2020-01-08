//**********************************************************************************//
//Author: 1625928																	//
//Description: The Registration class enables the user to register and store his or	//
//				her data, such as: full name details, user-name and password inside	//
//				'myLoginBooks' MySQL Database Table									//
//**********************************************************************************//

//importing all the packages in order to run the code
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

//The following class extends the Java Applet that works like the main window where components are added to create an AppletGUI
//The class also implements ActionListener which is notified whenever you click on the button or menu item
public class Registration extends JApplet implements ActionListener
{
	/************************************************ Declaring the variables ************************************************************/
	public boolean isWorking= true;				// Assigning Boolean object representing the value true according to the string
	public String company= "Limited";			// Assigning String object displaying the given value
	private String Status;						// Declaring the current status
	
	// Instance fields for label
	private static JLabel fullnamelb;
	private static JLabel usernamelb;
	private static JLabel user_pwd_lb;
	
	// Instance fields for text fields
	public static JTextField fullnametext;
	private static JTextField usernametext;
	private static JTextField user_pwd;
	
	private static JButton register;			// button instance field
	
	// Instance fields for the type String
	private static String filename;				// The JDBC URL for filename of the server
	private static String database;				// The JDBC URL for database of the connection for the server
	private static String userName;				// The JDBC URL for user-name for the connection
	private static String thePassword;			// The JDBC URL for the password for the connection
	private static String databaseConn;			// Full JDBC URL connection to the database variable
	private static String theFull_Name;			// User entered full name
	private static String theUser_Name;			// User entered user name
	private static String theUser_Password;		// User entered user password
	private static String record;				// Declaring a new variable for the action of entering the record
	
	// an execution environment that is responsible for adding the technical concerns to the COMPONENTS
	private Container my_container = null;
	private static JPanel my_panel;				// The Java Panel
	
	// The GridLayout class is a layout manager that lays out a container's components in a rectangular grid
	private GridLayout gridLayout1 = null;
	private Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
	
	// Declaring the connection, statement and result-set
	private static Connection aConnection;
	private static Statement stmt = null;
	private static ResultSet rs;				// Result-set of getting user-name details
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
		
		// new JPanel with flow layout
		my_panel = new JPanel(new FlowLayout());
		my_container.add(my_panel);
		add(my_container);
		my_panel.setBorder(loweredetched);
		
		fullnamelb = new JLabel("Full Name: ");			// Full name label
			my_panel.add(fullnamelb);
		fullnametext = new JTextField(20);				// Full name text field 
			my_panel.add(fullnametext);
		
		usernamelb = new JLabel("User Name: ");			// User-name label
			my_panel.add(usernamelb);
		usernametext = new JTextField(20);				// User-name text field
			my_panel.add(usernametext);
			
		user_pwd_lb = new JLabel("User Password: ");	// User password label
			my_panel.add(user_pwd_lb);
		user_pwd = new JTextField(20);					// User password text field
			my_panel.add(user_pwd);
			
		register = new JButton("Register");				// Registration button
			my_panel.add(register);
			register.addActionListener(this);
		
		repaint();										// A component repaints itself
	} //================================================== End of method init() ==========================================================
	
	/******************************************* The method for performing the actions ***************************************************/
	// The ActionEvent parameter is an Event object that represents an event (a button click)
	public void actionPerformed(ActionEvent event)
	{
		// if the register button has been clicked, do the following
		if(event.getSource() == register)
		{
			// Checking if the user-name and password are not empty
			if(usernametext.getText().equals("") && user_pwd.getText().equals(""))
			{
				// Displaying the message
				JOptionPane.showMessageDialog(this,"Invalid username or password!");
			}
			else
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
					
					// Assigning the variables taken from the user input
					theFull_Name = fullnametext.getText();
					theUser_Name = usernametext.getText();
					theUser_Password = user_pwd.getText();
					
					// Assigning a connection to the server with the variables stated above
					aConnection = DriverManager.getConnection(databaseConn,userName,thePassword);
					stmt = aConnection.createStatement();			// Creating and assigning the statement
					
					// Selecting the specific record from the myLoginBooks table and assigning it to the result-set
					rs = stmt.executeQuery("SELECT * FROM myLoginBooks WHERE userName = '" + theUser_Name + "'");
					// If the record does not exist
					if(rs.next() == false)
					{
						record = "INSERT INTO myLoginBooks (fullNameDetails, userName, userPassword) "
								+ "VALUES ('" + theFull_Name + "','" + theUser_Name + "','" + theUser_Password + "')";
						// Updating the statement, which inputs the record created above
						stmt.executeUpdate(record);
						
						// Afterwards, it shows the message of successful registration and exits from the application
						JOptionPane.showMessageDialog(this,"Thank you for your registration - " + theFull_Name + "!");
						System.exit(0);
					}
					else	// For every other outcome
					{
						do
						{
							// If the table already has the record, it will print it out with the following message
							JOptionPane.showMessageDialog(this,"The client with such 'USER-NAME' already exists - " + rs.getString("userName"));
						} while (rs.next());
					}  // end of if-else statement
				}
				catch (Exception e)		// Catches the exceptions within the code being executed
				{
					e.printStackTrace();
				} //-------------------------------------------- end of try-catch statement ------------------------------
			}
		}
	} //================================================== End of method actionPerformed ==========================================================
}// end of the class