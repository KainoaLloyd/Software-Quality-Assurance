import java.util.Scanner;
import java.util.Arrays;
public class AdminMode {
	//reads in current events file as an array each element of the array is a different event
    boolean wantsToDoIt = true;
    Scanner input= new Scanner(System.in);
    int userChoice;
    String word="";
    String[] CurrentEvents;
    //This method creates an event as a String of fixed length 36 containing the transaction code 03,
    //the event name, event date and number of tickets in the transaction
    
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
    	for (int i = len; i == 20; i++){
    		word+= " ";
    	}
    	
    	//
    	System.out.println("What is the new event date (YYMMDD format)?");
    	word += " " +input.next();
        
    	System.out.println("How many tickets are you willing to allocate for this event?"); 
    	String ticketnum = new String();
        ticketnum += input.next();
        
        //Constraint:  number of tickets can be at most 99999
        len = ticketnum.length();
    	while (len> 5){
			System.out.println("Number of tickets too many, please re-input number of tickets:");
			ticketnum = input.next();
			len = ticketnum.length();
	}
	
	//Constraint:  numeric fields are right justified
	for (int i = len; i == 5; i++){
		ticketnum = "0"+ ticketnum;
	}
	
	//add id and ticket number to event transaction
    word = "03 "+word+ " " +ticketnum;
    
        
        System.out.println("Event created. (Reminder: these wonít be available for sale inputthe current session.)"); 
        
        //this returns one full string of all the info needed to put in file as an event
        //note missing way to make it 36 characters long exactly and has no constraint checking
        return word+"\n";
    }
    
    //allows you to add more tickets to an event
    public String add(){
    	String name = new String();
    	
    	System.out.println("What is the event name?");
    	name = input.next();
    	//Check to see if this is an event, not most effective search
    	for (int i =0; i<CurrentEvents.length; i++){
    		if (name.equals( CurrentEvents[i].substring(0,20))){
    			break;
    		}else if (i == CurrentEvents.length-1){
    			System.out.println("that was not an event");
    			return null;
    		}
    	}
    	
    	name = "04 " + name + " 000000 ";
    	System.out.println("How many tickets do you want to add?");
    	name += input.next();
    	
    	return name+ "\n";
    }
    
    //allows you to delete an event and cancel tickets
    public String delete(){
    	System.out.println("What event would you like to delete");
    	word = input.next();
    	
    	//Check to see if this is an event, not most effective search
    	for (int i =0; i<CurrentEvents.length; i++){
    		if (word.equals( CurrentEvents[i].substring(0,20))){
    			break;
    		}else if (i == CurrentEvents.length-1){
    			System.out.println("that was not an event");
    			return null;
    		}
    	}
    	word = "05 " +word + " 000000" + " 00000";
    	return word + "\n" ;
    }
    
    	//sells tickets for and event and stores it into the event transaction file
    public String sell(){
    	int ticketnum = 0;
    	System.out.println("What event are you selling tickets for");
    	word = input.next();
    	
    	//Check to see if this is an event, not most effective search
    	for (int i =0; i<CurrentEvents.length; i++){
    		
    		if (word.equals( CurrentEvents[i].substring(0,20))){
    			String currentnum = CurrentEvents[i].substring(20);
    			System.out.println("how many tickets are you selling");	
    			
    			ticketnum = input.nextInt();
    			//subtract tickets here
    			ticketnum = Integer.parseInt(currentnum)- ticketnum;
    			break;
    		}else if (i == CurrentEvents.length-1){
    			System.out.println("that was not an event");
    			return null;
    		}
    	}
    	
    	word = "01 " + word + " 000000 " + Integer.toString(ticketnum);

    	return word + "\n";
    }
    
    //This function returns sold tickets for an event
    public String refund(){	
    	int ticketnum = 0;
    	System.out.println("What event are you returning tickets for");
    	word = input.next();
    	//Check to see if this is an event, not most effective search
    	for (int i =0; i<CurrentEvents.length; i++){
    		
    		if (word.equals( CurrentEvents[i].substring(0,20))){
    			String currentnum = CurrentEvents[i].substring(20);
    			System.out.println("how many tickets are you returning");	
    			
    			ticketnum = input.nextInt();
    			//subtract tickets here
    			ticketnum = Integer.parseInt(currentnum)- ticketnum;
    			break;
    		}else if (i == CurrentEvents.length-1){
    			System.out.println("that was not an event");
    			return null;
    		}
    	}
    	
    	word = "02 " + word + " 000000 " + Integer.toString(ticketnum);
    	return word + "\n";
    }
    
    
    //Prompting takes the mode  as in put 1 = sales 2 = admin 
    // this will keep prompting you to enter a command until you logout
    // entering a command will add a line to the EventTransaction file if it is input correctly
    //once the logout command is entered it will start the logout method
   public void Prompting(int mode){
	   	String EventTransactions = null;

            //make a while loop
    
    
	   	while  ( !word.equals("logout") ) {

	   		System.out.println("This is adminmode. Enter a command.");
    
	        word = input.next(); // getting a String value
	        
	        if ( word.equals("create") ) {
	        	if (mode == 2){
	        		EventTransactions += this.Create();
	        	}else{
	        		System.out.println("you must be an admin to do that.");
	        	}
	        }
	        else if ( word.equals("add")) {
	        	if (mode == 2){
	        		EventTransactions += this.add();
	        	}else{
	        		System.out.println("you must be an admin to do that.");
	        	}
	        }
	        else if ( word.equals("delete")) {
	        	if (mode == 2){
	        		EventTransactions += this.delete();
	        	}else{
	        		System.out.println("you must be an admin to do that.");
	        	}
	        }
	        else if ( word.equals("sell")) {
	        	EventTransactions += this.sell(); 
	        }
	
	        else if ( word.equals("return")) {
	        	EventTransactions += this.refund();
	        }
	        
	   	}
	   //	logout(EventTransactions);
   }
}


/*

//This class is used to hold all the functions/methods that can occur once a Transaction Day has started
public class TransactionDay {
		//reads in current events file as an array each element of the array is a different event
	    boolean wantsToDoIt = true;
	    Scanner input= new Scanner(System.in);
	    int userChoice;
	    String word="";
	    String[] CurrentEvents;
	    
*/