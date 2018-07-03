package clientRMI;
import serverRMI.ServicesInterface;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.*;
public class Client {
	public static void main(String[] args) throws Exception{
		Properties prop = new Properties();
		prop.put("java.naming.factory.initial", "com.sun.jndi.cosnaming.CNCtxFactory");		
		prop.put("java.naming.provider.url", "corbaloc:iiop:localhost:900/NameService");
		Context ctx = new InitialContext(prop);
		ServicesInterface obj = (ServicesInterface)ctx.lookup("hello");
		Scanner sc=new Scanner(System.in);
		String s;
		int ch;
		do {
			System.out.print("\n\n1. Select odd length words\n2.Check odd palindrome\n3.Exit\nEnter your choice: ");
			ch=sc.nextInt();
			sc.nextLine();
			switch(ch) {
				case 1:
				{
					System.out.print("\nEnter the sentence: ");
					s=sc.nextLine();
					System.out.print("\nFrom Server> "+obj.select_odd(s));
				}
				break;
				case 2:
				{
					System.out.print("\nEnter the word to check if it is odd palindrome or not: ");
					s=sc.nextLine();
					if(obj.check_palindrome(s)) {
						System.out.println("\n\tWord is odd palindrome!");
					}
					else {
						System.out.println("\n\tWord is not odd palindrome!");
					}
				}
				break;
				case 3:
					System.out.println("\nClosing program.....");
					break;
				default:
					System.out.println("\nNot a valid option!");
					break;
			}
		}while(ch!=3);
	}

}
