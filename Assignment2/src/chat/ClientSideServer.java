package chat;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JTextArea;
public class ClientSideServer extends Thread {
	private Socket connectionSocket=null;
	private JTextArea messageTextArea;
	private Scanner inFromServer;
	public ClientSideServer(JTextArea messageTextArea,Socket connectionSocket) {
		this.messageTextArea=messageTextArea;
		this.connectionSocket=connectionSocket;
		try{
			inFromServer=new Scanner(connectionSocket.getInputStream());
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	/*
	 * Print any receiving message from server to the text area
	 * */
	public void run() {
	  while(true) {
		  String response=inFromServer.nextLine();
		  messageTextArea.setText(messageTextArea.getText().toString()+"\nReceived: "+response);
	  }
	}
}
