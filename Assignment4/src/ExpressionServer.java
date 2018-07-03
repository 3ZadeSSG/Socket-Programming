import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Stack;
public class ExpressionServer{
	private static ServerSocket welcomeSocket;
	private static final int PORT=5500;
	public static void main(String [] args) throws IOException{
		try{
			welcomeSocket=new ServerSocket(PORT);
		}
		catch(IOException e){
			System.out.println("\nUnable to allocate port!");
			System.exit(0);
		}
		do{
			Socket client=welcomeSocket.accept();
			System.out.println("\nNew client accepted!");
			ClientHandler handler=new ClientHandler(client);
			handler.start();
		}while(true);
	}
}

class ClientHandler extends Thread{
	private Socket client;
	private Scanner inToServer;
	private PrintWriter outFromServer;
	private Mathematics exp=new Mathematics();
	
	public ClientHandler(Socket socket){
		client=socket;
		try{
			inToServer=new Scanner(client.getInputStream()); 
			outFromServer=new PrintWriter(client.getOutputStream(),true); 
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	public void run(){
		String message;
		do{
			message=inToServer.nextLine(); //read the incoming expression from client
			if(!message.equals("QUIT")){ //if input expression was not "QUIT" then evaluate expression
				outFromServer.println("Answer: "+exp.evaluate(message.replaceAll("\\s",""))); //send expression string to evaluate after removing white spaces
			}
		}while(!message.equals("QUIT")); //continue till request for closing connection doesn't arrives
		try{
			System.out.println("\nClosing the client connection!");
			client.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
class Mathematics {
	private Stack<Character>operator=new Stack<Character>(); 
	private Stack<Double>operand=new Stack<Double>();
	public String evaluate(String s) {
		double ans=0;
		int index=0;
		char exp[]=s.replaceAll("sqrt", "r").toCharArray();
		try{
			while(index<exp.length) { //scan each element of character array till end is reached
				if(!isOperand(exp[index])) { //if current element is an operator
					if(exp[index]=='(') {    //if '(' is current element then push it to operator stack
						operator.push('(');
						index++;
					}
					else if(exp[index]==')') {  //if ')' is current element then pop the operator stack and perform arithmetic operation till '(' is encountered
						while(operator.peek()!='(') {
						ans=operand.peek();  //read the top most element of operand 
						operand.pop();  
						ans=perform(operand.peek(),operator.peek(),ans); 
						operand.pop();
						operand.push(ans); //push current number after performing the arithmetic operation
						operator.pop(); 
						} 
						operator.pop(); //pop the top operator of stack which is '('
						index++;
					}
					else if(operator.empty()||operator.peek()=='(') { //f there's no other operator in stack or '(' then no need to check precedence
						operator.push(exp[index]); 
						index++;
					}
					else if(precedence(exp[index])<=precedence(operator.peek())) { //if operator at stack top has lower precedence than incoming one then perform arithmetic operation with top 2 operands
						ans=operand.peek();
						operand.pop();
						ans=perform(operand.peek(),operator.peek(),ans);
						operand.pop();
						operand.push(ans);
						operator.pop();
					}
					else if(precedence(exp[index])>precedence(operator.peek())) { //if incoming operator has higher precedence then push it into stack
						operator.push(exp[index]);
						index++;
					}
				}
				else {
					String str="";
					while(index<exp.length && isOperand(exp[index])) {
						str=str+exp[index++];
					}
					operand.push(Double.parseDouble(str));
				}
			}
			while(!operator.empty()) {
				ans=operand.peek();
				operand.pop();
				ans=perform(operand.peek(),operator.peek(),ans);
				operand.pop();
				operand.push(ans);
				operator.pop();
			}
		    ans=operand.peek();
		}
		catch(Exception e){
			return "Invalid expression!";
		}
		return Double.toString(ans);
	}
	private boolean isOperand(char c) {
		if(c>='0' && c<='9') 
			return true;
		else
			return false;
	}
	private double perform(double d1,char c,double d2) {
		double ans=0;
		if(c=='+') {
			ans=d1+d2;
		}
		else if(c=='-') {
			ans=d1-d2;
		}
		else if(c=='*') {
			ans=d1*d2;
		}
		else if(c=='/') {
			ans=d1/d2;
		}
		else if(c=='^'){
			ans=Math.pow(d1,d2);
		}
		else if(c=='r') {
			ans=Math.sqrt(d2);
			operand.push(d1);
		}
		return ans;
	}
	private int precedence(char c) {
		if(c=='+'||c=='-')
			return 1;
		else if(c=='*'||c=='/')
			return 2;
		else
			return 3;
	}
}