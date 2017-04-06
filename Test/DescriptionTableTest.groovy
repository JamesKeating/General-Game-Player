package Test

import DeductiveDatabase.DescriptionTable
import GDLParser.LexicalAnalyser
import GDLParser.RDP
import GDLTokens.IdToken
import GDLTokens.ImplicationToken
import GDLTokens.LparToken
import GDLTokens.RparToken
import GDLTokens.Token
import org.junit.Before
import org.junit.Test

/**
 * Created by siavj on 06/04/2017.
 */
class DescriptionTableTest extends GroovyTestCase {

    private  DescriptionTable descriptionTable;
    private LexicalAnalyser lexicalAnalyser;


    @Before
    public void setUp() throws Exception {
        lexicalAnalyser = new LexicalAnalyser();

        lexicalAnalyser.analyseFile("Test\\DescriptionTableTest")
        descriptionTable = new DescriptionTable(lexicalAnalyser.tokenStream)
    }

    @Test
    void testListTable() {
        String expected = "[( base test ( a b ) ), ( init ( test a ) )]"
        assertEquals("unexpected output", expected, descriptionTable.listTable().toString());
    }
}
