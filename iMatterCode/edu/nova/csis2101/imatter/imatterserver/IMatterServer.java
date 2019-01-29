package edu.nova.csis2101.imatter.imatterserver;

import edu.nova.csis2101.imatter.IMessageReceiver;
import edu.nova.csis2101.imatter.ballot.*; 
import edu.nova.csis2101.imatter.results.*; 
import edu.nova.csis2101.imatter.communicator.*; 

import java.io.FileNotFoundException; 
import java.util.*;

public class IMatterServer implements IMessageReceiver 
{
	private ArrayList<String> clients;	
	private String currentIP="";			
	private Ballot ballot;				
	private Results results;			
	
	
	private Communicator communicator;  
	
	
	public IMatterServer(){
		clients = new ArrayList<String>();
	}
	
	private void process(String filename) throws FileNotFoundException
	{
		
		
		communicator = new Communicator(this);
		
		
		if(!filename.equals(""))
			ballot = Ballot.load(filename);
		else
		{
			ballot = new Ballot();
			ballot.getBallotQuestionsFromAdmin();
		}
		
		results = new Results();  
		results.fromBallot(ballot); 
		
		
		communicator.launchListener(communicator.getServerPort());
	}
		
	public void countVote(String ip, String message)
	{
		results.vote(Integer.parseInt(message));
		clients.add(ip);
		currentIP = "";
	}
	
	public void previousClient(Results results)
	{
		Communicator c = new Communicator(this);
		
		c.send("Already Voted:" + results.toString() ,currentIP,c.getClientPort());
		
	}
	
	public void newClient(String ip, Ballot ballot)
	{
		Communicator c = new Communicator(this);
		currentIP = ip;
		
		c.send("Vote: " + ballot.toString(), ip, c.getClientPort());
	}
	
	public void receivedMessage(String message, String ip) 
	{
		if(currentIP.equals(ip)) 
			countVote(ip, message);
		else if(clients.contains(ip))
			previousClient(results);
		else
			newClient(ip, ballot);
		results.print();
		System.out.println();
	}
	
	public static void test(String[] args)
	{
		IMatterServer s = new IMatterServer();
		try {
			if(args.length == 0)
				s.process("");
			else
				s.process(args[0]);
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		
		
		IMatterServer s = new IMatterServer();
		try {
			if(args.length == 0)
				s.process("");
			else
				s.process(args[0]);
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		
	}
}