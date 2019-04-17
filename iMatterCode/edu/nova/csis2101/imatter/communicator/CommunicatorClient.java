package edu.nova.csis2101.imatter.communicator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import edu.nova.csis2101.imatter.IMessageReceiver;



public class CommunicatorClient extends Communicator {

	public CommunicatorClient(IMessageReceiver messageReceiver) {
		super(messageReceiver);
		
	}
	
	public CommunicatorClient(IMessageReceiver messageReceiver, String ip) {
		super(messageReceiver,ip);
	}
	
	protected void connection(Socket clientSocket, ServerSocket listenSocket) throws IOException{
		
			clientSocket = listenSocket.accept();
			Connection c = new Connection(clientSocket, this);
		
	}

}
