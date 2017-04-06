package Test

import GDLParser.LexicalAnalyser
import org.junit.Before
import org.junit.BeforeClass

import static org.junit.Assert.assertArrayEquals
import static org.junit.Assert.assertEquals;

import org.junit.Test;

class LexicalAnalyserTest extends GroovyTestCase {


    private LexicalAnalyser lexicalAnalyser;

    @Before
    public void setUp() throws Exception {
        lexicalAnalyser = new LexicalAnalyser();
    }

    @Test
    public void testAnalyseFile() {

        assertEquals("false as file dosn't exist", false, lexicalAnalyser.analyseFile("invalid directory"))
        assertEquals("true as file is valid", true, lexicalAnalyser.analyseFile("Data\\TicTacToe"))
        assertEquals("false as file has invalid token", false, lexicalAnalyser.analyseFile("Test\\ParserTestBadToken"))
    }

    @Test
    public void testCreateToken() {

        for(int i =1; i <= 5; i++)
            lexicalAnalyser.createToken(i)

        assertEquals("state 1 = lpar", "(", lexicalAnalyser.tokenStream.get(0).getID())
        assertEquals("state 2 = rpar", ")", lexicalAnalyser.tokenStream.get(1).getID())
        assertEquals("state 3 = ;", ";", lexicalAnalyser.tokenStream.get(2).getID())
        assertEquals("state 4 = <=", -3, lexicalAnalyser.tokenStream.get(3).getValue())
        assertEquals("state 5 = atom", -3, lexicalAnalyser.tokenStream.get(4).getValue())

    }


}
