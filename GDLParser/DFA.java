package GDLParser;

//A state machine used to determine the validity and type of tokens
public class DFA {
	
	public int state0(char c){
		if(Character.isWhitespace(c))
			return 0;
			
		else if(c == '(')
			return 1;

		else if(c == ')')
			return 2;

		else if(c == ';')
			return 3;

		else if(Character.isLetter(c))
			return 5;
		
		else if(Character.isDigit(c))
			return 6;
		
		else if(c == '<')
			return 7;

		else if(c == '?')
			return 8;

		else
			return -1;
	}

	public int state5(char c){
		if(Character.isLetter(c) || Character.isDigit(c))
			return 5;
		else
			return -1;
	} 
	
	public int state6(char c){
		if(Character.isDigit(c))
			return 6;
		if(Character.isLetter(c))
			return 5;
		else
			return -1;
	} 
	
	public int state7(char c){
		if (c == '=')
			return 4;
		else
			return -1;
		
	}

	public int state8(char c){
		if(Character.isLetter(c) || Character.isDigit(c))
			return 5;
		else
			return -1;

	}
}
