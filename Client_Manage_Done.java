//**************************************************************************************************//
//Author: 1625928																					//
//Description: The Client Booking Done class connects to the MySQL Database	Table 'myBookingsBook'	//
//				with the use of JDBC. It allows multiple users to share the connection with the 	//
//				database. Afterwards, 100 users shared the same connection with the database and	//
//				attempted to book the apartment simultaneously. Because all Oracle JDBC API methods	//
//				are synchronised, if two threads try to use the connection object simultaneously,	//
//				then one will be forced to wait until the other one finishes its use.				//
//**************************************************************************************************//

//importing all the packages in order to run the code
import java.sql.*;

//This is a  multi-threaded JDBC program which allows multiple users to try to simultaneously insert an existing record
public class Client_Manage_Done extends Thread
{
	/****************************************************** Instantiating all the variables *******************************************************************/
	private static Connection theConnection = null;		// Declaring the connection and assigning as null
	private static ResultSet resultset = null;			// Assigning the result-set as null
	private Statement statement = null;					// Assigning the statement as null
	
	private static boolean share_connection = false;	// Declaring the boolean connection and making it false
	private static boolean greenLight = false;			// The initial point of the greenLight method is declared as false
	
	// Declaring the integers
	private static int NUM_OF_THREADS = 100;				// Declares the default number of threads as 10
	private int m_myId = 0;								// Declaring a new variable
	private static int c_nextId = 0;					// Declaring the variable of the next ID and setting it as 1
	
	// Instance fields for the type String
	private static String filename;						// Declaring the filename of the server
	private static String database;						// Declaring the database of the connection for the server
	private static String userName;						// Declaring the user-name for the connection
	private static String password;						// Declaring the password for the connection
	private static String databaseConnection;			// Declaring the connection to the database variable
	private static String Catering_Included;			// The price with the catering option
	private static String Catering_Not_Included;		// The price without the catering option
	
	// Instantiating the threads
	private static Client_Manage_Done Thread;
	private static Thread[] aThread = new Thread[NUM_OF_THREADS];
	/*==========================================================================================================================================================*/
	
	// The following method obtains the next thread ID by increasing the number of the thread by one.
	synchronized static int getNextId()
	{
		return c_nextId++;		// Adds one to the counter of the nextId
	}
	
	// A static method which starts all 10 threads at the same time that has been synchronised on the object
	static synchronized void setGreenLight()
	{
		greenLight = true;		// sets the value as true, in order to start the threads simultaneously
	}
	
	// The following method gets the value of the greenLight() method
	synchronized boolean getGreenLight()
	{
		return greenLight;		// Returns the greenLight, which either true or false
	}
	
	// Constructor for assigning the ID to the thread
	public Client_Manage_Done()
	{
		super();						// This invokes the constructor of immediate parent class
		m_myId = getNextId();			// Assigning an ID to the thread
	}
	
