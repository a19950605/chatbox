import java.net.*;
import java.io.*;
import java.util.*;


public class server {
	boolean started  = false;
	ServerSocket ss  = null;
	List<Client> clients = new ArrayList<Client>();
	public static void main(String[] args) {
      new server().start();


	}

	
	public void start() {
		try {
			 ss = new ServerSocket(8888);
			 started=true;

		}catch(BindException e) {
			System.out.println("socket is running");
			System.out.print("Please close the related application");
			System.exit(0);
		}catch(IOException e) {
			e.printStackTrace();
			
		}
		
		try {

			while (started) {
				boolean bConnected = false;
				Socket s = ss.accept();
				Client c=new Client(s);
				 
				System.out.println("a client connected");
				new Thread(c).start();
				clients.add(c);
				/*

				
				}*/
			//	dis.close();
			 }

		} catch (IOException e) {
			

			e.printStackTrace();
   
		}finally {
		try {
			ss.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
	
	class Client implements Runnable{
        private Socket s;
		private DataInputStream dis= null;
		private DataOutputStream dos=null;
		private boolean bConnected = false;
		public Client(Socket s) {
			this.s= s;
			try {
						dis = new DataInputStream(s.getInputStream());
				dos = new DataOutputStream(s.getOutputStream());
				bConnected =true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		public void send(String str)  {
			
				try {
					dos.writeUTF(str);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					clients.remove(this);
					System.out.println("he quit,i quit from list");
					e.printStackTrace();
				}
				

			
		}
		
		public void run() {
		Client c  =null;
         try {
			while(bConnected) {
			String str = dis.readUTF();
			System.out.println(str);
			for(int i=0;i<clients.size();i++) {
			    c = clients.get(i);
				c.send(str);
		System.out.println("a string sent!");
				
			}
			//v2
			/*
			for(Iterator<Client> it=clients.iterator();it.hasNext();) {
				Client c=it.next();
				c.send(str);
			}
			*/
			/*
			 * Iterator it = clients.iterator();
			 * while(it.hasNext()){
			 * Client c =it.next();
			 * c.send(str);
			 * }
			 * }
			 * */
			// TODO Auto-generated method stub
		    	}
			}catch (EOFException e) {
			     System.out.println("Client closed!");

		} catch (IOException e) {
			

			e.printStackTrace();
     System.out.println("Client closed!");
		}finally {
			try {  //if found some problems, the socket should be closed
				if(dis!=null)dis.close();
				if(dos!=null)dos.close();
			    if(s!=null) {
                   s.close();
                   s=null;
			    }
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			if(c != null)clients.remove(c);
		}
	 
		}
	}
}

