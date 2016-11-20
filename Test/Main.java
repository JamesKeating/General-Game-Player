package Test;

import DescriptionProcessing.Prover;
import GDLParser.LexicalAnalyser;
import GDLParser.RDP;
import GDLTokens.*;
import SylmbolTable.Description;
import SylmbolTable.DescriptionTable;
import SylmbolTable.Fact;
import SylmbolTable.FactTable;

import java.util.AbstractList;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		LexicalAnalyser l = new LexicalAnalyser();
		l.analyseFile();

		DescriptionTable dt = new DescriptionTable(l.getTokenStream());
		FactTable factTable = new FactTable();

		for(Description d : dt.getTable().get("<init>").get(1)){
			for(Fact f : d.getFacts()){
				factTable.addFact(f);
			}
		}
		Prover p = new Prover();
		p.setDt(dt);
		p.setFt(factTable);

		System.out.println(factTable.size());


		//(does xplayer (mark ?m ?n))
//		ArrayList<Token> test = new ArrayList<>();
//		test.add(new LparToken());
//		test.add(new IdToken("does"));
//		test.add(new IdToken("oplayer"));
//		test.add(new LparToken());
//		test.add(new IdToken("mark"));
//		test.add(new IntToken("2"));
//		//test.add(new SemiCoToken());
//		test.add(new IntToken("2"));
//		test.add(new RparToken());
//		test.add(new RparToken());
//
//		Fact move = new Fact(test);
//		factTable.addFact(move);

		System.out.println(factTable + "\n Generating all posible Facts from initial Fact Table:\n\n");
		System.out.println(p.getNewFacts().size());
		System.out.println(factTable);

	}

}
