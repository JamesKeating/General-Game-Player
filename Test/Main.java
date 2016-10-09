package Test;

import GDLParser.LexicalAnalyser;

public class Main {

	public static void main(String[] args) {
		LexicalAnalyser l = new LexicalAnalyser();
		l.analyseFile();
		System.out.println(l.getTokenStream());

	}

}
