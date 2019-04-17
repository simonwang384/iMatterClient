package edu.nova.csis2101.imatter.communicator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import edu.nova.csis2101.imatter.IMessageReceiver;
import edu.nova.csis2101.imatter.imatterserver.IMatterServer;

public class Communicator {
	protected IMessageReceiver messageReceiver;
	protected String ip;
	protected final int iMatterClientPort;
	protected final int iMatterServerPort;

	public Communicator(IMessageReceiver messageReceiver) {
		iMatterClientPort = 7897;
		iMatterServerPort = 7896;
		this.messageReceiver = messageReceiver;
	}
	
	public Communicator(IMessageReceiver messageReceiver, String ip) {
		iMatterClientPort = 7897;
		iMatterServerPort = 7896;
		this.ip = ip; 
		this.messageReceiver = messageReceiver;
	}
	
	public int getClientPort(){
		return iMatterClientPort;
	}
	public int getServerPort(){
		return iMatterServerPort;
	}
	
	public IMessageReceiver getMessageReceiver() {
		return messageReceiver;
	}


	public void send(String message) {
		Socket s = null;
		try {
			s = new Socket(ip, iMatterServerPort);
			DataOutputStream out = new DataOutputStream(s.getOutputStream());
			out.writeUTF(message);
		} catch (UnknownHostException e) {
			System.out.println("Socket:" + e.getMessage());
		} catch (EOFException e) {
			
		} catch (IOException e) {
			System.out.println("readline:" + e.getMessage());
		} finally {
			if (s != null)
				try {
					s.close();
				} catch (IOException e) {
					System.out.println("close:" + e.getMessage());
				}
		}
	}

	public void send(String message, String ip, int port) {
		Socket s = null;
		try {
			s = new Socket(ip, port);
			DataOutputStream out = new DataOutputStream(s.getOutputStream());
			out.writeUTF(message);
		} catch (UnknownHostException e) {
			System.out.println("Socket:" + e.getMessage());
		} catch (EOFException e) {
			
		} catch (IOException e) {
			System.out.println("readline:" + e.getMessage());
		} finally {
			if (s != null)
				try {
					s.close();
				} catch (IOException e) {
					System.out.println("close:" + e.getMessage());
				}
		}
	}

	public void print() {
		System.out.println("IP Address="+ip+"; client port="+iMatterClientPort+"; server port="+iMatterServerPort);
	}

	protected void connection(Socket clientSocket, ServerSocket listenSocket) throws IOException{
		while (true) {
			clientSocket = listenSocket.accept();
			Connection c = new Connection(clientSocket, this);
		}
	}
	
	public void launchListener(int port) {
		Socket clientSocket=null;
		ServerSocket listenSocket = null;
		try {
			listenSocket = new ServerSocket(port);
			System.out.println("Listener is running ..");
			connection(clientSocket,listenSocket);		
		} catch (IOException e) {
			System.out.println("Listen socket:" + e.getMessage());
		}finally{
			if(clientSocket!=null)
				try{
					clientSocket.close();
					}
			catch(IOException e){
				System.out.println("close:" + e.getMessage());
			}	
			if(listenSocket!=null)
				try{
					listenSocket.close();
					}
			catch(IOException e){
				System.out.println("close:" + e.getMessage());
			}				
		}
	}

	public static void test() {
		Communicator server = new Communicator(new IMatterServer());
		server.print();
	}

	public static void main(String args[]) {
		Communicator.test();
	}
}

class Connection extends Thread {
	private DataInputStream in;
	private DataOutputStream out;
	private Socket clientSocket;
	public Communicator server;

	public Connection(Socket aClientSocket, Communicator server) {
		try {
			this.server = server;
			clientSocket = aClientSocket;
			in = new DataInputStream(clientSocket.getInputStream());
			out = new DataOutputStream(clientSocket.getOutputStream());
			this.start();
		} catch (IOException e) {
			System.out.println("Connection:" + e.getMessage());
		}
	}

	public void run() {
		try { 
			String clientIp = clientSocket.getInetAddress().toString().substring(1);
				String message = in.readUTF(); 
				
				server.getMessageReceiver().receivedMessage(message,clientIp);
		} catch (EOFException e) {
			System.out.println("EOF:" + e.getMessage());
		} catch (IOException e) {
			System.out.println("readline:" + e.getMessage());
		} finally {
			try {
				clientSocket.close();
			} catch (IOException e) {
				
			}
		}

	}
}