	/******************************** The method that connects to the ecommsvr5 server and sets the sharing connection *************************************/
	public static void Client_Manage_Connection()
	{
		// The try statement allows you to define a block of code to be tested for errors while it is being executed
		// It will test if there is a connection with the driver and if it is sharing the connection
		try
		{
			// Loading and connecting the external JDBC driver, which has been installed to this project
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			// Assigning the variables for the connection through the driver such as: the path, user-name, password, etc.
			filename = "jdbc:mysql://ecommsvr5:3306/";
					/******** Making the SSL connection *****/
					// + "?verifyServerCertificate=false"+	//
					// "&useSSL=true" +						//
					// "&requireSSL=true";					//
					//**************************************//
			database = "kanatbeckj_oop";
			userName = "kanatbeckj_aoop";
			password = "1625928";
			databaseConnection = filename + database;
			
			theConnection = DriverManager.getConnection(databaseConnection, userName, password);
			share_connection = true;
			
			for (int i = 0; i < NUM_OF_THREADS; i++)
			{
				// Starting the threads
				Thread = new Client_Manage_Done();
				aThread[i] = new Thread(Thread);
				aThread[i].start();
			}
			
			setGreenLight ();	// Calling the following method that starts all the threads at the same time
			
			for (int i = 0; i < NUM_OF_THREADS; i++)
			{
				aThread[i].join();		// Joining to the threads
			}
			
			if(share_connection)
			{
				// Closing the connection with the server
				theConnection.close();
				theConnection = null;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		} // end of try-catch statement
	
	} /*=======================================================================================================================================================*/

	/************************************** The method that allows 100 users to modify the record simultaneously *******************************************/
	public void run()
	{
		// This time, assuming it connected to the driver, it will try to remove the record simultaneously
		try
		{
			// If the connection will be shared, then the same JDBC connection object will be utilised
			// by all threads, which means that each thread will have its own statement object
			if (share_connection)
			{
				statement = theConnection.createStatement ();		// Creating the statement and assigning it to the variable
			}
			
			// While the GreenLight method is not true, it checks if the 'race' has started
			while (!getGreenLight())
			{
				// This method causes the currently executing thread object to temporarily pause
				// and allow other threads to execute
				yield();
			}
			
			// Assigning the variables with the Catering included and not-included
			Catering_Included = "UPDATE myBookingsBook SET guestsNumber = " + Client_Manage_Booking.mdfy_guests + ", bookingStartDate = '" + Client_Manage_Booking.mdfy_strdt
					+ "', bookingEndDate = '" + Client_Manage_Booking.mdfy_enddt + "', totalCateringPrice = " + Client_Manage_Booking.total_cat_price + ", totalPrice = "
					+ Client_Manage_Booking.total_aprt_price + " WHERE bookingIDNumber = '" + Client_View_Bookings.bk_ID_text.getText() + "'";
					
			Catering_Not_Included = "UPDATE myBookingsBook SET guestsNumber = " + Client_Manage_Booking.mdfy_guests + ", bookingStartDate = '" + Client_Manage_Booking.mdfy_strdt
					+ "', bookingEndDate = '" + Client_Manage_Booking.mdfy_enddt + "', totalCateringPrice = " + Client_Manage_Booking.total_cat_price2 + ", totalPrice = "
					+ Client_Manage_Booking.total_aprt_price2 + " WHERE bookingIDNumber = '" + Client_View_Bookings.bk_ID_text.getText() + "'";
			
			// Selecting the booking with the specific booking ID number, in order to check if it exists
			resultset = statement.executeQuery("SELECT * FROM myBookingsBook WHERE bookingIDNumber = '" + Client_View_Bookings.bk_ID_text.getText() + "'");
			if(resultset.next())
			{
				// Selecting the specific booking with the booking ID number and the guests number, in order to modify the booking
				resultset = statement.executeQuery("SELECT * FROM myBookingsBook WHERE bookingIDNumber = '" + Client_View_Bookings.bk_ID_text.getText() + "' and guestsNumber = '"
						+ Client_Manage_Booking.mdfy_guests + "'");
				if(resultset.next())
				{
					// Displays the following message when the booking has already been modified
					System.out.println("Thread " + m_myId + "\t- SORRY - The apartment already has been modified!");
				}
				else
				{
					// If the catering option is 'Yes'
					if(Client_Manage_Booking.box_opt.equals("Yes"))
					{
						// Updating the statement, which modifies the record created above
						statement.executeUpdate(Catering_Included);
						yield();			// stops the threads
						// Prints out the following message
						System.out.println("Thread " + m_myId + "\t- The Apartment has been successfully modified");
					}
					// If the catering option is 'No'
					else
					{
						// Updating the statement, which modifies the record created above
						statement.executeUpdate(Catering_Not_Included);
						yield();			// stops the threads
						// Prints out the following message
						System.out.println("Thread " + m_myId + "\t- The Apartment has been successfully modified");
					}
				}
			}
			else
			{
				System.out.println("The booking does not exist!");
			}
			
			// Closing the statement
			statement.close();
			statement = null;
			
			// Prints out the message of threads that has finished its execution
			System.out.println("Thread " + m_myId +  " is finished.");
		}
		catch (Exception e)		// Catching the exception within the code being executed
		{
								// Prints out the message of which thread has got an exception
			System.out.println("Thread " + m_myId + " got Exception: " + e);
			e.printStackTrace();
			return;
		} // end of try-catch statement
	} /*=================================================== end of run() method =====================================================================*/
} // end of class