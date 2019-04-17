package edu.nova.csis2101.imatter.imatterclient;

import java.util.Scanner;
import edu.nova.csis2101.imatter.IMessageReceiver;
import edu.nova.csis2101.imatter.communicator.CommunicatorClient;
import edu.nova.csis2101.imatter.ballot.*;
import edu.nova.csis2101.imatter.results.*;

public class IMatterClient implements IMessageReceiver {
	private CommunicatorClient client; 
	public String serverIpAddress;
	public String message;  
	private int ballotSize;
	
	public IMatterClient(){
		client = new CommunicatorClient(this);
		this.message = "";
	}

	public void setServerIpAddress(String serverIpAddress){
		this.serverIpAddress = serverIpAddress;
	}

	public String getIpAddressOfServerFromUser(){
		Scanner in = new Scanner(System.in);
		System.out.print("Enter server IP Address: ");
		serverIpAddress = in.nextLine();
		return serverIpAddress;
	}

	public void connectToServer(){
		client.send("0",serverIpAddress, client.getServerPort());
		client.launchListener(client.getClientPort());
	}


	public void displayBallot(String message){
		Ballot receivedBallot = Ballot.fromString(message);
		receivedBallot.print();
		ballotSize = receivedBallot.getQuestions().size();
	}


	public void displayResults(String message){
		System.out.println("Here are the results");
		Results receivedResults = Results.fromString(message);
		receivedResults.print();
	}

	public void displayMessage(){
		System.out.println(message);
	}

	public int getVote(Scanner in){
		System.out.print("Vote: ");
		int vote = in.nextInt();
		while(vote > ballotSize || vote == 0) {
			System.out.print("Not a valid choice. Revote: ");
			vote = in.nextInt();
		}
		client.send(Integer.toString(vote),serverIpAddress,client.getServerPort());
		
		return vote;
	}

	public void process(){
		Scanner in = new Scanner(System.in);
		if(message.contains("Already Voted")){
			message = message.substring(14,message.length());
			displayResults(message);
		}else if(message.contains("Vote")){
			message = message.substring(6,message.length());
			displayBallot(message);
			getVote(in);
		}
		in.close();
	}

	public void receivedMessage(String message, String ip) {
		if(message.contains("Already Vote"))
			System.out.println("This is printed out from Client("+ip+"): Already Voted");
		else if(message.contains("Vote"))
			System.out.println("This is printed out from Client("+ip+"): Vote: ");
		this.message = message;

	}

	public static void test(String[] args) {
		IMatterClient client = new IMatterClient();
		if(args.length==0){
			client.getIpAddressOfServerFromUser();
		}else{
			client.setServerIpAddress(args[0]);
		}
		client.connectToServer();
		client.process();
	}

	public static void main(String[] args) {
		IMatterClient.test(args);
	}
}
