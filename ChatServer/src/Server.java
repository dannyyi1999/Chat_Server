import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;

public class Server {

	static Vector<ClientHandler> ar = new Vector<>(); 
	
	public static void main(String[] args) throws IOException{
		
		ServerSocket ss = new ServerSocket(5004); 
		
		Socket s = null;
		
		Scanner in = new Scanner(System.in);
		
		
		while(true) {
			
			try {
				System.out.println("Waiting for a connection...");
				s = ss.accept();
				System.out.println("new client recieved");
				
				DataInputStream dis = new DataInputStream(s.getInputStream()); 
	            DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
	            
	            System.out.println(" connected! ");
	            
	            ClientHandler match = new ClientHandler(s, "Joe", dis, dos);
	            Thread t = new Thread(match);
	            ar.add(match);
	            t.start();
			}catch (Exception e) {
				s.close();
			}
            
            
		}
		
		
	}

}


class ClientHandler implements Runnable{

	public String name;
	public Socket s;
	public DataInputStream dis;
	public DataOutputStream dos;
	
	public ClientHandler(Socket s, String name, DataInputStream dis, DataOutputStream dos) {
		this.s = s;
		this.name = name;
		this.dis = dis;
		this.dos = dos;
		
	}
	
	@Override
	public void run() {
		String receive;
		while(true) {
			try {
				receive = dis.readUTF();
				
				for(ClientHandler mc: Server.ar) {
					mc.dos.writeUTF(receive);
				}
				
			}catch (IOException e){
				break;
			}
		}
		
		try
        { 
            // closing resources 
            this.dis.close(); 
            this.dos.close(); 
            this.s.close();
              
        }catch(IOException e){ 
            e.printStackTrace(); 
        } 
	}
	
}







