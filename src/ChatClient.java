import java.awt.*;																				
import java.awt.event.*;
import java.io.*;
import java.net.*;

/*
 * list catch exception> set boolean to confirm quit or not
 * if quit no send >
 * 
 * */
public class ChatClient extends Frame {
	Socket s=null;
	DataOutputStream dos =null;
	DataInputStream  dis =null;	
	private boolean bConnect =false;
	TextField tfTxt = new TextField();
	TextArea taContent = new TextArea();
	Thread tRecv = new Thread(new RecvThread());

	public static void main(String[] args) {

		new ChatClient().launchFrame();

	}

	public void launchFrame() {
		setLocation(400, 300);
		this.setSize(300, 300);
		add(tfTxt, BorderLayout.SOUTH);
		add(taContent, BorderLayout.NORTH);
		pack();
		this.addWindowListener(new WindowAdapter() {
			//this used for exit version1
			@Override
			public void windowClosing(WindowEvent arg0) {
				disconnect();
				System.exit(0);
			}
			
			
			
		}); 
		tfTxt.addActionListener(new TfListener());
		setVisible(true);
		connect();		
		tRecv.start();
	}

	public void connect() {
		try {
		 s = new Socket("127.0.0.1", 8888);
		  dos =new DataOutputStream(s.getOutputStream());
		  dis =new DataInputStream (s.getInputStream());
		  

			 System.out.println("connected");
			 bConnect =true;
		} catch (UnknownHostException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

public void disconnect() {
	 try {
			dos.close();
			dis.close();
			s.close();		
	 }catch(SocketException e) {
		  System.out.println("quit,bye!");
		}catch(IOException e) {
	       e.printStackTrace();		
		}


}
	/*
	try {
		bConnect=false;
		
	
			tRecv.join();


	}catch(InterruptedException e) {
		e.printStackTrace();
	} finally {
		
 try {
		dos.close();
		dis.close();
		s.close();		
		
	}catch(IOException e) {
       e.printStackTrace();		
	}
 }
	
	*/

	private class TfListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String str = tfTxt.getText().trim();
		//	taContent.setText(str);
			tfTxt.setText("");
			try {
	
			  //  dos =new DataOutputStream(s.getOutputStream());
			    dos.writeUTF(str);
			    dos.flush();
			  //  dos.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}
    
	private class RecvThread implements Runnable{

		@Override
		public void run() {
			try {
         while(bConnect) {

        	 String	str = dis.readUTF();
	        	// System.out.println(str);
             taContent.setText(taContent.getText()+ str+"\n");
         }
         } catch(SocketException e) {
           System.out.println("quit bye");
         }catch(EOFException e) {
           System.out.println("QUIT BYE");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	 
         
		}
		
	}
	
}