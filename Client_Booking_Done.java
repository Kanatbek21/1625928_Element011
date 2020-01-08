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
import java.util.concurrent.TimeUnit;

//This is a  multi-threaded JDBC program which allows multiple users to try to simultaneously insert an existing record
public class Client_Booking_Done extends Thread
{
	/****************************************************** Instantiating all the variables *******************************************************************/
	private static Connection theConnection = null;			// Declaring the connection and assigning as null
	private ResultSet resultset = null;						// Assigning the result-set as null
	private ResultSet GettingID_rs = null;					// Result-set for getting the client-ID number
	private ResultSet SettingID_rs = null;					// Result-set for setting the new client-ID number
	private ResultSet GettingApartmentDetails_rs = null;	// Result-set for getting the apartment details
	private ResultSet GettingFullNameDetails_rs = null;		// Result-set for getting full name details
	private Statement statement = null;						// Assigning the statement as null
	
	private static boolean share_connection = false;	// Declaring the boolean connection and making it false
	private static boolean greenLight = false;			// The initial point of the greenLight method is declared as false
	
	private static int NUM_OF_THREADS = 100;	// Declares the default number of threads as 100
	private int m_myId = 0;						// Declaring a new variable
	private static int c_nextId = 0;			// Declaring the variable of the next ID and setting it as 1
	private static int the_price = 0;			// Declaring the price of the apartment
	private static int num_of_guests = 0;		// Declaring the number of guests
	private static int num_of_nights = 0;		// Declaring the number of nights
	private static int total_aprt_price = 0;	// Declaring the total price of booking
	private static int aprt_cost = 0;			// Declaring the cost of an apartment per night
	private static int total_cat_price = 0;		// Declaring the total catering price
	private static int the_catering = 0;		// Declaring the catering
	public static int bookID = 0;				// Declaring the booking ID number
	public static int theBookID = 0;			// Declaring the new booking ID number after it has been incremented
	public static int counter = 0;				// Declaring the counter to check if the record has been insterted or not
	
	private static String filename;				// Declaring the filename of the server
	private static String database;				// Declaring the database of the connection for the server
	private static String userName;				// Declaring the user-name for the connection
	private static String password;				// Declaring the password for the connection
	private static String databaseConnection;	// Declaring the connection to the database variable
	private String record;						// Declaring a new variable for the action of entering the record
	
	// the variables from the ApartmentsBook table for the use of 
	private static String aprt_name;
	private static String price;
	private static String bedrm_num;
	private static String sprt_living_rm;
	private static String bathrm_num;
	private static String f_name;
	
	// The variables from the user input
	private static String full_name;
	private static String the_aptm_name;
	private static String guests_num;
	private static String start_date;
	private static String end_date;
	private static String box_option;
	
	// Instantiating the threads
	private static Client_Booking_Done Thread;
	private static Thread[] aThread = new Thread[NUM_OF_THREADS];
	/*==========================================================================================================================================================*/
	
	// A static method that has been synchronised on the object
	// Synchronised static methods are synchronised on the class object of the class the synchronised static method belongs to.
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
	public Client_Booking_Done()
	{
		super();						// This invokes the constructor of immediate parent class
		m_myId = getNextId();			// Assigning an ID to the thread
	}
	
	/******************************** The method that connects to the ecommsvr5 server and sets the sharing connection *************************************/
	public void Client_Booking_Connection()
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
				Thread = new Client_Booking_Done();
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
	
