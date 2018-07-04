package clientRMI;
import serverRMI.ServicesInterface;
import java.util.Properties;
import java.util.Scanner;
import javax.naming.Context;
import javax.naming.InitialContext;
public class Client {
	public static void main(String[] args) throws Exception{
		Properties prop = new Properties();
		prop.put("java.naming.factory.initial", "com.sun.jndi.cosnaming.CNCtxFactory");
		prop.put("java.naming.provider.url", "corbaloc:iiop:localhost:900/NameService");
		Context ctx = new InitialContext(prop);
		ServicesInterface obj=(ServicesInterface)ctx.lookup("hello");
		Scanner sc=new Scanner(System.in);
		int ch;
		String Password,ID;
		double amount;
		do {
			System.out.print("\n\n============\n1.Create account\n2.Debit\n3.Credit\n4.Exit\nEnter your choice: ");
			ch=sc.nextInt();
			sc.nextLine();
			switch(ch) {
				case 1:
				{
					System.out.print("\nEnter a bank id you want to use: ");
					ID=sc.nextLine();
					System.out.print("\nEnter a password: ");
					Password=sc.nextLine();
					if(obj.createAccount(ID, Password)) {
						System.out.print("\nAccount created successfully!");
					}
					else {
						System.out.print("\nAccount already exists!");
					}
				}
				break;
				case 2:
				{
					System.out.print("\nEnter your bank ID: ");
					ID=sc.nextLine();
					System.out.print("\nEnter your password: ");
					Password=sc.nextLine();
					System.out.print("\nEnter amount to be debited: ");
					amount=sc.nextDouble();
					System.out.print("\nFrom Server> "+obj.debit(ID, Password, amount));
				}
				break;
				case 3:
				{
					System.out.print("\nEnter your bank ID: ");
					ID=sc.nextLine();
					System.out.print("\nEnter your password: ");
					Password=sc.nextLine();
					System.out.print("\nEnter amount to be credited: ");
					amount=sc.nextDouble();
					System.out.print("\nFrom Server> "+obj.credit(ID, Password, amount));
				}
				break;
				case 4:
					System.out.print("\nClosing program......");
					break;
				default:
					System.out.print("\nInvalid Option! Try Again");
					break;
			}
		}while(ch!=4);
	}
}
