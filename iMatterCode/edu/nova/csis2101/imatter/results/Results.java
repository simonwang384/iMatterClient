package edu.nova.csis2101.imatter.results;


import java.util.ArrayList;
import java.util.Scanner;
import edu.nova.csis2101.imatter.ballot.Ballot;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Results extends Ballot{ 

	
	protected ArrayList<Integer> votes;

	public Results(){
		votes= new ArrayList<Integer>();
	} 


	
	public String toString(){             
		String str = "";
		for(int i=0; i< questions.size();i++){
			str += (questions.get(i)+"\n");   	
			str += (votes.get(i)+"\n");
		}
		return str;
	}

	
	
	

	public void addVotes(Integer votes){
		this.votes.add(votes);
	}

	public static Results fromString(String string){
		Results b = new Results();
		String[] statements = string.split("\n");
		for(int i =0;i<statements.length;i+=2){
			b.addBallotQuestion(statements[i]);
			String votesStr = statements[i+1];
			int ii = Integer.parseInt(votesStr);
			b.addVotes(ii);
		}
		return b;
	}

	public static Results load(String filename) throws FileNotFoundException{   
		Results aResults = new Results();
		File file = new File(filename);
		Scanner in = new Scanner(file);
		String str;
		do{
			str =  in.nextLine();
			aResults.addBallotQuestion(str);
			str = in.nextLine();
			int ii = Integer.parseInt(str);
			aResults.addVotes(ii);
		}while(!str.equals("") && in.hasNextLine());
		in.close();
        return aResults;
		
	}

	public void vote (int questionindex) { 
		try {
			votes.set(questionindex-1, votes.get(questionindex-1)+1);
		} catch(Exception e) {
			System.out.println("There is no choice at: " + questionindex);
		}

	}
	
	
	public int getTotalVotesCast ()  {

		int totalVotes=0;
		for(int i=0;i<votes.size();i++){
			totalVotes =totalVotes + votes.get(i);
		}
		return totalVotes;
	}


	public boolean isFiveVotesCasted(){
		if( getTotalVotesCast()>=5){return true;}
		else{return false;}
	}

	public void save(String filename) throws FileNotFoundException{
		File file = new File(filename);
		PrintWriter out = new PrintWriter(file);
		
		out.print(toString());
		out.close();
	}

	public  void print(){

		for (int i=0;i<questions.size();i++){
			System.out.println((i+1)+"\t"+questions.get(i)+"\t"+"Votes = "+votes.get(i));
		}

	}
	
	
	public void fromBallot(Ballot aBallot){
		this.questions = aBallot.getQuestions();
		for(int i=0;i<questions.size();i++){
			votes.add(0);
		}
	}

	public static void test() {
		Results aResults = new Results();
		Ballot aBallot;
		try {
			aBallot = Ballot.load("ballot.txt");
			aResults.fromBallot(aBallot);
			System.out.println("Priting intialized results from the File aBallot");
			aResults.print();
			aResults.vote(2);
			aResults.vote(1);
			
			aResults.vote(1);
			aResults.vote(1);
			aResults.vote(2);
			System.out.print("Priting results after "+aResults.getTotalVotesCast());
			System.out.println(" votes have been casted. \nFive or more casted? "+aResults.isFiveVotesCasted());
			aResults.print();
			
			System.out.println("Testing to and from String");
			String str = aResults.toString();
			Results r = Results.fromString(str);
			r.print();
			System.out.println("Testing save(). Will be saved in results.txt");
			r.save("results.txt");
			System.out.println("Testing load(). Will be loaded from results.txt");
			Results rLoaded = Results.load("results.txt");
			rLoaded.print();
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}

	}
	
	public static void main(String[] args) {
		Results.test(); 
	}

}
