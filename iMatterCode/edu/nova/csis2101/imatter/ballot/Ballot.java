package edu.nova.csis2101.imatter.ballot;


import java.util.*;
import edu.nova.csis2101.imatter.imatterserver.IMatterServer;
import java.io.*;



public class Ballot{
    
    protected ArrayList<String> questions;
    
    public Ballot(){
        questions = new ArrayList<String>();
    }
    public ArrayList<String> getQuestions(){
    	return questions;
    }
    
    public void getBallotQuestionsFromAdmin(){     
        Scanner in = new Scanner(System.in);
        
        
        int i=1;
        String th = "st";
        String input ="";
        do{
        	do{
        		System.out.print("Please enter the "+i+th+" Ballot Choice: ");
        		i++;
        		switch(i){
        		case 2: th = "nd"; break;
        		case 3: 
        			th = "rd"; 
        			break;
        		default: 
        			th = "th";	
        			System.out.print("(press just ENTER if you have no more questions to enter): ");
        		}
        		input = in.nextLine();
        		if(!input.equals("")) questions.add(input);
        		        		
        	}while(!input.equals(""));  
        	i--;
        	if (i<3) System.out.println("You have to enter at least two question to complete the ballot");
        }while(i< 3); 
        
        in.close();
        
        
    }
    
     
    public String toString(){
    	String str = "";
    	for(String s: questions){  
    	
    		str += (s+"\n");
    	}
    	return str;
    	
    }
    
    
    public void addBallotQuestion(String statement){
        questions.add(statement);
    }
    
    public static Ballot fromString(String string){
        Ballot b = new Ballot();
		String[] statements = string.split("\n");
		for(int i =0;i<statements.length;i++){
			b.addBallotQuestion(statements[i]);
		}
        
        
        
        
        
        
        return b;
    }
    
    
     public void save(String filename) throws FileNotFoundException{
            PrintWriter writeFile = new PrintWriter(filename);
            writeFile.print(toString());
            writeFile.close();
    }
    
    
    public static Ballot load(String filename) throws FileNotFoundException{
    	
        Ballot aBallot = new Ballot();
        
		File file = new File(filename);
		Scanner in = new Scanner(file);
		String str;
		do{
			str =  in.nextLine();
			aBallot.addBallotQuestion(str);
		}while(!str.equals("") && in.hasNextLine());
		in.close();
        return aBallot;
    }
    
    
    private static void printBanner(){
        System.out.println("\t");
        System.out.println("");
    }
    
    
       
    
    public void print(){
    	
    	for(int i=0;i< questions.size();i++){
    		System.out.println((i+1)+". "+questions.get(i));
    	}
    }
    
     
    public static void test(){
        
    	try {
    		Ballot.printBanner();
    		Ballot aBallot = new Ballot();

    		aBallot.getBallotQuestionsFromAdmin();

    		aBallot.print();
    		System.out.println("Testing save() ... saving to ballot.txt");
    		aBallot.save("ballot.txt");
    		String str = aBallot.toString();
    		
    		Ballot b = Ballot.fromString(str);     
    		System.out.println("Testing toString() and fromString()");
    		b.print();
    		System.out.println("Testing load() ... saving to ballot.txt");
    		Ballot c = Ballot.load("ballot.txt");
    		c.print();
    		
    	}
    	catch (FileNotFoundException e) {
    		
    		e.printStackTrace();
    	}
        
        
    }
    
     
    public static void main(String[] args){
        Ballot.test();
    }
}