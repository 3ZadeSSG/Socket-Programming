package Peer;
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
public class ClientWindow extends JFrame implements ActionListener {
	private JTextField messageTextView;
	private JTextField ipTextView;
	private JTextField portTextView;
	private static JTextArea messageTextArea;
	private JButton setSocket;
	private JButton sendButton;
	private String message;
	private String ip=null;
	private static int port=5500;
	public ClientWindow() {
		setTitle("Chat Window");
		setSize(500,500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		initializeComponents();
	}
	private void initializeComponents() {
		messageTextView=new JTextField(20);
		ipTextView=new JTextField(20);
		portTextView=new JTextField(20);
		messageTextArea=new JTextArea();
		messageTextArea.setEditable(false); //user won't be able to select text of chat window
		JPanel panel=new JPanel();
		panel.add(new JLabel("IP Address of server"));
		panel.add(ipTextView);
		panel.add(new JLabel("PORT of TargetServer"));
		panel.add(portTextView);
		setSocket=new JButton("SET SOCKET");
		setSocket.addActionListener(this);
		panel.add(setSocket);
		this.add(panel,BorderLayout.PAGE_START);
		JPanel panel2=new JPanel();
		panel2.add(new JLabel("Message"));
		panel2.add(messageTextView);
		sendButton=new JButton("SEND MESSAGE");
		sendButton.addActionListener(this);
		panel2.add(sendButton);
		this.add(panel2,BorderLayout.SOUTH);
		add(messageTextArea);
	}
	public static void main(String[] args) {
		ClientWindow c=new ClientWindow();
		c.setVisible(true);
		PeerServer p=new PeerServer(messageTextArea,port);
		p.start();
	}
	@Override
	public void actionPerformed(ActionEvent action) {
		if(action.getSource()==setSocket) {
			port=Integer.parseInt(portTextView.getText());
			ip=ipTextView.getText();
		}
		else if(action.getSource()==sendButton) {
			if(port!=0 && ip!=null) {
				try {
					Socket connectionSocket=new Socket(ip,port);
					PrintWriter outToServer=new PrintWriter(connectionSocket.getOutputStream(),true);
					message=messageTextView.getText();
					outToServer.println(message);
					messageTextArea.setText(messageTextArea.getText()+"\nSENT: "+message);
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
