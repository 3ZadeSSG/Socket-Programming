import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
public class Client {
	private static InetAddress host;
	private static final int PORT=5500;
	public static void main(String[] args) {
		try {
			host=InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			System.out.println("\nUnable to find host address!");
			e.printStackTrace();
		}
		sendMessage();
	}
	private static void sendMessage() {
		Socket connectionSocket=null;
		try {
			connectionSocket=new Socket(host,PORT);
			PrintWriter outToServer=new PrintWriter(connectionSocket.getOutputStream(),true);
			Scanner inFromServer=new Scanner(connectionSocket.getInputStream());
			Scanner inFromUser=new Scanner(System.in);
			String message,response;
			do {
				System.out.print("\nEnter the text(END in new line to mark as complete and QUIT to exit program: ");
				do {
					message=inFromUser.nextLine();
					outToServer.println(message);
				}while((!message.equals("END")) && (!message.equals("QUIT")));
				if(message.equals("END")) {
					response=inFromServer.nextLine();
					System.out.print("\nFROM SERVER> "+response);
				}
			}while(!message.equals("QUIT"));
		} catch (IOException e) {
			System.out.println("Unable to allocate port!");
			e.printStackTrace();
		}
		finally {
			try {
				connectionSocket.close();
			}
			catch(IOException e) {
				System.out.println("Unable to close connection!");
				e.printStackTrace();
			}
		}
	}
}
