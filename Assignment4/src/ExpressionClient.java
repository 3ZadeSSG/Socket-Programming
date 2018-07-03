import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
public class ExpressionClient{
	private static InetAddress host;
	private static int PORT=5500;
	public static void main(String[] args){
			try{
				host=InetAddress.getLocalHost();
			}
			catch(Exception e){
				System.out.println("Unable to resolve hsot!");
				e.printStackTrace();
				System.exit(0);
			}
			accessServer();
	}
	public static void accessServer(){
		Socket connectionSocket=null;
		try{
			connectionSocket=new Socket(host,PORT);  
			String message,response;
			PrintWriter outToServer=new PrintWriter(connectionSocket.getOutputStream(),true);
			Scanner inFromUser=new Scanner(System.in);
			Scanner outFromServer=new Scanner(connectionSocket.getInputStream());
			do{
				System.out.print("\nEnter the expression (Type QUIT to end program) : "); 
				message=inFromUser.nextLine();
				outToServer.println(message); //send expression to server
				if(!message.equals("QUIT")) { //if "QUIT" was not typed then fetch response from server display
					System.out.println("\nSERVER:>> "+outFromServer.nextLine());
				}
			}while(!message.equals("QUIT")); //do till "QUIT" is not typed
		}
		catch(Exception e){
			System.out.println("\nUnable to connect to server!");
			e.printStackTrace();
		}
		finally{
			try{
				connectionSocket.close(); 
				System.out.print("Connection Closed!");
			}
			catch(Exception e){
				System.out.println("\nUnable to close the conneciton!");
				e.printStackTrace();
				System.exit(0);
			}
		}
	}
}