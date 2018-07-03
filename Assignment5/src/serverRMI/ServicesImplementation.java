package serverRMI;
import java.rmi.RemoteException;
import javax.rmi.PortableRemoteObject;
public class ServicesImplementation extends PortableRemoteObject implements ServicesInterface{
	String validPassword="window10";
	protected ServicesImplementation() throws RemoteException {
		super();
	}
	public boolean isValid(String password) throws RemoteException {
		if(password.equals(validPassword))
			return true;
		else
			return false;
	}
	public double[] selectionSort(double[] arr, String password) throws RemoteException {
		for(int i=0;i<arr.length;i++) {
			int min=i;
			double temp;
			for(int j=i+1;j<arr.length;j++) {
				if(arr[j]<arr[min]) {
					min=j;
				}
			}
			temp=arr[min];
			arr[min]=arr[i];
			arr[i]=temp;
		}
		return arr;
	}
	public String recursiveBinarySearch(double[] arr,double KEY, String password)throws RemoteException {
		if(!isSorted(arr)) {
			return "Array is not sorted!";
		}
		return search(arr,0,arr.length,KEY); //call recursive method and return result
	}
	public boolean isSorted(double[] arr)throws RemoteException {
		for(int i=0;i<arr.length-1;i++) {
			if(arr[i]>arr[i+1])
				return false;
		}
		return true;
	}
	public String search(double[] arr, int l, int u, double KEY)throws RemoteException {
		if (u >= l)
		   {
		        int mid = l + (u - l)/2;
		        if (arr[mid] == KEY)
		        	return "Element found at "+Integer.toString(mid+1);
		        if (arr[mid] > KEY) 
		        	return search(arr, l, mid-1, KEY);
		        return search(arr, mid+1, u, KEY);
		   }
		   return "Element not found!";
	}
}
