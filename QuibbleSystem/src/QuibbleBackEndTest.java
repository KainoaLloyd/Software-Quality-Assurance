/*This Program is the protoype for Quibble, the Queen's Basic Event Ticket Service
 * This code mainly highlights the Back End of the program :
 * 
 * The Back Office reads in the previous day’s master events 
 * file and then applies all of the ticket transactions from 
 * a merged set of event transaction files to the events to 
 * produce the new master events file.
 * 
 * Because transactions may also create or delete new events,
 * it also produces a new current events file for tomorrow’s 
 * Front End runs.
 * 
 * Note: every point where constraint is accounted for there should be a comment "constraint:"


 *Input files: Old Master Events File and Merged Transaction Summary File
 *output files: New Master Events File and New Current Events File,
 * 
 *
 * 
 * Copyright: Basement Gurus Inc
 * Authors: Kainoa Lloyd    10114858    13krl1@queensu.ca
 *          Natu Myers      10068437    12nm17@queensu.ca
 */

import java.nio.file.Path;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;



import java.nio.file.Files;

import java.text.SimpleDateFormat;
public class QuibbleBackEndTest {

    //reads in current events file as an array each element of the array is a different event
   static  Scanner input= new Scanner(System.in);

    static List<String> masterEventsFile;
    static List<String> mergedEventTransactionFile= new ArrayList<String>();
    static List<Event> alteredEventsFile = new ArrayList<Event>();
    static List<Transaction> allTransactions= new ArrayList<Transaction>();
//This is the main function of the Program, mainly used to Start The back Office process
  public static void main(String[] args) throws IOException {

      // Back Office reads the Master Events File and the Merged Event Transaction File
		
	  //the following comments were used for testing purposes
	 /* Event event1 = new Event("012345 45322 qwertyuiopasdfghjklz");
	  Event event2 = new Event("543721 62578 qwertyuiopasdfghjklz");
	  System.out.println(event1.equals(event2));
	  List<Event> aList = new ArrayList<Event>();
	  aList.add(event1);
	  
	  System.out.println("this is second " + aList.contains(event2) );*/
	  
	  //reads in Merged Event transaction file from argument 0
	try {
		//mergedEventTransactionFile = readIn("eventTransactionCreateTests1.txt");
		mergedEventTransactionFile = readIn(args[0]);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	//reads in MasterEventsFile from argument 1
	try {
		//masterEventsFile = readIn("oldMasterEventsFile.txt");
		masterEventsFile = readIn(args[1]);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	ProcessTransaction();
  }
/*This method processes all the transactions from the merged event transaction file
 * and creates a list with all the altered events and new set of current events
 * then calls endBackEnd to output the files
 */
  public static void ProcessTransaction(){
	  Iterator<String> iter = masterEventsFile.iterator();
	  //this uses the deletePastEvents method to delete all the past events in the masterEventsFile
	  while (iter.hasNext()){
		  String e = iter.next();
	  
	  //for (String e : masterEventsFile){
		  Event CheckEvent =  new Event(e);
		  boolean pastDate = deletePastEvents(CheckEvent);
		  if (!pastDate){
			  break;
		  }  
	  }
	 //makes each line of the masterEventFile into an Event object for easier processing
	  for (String e : masterEventsFile){
		  Event event = new Event(e);
		  alteredEventsFile.add(event);
	  }
	  //makes each line into a Transaction object for easier processing
	  for (String t: mergedEventTransactionFile){
		  Transaction transaction= new Transaction(t);
		  allTransactions.add(transaction);
	  }
	  
	  //Iterates through each Transaction processing them one at a time based on their id
	  for (Transaction t: allTransactions){
		  //process sell, return, create, add, delete,end
		  if(t.id == 00){
			  break;
		  }
		  Event changeEvent = findEvent(t.name);
		  if (changeEvent != null){
			  if (t.id == 01){						//sell transaction
				  if((changeEvent.ticket -t.ticket)<0){
					  System.out.println("Sell transaction could not be performed, not enough tickets"); //Contraints: no event should ever have a negative nmber of ticekts
				  }else{
					  changeEvent.ticket = changeEvent.ticket -t.ticket;
				  }
			  }else if (t.id == 02){				//return transaction
				  changeEvent.ticket =changeEvent.ticket + t.ticket;
			  }else if (t.id == 04){				//add transaction
				  changeEvent.ticket += t.ticket;	
			  }else if (t.id == 05){				//delete transaction
				  changeEvent = null;
			  }
		  }else{								//constraint: a new event must have a name different from all existing events
			  if(t.id == 03){						//create transaction
				  Event newEvent = new Event (t.getEventLine());
				  InsertEvent(newEvent);		//constraint: <asterEventFile must be kept in ascending order by date
			  }
		  }
	  }
	  
	  String newMasterEventsFile = "";
	  String newCurrentEventsFile = ""
			  ;
	 // creates two strings in proper format for output to currenteventsFile and MasterEventsFile
	  for (Event e: alteredEventsFile){								//assumes correct input... constraint:every line is exactly 33 characters(plus newline)
		  newMasterEventsFile += e.getEventLine() +"\n";
		  newCurrentEventsFile += e.getCurrentEventLine() + "\n";
	  }
	  
	  //does file output stuff
	  try{
		  endBackEnd(newMasterEventsFile, newCurrentEventsFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }
  
  /*This method inserts a new event in the chronological order 
   * according to its date
   */
  public static void InsertEvent(Event newEvent){
	  int index=0;
	  while (newEvent.date > alteredEventsFile.get(index).date ){
		  index++;
	  }
	  alteredEventsFile.add(index, newEvent);
  }
  
  /*This method searchs through a List of event objects to see
   * if it contains the name of the event that was passed as a string
   * returns EVent object if found and null if not found
   */
  public static Event findEvent(String eventline){
	  for(Event e: alteredEventsFile){
		  if ( e.name.equals(eventline)){
			  return e;
		  }
	  }
	  return null;
  }
  
  /*removes the an event from the master event from the masterEventFile
   * if it's date is less than that of the current date and returns true if an 
   * event was removed
   * 
   */
  public static boolean deletePastEvents (Event event){
	  int currentDate = getCurrentDate();
	  int date = event.date;
	  if (date<currentDate){
		  masterEventsFile.remove(event.getEventLine());
		  return true;
	  }
	  return false;
  }
  
  //This method returns the current date as a string in the format YYMMDD
  public static int getCurrentDate(){
 	 String worddate = new SimpleDateFormat("yy-MM-dd").format(new Date());
 	 String worddate2 = worddate.replaceAll("-", "");
 	 int currentdate = Integer.parseInt(worddate2);
 	 return currentdate;
  }
  
/*This method reads a file and returns the each line of the file as a separate element 
 * of a java List
 */
  public static List<String>   readIn(String filename) throws Exception {
      Path filePath = new File(filename).toPath();
      Charset charset = Charset.defaultCharset();        
      List<String> stringList = Files.readAllLines(filePath, charset);

      return stringList;
  }
  
  /*This method writes the output of the program
   * outputs a new MasterEventsFile and a new CurrentEventsFile
   */
  public static void endBackEnd(String MasterFile, String CurrentFile) throws Exception {
      FileWriter fw = new FileWriter("MasterEventsFile.txt");
      fw.write(MasterFile.trim());
      fw.close();
      fw = new FileWriter("CurrentEventsFile.txt");
      fw.write(CurrentFile.trim());
      fw.close();
  }
  
  /*This class was created to be able to more easily process
   * Event Transactions
   * 
   */
  public static class Transaction{
	  int id;
	  String name;
	  int date;
	  int ticket;
	  /*
	   * Reads a transaction String that is in the format of a transaction event
	   * and splits it so we can deal with the id, name, date and tickets separately
	   */
	  public Transaction(String transactionLine){
			//String delims = "[\\s+]";
			String[] tokens = transactionLine.split(" +");
			if (tokens[0].equals("")){
				id = 00;
			}else{
				id = Integer.parseInt(tokens[0]);
			}
			if (id == 0){
				date = 0;
				ticket = 0;
				name = "";
			}else{
				date = Integer.parseInt(tokens[2]);
				ticket = Integer.parseInt(tokens[3]);
				name = tokens[1];
						
			}

	  }
	  public Transaction() {
			// TODO Auto-generated constructor stub
	  }
	  /*creates a String for the create transaction in the format required
	   * to be entered into the MAster Events File
	   */
	  public String getEventLine(){
			while(name.length()<20){
				name+= " ";
			}
			String Tword;
			Tword = String.format("%05d", ticket);
		  return Integer.toString(date)+ " " +Tword+" "+name;
	  }
  }
  
  /*THis class was created to be able to change details of events
   * in a more isolated fashion
   */
	public static class Event  implements Comparable <Event>{
		int date;
		int ticket;
		String name;
		String original;
		/*seperates an line from theMaster events Transaction file into
		 * the date, ticket, name and original string;
		 */
		public Event (String eventline){
			//String delims = "[ +]";

			String[] tokens = eventline.split(" +");
			date = Integer.parseInt(tokens[0]);
			ticket = Integer.parseInt(tokens[1]);
			name = tokens[2];
			original = eventline;
		}
		
		  /*creates a String for the create transaction in the format required
		   * to be entered into the MAster Events File
		   */
		public String getEventLine(){
			while(name.length()<20){
				name+= " ";
			}
			String Tword;
			Tword = String.format("%05d", ticket);
			return Integer.toString(date)+ " " +Tword+" "+name;
		}
		  /*creates a String for the create transaction in the format required
		   * to be entered into the current Events File
		   */
		public String getCurrentEventLine(){
			while(name.length()<20){
				name+= " ";
			}
			return name+ticket;
		}
		public Event() {
			// TODO Auto-generated constructor stub
		}
		
		//for comparing two event dates
		@Override
		public int compareTo(Event compareEvent){
			
			int compareQuantity = ((Event)compareEvent).date;
			//ascending order
			return this.date-compareQuantity;
			//descending order
			//return compareQuantity -this.quantity;
		}
		
		//for comparing two event names
		public boolean equals(Event compareEvent){
			if (this.name.equals(compareEvent.name)){
				return true;
			}
			return false;
		}
	}     
       
}
