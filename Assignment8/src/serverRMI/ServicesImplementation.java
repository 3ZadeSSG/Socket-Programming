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
	private Path database=Paths.get("F:\\Banking\\bank.txt");
	protected ServicesImplementation() throws RemoteException {
		super();
		if(!Files.exists(database)) {
			try {
				Files.createFile(database);
			} catch (IOException e) {
				System.out.println("\nUnable to creat bank database!");
				e.printStackTrace();
			}
		}
	}
	public boolean createAccount(String ID, String Password) throws RemoteException {
		if(isAccountThere(ID)) {
			return false;
		}
		else {
			try(OutputStream out=new BufferedOutputStream(Files.newOutputStream(database,WRITE,APPEND))){
				String s=ID+" "+Password+" 1000"+System.lineSeparator();
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
		int lineNumber=login(ID,Password);
		if(lineNumber!=-1) {
			try {
				List<String> lines = Files.readAllLines(database, StandardCharsets.UTF_8);
				String details[]=lines.get(lineNumber).split(" ");
				double balance=Double.parseDouble(details[2]);
				if(balance<amount) {
					return "Balance is low!";
				}
				else {
					balance=balance-amount;
					lines.set(lineNumber,details[0]+" "+details[1]+" "+balance);
					Files.write(database, lines, StandardCharsets.UTF_8);
					return "Amount debited! current balance: "+balance;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "Invalid credentials";
	}
	public String credit(String ID, String Password, double amount) throws RemoteException { 
		try {
		int lineNumber=login(ID,Password);
		if(lineNumber!=-1) {
				List<String> lines = Files.readAllLines(database, StandardCharsets.UTF_8);
				String details[]=lines.get(lineNumber).split(" ");
				double balance=Double.parseDouble(details[2]);
					balance=balance+amount;
					lines.set(lineNumber,details[0]+" "+details[1]+" "+balance);
					Files.write(database, lines, StandardCharsets.UTF_8);
					return "Amount credited! current balance: "+balance;
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
				if(str[0].equals(ID)) {
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
			int count=-1;
			while((s=reader.readLine())!=null) {
				count++;
				String str[]=s.split(" ");
				if(str[0].equals(ID) && str[1].equals(Password)) {
					return count;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}

}
