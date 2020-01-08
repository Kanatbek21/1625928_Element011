//******************************************************************************//
//Author: 1625928																//
//Description: Manager View Tables class allows the user to manage three MySQL	//
//				Database Tables by pressing the correspondent button.			//
//******************************************************************************//

//importing all the packages in order to run the code
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

//The following class extends the Java Applet that works like the main window where components are added to create an AppletGUI
//The class also implements ActionListener which is notified whenever you click on the button or menu item
public class Manager_View_Tables extends JApplet implements ActionListener
{
	/************************************************ Declaring the variables ************************************************************/
	public boolean isWorking= true;				// Assigning Boolean object representing the value true according to the string
	public String company= "Limited";			// Assigning String object displaying the given value
	private String Status;						// Declaring the current status
	
	// an execution environment that is responsible for adding the technical concerns to the COMPONENTS
	private Container my_container = null;
	private GridLayout gridLayout1 = null;		// instance filed for the grid layout
	private Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
	
	private static JPanel thePanel;				// The Java Panel
	private static JLabel my_label;				// The label for the table
	private static Font aFont;					// The Font of the labels, buttons, etc
	
	// Instance fields for buttons
	private static JButton apartments_bt;
	private static JButton bookings_bt;
	private static JButton clients_bt;
	private static JButton back_bt;
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
		
		add(my_container);
		thePanel = new JPanel();		// Creating the panel
		
		my_container.add(thePanel);
		thePanel.setBorder(loweredetched);
		thePanel.setLayout(new BoxLayout(thePanel, BoxLayout.Y_AXIS));	// Setting the layout of the panel as a box layout
		aFont = new Font("Arial", Font.ITALIC, 12);								// Setting the font
		
		//Format the label Client using DOMs
		StringBuilder buff = new StringBuilder();
		buff.append("<html><table>");
		buff.append(String.format("<tr><td align='left'>%s</td>"+"</tr>", "MANAGER - " + Login.logintext.getText()));
		buff.append(String.format("<tr><td align='left'>%s</td>"+"</tr>", "-----------"));
		buff.append("</table></html>");
		
		my_label = new JLabel(buff.toString());					// Label for table above
			my_label.setForeground(Color.black);
			my_label.setFont(aFont);
			thePanel.add(my_label);
		
		apartments_bt = new JButton("Apartments Table");		// Button for the Apartments table
			apartments_bt.setSize(140,30);
			apartments_bt.setBounds(1,10,130,20);
			apartments_bt.setFont(aFont);
			thePanel.add(apartments_bt);
			apartments_bt.addActionListener(this);
			
		bookings_bt = new JButton("Bookings Table");			// Button for the Bookings table
			bookings_bt.setSize(140,30);
			bookings_bt.setFont(aFont);
			thePanel.add(bookings_bt);
			bookings_bt.addActionListener(this);
			
		clients_bt = new JButton("Clients Table");				// Button for the Clients table
			clients_bt.setSize(140,30);
			clients_bt.setFont(aFont);
			thePanel.add(clients_bt);
			clients_bt.addActionListener(this);
			
		back_bt = new JButton("Back");							// Button for going to the previous page
			back_bt.setSize(140,30);
			back_bt.setFont(aFont);
			thePanel.add(back_bt);
			back_bt.addActionListener(this);
		
		repaint();												// A component repaints itself
	} //================================================== End of method init() ==========================================================
	
	/******************************************* The method for performing the actions ***************************************************/
	// The ActionEvent parameter is an Event object that represents an event (a button click)
	public void actionPerformed(ActionEvent event)
	{
		// If the apartments button has been clicked
		if(event.getSource() == apartments_bt)
		{
			// Displays the Manager View Apartments class
			getContentPane().removeAll();
	        Manager_View_Apartments page = new Manager_View_Apartments();
	        page.init();
	        getContentPane().add(page);
	        getContentPane().validate();
		}
		// If the bookings button has been clicked
		if(event.getSource() == bookings_bt)
		{
			// Displays the Manager View Bookings class
			getContentPane().removeAll();
	        Manager_View_Bookings page = new Manager_View_Bookings();
	        page.init();
	        getContentPane().add(page);
	        getContentPane().validate();
		}
		// If the clients button has been clicked
		if(event.getSource() == clients_bt)
		{
			// Displays the Manager View Clients class
			getContentPane().removeAll();
	        Manager_View_Clients page = new Manager_View_Clients();
	        page.init();
	        getContentPane().add(page);
	        getContentPane().validate();
		}
		// If the back button has been clicked
		if(event.getSource() == back_bt)
		{
			// Returns to the precious page
			getContentPane().removeAll();
	        Manager_Hotel_Management page = new Manager_Hotel_Management();
	        page.init();
	        getContentPane().add(page);
	        getContentPane().validate();
		}
	} //================================================== End of method actionPerformed ==========================================================
}// end of the class