	/************************************************** The method for making the calculations *************************************************************/
	public void details()
	{
		try
		{
			/******************************************** Setting the ID number for the Client *************************************************************/
			statement = theConnection.createStatement();			
			GettingID_rs = statement.executeQuery("SELECT * FROM myBookingsBook");
			if(GettingID_rs.next())
			{
				SettingID_rs = statement.executeQuery("SELECT * FROM myBookingsBook");
				while(SettingID_rs.next())
				{
					String booking_id2 = SettingID_rs.getString("bookingIDNumber");
					bookID = Integer.parseInt(booking_id2);
				}
				bookID++;
			} else {
				bookID = 1010;
			}
			theBookID = bookID;
			/*=============================================================================================================================================*/
			
			/*************************************** Getting the details from the Apartments Book Table ****************************************************/
			GettingApartmentDetails_rs = statement.executeQuery("SELECT * FROM myApartmentsBook WHERE apartmentName = '" + Client_Do_Booking.aprt_name_text.getText() + "'");
			if(GettingApartmentDetails_rs.next())
			{
				aprt_name = GettingApartmentDetails_rs.getString("apartmentName");
				price = GettingApartmentDetails_rs.getString("price");
				bedrm_num = GettingApartmentDetails_rs.getString("BedroomsNumber");
				sprt_living_rm = GettingApartmentDetails_rs.getString("separateLivingRoom");
				bathrm_num = GettingApartmentDetails_rs.getString("BathroomsNumber");
			}
			/*=============================================================================================================================================*/
			
			/*************************** Getting the Full Name of the Client by checking it with their inserted 'UserName' *********************************/
			String u_name = Login.logintext.getText();
			GettingFullNameDetails_rs = statement.executeQuery("SELECT * FROM myLoginBooks WHERE userName = '" + u_name + "'");
			if(GettingFullNameDetails_rs.next())
			{
				f_name = GettingFullNameDetails_rs.getString("fullNameDetails");
			}
			/*=============================================================================================================================================*/
			
			/******************************************* Assigning the variables with the User input values ************************************************/
			full_name = f_name;
			the_aptm_name = Client_Do_Booking.aprt_name_text.getText();
			guests_num = Client_Do_Booking.num_guests_text.getText();
			start_date = Client_Do_Booking.start_date_text.getText();
			end_date = Client_Do_Booking.end_date_text.getText();
			box_option = (String) Client_Do_Booking.box.getSelectedItem();	// get the drop down menu value
			/*=============================================================================================================================================*/
			
			/*********************************** Calculating the Catering Price and the Total Price for the Client *****************************************/
			num_of_guests = Integer.parseInt(guests_num);
			
			java.text.DateFormat sql_format = new java.text.SimpleDateFormat("yyyy-MM-dd");
			java.util.Date SQLStartDate = sql_format.parse(start_date);
			java.sql.Date str_dt = new java.sql.Date(SQLStartDate.getTime());
			java.util.Date SQLEndDate = sql_format.parse(end_date);
			java.sql.Date end_dt = new java.sql.Date(SQLEndDate.getTime());
			
			long date_diff = end_dt.getTime() - str_dt.getTime();
			long dates_num = TimeUnit.DAYS.convert(date_diff, TimeUnit.MILLISECONDS);
			String diff_num = String.valueOf(dates_num);
			num_of_nights = Integer.parseInt(diff_num);
			the_price = Integer.parseInt(price);
			
			if(box_option.equals("Yes"))
			{
				the_catering = 15;
				aprt_cost = the_price * num_of_nights;
				
				total_cat_price = (the_catering * num_of_guests) * num_of_nights;
				total_aprt_price = aprt_cost + total_cat_price;
			}
			else
			{
				the_catering = 0;
				aprt_cost = the_price * num_of_nights;
				
				total_cat_price = (the_catering * num_of_guests) * num_of_nights;
				total_aprt_price = aprt_cost + total_cat_price;
			}
			/*===========================================================================================================================================*/
		}
		catch (Exception e)		// Catches the exceptions within the code being executed
		{
			e.printStackTrace();
		} //-------------------------------------------- end of try-catch statement ------------------------------
	} /*=================================================== End of details() method ========================================================================*/

	/************************************** The method that allows 100 users to insert the record simultaneously *******************************************/
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
			
			// Calling the following method that has the calculated variables
			details();
			
			// Executing a query with the original table records
			resultset = statement.executeQuery ("SELECT * FROM myBookingsBook");
			while (resultset.next())
			{
				System.out.print("");
				yield();
			}
			
			// Selecting the table from the myBookingsBook and assigning it to the result-set with the given value
			resultset = statement.executeQuery("SELECT * FROM myBookingsBook WHERE apartmentName = '" + aprt_name + "'");
			// If the result-set if true, then the record already exists
			if(resultset.next())
			{
				// Displays the following message
				System.out.println("Thread " + m_myId + "\t- SORRY - The chosen Apartment already booked!");
			}
			// Otherwise, means that the record does not exist
			else
			{
				// Creating the record
				record = "INSERT INTO myBookingsBook (bookingIDNumber, clientNameDetails, apartmentName, guestsNumber, bookingStartDate, bookingEndDate, " +
									"BedroomsNumber, separateLivingRoom, BathroomsNumber, totalCateringPrice, totalPrice) "
						+ "VALUES ('" + theBookID + "','" + full_name + "','" + the_aptm_name + "','" + guests_num + "','" + start_date + "','" + end_date
						+ "','" + bedrm_num + "','" + sprt_living_rm + "','" + bathrm_num + "','" + total_cat_price + "','" + total_aprt_price + "')";
				// Updating the statement, which inputs the record created above
				counter = statement.executeUpdate(record);
				yield();
				
				// Displays the following message
				System.out.println("Thread " + m_myId + "\t- Booking Successful. Thank you for booking the Apartment!");
			}
			
			// Executing a query with the modified table records
			resultset = statement.executeQuery ("SELECT * FROM myBookingsBook");
			while (resultset.next())
			{
				System.out.print("");
				yield();
			}
			
			// Closing the result-set
			resultset.close();
			resultset = null;
			
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