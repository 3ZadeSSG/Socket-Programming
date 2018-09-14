package serverRMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServicesInterface extends Remote{
	public boolean createAccount(String ID,String Password)throws RemoteException; //True if account creation was successful else false
	public String debit(String ID,String Password,double amount)throws RemoteException; //Return message with update balance in case of successful transaction else message indicating issue
	public String credit(String ID,String Password,double amount)throws RemoteException; 
	public boolean isAccountThere(String ID) throws RemoteException; //Checking if account with given ID is already in database
	public int login(String ID,String Password)throws RemoteException; //returns the line number in which the account information exists in the database else -1 to indicate that account is not in database
}
