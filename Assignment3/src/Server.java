/*
 * Implement a basic Java application that employs a multi-threaded server.
 * Both server and client need to be implemented. The description of server and client is as follows:
a) Each client use a text editor or word processor,
 create a text file containing Name, marks of four subjects and send it to the server.
b) When all records have been written by different clients,
 server creates a file named “record.dat”, calculate percentage of marks for each client separately 
 and store all records alphabetically (each record on separate line) by name in that file.
  */
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import static java.nio.file.StandardOpenOption.WRITE;
import static java.nio.file.StandardOpenOption.APPEND;
class Record{
	private Path database=Paths.get("F:\\record.dat"); 
	private List<String>students=new ArrayList<String>(); //List will be used to store the student data before actual write to database
	private int subjects=4; 
	public Record() {
		if(!Files.exists(database)) {
			try {
			Files.createFile(database); //if file is not already there then create one
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	//function to add new record to student list
	public void addNew(String name,double marks[]) {
	 double percentage=0;
	 for(int i=0;i<subjects;i++) {
		 percentage+=marks[i];
	 }
	 percentage=percentage/subjects;
	 students.add(name+" "+percentage);
	}
	/*function will first sort the list of students according to their name
	 * and then store record into database
	 * */
	public void FinalWrite() {
		Collections.sort(students, String.CASE_INSENSITIVE_ORDER); //sort names in case insensetive order
		try(OutputStream out=new BufferedOutputStream(Files.newOutputStream(database,WRITE,APPEND))){
			for(int i=0;i<students.size();i++) { //for each student perform write 
				byte[]data=new byte[1000];
				data=(students.get(i)+"%"+"\n").getBytes();
				out.write(data, 0, data.length);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
public class Server {
	public static Record record; //static because the Client Handler needs to invoke this object to access the addNew function
	private static final int PORT=5500;
    private static ServerSocket welcomeSocket;
	public static int MAX_count=8;   //maximum number of records that can be written to server database
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		record=new Record();
        try {
        	welcomeSocket=new ServerSocket(PORT);
        }
        catch(Exception e) {
        	e.printStackTrace();
        	System.exit(0);
        }
        System.out.println("Server started!");
        while(true) {
        	try {
	        	Socket connectionSocket=welcomeSocket.accept();
	        	ClientHandler client=new ClientHandler(connectionSocket);
	        	client.start();
        	}
        	catch(Exception e) {
        		System.out.print("\nUnable to accept new client!");
        		e.printStackTrace();
        	}
        }
	}
	//after receiving each individual record from client MAX_count will checked if it had reached 0
	public static void triggerUpdateLog() {
		if(MAX_count==0) {
      		 record.FinalWrite(); //if max count=0 then final write and stop receiving requests
      		 try {
				welcomeSocket.close();
				System.out.println("\nServer had written the required data into file and have stopped serving requests!");
				System.exit(0);
			} catch (IOException e) {
				e.printStackTrace();
			}
      		 System.exit(0);
      	  }
	}
}
class ClientHandler extends Thread{
	private Socket connectionSocket;
	private Scanner inFromClient;
	public ClientHandler(Socket connectionSocket) {
		try {
		this.connectionSocket=connectionSocket;
		inFromClient=new Scanner(connectionSocket.getInputStream());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void run() {
		String message;
		do {
		 message=inFromClient.nextLine();
		 if(!message.equals("NO") && Server.MAX_count>0) {
			 Server.MAX_count--;  //update the global count since one individual record has been received
			 String data[]=message.split(" "); //store name and marks of 4 subjects in a string array
			 double marks[]=new double[data.length-1];
			 for(int i=1;i<data.length;i++) {
				 marks[i-1]=Double.parseDouble(data[i]); //convert string into double to calculate percentage
			 }
			 Server.record.addNew(data[0], marks); //send name and marks array to addNew function
			 Server.triggerUpdateLog(); //trigger the update function to check if server needs to write the data finally and close its welcome socket
		 }
		}while(!message.equals("NO") && Server.MAX_count>0);
		try {
			connectionSocket.close(); //if max count limit reached or "NO" received then stop
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}	
}
