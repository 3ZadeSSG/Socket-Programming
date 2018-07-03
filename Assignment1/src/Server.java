import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Server {
	private static ServerSocket welcomeSocket;
	private static final int PORT=5500;
	public static void main(String[] args) {
		try {
			welcomeSocket=new ServerSocket(PORT);
		} catch (IOException e) {
			System.out.println("Unable to allocate port!");
			e.printStackTrace();
		}
		while(true) {
			try {
				Socket connectionSocket=welcomeSocket.accept();
				ClientHandler client=new ClientHandler(connectionSocket);
				client.start();
			} catch (IOException e) {
				System.out.println("Unable to accept clinet!");
				e.printStackTrace();
			}	
		}
	}
}
class ClientHandler extends Thread{
	private Socket connectionSocket; 
	PrintWriter outFromServer;
	Scanner inFromClient;
	public ClientHandler(Socket connectionSocket){
		this.connectionSocket=connectionSocket;
		try {
			outFromServer=new PrintWriter(connectionSocket.getOutputStream(),true);
			inFromClient=new Scanner(connectionSocket.getInputStream());
		} catch (IOException e) {
			System.out.println("\nUnable to allocate resources!");
			e.printStackTrace();
		}
	}
	public void run() {
		String message;
		do {
			StringBuilder msg=new StringBuilder();
			do {
				message=inFromClient.nextLine();
				if(!message.equals("END")) {
					msg.append("\n"+message);
				}
			}while((!message.equals("END"))&&(!message.equals("QUIT")));
		outFromServer.println("ECHO: "+Clean(msg.toString()));
		}while(!message.equals("QUIT"));
		try{
			System.out.println("\nClosing the client!");
			connectionSocket.close();
		}
		catch(IOException e){
			System.out.println("Unable to close the connection!");
		}
	}
	private String Clean(String s) {
		String REGEX="((http:\\/\\/|https:\\/\\/)?(www.)?(([a-zA-Z0-9-]){2,}\\.){1,4}([a-zA-Z]){2,6}(\\/([a-zA-Z-_\\/\\.0-9#:?=&;,]*)?)?)";
		Pattern p=Pattern.compile(REGEX);
		Matcher m=p.matcher(s);
		int count=0;
		while(m.find()) {
			count++;
		}
		System.out.println("\nUrls Found : "+count); //print the count of hyperlinks at server side.
		s=m.replaceAll(" "); //replace the pattern of hyperlink with spaces
		s=s.replaceAll("\n"," ");
		s=s.replaceAll("\\p{Punct}|\n|\\d"," "); //remove all punctuation
		s = s.replaceAll("( )+", " "); //remove extra space
		s =s.replaceAll("(\\w)\\1\\1+","$1$1");
		return s;
	}
}
