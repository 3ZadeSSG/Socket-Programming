package serverRMI;
import java.rmi.RemoteException;
import javax.rmi.PortableRemoteObject;
public class ServicesImplementation extends PortableRemoteObject implements ServicesInterface{
	protected ServicesImplementation() throws RemoteException {
		super();
	}
	public String select_odd(String s) throws RemoteException {
		String arr[]=s.split(" ");
		StringBuilder str=new StringBuilder();
		int count=0;
		for(int i=0;i<arr.length;i++) {
			if(arr[i].length()%2!=0) {
				str.append("\n\t"+(++count)+". "+arr[i]);
			}
		}
		return str.toString();
	}
	public boolean check_palindrome(String s) throws RemoteException {
		if(s.length()%2==0) {
			return false;
		}
		String reverse = new StringBuilder(new String(s)).reverse().toString();
		if(reverse.equals(s))
			return true;
		else
			return false;
	}
}