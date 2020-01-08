//******************************************************************************//
//Author: 1625928																//
//Description: The Manager Hotel Management class displays the options for the	//
//				manager to choose. The manager needs to write the number of the	//
//				chosen option and press enter. Additionally, it will display	//
//				manager's full name details.									//
//******************************************************************************//

//importing all the packages in order to run the code
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

//The following class extends the Java Applet that works like the main window where components are added to create an AppletGUI
//The class also implements ActionListener which is notified whenever you click on the button or menu item
public class Manager_Hotel_Management extends JApplet implements ActionListener
{
	/************************************************ Declaring the variables ************************************************************/
	public boolean isWorking= true;			// Assigning Boolean object representing the value true according to the string
	public String company= "Limited";		// Assigning String object displaying the given value
	private String Status;					// Declaring the current status
	
	// an execution environment that is responsible for adding the technical concerns to the COMPONENTS
	private Container my_container = null;
	private GridLayout gridLayout1 = null;			// instance filed for the grid layout
	private Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
	private static JPanel mng_panel;				// The Java Panel
	
	// Instance fields for labels
	private static JLabel my_mng_label;
	private static JLabel my_mng_opt_label;
	
	// Text fields instance fields
	private static JTextField my_mng_opt_field;

	// Font instance fields
	private static Font aFont;
	private static Font aTextFieldFont;
	
	private static String Mng_Option;		// The manager chosen option
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
		
		// JPanel for the manager
		mng_panel = new JPanel(new FlowLayout());
		my_container.add(mng_panel);
		add(my_container);
		mng_panel.setBorder(loweredetched);
		
		//Format the label Client using DOM
		StringBuilder buff2 = new StringBuilder();
		buff2.append("<html><table>");
		buff2.append(String.format("<tr><td align='left'>%s</td>"+"</tr>", "MANAGER - " + Login.logintext.getText()));
		buff2.append(String.format("<tr><td align='left'>%s</td>"+"</tr>", ""));
		buff2.append(String.format("<tr><td align='left'>%s</td>"+"</tr>", "1. View all the Tables"));
		buff2.append(String.format("<tr><td align='left'>%s</td>"+"</tr>", "2. View all the Bookings"));
		buff2.append(String.format("<tr><td align='left'>%s</td>"+"</tr>", "3. Manage the Booking"));
		buff2.append(String.format("<tr><td align='left'>%s</td>"+"</tr>", "4. EXIT"));
		buff2.append("</table></html>");
		
		// Font details
		aFont = new Font("Arial", Font.ITALIC, 24);
		aTextFieldFont = new Font("Arial", Font.BOLD, 18);
		
		my_mng_label = new JLabel(buff2.toString());		// label for the table
		my_mng_label.setForeground(Color.black);
		my_mng_label.setFont(aFont);
		mng_panel.add(my_mng_label);
		
		my_mng_opt_label = new JLabel("Option: ");			// Label for the option text field
		my_mng_opt_label.setBounds(20, 50, 80, 20);
		my_mng_opt_label.setFont(aTextFieldFont);
		my_mng_label.add(my_mng_opt_label);
		
		my_mng_opt_field = new JTextField();				// The options Text field
		my_mng_opt_field.setBounds(100, 50, 80, 20);
		my_mng_opt_field.setFont(aTextFieldFont);
		my_mng_label.add(my_mng_opt_field);
		my_mng_opt_field.addActionListener(this);
		
		repaint();											// A component repaints itself
	} //================================================== End of method init() ==========================================================
	
	/******************************************* The method for performing the actions ***************************************************/
	// The ActionEvent parameter is an Event object that represents an event (a button click)
	public void actionPerformed(ActionEvent event)
	{
		// Getting the value of the chosen option
		Mng_Option = my_mng_opt_field.getText();
		
		// If the option is equals to '1', then goes to the tables page
		if(Mng_Option.equals("1"))
		{
			getContentPane().removeAll();
	        Manager_View_Tables page = new Manager_View_Tables();
	        page.init();
	        getContentPane().add(page);
	        getContentPane().validate();
		}
		// If the option is equals to '2', then goes to the bookings page
		if(Mng_Option.equals("2"))
		{
			getContentPane().removeAll();
	        Manager_View_Bookings page = new Manager_View_Bookings();
	        page.init();
	        getContentPane().add(page);
	        getContentPane().validate();
		}
		// If the option is equals to '3', then goes to the manage bookings page
		if(Mng_Option.equals("3"))
		{
			getContentPane().removeAll();
	        Manager_Manage_Booking page = new Manager_Manage_Booking();
	        page.init();
	        getContentPane().add(page);
	        getContentPane().validate();
		}
		// If the option is equals to '4', then goes back to the login page
		if(Mng_Option.equals("4"))
		{
			getContentPane().removeAll();
	        Login page = new Login();
	        page.init();
	        getContentPane().add(page);
	        getContentPane().validate();
		}
	} //================================================== End of method actionPerformed ==========================================================
} // end of the class