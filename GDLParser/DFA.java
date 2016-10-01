package GDLParser;
public class DFA {
	
	public int state0(char c){
		if(Character.isWhitespace(c))
			return 0;
			
		else if(Character.toString(c).matches("[(|)|;|?]"))
			return 1;
		
		else if(Character.isLetter(c))
			return 2;
		
		else if(Character.isDigit(c))
			return 3;
		
		else if(c == '<')
			return 4;
		
		else
			return -1;
	}
	
	public int state2(char c){
		if(Character.isLetter(c) || Character.isDigit(c))
			return 2;
		else
			return -1;
	} 
	
	public int state3(char c){
		if(Character.isDigit(c))
			return 3;
		else
			return -1;
	} 
	
	public int state4(char c){
		if (c == '?')
			return 1;
		else
			return -1;
		
	} 
}
