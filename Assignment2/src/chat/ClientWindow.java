package chat;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
public class ClientWindow extends JFrame implements ActionListener{
    private JTextField messageTextView;
    private JTextField ipTextView;
    private JTextField portTextView;
    private JButton sendMessageButton;
    private JButton setTargetButton;
    private static JTextArea messageTextArea;
    private String message=null;
    private static int serverPORT=0;
    private Socket connectionSocket=null;
    private String ip=null;
    private PrintWriter outToServer;
    private ClientSideServer myServer;
    public ClientWindow() {
    	setTitle("Chat Window!");
    	setSize(500,500);
    	setDefaultCloseOperation(EXIT_ON_CLOSE);
    	initializeComponents();
    }
    
    public void initializeComponents() {
    	messageTextView=new JTextField(20); //input text field for message
    	ipTextView=new JTextField(20); //input text field for ip address
    	portTextView=new JTextField(20); //input text field for port
    	messageTextArea=new JTextArea();
    	messageTextArea.setEditable(false); //no text will be allowed to be selected from message view area
    	JPanel togglesPanel=new JPanel(); //first panel will be at the top with ip address and socket and a single button
    	togglesPanel.add(new JLabel("IP Address of Server "));
    	togglesPanel.add(ipTextView);
    	togglesPanel.add(new JLabel("PORT of Server  "));
    	togglesPanel.add(portTextView);
    	setTargetButton=new JButton("SET IP and PORT");
    	setTargetButton.addActionListener(this);
    	togglesPanel.add(setTargetButton);
    	this.add(togglesPanel,BorderLayout.PAGE_START); //first panel at the top of the page
    	JPanel messagePanel=new JPanel(); //second panel to contain message input text view and send button
    	messagePanel.add(new JLabel("Enter message "));
    	messagePanel.add(messageTextView);
    	sendMessageButton=new JButton("Send Message");
    	sendMessageButton.addActionListener(this);
    	messagePanel.add(sendMessageButton);
        this.add(messagePanel,BorderLayout.SOUTH); //second panel will be added to bottom
    	add(messageTextArea); //add the message view are 
    }
    
	public static void main(String[] args) {
      ClientWindow c=new ClientWindow();  //create a new client window for chat
      c.setVisible(true);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent event) {
	   	if(event.getSource()==setTargetButton) { //if action is set target then create the connnectionSocket and start the receiving thread
	   		try {
	   			ip=ipTextView.getText();
	   			serverPORT=Integer.parseInt(portTextView.getText());
	   			if(ip!=null && serverPORT!=0) {
	   				connectionSocket=new Socket(ip,serverPORT);
	   				outToServer=new PrintWriter(connectionSocket.getOutputStream(),true);
	   				myServer=new ClientSideServer(messageTextArea,connectionSocket);
	   				myServer.start();
	   			}
	   		}
	   		catch(Exception e) {
	   			
	   		}
	   	}
	   	else if(event.getSource()==sendMessageButton && ip!=null && serverPORT!=0) { //if sendMessage is pressed and ip and serverPORT is assigned properly then execute the sending process
	   		try {
	   			message=messageTextView.getText();
	   			outToServer.println(message);
	   			messageTextArea.setText(messageTextArea.getText()+"\nSENT: "+message);
	   			messageTextView.setText(null);
	   		}
	   		catch(Exception e){
	   			e.printStackTrace();
	   		}
	   		finally {
	   			if(message.equals("***bye***")) { //if message is ***bye*** then close the socket connection
	   				try {
	   				 connectionSocket.close();
	   				 myServer.stop();
	   				}
	   				catch(Exception e) {
	   					e.printStackTrace();
	   				}
	   			}
	   		}
	   	}
	}
}
