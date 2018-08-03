import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Client {
 //private static String fileAddress;
 private static InetAddress host;
 private static int PORT=5500;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			host=InetAddress.getLocalHost();
		}
		catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		sendMessage();
	}
 public static void sendMessage() {
	 Socket connectionSocket=null;
	 try {
		 //connectionSocket=new Socket(host,PORT);
		 connectionSocket=new Socket(host,PORT);
		 String message=null;
		 String fileAddress;
		 Scanner inFromUser=new Scanner(System.in);
		 PrintWriter outToServer=new PrintWriter(connectionSocket.getOutputStream(),true);
		 do {
			 System.out.print("\nEnter the file locaiton: ");
			 fileAddress=inFromUser.nextLine();
			 Path database=Paths.get(fileAddress);
			 List<String>student=Files.readAllLines(database, StandardCharsets.UTF_8);
			 for(int i=0;i<student.size();i++) {
				 outToServer.println(student.get(i));
			 }
			 System.out.print("\nWant to add more ?: ");
			 message=inFromUser.nextLine();
		 }while(!message.equalsIgnoreCase("NO"));
		  outToServer.println(message.toUpperCase());
	 }
	 catch(Exception e) {
		 e.printStackTrace();
	 }
	 finally {
		 try {
			 connectionSocket.close();
		 }
		 catch(Exception e) {
			 e.printStackTrace();
		 }
	 }
 }
}
