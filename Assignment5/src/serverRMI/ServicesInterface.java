package serverRMI;
import java.rmi.*;
import java.rmi.RemoteException;
public interface ServicesInterface extends Remote {
	public boolean isValid(String password) throws RemoteException;
	public double[] selectionSort(double[] arr,String password )throws RemoteException;
	public String recursiveBinarySearch(double[] arr,double KEY,String password)throws RemoteException;
	boolean isSorted(double []arr) throws RemoteException;
	String search(double[]arr,int l,int u,double KEY)throws RemoteException;
}
