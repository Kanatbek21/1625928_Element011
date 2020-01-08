//******************************************************************************//
//Author: 1625928																//
//Description: Manager View Apartments class displays all the records from the	//
//				'myApartmentsBook' MySQL Database Table. Next, the manager will //
//				be able to insert a new record or modify and delete the existing//
//				record from the table.											//
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
public class Manager_View_Apartments extends JApplet implements ActionListener
{
	/************************************************ Declaring the variables ************************************************************/
	public boolean isWorking= true;				// Assigning Boolean object representing the value true according to the string
	public String company= "Limited";			// Assigning String object displaying the given value
	private String Status;						// Declaring the current status
	
	// an execution environment that is responsible for adding the technical concerns to the COMPONENTS
	private Container my_container = null;
	private GridLayout gridLayout1 = null;		// instance filed for the grid layout
	private Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
	
	private static JPanel thePanel;			// The Java Panel
	private static JLabel my_label;			// The label for the table
	private static Font aFont;				// The Font of the components
	private static int z = 1;				// An integer for showing the number of rows
	
	// Instance fields for the buttons
	private static JButton insert_btn;
	private static JButton remove_btn;
	private static JButton modify_btn;
	private static JButton back_btn;
	
	// Declaring the connection, statement and result-set
	private static Connection aConnection;
	private static Statement stmt = null;
	private static ResultSet Getting_Aprt_Rcrds;		// Getting the records from the Apartments book
	private static ResultSet Inserting_Aprt;			// Inserting the record to the Apartments book
	private static ResultSet Removing_Aprt;				// Removing the record from the Apartments book
	private static ResultSet Modifying_Aprt;			// Modifying the record from the Apartments book
	
	// Instance fields for the type String
	private static String filename;			// The JDBC URL for filename of the server
	private static String database;			// The JDBC URL for database of the connection for the server
	private static String userName;			// The JDBC URL for user-name for the connection
	private static String thePassword;		// The JDBC URL for the password for the connection
	private static String databaseConn;		// Full JDBC URL connection to the database variable
	private static String record_insert;	// The record to be inserted
	private static String record_remove;	// The record to be removed
	private static String record_modify;	// The record to be modified
	
	// The user input details
	private static String the_aprt_nm;
	private static String the_price;
	private static String the_avlb_start_dt;
	private static String the_avlb_end_dt;
	private static String max_num_guests;
	private static String bedroom_num;
	private static String sprt_living_rm;
	private static String bathroom_num;
	private static String delete;
	private static String mdfy_aprt_nm;
	private static String mdfy_record;
	private static String mdfy_value;
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
		
		// new JPanel with vertical box layout
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
			
			stmt = aConnection.createStatement();		// Creating the statement
			
			// Selecting all the records from the Apartments book table
			Getting_Aprt_Rcrds = stmt.executeQuery("SELECT * FROM myApartmentsBook");
			
