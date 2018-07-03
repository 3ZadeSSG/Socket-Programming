package serverRMI;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
public class Server {
	public static void main(String[] args) throws Exception {
		ServicesImplementation obj = new ServicesImplementation();
		Properties prop = new Properties();
		prop.put("java.naming.factory.initial", "com.sun.jndi.cosnaming.CNCtxFactory");
		prop.put("java.naming.provider.url", "corbaloc:iiop:localhost:900/NameService");
		Context ctx = new InitialContext(prop);
		ctx.rebind("hello", obj);
		System.out.println("Server ready...");
	}
}
