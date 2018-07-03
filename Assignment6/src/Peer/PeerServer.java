package Peer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JTextArea;
public class PeerServer extends Thread{
	private JTextArea messageTextArea;
	private int PORT;
	public PeerServer(JTextArea messageTextArea,int PORT) {
		this.messageTextArea=messageTextArea;
		this.PORT=PORT;
	}
	@Override
	public void run() {
		try {
			ServerSocket welcomeSocket=new ServerSocket(PORT);
			while(true) {
				Socket connectionSocket=welcomeSocket.accept();
				Scanner inFromClient=new Scanner(connectionSocket.getInputStream());
				messageTextArea.setText(messageTextArea.getText()+"\nReceived: "+inFromClient.nextLine());
				connectionSocket.close();
			}
		}
		catch(Exception e) {
			 System.out.println("Unable to allocate resouces!");
			 e.printStackTrace();
		}
	}
}
