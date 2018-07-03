package serverRMI;
import java.rmi.*;
import java.rmi.RemoteException;
public interface ServicesInterface extends Remote {
	public String select_odd(String s) throws RemoteException;
	public boolean check_palindrome(String s)throws RemoteException;
}
