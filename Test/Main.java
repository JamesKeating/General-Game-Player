package Test;

import DescriptionProcessing.Prover;
import GDLParser.LexicalAnalyser;
import GDLParser.RDP;
import SylmbolTable.Description;
import SylmbolTable.DescriptionTable;
import SylmbolTable.Fact;
import SylmbolTable.FactTable;

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
		System.out.println(p.getNewFacts().size());

	}

}