			while(Getting_Aprt_Rcrds.next())
			{
				buff.append(String.format("<tr><td align='left'>%s</td>", z));
				buff.append(String.format("<td align='left'>%s</td>", Getting_Aprt_Rcrds.getString(1)));
				buff.append(String.format("<td align='left'>%s</td>", Getting_Aprt_Rcrds.getString(2)));
				buff.append(String.format("<td align='left'>%s</td>", Getting_Aprt_Rcrds.getString(3)));
				buff.append(String.format("<td align='left'>%s</td>", Getting_Aprt_Rcrds.getString(4)));
				buff.append(String.format("<td align='left'>%s</td>", Getting_Aprt_Rcrds.getString(5)));
				buff.append(String.format("<td align='left'>%s</td>", Getting_Aprt_Rcrds.getString(6)));
				buff.append(String.format("<td align='left'>%s</td>", Getting_Aprt_Rcrds.getString(7)));
				buff.append(String.format("<td align='left'>%s</td>"+"</tr>", Getting_Aprt_Rcrds.getString(8)));
				z++;
			}
		}
		catch (Exception e)		// Catches the exceptions within the code being executed
		{
			e.printStackTrace();
		} //-------------------------------------------- end of try-catch statement ------------------------------
		//***************************************************************************************************************************************//
		buff.append(String.format("<tr><td align='left'>%s</td>"+"</tr>", ""));
		buff.append("</table></html>");
		
		aFont = new Font("Arial", Font.ITALIC, 12);			// Font details for the components
		
		my_label = new JLabel(buff.toString());				// Label for table
			my_label.setForeground(Color.black);
			my_label.setFont(aFont);
			thePanel.add(my_label);
		
		insert_btn = new JButton("Insert a new record");	// Button for inserting the record
			insert_btn.setBounds(1, 200, 150, 30);
			insert_btn.setFont(aFont);
			thePanel.add(insert_btn);
			insert_btn.addActionListener(this);
			
		remove_btn = new JButton("Remove the record");		// Button for removing the record
			remove_btn.setBounds(1, 250, 150, 30);
			remove_btn.setFont(aFont);
			thePanel.add(remove_btn);
			remove_btn.addActionListener(this);
			
		modify_btn = new JButton("Modify the record");		// Button for modifying the record
			modify_btn.setBounds(1, 300, 150, 30);
			modify_btn.setFont(aFont);
			thePanel.add(modify_btn);
			modify_btn.addActionListener(this);
			
		back_btn = new JButton("Back");						// Button for going to the previous page
			back_btn.setSize(140,30);
			back_btn.setFont(aFont);
			thePanel.add(back_btn);
			back_btn.addActionListener(this);
		
		repaint();											// A component repaints itself
	} //================================================== End of method init() ==========================================================
	
	/******************************************* The method for performing the actions ***************************************************/
	// The ActionEvent parameter is an Event object that represents an event (a button click)
	public void actionPerformed(ActionEvent event)
	{
		/********************************************** If the insert button has been clicked ****************************************/
		if(event.getSource() == insert_btn)
		{
			try
			{
				stmt = aConnection.createStatement();		// Creating the statement
				
				// Prompting the user to enter the apartment details
				the_aprt_nm = JOptionPane.showInputDialog(this,"Enter the Apartmnets name: ");
				the_price = JOptionPane.showInputDialog(this,"Enter the Price: ");
				the_avlb_start_dt = JOptionPane.showInputDialog(this,"Enter the Available Start-Date: ");
				the_avlb_end_dt = JOptionPane.showInputDialog(this,"Enter the Available End-Date: ");
				max_num_guests = JOptionPane.showInputDialog(this,"Enter the Maximum Number of Guests: ");
				bedroom_num = JOptionPane.showInputDialog(this,"Enter the Number of Bedrooms: ");
				sprt_living_rm = JOptionPane.showInputDialog(this,"Enter if the Living room is Separate or Not: ");
				bathroom_num = JOptionPane.showInputDialog(this,"Enter the Number of Bathrooms: ");
				
				// Selecting the apartment with the specific name, in order to check if it already exists
				Inserting_Aprt = stmt.executeQuery("SELECT * FROM myApartmentsBook WHERE apartmentName = '" + the_aprt_nm + "'");
				if(Inserting_Aprt.next() == false)
				{
					record_insert = "INSERT INTO myApartmentsBook (apartmentName,price,availableStartDate,availableEndDate,maxGuestsNumber,BedroomsNumber,separateLivingRoom,BathroomsNumber) "
							+ "VALUES ('" + the_aprt_nm + "','" +  the_price + "','" + the_avlb_start_dt + "','" + the_avlb_end_dt + "','" + max_num_guests + "','"
							+ bedroom_num + "','" + sprt_living_rm + "','" + bathroom_num + "')";
					stmt.executeUpdate(record_insert);										// Updating the statement, which inputs the record created above
					JOptionPane.showMessageDialog(this,"The record has been inserted!");	// Displaying the message
					System.exit(0);															// Exiting from the application
				}
				else
				{
					// The error message
					JOptionPane.showMessageDialog(this,"The Apartment with such name already exists!");
				}
			} catch (Exception e)		// Catches the exceptions within the code being executed
			{
				e.printStackTrace();
			}
		
		} /*=========================================================================================================================*/
		
		/********************************************** If the remove button has been clicked ****************************************/
		if(event.getSource() == remove_btn)
		{
			try
			{
				stmt = aConnection.createStatement();												// Creating the statement
				delete = JOptionPane.showInputDialog(this,"Enter the Apartment name to delete: ");	// Asking the user to enter Apartment name
				
				// Selecting the specific apartment, in order to check if it exists
				Removing_Aprt = stmt.executeQuery("SELECT * FROM myApartmentsBook WHERE apartmentName = '" + delete + "'");
				if(Removing_Aprt.next())
				{
					record_remove = "DELETE FROM myApartmentsBook WHERE apartmentName = '" + delete + "'";
					// Updating the statement, which removes the record
					stmt.executeUpdate(record_remove);
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
		} /*=========================================================================================================================*/
		
		/********************************************** If the modify button has been clicked ****************************************/
		if(event.getSource() == modify_btn)
		{
			try
			{
				stmt = aConnection.createStatement();		// Creating the statement
				
				// Prompting the manager to enter the apartment details to modify
				mdfy_aprt_nm = JOptionPane.showInputDialog(this,"Enter the Apartment name to modify: ");
				mdfy_record = JOptionPane.showInputDialog(this,"Enter the column name: ");
				mdfy_value = JOptionPane.showInputDialog(this,"Enter the value: ");
				
				// Selecting the apartment with specific name to check if it exists
				Modifying_Aprt = stmt.executeQuery("SELECT * FROM myApartmentsBook WHERE apartmentName = '" + the_aprt_nm + "'");
				if(Modifying_Aprt.next())
				{
					record_modify = "UPDATE myApartmentsBook SET " + mdfy_record + " = " + mdfy_value + " WHERE apartmentName = '" + the_aprt_nm + "'";
					stmt.executeUpdate(record_modify);
					JOptionPane.showMessageDialog(this,"The record has been modified!");
					System.exit(0);
				} else {
					JOptionPane.showMessageDialog(this,"The record with such name DOES NOT EXIST!");
				}
			}
			catch (Exception e)		// Catches the exceptions within the code being executed
			{
				e.printStackTrace();
			}
		} /*=========================================================================================================================*/
		
		/********************************************** If the back button has been clicked ******************************************/
		if(event.getSource() == back_btn)
		{
			// Goes back to the previous page
			getContentPane().removeAll();
	        Manager_View_Tables page = new Manager_View_Tables();
	        page.init();
	        getContentPane().add(page);
	        getContentPane().validate();
		} /*=========================================================================================================================*/
	} //================================================== End of method =======================================================================
}// end of class