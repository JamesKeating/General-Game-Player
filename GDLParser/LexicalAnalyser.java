package GDLParser;

import java.io.FileInputStream;
import java.util.ArrayList;

import GDLTokens.*;
import SylmbolTable.Trie;
import SylmbolTable.TrieNode;

public class LexicalAnalyser {
	
	private int currState = 0;
	private String[] keyWords = {"role", "init", "terminal", "legal", "next", "true", "does", "goal",
									"or", "distinct", "not", "base", "drawit", "data"};
	private Trie symbolTable = new Trie(keyWords);
	private ArrayList<Token> tokenStream = new ArrayList<>();
	
	public void analyseFile(String directory){
		
		DFA myDFA =new DFA(); 
		int lastState = 0;
		
		try{
			FileInputStream fileInput = new FileInputStream(directory);
			int fileChar = fileInput.read();

			char c = 0;

			while (fileChar != -1) {
			   c = (char) fileChar;

			   lastState = currState;
			   switch(currState){

			   case 0:	currState = myDFA.state0(c);
					break;
			   case 5:	currState = myDFA.state5(c);
					break;
			   case 6:	currState = myDFA.state6(c);
					break;
			   case 7:  currState = myDFA.state7(c);
				   break;
			   case 8:  currState = myDFA.state8(c);
				   break;
			   }

			   if(currState == -1) {

					createToken(lastState);
					currState = 0;
			   }

			   else if(currState > 0 && currState < 5){
				   createToken(currState);
				   currState = 0;
				   fileChar = fileInput.read();
			   }

			   else{
				   symbolTable.processChar(c, true);
				   fileChar = fileInput.read();
			   }
			}

			if(currState != -1|| currState != 7 || currState != 8) {
				createToken(currState);
			}

			fileInput.close();
			tokenStream.add(new EofToken());
		}catch(Exception e){ e.printStackTrace();}
	}
	
	public void createToken(int state){

		String value = symbolTable.getString();
		TrieNode node = symbolTable.getNode(true);
		switch (state) {
			case 1:
				this.tokenStream.add(new LparToken());
				break;
			case 2:
				this.tokenStream.add(new RparToken());
				break;
			case 3:
				this.tokenStream.add(new SemiCoToken());
				break;
			case 4:
				this.tokenStream.add(new ImplicationToken());
				break;
			case 5:
				if (value.startsWith("?"))
					this.tokenStream.add(new VarToken(value));
				else if (node.isKey())
					this.tokenStream.add(new KeyWordToken(value));
				else
					this.tokenStream.add(new IdToken(value));
				break;
			case 6:
				this.tokenStream.add(new IntToken(value));
				break;
		}
	}

	public ArrayList<Token> getTokenStream() {
		return tokenStream;
	}
	
}
