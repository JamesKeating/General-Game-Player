package GDLParser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import SylmbolTable.Trie;
import GDLParser.DFA;
public class LexicalAnalyser {
	
	private int currState = 0;
	private String[] keyWords = {"init", "terminal"};
	private Trie symbolTable = new Trie(keyWords);
	
	public void analyseFile(){
		
		DFA myDFA =new DFA(); 
		int lastState = 0;
		
		try{
		FileInputStream fileInput = new FileInputStream("Data\\lexerTest.txt");
		int fileChar = fileInput.read();
		char c = (char) fileChar;	
		
		while (fileChar != -1) {
		   c = (char) fileChar;	
		   System.out.print(c);  
		   lastState = currState;
		   switch(currState){
		   case 0:	currState = myDFA.state0(c);
		   		break;
		   case 2:	currState = myDFA.state2(c);
		   		break;
		   case 3:	currState = myDFA.state3(c);
		   		break;
		   }
		   
		   if(currState ==1){
			   createToken(1);
			   currState = 0;
		   }
		   
		   if(currState != -1)
			   fileChar = fileInput.read();
		   
		   else{
			   createToken(lastState);
			   currState = 0;
		   }
		}
		if(currState == 1|| currState == 2 || currState == 3)
   			createToken(currState);
		
		fileInput.close();
		}catch(Exception e){ e.printStackTrace();}
	}
	
	public void createToken(int state){
		System.out.print(state);
	}
	
}
