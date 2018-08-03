package chatServer;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class ChatRoomServer {
    private static ServerSocket welcomeSocket;
    private static int PORT=6500;
    private static List<ClientHandler>clientList=new ArrayList<ClientHandler>(); //this will hold all active clients connected
	public static void main(String[] args) {
		try {
			welcomeSocket=new ServerSocket(PORT); //accept any new client
		}
		catch(Exception e) {
			System.out.print("\nUnable to allocate port!");
			e.printStackTrace();
			System.exit(0);
		}
		while(true) {
			try {
				Socket connectionSocket=welcomeSocket.accept();
				ClientHandler myClient=new ClientHandler(connectionSocket);  
				clientList.add(myClient); //add new client into list
				myClient.start(); //start serving client
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	/*this function will broadcast the message received to all clients except the one which sent it
	 * */
	public static void broadCast(String message,Socket mySocket) { //message should not be broadcasted to mySocket client
		for(int i=0;i<clientList.size();i++) {
			if(!clientList.get(i).connectionSocket.isClosed()) {
				if(clientList.get(i).connectionSocket!=mySocket) { //if client is not same as the client which generated message then send message to that client
					clientList.get(i).outToClient.println(message);
				}
			}
			else {
				clientList.remove(i); //remove the disconnected client from the list of active clients
			}
		}
	}
}
class ClientHandler extends Thread{
	public Socket connectionSocket=null;
	public PrintWriter outToClient;
	private Scanner inFromClient;
	private String message=null;
	public ClientHandler(Socket connectionSocket) {
		this.connectionSocket=connectionSocket;
		try {
			outToClient=new PrintWriter(connectionSocket.getOutputStream(),true);
			inFromClient=new Scanner(connectionSocket.getInputStream());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void run() {
		try {
			do {
				message=inFromClient.nextLine();
				//add the description of sender's IP address and PORT along with the message
				String add=connectionSocket.getInetAddress().toString()+"| PORT "+String.valueOf((connectionSocket.getPort()));
				ChatRoomServer.broadCast(add+">>>"+message, connectionSocket);
			}while(!message.equals("***bye***"));
			//if client sent ***bye*** then notify others that client has disconnected! 
			ChatRoomServer.broadCast("\n"+connectionSocket.getInetAddress().toString()+"| PORT "+String.valueOf((connectionSocket.getPort()))+">>>Client Disconnected!", connectionSocket);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				connectionSocket.close();
				System.out.print("\nClient Disconnected!");
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}