package clientRMI;
import serverRMI.ServicesInterface;
import java.util.Properties;
import java.util.Scanner;
import javax.naming.Context;
import javax.naming.InitialContext;
public class Client {
	public static void main(String[] args) throws Exception {
		Properties prop = new Properties();
		prop.put("java.naming.factory.initial", "com.sun.jndi.cosnaming.CNCtxFactory");
		prop.put("java.naming.provider.url", "corbaloc:iiop:localhost:900/NameService");
		Context ctx = new InitialContext(prop);
		String password=null;
		Scanner sc=new Scanner(System.in);
		ServicesInterface obj=(ServicesInterface)ctx.lookup("hello");
		int ch;
		do {
			System.out.print("\n\n\n================\n1.Selection Sort\n2.Recursive Binary Search\n3. Exit\nEnter your choice: ");
			ch=sc.nextInt();
			sc.nextLine();
			if(ch==1) {
				System.out.print("\nEnter password to use this service: ");
				password=sc.nextLine();
				if(obj.isValid(password)) {
					int size;
					System.out.print("\nEnter size of array: ");
					size=sc.nextInt();
					double d[]=new double[size];
					System.out.print("\nEnter elements of array: ");
					for(int i=0;i<size;i++) {
						d[i]=sc.nextDouble();
					}
					d=obj.selectionSort(d, password);
					System.out.print("\nSorted array: ");
					for(int i=0;i<size;i++) {
						System.out.print(" "+d[i]);
					}
				}
				else {
					System.out.print("\nNot a valid password!");
				}
			}
			else if(ch==2) {
				int size;
				System.out.print("\nEnter size of array: ");
				size=sc.nextInt();
				double d[]=new double[size];
				System.out.print("\nEnter elements of array: ");
				for(int i=0;i<size;i++) {
					d[i]=sc.nextDouble();
				}
				System.out.print("\nEnter element to be searched: ");
				double KEY=sc.nextDouble();
				System.out.print("\nFrom Server: "+obj.recursiveBinarySearch(d, KEY, password));
			}
			else if(ch!=3) {
				System.out.print("\nNot a valid choice!");
			}
		}while(ch!=3);
		System.out.print("Closing program.....");
	}

}
