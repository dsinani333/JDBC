package p2;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.io.FileReader;
import oracle.jdbc.driver.*;
import org.apache.ibatis.jdbc.ScriptRunner;


public class Student{
    static Connection con;
    static Statement stmt;
    static Scanner s;

    public static void main(String argv[]) throws Exception
    {
	connectToDatabase();
	runSQL();
	menu();
	s.close();
    }
    
/*
 * Prompts the user for specific attributes, searches through the tables by the provided attributes and displays results.
 */    
    public static void searchAtt() throws Exception {
    	ScriptRunner sr = new ScriptRunner(con);
    	FileWriter f;
		String temp;
		Reader r;
		boolean tid = false;
		boolean distinct = false;
		boolean[] t = new boolean[4];
		boolean[] tc = new boolean[2];
		boolean tmp1 = false;
		boolean tmp2 = false;
		
		//input fields
		System.out.println("\nInput Fields:");
		System.out.println("For input fields enter correct value or enter the word 'empty' for no value!!!");
		System.out.println("\nEnter customer_ID:");
		String CID = s.nextLine();
		if(!CID.equals("empty")) {
			try {
				Integer.parseInt(CID);
			}catch(NumberFormatException e) {
				System.out.print("Invalid Input for customer_ID!!!");
				return;
			}
			if((Integer.parseInt(CID) <1) || (Integer.parseInt(CID) >10)){
				System.out.print("Invalid Input for customer_ID!!!");
				return;
			}
		}
		System.out.println("\nEnter total:");
		String total = s.nextLine();
		if(!total.equals("empty")) {
			try {
				Float.parseFloat(total);
			}catch(NumberFormatException e) {
				System.out.print("Invalid Input for total!!!");
				return;
			}
			if((Float.parseFloat(total) <12.5) || (Float.parseFloat(total) >96.15)){
				System.out.print("Invalid Input for total!!!");
				return;
			}
		}
		System.out.println("\nEnter UPC:");
		String UPC = s.nextLine();
		if(!UPC.equals("empty")) {
			try {
				Integer.parseInt(UPC);
			}catch(NumberFormatException e) {
				System.out.print("Invalid Input for UPC!!!");
				return;
			}
			if((Integer.parseInt(UPC) <10001) || (Integer.parseInt(UPC) >10032)){
				System.out.print("Invalid Input for UPC!!!");
				return;
			}
		}
		System.out.println("\nEnter quantity:");
		String quantity = s.nextLine();
		if(!quantity.equals("empty")) {
			try {
				Integer.parseInt(quantity);
			}catch(NumberFormatException e) {
				System.out.print("Invalid Input for quantity!!!");
				return;
			}
			if((Integer.parseInt(quantity) <1) || (Integer.parseInt(quantity) >5)){
				System.out.print("Invalid Input for quantity!!!");
				return;
			}
		}
		
		//output fields
		System.out.println("\n\nOutput Fields:");
		//transaction_ID
		System.out.println("\ntransaction_ID(Yes/No):");
		temp = s.nextLine();
		if(temp.equals("Yes") || temp.equals("yes") || temp.equals("YES")) {
			tid=true;
		}else if(temp.equals("No") || temp.equals("no") || temp.equals("NO")){
			tid=false;
		}else {
			System.out.println("\n Invalid input! Please enter Yes/No as an input.");
			return;
		}
		
		//transactions fields
		System.out.println("\ncustomer_ID(Yes/No):");
		temp = s.nextLine();
		if(temp.equals("Yes") || temp.equals("yes") || temp.equals("YES")) {
			t[0]=true;
		}else if(temp.equals("No") || temp.equals("no") || temp.equals("NO")){
			t[0]=false;
		}else {
			System.out.println("\n Invalid input! Please enter Yes/No as an input.");
			return;
		}
		
		System.out.println("\ntransaction_date(Yes/No):");
		temp = s.nextLine();
		if(temp.equals("Yes") || temp.equals("yes") || temp.equals("YES")) {
			t[1]=true;
		}else if(temp.equals("No") || temp.equals("no") || temp.equals("NO")){
			t[1]=false;
		}else {
			System.out.println("\n Invalid input! Please enter Yes/No as an input.");
			return;
		}
		
    	System.out.println("\npayment_method(Yes/No):");
		temp = s.nextLine();
		if(temp.equals("Yes") || temp.equals("yes") || temp.equals("YES")) {
			t[2]=true;		
		}else if(temp.equals("No") || temp.equals("no") || temp.equals("NO")){
			t[2]=false;
		}else {
			System.out.println("\n Invalid input! Please enter Yes/No as an input.");
			return;
		}
		
		System.out.println("\ntotal(Yes/No):");
		temp = s.nextLine();
		if(temp.equals("Yes") || temp.equals("yes") || temp.equals("YES")) {
			t[3]=true;
		}else if(temp.equals("No") || temp.equals("no") || temp.equals("NO")){
			t[3]=false;
		}else {
			System.out.println("\n Invalid input! Please enter Yes/No as an input.");
			return;
		}
		
		//transaction_contains fields
		System.out.println("\nUPC(Yes/No):");
		temp = s.nextLine();
		if(temp.equals("Yes") || temp.equals("yes") || temp.equals("YES")) {
			tc[0]=true;
		}else if(temp.equals("No") || temp.equals("no") || temp.equals("NO")){
			tc[0]=false;
		}else {
			System.out.println("\n Invalid input! Please enter Yes/No as an input.");
			return;
		}
		
		System.out.println("\nquantity(Yes/No):");
		temp = s.nextLine();
		if(temp.equals("Yes") || temp.equals("yes") || temp.equals("YES")) {
			tc[1]=true;
		}else if(temp.equals("No") || temp.equals("no") || temp.equals("NO")){
			tc[1]=false;
		}else {
			System.out.println("\n Invalid input! Please enter Yes/No as an input.");
			return;
		}
		
		//distinct field
		System.out.println("\n\nDistinct(Yes/No):");
		temp = s.nextLine();
		if(temp.equals("Yes") || temp.equals("yes") || temp.equals("YES")) {
			distinct=true;
		}else if(temp.equals("No") || temp.equals("no") || temp.equals("NO")){
			distinct=false;
		}else {
			System.out.println("\n Invalid input! Please enter Yes/No as an input.");
			return;
		}
		
		System.out.println("\n");
		
		//Checking which table to use for query or if both need to be used.
		for(int i=0; i<t.length; i++) {
			if(t[i]==true) {
				tmp1=true;
				break;
			}
		}
		
		for(int j=0; j<tc.length; j++) {
			if(tc[j]==true) {
				tmp2=true;
				break;
			}
		}
		
		f = new FileWriter("temp.txt");
		//Writing query based on which table information is requested.
		//Transactions
		if(tmp1==true) {
			//inputs from other table are both empty
			if(UPC.equals("empty") && quantity.equals("empty")) {
				f.write("select");
				if(distinct) {
					f.write(" distinct");
				}
				if(tid==true) {
					f.write(" transaction_ID ");
				}
				if(t[0]==true) {
					if(tid==true) {
						f.write(",");
					}
					f.write(" customer_ID ");
				}
				if(t[1]==true) {
					if(t[0]==true || tid==true) {
						f.write(",");
					}
					f.write(" transaction_date ");
				}
				if(t[2]==true) {
					if(t[1]==true || t[0]==true || tid==true) {
						f.write(",");
					}
					f.write(" payment_method ");
				}
				if(t[3]==true) {
					if(t[2]==true || t[1]==true || t[0]==true || tid==true) {
						f.write(",");
					}
					f.write(" total ");
				}
				f.write("from transactions");
				if(!CID.equals("empty") || !total.equals("empty")) {
					f.write(" where");
				}
				if(!CID.equals("empty") && !total.equals("empty")) {
					f.write(" customer_ID="+CID+" union");
					f.write(" select");
					if(tid==true) {
						f.write(" transaction_ID ");
					}
					if(t[0]==true) {
						if(tid==true) {
							f.write(",");
						}
						f.write(" customer_ID ");
					}
					if(t[1]==true) {
						if(t[0]==true || tid==true) {
							f.write(",");
						}
						f.write(" transaction_date ");
					}
					if(t[2]==true) {
						if(t[1]==true || t[0]==true || tid==true) {
							f.write(",");
						}
						f.write(" payment_method ");
					}
					if(t[3]==true) {
						if(t[2]==true || t[1]==true || t[0]==true || tid==true) {
							f.write(",");
						}
						f.write(" total ");
					}
					f.write("from transactions where total="+total+";");
				}else {
					if(!CID.equals("empty")) {
						f.write(" customer_ID="+ CID);
					}
					if(!total.equals("empty")) {
						if(!CID.equals("empty")) {
							f.write(" and");
						}
						f.write(" total="+ total);
					}
				}
				f.write(";");
			//input from other table are not both empty
			}else{
				f.write("select");
				if(distinct) {
					f.write(" distinct");
				}
				if(tid==true) {
					f.write(" transaction_ID ");
				}
				if(t[0]==true) {
					if(tid==true) {
						f.write(",");
					}
					f.write(" customer_ID ");
				}
				if(t[1]==true) {
					if(t[0]==true || tid==true) {
						f.write(",");
					}
					f.write(" transaction_date ");
				}
				if(t[2]==true) {
					if(t[1]==true || t[0]==true || tid==true) {
						f.write(",");
					}
					f.write(" payment_method ");
				}
				if(t[3]==true) {
					if(t[2]==true || t[1]==true || t[0]==true || tid==true) {
						f.write(",");
					}
					f.write(" total ");
				}
				f.write("from transactions");
				if(!CID.equals("empty") || !total.equals("empty")) {
					f.write(" where");
				}
				//add && part
				if(!CID.equals("empty") && !total.equals("empty")) {
					f.write(" customer_ID="+CID+" union");
					f.write(" select");
					if(distinct) {
						f.write(" distinct");
					}
					if(tid==true) {
						f.write(" transaction_ID ");
					}
					if(t[0]==true) {
						if(tid==true) {
							f.write(",");
						}
						f.write(" customer_ID ");
					}
					if(t[1]==true) {
						if(t[0]==true || tid==true) {
							f.write(",");
						}
						f.write(" transaction_date ");
					}
					if(t[2]==true) {
						if(t[1]==true || t[0]==true || tid==true) {
							f.write(",");
						}
						f.write(" payment_method ");
					}
					if(t[3]==true) {
						if(t[2]==true || t[1]==true || t[0]==true || tid==true) {
							f.write(",");
						}
						f.write(" total ");
					}
					f.write("from transactions where total="+total+";");
				}else {
					if(!CID.equals("empty")) {
						f.write(" customer_ID="+ CID);
					}
					if(!total.equals("empty")) {
						if(!CID.equals("empty")) {
							f.write(" and");
						}
						f.write(" total="+ total);
					}
				}

				if(CID.equals("empty") && total.equals("empty")) {
					f.write(" where transaction_ID in");
				}else {
					f.write(" and transaction_ID in");
				}
				
				if((!UPC.equals("empty")) && (!quantity.equals("empty"))) {
					f.write(" (select transaction_ID from transaction_contains where");
					f.write(" UPC="+ UPC+ " union select transaction_ID from transaction_contains where");
					f.write(" quantity="+ quantity+ ")");
				}else {
					if(!UPC.equals("empty")) {
						f.write("(select transaction_ID from transaction_contains where UPC="+UPC+")");
					}
					if(!quantity.equals("empty")) {
						f.write("(select transaction_ID from transaction_contains where quantity="+quantity+")");
					}
				}
				f.write(";\n");	
			}
		}
		
		//Transaction_Contains
		if(tmp2==true) {
			//inputs from other table are empty
			if(CID.equals("empty") && total.equals("empty")) {
				f.write("select");
				if(distinct) {
					f.write(" distinct");
				}
				if(tid==true) {
					f.write(" transaction_ID ");
				}
				if(tc[0]==true) {
					if(tid==true) {
						f.write(",");
					}
					f.write(" UPC ");
				}
				if(tc[1]==true) {
					if(tc[0]==true || tid==true) {
						f.write(",");
					}
					f.write(" quantity ");
				}
				f.write("from transaction_contains");
				if(!UPC.equals("empty") || !quantity.equals("empty")) {
					f.write(" where");
				}
				if(!UPC.equals("empty") && !quantity.equals("empty")) {
					f.write(" UPC="+UPC+" union");
					f.write(" select");
					if(tid==true) {
						f.write(" transaction_ID ");
					}
					if(tc[0]==true) {
						if(tid==true) {
							f.write(",");
						}
						f.write(" UPC ");
					}
					if(tc[1]==true) {
						if(tc[0]==true || tid==true) {
							f.write(",");
						}
						f.write(" quantity ");
					}
					f.write("from transaction_contains where quantity="+quantity+";");
				}else {
					if(!UPC.equals("empty")) {
						f.write(" UPC="+ UPC);
					}
					if(!quantity.equals("empty")) {
						if(!UPC.equals("empty")) {
							f.write(" and");
						}
						f.write(" quantity="+ quantity);
					}
				}
				f.write(";");
			//input from other table are both not empty
			}else{
				f.write("select");
				if(distinct) {
					f.write(" distinct");
				}
				if(tid==true) {
					f.write(" transaction_ID ");
				}
				if(tc[0]==true) {
					if(tid==true) {
						f.write(",");
					}
					f.write(" UPC ");
				}
				if(tc[1]==true) {
					if(tc[0]==true || tid==true) {
						f.write(",");
					}
					f.write(" quantity ");
				}
				f.write("from transaction_contains");
				if(!UPC.equals("empty") || !quantity.equals("empty")) {
					f.write(" where");
				}
				//add && part
				if(!UPC.equals("empty") && !quantity.equals("empty")) {
					f.write(" UPC="+UPC+" union");
					f.write(" select");
					if(distinct) {
						f.write(" distinct");
					}
					if(tid==true) {
						f.write(" transaction_ID ");
					}
					if(tc[0]==true) {
						if(tid==true) {
							f.write(",");
						}
						f.write(" UPC ");
					}
					if(tc[1]==true) {
						if(tc[0]==true || tid==true) {
							f.write(",");
						}
						f.write(" quantity ");
					}
					f.write("from transaction_contains where quantity="+quantity+";");
				}else {
					if(!UPC.equals("empty")) {
						f.write(" UPC="+ UPC);
					}
					if(!quantity.equals("empty")) {
						if(!UPC.equals("empty")) {
							f.write(" and");
						}
						f.write(" quantity="+ quantity);
					}
				}
				
				if(UPC.equals("empty") && quantity.equals("empty")) {
					f.write(" where transaction_ID in");
				}else {
					f.write(" and transaction_ID in");
				}
				
				if((!CID.equals("empty")) && (!total.equals("empty"))) {
					f.write(" (select transaction_ID from transactions where");
					f.write(" CID="+ CID+ " union select transaction_ID from transactions where");
					f.write(" total="+ total+ ")");
				}else {
					if(!CID.equals("empty")) {
						f.write("(select transaction_ID from transactions where customer_ID="+CID+")");
					}
					if(!total.equals("empty")) {
						f.write("(select transaction_ID from transactions where total="+total+")");
					}
				}
				f.write(";");	
			}
		}
		
		f.close();
		r = new BufferedReader(new FileReader("temp.txt"));
		sr.runScript(r);	
    }
/*
 * Searches through the tables by transaction_ID that is provided.
 */
    public static void searchTID() {
    	ScriptRunner sr = new ScriptRunner(con);
    	FileWriter fw;
//    	File f;
    	
		System.out.println("\nEnter the transaction_ID to search:");
		String temp = s.nextLine();
		try {
			Integer.parseInt(temp);
		}catch(NumberFormatException e) {
			System.out.print("Invalid Input for transaction_ID!!!");
			return;
		}
		if(Integer.parseInt(temp) <101 || Integer.parseInt(temp) >106){
			System.out.print("Invalid Input for transaction_ID!!!");
			return;
		}
		System.out.println("\n");
		
		try {
//			  f = File.("temp.txt");
		      fw = new FileWriter("temp.txt");
		      fw.write("select * from transactions where transaction_ID="+ temp +";");
		      fw.write("\nselect avg(quantity) as average from transaction_contains where transaction_ID="+ temp +";");
		      fw.close();
		    } catch (IOException e) {
		      System.out.println("A file error occurred.");
		      e.printStackTrace();
		    }
		
		try {
	    	Reader r = new BufferedReader(new FileReader("temp.txt"));
	    	sr.runScript(r);
	    }catch (FileNotFoundException exception) {
	    	System.out.println("\nFile Not Found!!!");
	    } 
			
//		f.delete();
    }
    
/*
 * Displays the table contents after being prompted for each table.
 */
    public static void viewTables() throws Exception {
    	ScriptRunner sr = new ScriptRunner(con);
		FileWriter f;
		String temp;
		Reader r;
    	
		System.out.println("\nProduct(Yes/No):");
		temp = s.nextLine();
		System.out.println("\n");
		if(temp.equals("Yes") || temp.equals("yes") || temp.equals("YES")) {	
			f = new FileWriter("temp.txt");
			f.write("select * from product;");
			f.close();
			
		    r = new BufferedReader(new FileReader("temp.txt"));
		   	sr.runScript(r);
		}else if(temp.equals("No") || temp.equals("no") || temp.equals("NO")) {
			//do nothing
		}else{
			System.out.println("\n Invalid input! Please enter Yes/No as an input.");
			return;
		}
		
		System.out.println("\nCustomer(Yes/No):");
		temp = s.nextLine();
		System.out.println("\n");
		if(temp.equals("Yes") || temp.equals("yes") || temp.equals("YES")) {
			f = new FileWriter("temp.txt");
			f.write("select * from customer;");
			f.close();
			
		    r = new BufferedReader(new FileReader("temp.txt"));
		   	sr.runScript(r);
		}else if(temp.equals("No") || temp.equals("no") || temp.equals("NO")) {
			//do nothing
		}else{
			System.out.println("\n Invalid input! Please enter Yes/No as an input.");
			return;
		}
		
		System.out.println("\nTransactions(Yes/No):");
		temp = s.nextLine();
		System.out.println("\n");
		if(temp.equals("Yes") || temp.equals("yes") || temp.equals("YES")) {
			f = new FileWriter("temp.txt");
			f.write("select * from transactions;");
			f.close();
			
		    r = new BufferedReader(new FileReader("temp.txt"));
		   	sr.runScript(r);
		}else if(temp.equals("No") || temp.equals("no") || temp.equals("NO")) {
			//do nothing
		}else{
			System.out.println("\n Invalid input! Please enter Yes/No as an input.");
			return;
		}
		
		System.out.println("\nTransaction_Contains(Yes/No):");
		temp = s.nextLine();
		System.out.println("\n");
		if(temp.equals("Yes") || temp.equals("yes") || temp.equals("YES")) {
			f = new FileWriter("temp.txt");
			f.write("select * from transaction_contains;");
			f.close();
			
		    r = new BufferedReader(new FileReader("temp.txt"));
		   	sr.runScript(r);
		}else if(temp.equals("No") || temp.equals("no") || temp.equals("NO")){
			// do nothing
		}else {
			System.out.println("\n Invalid input! Please enter Yes/No as an input.");
			return;
		}
    }
    
/*	
 * Menu method that provides a list of options and what they do when interacting with the database.
 */
    public static void menu() throws Exception{
    	System.out.println("\n\n-----------------------Menu------------------------");
    	System.out.println("Enter 1 for viewing table contents");
    	System.out.println("Enter 2 for searching by transaction_ID");
    	System.out.println("Enter 3 for searching by different attributes");
    	System.out.println("Enter 4 for exiting");
    	System.out.println("---------------------------------------------------");
    	System.out.println("\nChoose one of the options above: ");
    	String option = s.nextLine();
    	
    	if(option.equals("1")) {
    		viewTables();
    		//menu();
    		
    	}else if(option.equals("2")) {
    		searchTID();
    		//menu();
    		
    	}else if (option.equals("3")) {
    		searchAtt();
    		//menu();
    		
    	}else if (option.equals("4")) {
    		System.out.println("\nYou have exited the program!");
    		//System.exit(0);
    	}else {
    		System.out.println("\nOption entered is invalid!");
    		//menu();
    	}
    	
    	if(!option.equals("4")) {
    		menu();
    	}
    }
    
/*
 * Method that prompts the user for the path to the sql script and runs it.
 */
    public static void runSQL(){
    	ScriptRunner sr = new ScriptRunner(con);
    	System.out.println("\nPlease enter a path for the .sql file: ");
    	String fl = s.nextLine(); 
    	
	    try {
	    	Reader r = new BufferedReader(new FileReader(fl));
	    	sr.runScript(r);
	    }catch (FileNotFoundException exception) {
	    
	    	System.out.println("\nFile Not Found!!!");
	    	runSQL();
	    } 	
    }
    
/*
 * Method that connects to the oracle database by asking for username and password.
 */
    public static void connectToDatabase()
    {
	String driverPrefixURL="jdbc:oracle:thin:@";
	String jdbc_url="artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu";
	
        // IMPORTANT: DO NOT PUT YOUR LOGIN INFORMATION HERE. INSTEAD, PROMPT USER FOR HIS/HER LOGIN/PASSWD
		
		s = new Scanner(System.in); 
        	
        System.out.println("Enter your username: ");  
        String username = s.nextLine();  
        System.out.println("Enter your password: ");  
        String password = s.nextLine();
	
        try{
	    //Register Oracle driver
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (Exception e) {
            System.out.println("Failed to load JDBC/ODBC driver.");
            return;
        }

       try{
            System.out.println(driverPrefixURL+jdbc_url);
            con=DriverManager.getConnection(driverPrefixURL+jdbc_url, username, password);
            DatabaseMetaData dbmd=con.getMetaData();
            stmt=con.createStatement();

            System.out.println("Connected.");

            if(dbmd==null){
                System.out.println("No database meta data");
            }
            else {
                System.out.println("Database Product Name: "+dbmd.getDatabaseProductName());
                System.out.println("Database Product Version: "+dbmd.getDatabaseProductVersion());
                System.out.println("Database Driver Name: "+dbmd.getDriverName());
                System.out.println("Database Driver Version: "+dbmd.getDriverVersion());
            }
        }catch( Exception e) {e.printStackTrace();}

    }// End of connectToDatabase()
}// End of class

