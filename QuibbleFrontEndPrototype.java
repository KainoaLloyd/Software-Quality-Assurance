/*This Program is the protoype for Quibble, the Queen's Basic Event Ticket Service
 * This code mainly highlights the Front End of the program : A point of sale ticket 
 * sales terminal for simple ticketing transactions.
 * The Front end reads in a file called QuibblecurrentEvents with the of event names and number of tickets.
 * It also outputs a file with all the transactions made that day
 * Important note file input and output does not work
 * input file: QuibblecurrentEvents
 * 				format for line in file Eventname(right justified with spaces)
 * output file : QuibbleDailyEventTransactions
 * 
 * Copyright: Basement Gurus Inc
 * Authors: Kainoa Lloyd	10114858 	13krl1@queensu.ca
 * 			Natu Myers		10068437	12nm17@queensu.ca
 */

import java.nio.file.Path;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Scanner;
public class QuibbleFrontEndPrototype {

	//reads in current events file as an array each element of the array is a different event
	static  Scanner input= new Scanner(System.in);
    String word="";
    static String[] currentEvents;
	
//This is the main function of the Program, mainly used to Start login process
  public static void main(String[] args) throws IOException {

	  //System.out.print(String.format("%08d", 22));
	  try {
		currentEvents = readIn(args[0]);
		// currentEvents = readIn("CurrentEventsFile.txt");
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      //creates a scanner to read user input
	 // Scanner input = new Scanner(System.in);
        
       System.out.print("Welcome. Login by typing 'login' to continue.\n");
      /*  String command = input.next();
        
        while (command != input.next())  {
   
            System.out.print("Invalid input. Login by typing 'login' to continue.\n");
            command = input.next();
        }*/
        
        int mode = UserLogin();        
      
        QuibbleFrontEndPrototype Day = new QuibbleFrontEndPrototype();
        Day.Prompting(mode);

    }
  
  //This function prompts user to login and returns 1 if you login as sales mode and 2 if you login as admin mode
  //admin mode has privileged transaction (only admins can use them)
  public static int UserLogin(){
		//creates a scanner to read user input
	 // Scanner input = new Scanner(System.in);
      //  int userChoice;
      //  boolean valid = true;
        String command = "";  
        
        command = input.next();  // get string from user
        //Invalid input checking
        if (command.equals("die")){
        	input.close(); 
        	System.exit(0);
        }
        while (!command.equals("login")){

            System.out.print("Invalid input. Login by typing 'login' to continue.\n");  
            command = input.next();
            if (command.equals("die")){
            	input.close(); 
            	System.exit(0);
            }
   
        }
       
        String mode ="";
        System.out.print("Welcome to the front end.\nType 1 to go to sales mode,\ntype 2 to go to admin mode.\n");
        mode = input.next(); // get string from user
        //System.out.print(command);
        while ( (!mode.equals("1") && !mode.equals("2"))){

            System.out.print("Invalid input. Type 1 for sales mode or 2 for admin mode.\n");  
            mode = input.next();
            
        }    
       
        return Integer.parseInt(mode);


	}  
  
  

	    //Privileged Transaction
	    //This method creates and returns an event as a String of fixed length 36 containing the transaction code 03,
	    //event Id:03 the event name, event date and number of tickets in the transaction
	    
	    public String Create(){
	    	System.out.println("What is the new event name?");
	    	word = input.next();
	    	int len = word.length();
	    	
	    	//Constraint: event name must be no more than 20 characters
	    	while (len> 20){
	    			System.out.println("Event name too long, please re-input name:");
	    			word = input.next();
	    			len = word.length();
	    	}
	    	
	    	//Constraint:  alphabetic fields are left justified

	    	boolean alreadyEvent = false;
	    	do{
	    		alreadyEvent = false;

		    	for (String line : currentEvents){

		    		if (line.contains(word)){
		    			System.out.println("Already an event try another name:");
		    			alreadyEvent = true;
			    		System.out.println("must enter a new event");
			    		word = input.next();
		    			break;
		    		}
		    	}
		    	
	    	}while(alreadyEvent == true);
	    	//System.out.println("len is : "+len);
	    	for (int i = len; i < 20; i++){
	    		word+= " ";
	    		//System.out.println("i is : "+i);
	    	}//
	    	//put in check for date here
	    	 System.out.println("What is the new event date (YYMMDD format)?");
	    	 
	    	 
	    	 String worddate = new SimpleDateFormat("yy-MM-dd").format(new Date());
	    	 String worddate2 = worddate.replaceAll("-", "");
	    	int currentdate = Integer.parseInt(worddate2);
	    	  boolean dateinRange = false;
	    	  String date = "";
	    	  do{
	    	  	  	date = input.next();
	    	  	  	int dateInt = Integer.parseInt(date);
	    	  		if (dateInt >currentdate && dateInt <= (currentdate + 020000)){
	    	  			dateinRange = true;
	    	  			
	    	  		}else{
	    	  			System.out.println("date out of range, enter a different date");
	    	  		}
	    	  }while(dateinRange == false);
	    	  
	    	 
	    	 

	        
	    	System.out.println("How many tickets are you willing to allocate for this event?"); 
	    	String ticketnum = new String();
	    	
	        ticketnum += input.next();
	        if (!isInteger(ticketnum)){
	        	System.out.println("That is not a number! create canceled");
	        	return null;
	        }
	        //Constraint:  number of tickets can be at most 99999
	        len = ticketnum.length();
	    	while (len> 5){
				System.out.println("Number of tickets too many, please re-input number of tickets:");
				ticketnum = input.next();
				len = ticketnum.length();
		}
		
		//Constraint:  numeric fields are right justified
		ticketnum = String.format("%05d", Integer.parseInt(ticketnum));
	    	/*for (int i = len; i <= 5; i++){
			ticketnum = "0"+(ticketnum);
		}*/
		
		//add id and ticket number to event transaction
	    word = "03 "+word+ " "+date+" " +ticketnum;
	    
	        
	        System.out.println("Event created. (Reminder: these won't be available for sale inputthe current session.)"); 
	        
	        //this returns one full string of all the info needed to put in file as an event
	        //note missing way to make it 36 characters long exactly and has no constraint checking
	        return word+"\n";
	    }
	    
	    //Privileged Transaction
	    //allows you to add more tickets to an event
	    //returns an event transaction string Id for add: 04
	    public String add(){
	    	String name = new String();
	    	
	    	System.out.println("What is the event name?");
	    	name = input.next();
	    	//Check to see if this is an event, not most effective search
	    	for (int i =0; i<currentEvents.length; i++){
	    		if (currentEvents[i].contains(name)){
	    			break;
	    		}else if (i == currentEvents.length-1){
	    			System.out.println("that was not an event");
	    			return null;
	    		}
	    	}
	    	for (int i = name.length(); i < 20; i++){
	    		name+= " ";
	    		//System.out.println("i is : "+i);
	    	}//
	    	System.out.println("How many tickets do you want to add?");
	    	String ticketword = input.next();
	    	if (!isInteger(ticketword)|| Integer.parseInt(ticketword)<0){
	    		System.out.println("not a valid number transaction canceled");
	    		return null;
	    	}
	    	ticketword = String.format("%05d", Integer.parseInt(ticketword));
	    	
	    	name = "04 " + name + " 000000 ";
	    	
	    	
	    	name += ticketword;
	    	
	    	return name+ "\n";
	    }
	    //Privileged Transaction
	    //allows you to delete an event and cancel tickets
	    //returns an event transaction string, Id for delete: 05
	    public String delete(){
	    	System.out.println("What event would you like to delete");
	    	word = input.next();
	    	boolean found = false;
	    	//Check to see if this is an event, not most effective search
	    	for (int i =0; i<currentEvents.length; i++){
	    		if ( currentEvents[i].contains(word)){
	    			currentEvents[i] = "";
	    			System.out.println("Event deleted");
	    			found = true;
	    			break;
	    		}
	    	}
	    	for (int i = word.length(); i < 20; i++){
	    		word+= " ";
	    		//System.out.println("i is : "+i);
	    	}//
	    	if (found == false){
	    			System.out.println("that was not an event");
	    			return null;
	    		}
	    	word = "05 " +word + " 000000" + " 00000";
	    	return word + "\n" ;
	    }
	    
	    	//sells tickets for and event and stores it into the event transaction file
	    //returns an event Transaction String, id for sell: 01
	    public String sell(int mode){
	    	int ticketnum = 0;
	    	System.out.println("What event are you selling tickets for");
	    	word = input.next();
	    	int index= 0;
	    	boolean valid = false;
	    	boolean found = false;
	    	//Check to see if this is an event, not most effective search
	    	for (int i =0; i<currentEvents.length; i++){
	    		
	    		if (currentEvents[i].contains(word)){
	    			
	    			index = i;
	    			found = true;
	    			break;
			
	    		}
	    	}
			
		if (found== false){
			System.out.println("that was not an event");
			return null;
		}
		String currentnum = currentEvents[index].substring(20);
				System.out.println("how many tickets are you selling");	
	    			do{
	    				
	    				String ticketword =  input.next();
	    				if (isInteger(ticketword)){
	    					ticketnum = Integer.parseInt(ticketword);
	    					if (mode == 2 || ticketnum< 9||ticketnum <0){
	    						valid = true;
	    						
	    					}else{
	    					System.out.println("you can only sell 8 tickets or less in sales mode");
	    					}
	    				}else{
	    					System.out.println("not an int");
	    				}
	    			}while(valid == false);
	    			//subtract tickets here
	    			ticketnum = Integer.parseInt(currentnum)- ticketnum;
	    			if (ticketnum<0){
	    				System.out.println("too many tickets being sold");
	    				return null;
	    			}
	    			
	    	int len = word.length();
	    	for (int i = len; i < 20; i++){
	    		word+= " ";
	    	}
	    	String ticketword = Integer.toString(ticketnum);
	    	/*for (int i = ticketword.length(); i == 5; i++){
	    		ticketword = "0"+ ticketword;
	    	}*/
	    	ticketword = String.format("%05d", Integer.parseInt(ticketword));
	    	currentEvents[index]= word + ticketword;
	    	word = "01 " + word + " 000000 " + ticketword;

	    	return word + "\n";
	    }
	    
	    //This function returns sold tickets for an event
	    //returns an event transaction string, id for return/refund: 02
	    public String refund(int mode ){	
	    	int ticketnum = 0;
	    	System.out.println("What event are you returning tickets for");
	    	word = input.next();
	    	boolean valid = false;
	    	int index = 0;
	    	String ticketword = "";
	    	boolean found = false;
	    	//Check to see if this is an event, not most effective search
	    	for (int i =0; i<currentEvents.length; i++){
	    		
	    		if (currentEvents[i].contains(word)){
	    			
	    			found = true;
	    			index = i;
	    			break;
	    		}
	    		
	    	}

		if (found == false){
			System.out.println("that was not an event");
			return null;
		}
		String currentnum = currentEvents[index].substring(20);
	    	System.out.println("how many tickets are you returning");	
	    			
	    			do{
	    				ticketword= input.next();
	    				if (isInteger(ticketword)){
	    					ticketnum = Integer.parseInt(ticketword);
	    					if (mode == 2 || ticketnum< 9){
	    						valid = true;
	    					}else{
	    						System.out.println("you can only refund 8 tickets or less in sales mode");
	    					}
	    				}else{
	    					System.out.println("not an int");
	    				}
	    			}while(valid == false);
	    			//add tickets here

	    			ticketnum = Integer.parseInt(currentnum)+ ticketnum;
	    			if (ticketnum>99999){
	    				System.out.println("too many tickets being returned");
	    				return null;
	    			}

	    	
	    	int len = word.length();
	    	for (int i = len; i < 20; i++){
	    		word+= " ";
	    	}
	    	
	    	/*for (int i = ticketword.length(); i == 5; i++){
	    		ticketword = "0"+ ticketword;
	    	}*/
	    	ticketword = String.format("%05d", Integer.parseInt(ticketword));
	    	currentEvents[index]= word + ticketword;
	    	word = "01 " + word + " 000000 " + ticketword;
	    	
	    	return word + "\n";
	    }
	    
	    
	    //Prompting takes the mode  as in put 1 = sales 2 = admin 
	    // this will keep prompting you to enter a command until you logout
	    // entering a command will add a line to the EventTransaction file if it is input correctly
	    //once the logout command is entered it will start the logout method
	   public  void Prompting(int mode) throws IOException{
		   	String EventTransactions = "";
		   	
	            //make a while loop
	    
		   	word = "";
		   	while  ( !word.equals("logout") ) {

		   		if (mode == 2){
		   			System.out.println("This is Admin mode. Enter a command.");
		   		}else{
		   			System.out.println("This is Sales mode. Enter a command.");
		   		}
	    
		        word = input.next(); // getting a String value
		        String transaction = "";
		        if ( word.equals("create") ) {
		        	if (mode == 2){
		        			transaction = this.Create();
		        	}else{
		        		System.out.println("you must be an admin to do that.");
		        	}
		        }
		        else if ( word.equals("add")) {
		        	if (mode == 2){
		        		transaction= this.add();
		        	}else{
		        		System.out.println("you must be an admin to do that.");
		        	}
		        }
		        else if ( word.equals("delete")) {
		        	if (mode == 2){
		        		transaction += this.delete();
		        	}else{
		        		System.out.println("you must be an admin to do that.");
		        	}
		        }
		        else if ( word.equals("sell")) {
		        	transaction += this.sell(mode); 
		        }
		
		        else if ( word.equals("return")) {
		        	transaction += this.refund(mode);
		        }
		        if (transaction != null){
		        	EventTransactions += transaction;
		        }
		   	}
		  	try {
				logout(EventTransactions);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	   }
	   
	   
       public static  String[]  readIn(String filename) throws Exception {
           Path filePath = new File(filename).toPath();
           Charset charset = Charset.defaultCharset();        
           List<String> stringList = Files.readAllLines(filePath, charset);
           String[] stringArray = stringList.toArray(new String[]{});
           return stringArray;
       }

       public void logout (String newInfo) throws Exception {
    	   String filename = createFile("DailyEventsTransaction.txt");
           FileWriter fw = new FileWriter(filename);
           //for (int i = 0; i < newInfo.length; i++) {
           String trimmedInfo = newInfo.trim();
             fw.write(trimmedInfo);
        //   }
           fw.close();
           System.out.println("you have succesfully logged out type login to log back in or die to end program");
           int mode = UserLogin();
           QuibbleFrontEndPrototype Day = new QuibbleFrontEndPrototype();
           Day.Prompting(mode);
       }
       

//for integration
       public String createFile(String name) throws IOException{
    	 int count = 0;
         File f;
         f = new File(name);
         while (f.exists()){
        	 count++;
        	 name = "DailyEventsTransaction("+ count +").txt";
        	 f = new File(name);
         }
         if (!f.exists())
         {
           f.createNewFile();
         }
        return name;
       }
       
	 /*  // On logout, write to the daily transaction file, and quit.
       public  static void  logout(String line) throws IOException {
           Files.write(Paths.get("DailyEventsTransaction.txt"), line.getBytes());
           System.out.print( line);
       }
       
       // Get a line of output
       public String getFromCurrentEvent() {       
           final Scanner s = new Scanner("QuibblecurrentEvents.txt");
           final String line = s.nextLine();
           return line;
       }*/
       public boolean isInteger( String input ) {
    	    try {
    	        Integer.parseInt( input );
    	        return true;
    	    }
    	    catch( Exception e ) {
    	        return false;
    	    }
      }


	   
	   
}