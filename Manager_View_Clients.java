//******************************************************************************//
//Author: 1625928																//
//Description: Manager View Clients class displays all the records from the		//
//				'myClientsBook' MySQL Database Table. The manager will be able	//
//				to modify or remove the specific booking from that table.		//
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
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

//The following class extends the Java Applet that works like the main window where components are added to create an AppletGUI
//The class also implements ActionListener which is notified whenever you click on the button or menu item
public class Manager_View_Clients extends JApplet implements ActionListener
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
	
	// Instance fields for the buttons
	private static JButton remove_btn;
	private static JButton modify_btn;
	private static JButton back_btn;
	
	// Declaring the connection, statement and result-set
	private static Connection aConnection;
	private static Statement stmt = null;
	private static ResultSet Getting_Clients;	// Getting the records from the Clients book table
	private static ResultSet Removing_Record;	// Removing the record from the Clients book table
	private static ResultSet Modifying_Record;	// Modifying the record from the Clients book table
	
	// Instance fields for the type String
	private static String filename;				// The JDBC URL for filename of the server
	private static String database;				// The JDBC URL for database of the connection for the server
	private static String userName;				// The JDBC URL for user-name for the connection
	private static String thePassword;			// The JDBC URL for the password for the connection
	private static String databaseConn;			// Full JDBC URL connection to the database variable
	private static String User_ClientID;		// The user input to remove the record
	private static String Record_Delete;		// The record to be removed
	private static String Record_Modify;		// The record to be modified
	private static String clnt_id;				// The user input Client ID
	private static String mdfy_record;			// The user input record name
	private static String mdfy_value;			// The user input record value
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
		thePanel = new JPanel();
		my_container.add(thePanel);
		add(my_container);
		thePanel.setBorder(loweredetched);
		thePanel.setLayout(new BoxLayout(thePanel, BoxLayout.Y_AXIS));
		
		//Format the label Client using DOM
		StringBuilder buff = new StringBuilder();
		buff.append("<html><table>");
		buff.append(String.format("<tr><td align='left'>%s</td>"+"</tr>", "MANAGER - " + Login.logintext.getText()));
		buff.append(String.format("<tr><td align='left'>%s</td>"+"</tr>", "-----------"));
		buff.append(String.format("<td align='left'>%s</td>", "Client ID"));
		buff.append(String.format("<td align='left'>%s</td>", "Name Details"));
		buff.append(String.format("<td align='left'>%s</td>", "Apartment Name"));
		buff.append(String.format("<td align='left'>%s</td>", "Number of guests"));
		buff.append(String.format("<td align='left'>%s</td>", "Booking Start-Date"));
		buff.append(String.format("<td align='left'>%s</td>"+"</tr>", "Booking End-Date"));
			
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
			
			stmt = aConnection.createStatement();		// Creating the statement
			
			// Selecting all the records from the Clients Book table
			Getting_Clients = stmt.executeQuery("SELECT * FROM myClientsBook");
			while(Getting_Clients.next())
			{
				// Appending the table with the MySQL Database Table values
				buff.append(String.format("<td align='left'>%s</td>", Getting_Clients.getString(1)));
				buff.append(String.format("<td align='left'>%s</td>", Getting_Clients.getString(2)));
				buff.append(String.format("<td align='left'>%s</td>", Getting_Clients.getString(3)));
				buff.append(String.format("<td align='left'>%s</td>", Getting_Clients.getString(4)));
				buff.append(String.format("<td align='left'>%s</td>", Getting_Clients.getString(5)));
				buff.append(String.format("<td align='left'>%s</td>"+"</tr>", Getting_Clients.getString(6)));
			}
		}
		catch (Exception e)		// Catches the exceptions within the code being executed
		{
			e.printStackTrace();
		} //-------------------------------------------- end of try-catch statement ------------------------------
		//***************************************************************************************************************************************//
		buff.append("</table></html>");
		
		aFont = new Font("Arial", Font.ITALIC, 12);		// font details for the labels
		
		my_label = new JLabel(buff.toString());			// Label to store the table
			my_label.setForeground(Color.black);
			my_label.setFont(aFont);
			thePanel.add(my_label);
		
		modify_btn = new JButton("Modify the record");	// Button for modifying the record
			modify_btn.setBounds(1, 300, 150, 30);
			modify_btn.setFont(aFont);
			thePanel.add(modify_btn);
			modify_btn.addActionListener(this);
			
		remove_btn = new JButton("Remove the record");	// Button for removing the record
			remove_btn.setBounds(1, 250, 150, 30);
			remove_btn.setFont(aFont);
			thePanel.add(remove_btn);
			remove_btn.addActionListener(this);
			
		back_btn = new JButton("Back");					// Button for going to the previous page
			back_btn.setSize(140,30);
			back_btn.setFont(aFont);
			thePanel.add(back_btn);
			back_btn.addActionListener(this);
		
		repaint();										// A component repaints itself
	} //================================================== End of method init() ==========================================================
	
	/******************************************* The method for performing the actions ***************************************************/
	// The ActionEvent parameter is an Event object that represents an event (a button click)
	public void actionPerformed(ActionEvent event)
	{
		// If the Remove button has been clicked
		if(event.getSource() == remove_btn)
		{
			try
			{
				stmt = aConnection.createStatement();												 // Creating the statement				
				User_ClientID = JOptionPane.showInputDialog(this,"Enter the Client ID to delete: "); // Asking the user the Client ID number
				
				// Selecting the record with the specific client ID number
				Removing_Record = stmt.executeQuery("SELECT * FROM myClientsBook WHERE clientID = '" + User_ClientID + "'");
				if(Removing_Record.next())
				{
					Record_Delete = "DELETE FROM myClientsBook WHERE clientID = '" + User_ClientID + "'";
					stmt.executeUpdate(Record_Delete);			// Updating the statement, which removes the record
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
			} //-------------------------------------------- end of try-catch statement ------------------------------
		}
		
		// If the modify button has been clicked
		if(event.getSource() == modify_btn)
		{
			try
			{
				stmt = aConnection.createStatement();	// Creating the statement
				
				// Assigning the variables with the user input values
				clnt_id = JOptionPane.showInputDialog(this,"Enter the Client ID to modify: ");
				mdfy_record = JOptionPane.showInputDialog(this,"Enter the column name: ");
				mdfy_value = JOptionPane.showInputDialog(this,"Enter the value: ");
				
				// Changing the user input values in order to modify the record
				if(mdfy_record.equals("Name Details")) { mdfy_record = "clientNameDetails"; }
				if(mdfy_record.equals("Apartment Name")) { mdfy_record = "apartmentName"; }
				if(mdfy_record.equals("Number of Guests")) { mdfy_record = "guestsNumber"; }
				if(mdfy_record.equals("Booking Start-Date")) { mdfy_record = "bookingStartDate"; }
				if(mdfy_record.equals("Booking End-Date")) { mdfy_record = "bookingEndDate"; }
				if(mdfy_record.equals("Client ID")) { JOptionPane.showMessageDialog(this,"You CANNOT modify this column!"); }
				
				Modifying_Record = stmt.executeQuery("SELECT * FROM myClientsBook WHERE clientID = '" + clnt_id + "'");
				if(Modifying_Record.next())
				{
					// Modifying the Database table with the new record values
					Record_Modify = "UPDATE myClientsBook SET " + mdfy_record + " = " + mdfy_value + " WHERE clientID = '" + clnt_id + "'";
					stmt.executeUpdate(Record_Modify);
					JOptionPane.showMessageDialog(this,"The record has been modified!");
					System.exit(0);
				}
				else
				{
					JOptionPane.showMessageDialog(this,"The record with such ID - DOES NOT EXIST!");
				}
			}
			catch (Exception e)		// Catches the exceptions within the code being executed
			{
				e.printStackTrace();
			} //-------------------------------------------- end of try-catch statement ------------------------------
		}
		
		// If the back button has been clicked
		if(event.getSource() == back_btn)
		{
			// Goes back to the previous page
			getContentPane().removeAll();
	        Manager_View_Tables page = new Manager_View_Tables();
	        page.init();
	        getContentPane().add(page);
	        getContentPane().validate();
		}
	} //================================================== End of method =======================================================================
}// end class