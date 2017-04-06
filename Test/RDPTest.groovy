package Test

import GDLParser.LexicalAnalyser
import GDLParser.RDP
import org.junit.Before
import org.junit.Test

/**
 * Created by siavj on 06/04/2017.
 */
class RDPTest extends GroovyTestCase {


    private LexicalAnalyser lexicalAnalyser;
    private RDP rdp;

    @Before
    public void setUp() throws Exception {
        lexicalAnalyser = new LexicalAnalyser();
        rdp = new RDP();


    }

    @Test
    void testParse() {
        lexicalAnalyser.analyseFile("Test\\ParserTestBadSyntax");
        assertEquals("false as file has invalid syntax", false, rdp.parse(lexicalAnalyser.tokenStream));
        lexicalAnalyser.tokenStream.clear();
        lexicalAnalyser.analyseFile("Data\\TicTacToe");
        assertEquals("true as file has valid syntax", true, rdp.parse(lexicalAnalyser.tokenStream));
    }
}
