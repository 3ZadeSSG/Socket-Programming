package serverRMI;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.List;
import javax.rmi.PortableRemoteObject;

public class ServicesImplementation extends PortableRemoteObject implements ServicesInterface {
	private Path database=Paths.get("F:\\Banking\\bank.txt");   //location to bank account database
	protected ServicesImplementation() throws RemoteException {
		super();
		if(!Files.exists(database)) { 
			try {
				Files.createFile(database);  //if file not exists then create one
			} catch (IOException e) {
				System.out.println("\nUnable to creat bank database!");
				e.printStackTrace();
			}
		}
	}
	public boolean createAccount(String ID, String Password) throws RemoteException { 
		if(isAccountThere(ID)) {
			return false; //if account already exists then return -ve result indicating faliure
		}
		else {
			try(OutputStream out=new BufferedOutputStream(Files.newOutputStream(database,WRITE,APPEND))){
				String s=ID+" "+Password+" 1000"+System.lineSeparator();  //open account with given ID and password with opening balance of 1000
				byte[]data=new byte[1000];
				data=s.getBytes();
				out.write(data,0,data.length);
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	public String debit(String ID, String Password, double amount) throws RemoteException {
		int lineNumber=login(ID,Password);  //search in which line the current account exists in database
		if(lineNumber!=-1) { //if account is there then do
			try {
				List<String> lines = Files.readAllLines(database, StandardCharsets.UTF_8);
				String details[]=lines.get(lineNumber).split(" "); //details array of string will contain ID, Password and balance read from the  "linunumber"
				double balance=Double.parseDouble(details[2]); //convert the string balance into double format
				if(balance<amount) {  //if debit request is more than current balance then reject request
					return "Balance is low!";
				}
				else {
					balance=balance-amount; //debit the account balance 
					lines.set(lineNumber,details[0]+" "+details[1]+" "+balance); //update the linenumber with new data i.e., balance
					Files.write(database, lines, StandardCharsets.UTF_8);
					return "Amount debited! current balance: "+balance; //return success message
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "Invalid credentials"; //if no line number exists then account is not in database hence return error message
	}
	public String credit(String ID, String Password, double amount) throws RemoteException { 
		try {
		int lineNumber=login(ID,Password); //with received ID and password search for acount in database
		if(lineNumber!=-1) { //if account exists then read in which line number the account info exists in database
				List<String> lines = Files.readAllLines(database, StandardCharsets.UTF_8);
				String details[]=lines.get(lineNumber).split(" "); //read the deatils of account in array of string
				double balance=Double.parseDouble(details[2]);
					balance=balance+amount;
					lines.set(lineNumber,details[0]+" "+details[1]+" "+balance); //update the database with balance
					Files.write(database, lines, StandardCharsets.UTF_8);
					return "Amount credited! current balance: "+balance; //return success message
		}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return "Invalid credentials";
	}
	public boolean isAccountThere(String ID) throws RemoteException{
		try {
			InputStream in=Files.newInputStream(database,READ);
			BufferedReader reader=new BufferedReader(new InputStreamReader(in));
			String s=null;
			while((s=reader.readLine())!=null) {
				String str[]=s.split(" ");
				if(str[0].equals(ID)) { //str[0] will contain the account ID so matching this will conform the account
					return true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	public int login(String ID, String Password) throws RemoteException {
		try {
			InputStream in=Files.newInputStream(database,READ);
			BufferedReader reader=new BufferedReader(new InputStreamReader(in));
			String s=null;
			int count=-1; //initially line number -1 indicates no account with given info exists in database
			while((s=reader.readLine())!=null) {
				count++;
				String str[]=s.split(" ");
				if(str[0].equals(ID) && str[1].equals(Password)) { //str[0] contains ID and str[1] contains password if they match then return the line number 
					return count;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1; //if not match found then return -1 as indicator account doesnot exists
	}
}
