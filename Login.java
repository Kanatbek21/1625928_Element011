//******************************************************************************//
//Author: 1625928																//
//Description: The main Java Applet page that asks the user to input his/her	//
//				login details, which is stored in the 'myloginsbook' MySQL		//
//				Database Table. Additionally it has the Registration button,	//
//				which allows to register and store the name details, user-name	//
//				and password in the same database.								//
//******************************************************************************//

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

//The following class extends the JApplet that works like the main window where components are added to create an AppletGUI
//The class also implements ActionListener which is notified whenever you click on the button or menu item
public class Login extends JApplet implements ActionListener
{
	/************************************************ Declaring the variables ************************************************************/
	public boolean isWorking= true;			// Assigning Boolean object representing the value true according to the string
	public String company= "Limited";		// Assigning String object displaying the given value
	private String Status;					// Declaring the current status
	
	// Instance fields for label
	private static JLabel loginlb;
	private static JLabel password;
	
	// Instance fields for text fields
	public static JTextField logintext;
	private JTextField pwd;
	
	// Instance fields for buttons
	private static JButton loginbt;
	private static JButton registerbt;
	
	// Instance fields for the type String
	private static String box_opt;			// The user chosen box option
	private static String opt_login;		// The user login details
	private static String opt_pwd;			// The user password
	private static String filename;			// The JDBC URL for filename of the server
	private static String database;			// The JDBC URL for database of the connection for the server
	private static String userName;			// The JDBC URL for user-name for the connection
	private static String thePassword;		// The JDBC URL for the password for the connection
	private static String databaseConn;		// Full JDBC URL connection to the database variable
	
	// instance field for drop down menu
	private static JComboBox box = new JComboBox (new String[] {"Client", "Manager"});
	
	// an execution environment that is responsible for adding the technical concerns to the COMPONENTS
	private static Container my_container = null;
	private static JPanel my_panel;		// The Java Panel
	
	// The GridLayout class is a layout manager that lays out a container's components in a rectangular grid
	private GridLayout gridLayout1 = null;
	private Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
	
	// Declaring the connection, statement and result-set
	private static Connection aConnection;
	private static Statement stmt = null;
	private static ResultSet rs;			// Result-set of getting full name details
	//=====================================================================================================================================
	
	/***************************************************** The init() method *************************************************************/
	public void init()
	{
		Status = "Initializing!";			// Setting the current status
		setBackground(Color.lightGray);		// Setting the background colour to the light-gray so the border of text area will be visible
		
//		showStatus ("The Java Applet is Initializing!");// Showing the information in applet windows status bar of the browser/Appletviewer
//		JOptionPane.showMessageDialog(this,Status);		// Displaying the status in the message Dialog of JOptionPane
		
		gridLayout1 = new GridLayout(1, 1);		// set the grid layout with 1 row and 1 column
		my_container = new Container();			// creating a new container
		my_container.setLayout(gridLayout1);	// setting the containers layout
		
		my_panel = new JPanel(new FlowLayout());		// new JPanel with flow layout
		my_container.add(my_panel);						// adding the layout to the container
		add(my_container);								// adding the container, which displays it inside the window
		my_panel.setBorder(loweredetched);				// setting the borders of the panel
		
		my_panel.add(box);						// adds the drop down menu to the container, which has 'Client' and 'Manager' options
		
		loginlb = new JLabel("Login: ");			// new login label
			my_panel.add(loginlb);					// adds the label to the container
		logintext = new JTextField(20);				// new login text field
			my_panel.add(logintext);				// adds the text field to the container
		
		password = new JLabel("Password: ");		// new password label
			my_panel.add(password);
		pwd = new JTextField(20);					// new password text field
			my_panel.add(pwd);
		
		registerbt = new JButton("Register");		// new button to register
			my_panel.add(registerbt);
			registerbt.addActionListener(this);		// add the action
			
		loginbt = new JButton("Login");				// new button to login
			my_panel.add(loginbt);
			loginbt.addActionListener(this);
			
		resize(1500,800);	// setting the size of an applet
		repaint();			// A component repaints itself
	} //================================================== End of method init() ==========================================================
	
	/******************************************* The method for performing the actions ***************************************************/
	// The ActionEvent parameter is an Event object that represents an event (a button click)
	public void actionPerformed(ActionEvent event)
	{
		// if the register button has been clicked, displays to the Registration window
		if(event.getSource() == registerbt) {
			getContentPane().removeAll();
	        Registration page = new Registration();
	        page.init();
	        getContentPane().add(page);
	        getContentPane().validate();
		}
		// if the login button has been clicked, do the following
		if(event.getSource() == loginbt) {
			box_opt = (String) box.getSelectedItem();	// get the drop down menu value
			opt_login = logintext.getText();			// get the login value
			opt_pwd = pwd.getText();					// get the password value
			
			// if the drop down menu value equals 'Client'
			if(box_opt.equals("Client")) {
				// checks if the input values is empty
				if (opt_login.equals("") || opt_pwd.equals(""))
				{
					// Displays a message, asking the user to enter the details
					JOptionPane.showMessageDialog(this,"Insert the client username and/or password!");
				}
				else
				{
					//---------------------------------------- The connection to the JDBC driver -----------------------------
					try
					{
						Class.forName("com.mysql.cj.jdbc.Driver");
					}
					catch(ClassNotFoundException x)
					{
						System.out.println("Cannot find driver class. Check CLASSPATH");
						return;
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
						
						stmt = aConnection.createStatement();		// creating the statement
						// Selecting all the records from the loginbooks with the user input values
						rs = stmt.executeQuery("SELECT * FROM myLoginBooks WHERE userName = '"+opt_login+"' and userPassword = '"+opt_pwd+"'");
						// If the record exists, displays the Client Management window
						if(rs.next())
						{
							getContentPane().removeAll();
					        Client_Hotel_Management page = new Client_Hotel_Management();
					        page.init();
					        getContentPane().add(page);
					        getContentPane().validate();
						} else
						{
							// Otherwise, displays the following message
							JOptionPane.showMessageDialog(this,"Invalid client username or password!");
						}
					}
					catch (Exception e)		// Catches the exceptions within the code being executed
					{
						e.printStackTrace();
					} //-------------------------------------------- end of try-catch statement ------------------------------
				}
			}
			// If the drop down menu value equals 'Manager' 
			else
			{
				// checks if the input values is empty
				if(opt_login.equals("") || opt_pwd.equals(""))
				{
					JOptionPane.showMessageDialog(this,"Insert the manager username and/or password!");
				}
				else
				{
					// Checks if the input values are equals to the given values, displays the Manager Hotel Management window
					if(opt_login.equals("Kana") && opt_pwd.equals("qwerty123")) {
						getContentPane().removeAll();
				        Manager_Hotel_Management page = new Manager_Hotel_Management();
				        page.init();
				        getContentPane().add(page);
				        getContentPane().validate();
					}
					else {
						JOptionPane.showMessageDialog(this,"Invalid manager username or password!");
					}
				}
			}
		}
	} //================================================== End of method actionPerformed ==========================================================
}// end of class