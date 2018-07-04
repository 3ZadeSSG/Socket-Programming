package serverRMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServicesInterface extends Remote{
	public boolean createAccount(String ID,String Password)throws RemoteException;
	public String debit(String ID,String Password,double amount)throws RemoteException;
	public String credit(String ID,String Password,double amount)throws RemoteException;
	public boolean isAccountThere(String ID) throws RemoteException;
	public int login(String ID,String Password)throws RemoteException;
}
