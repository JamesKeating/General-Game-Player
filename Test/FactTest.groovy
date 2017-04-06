package Test

import DeductiveDatabase.Fact
import GDLTokens.IdToken
import GDLTokens.ImplicationToken
import GDLTokens.LparToken
import GDLTokens.RparToken
import GDLTokens.Token
import GDLTokens.VarToken
import org.junit.Before
import org.junit.Test

/**
 * Created by siavj on 06/04/2017.
 */
class FactTest extends GroovyTestCase {

    private Fact fact;

    @Before
    public void setUp() throws Exception {
        ArrayList<Token> test = new ArrayList<>();
        test.add(new LparToken());
        test.add(new IdToken("legal"));
        test.add(new RparToken());
        fact = new Fact(test);
    }

    @Test
    void testGetLeadAtom() {
        Token t = fact.getLeadAtom();
        assertEquals("lead atom is legal","legal", t.toString());
    }

    @Test
    void testIsGround() {
        assertEquals("lead atom is legal", true, fact.isGround());
        fact.fact.add(new VarToken("?x"));
        assertEquals("lead atom is legal", false, fact.isGround());
    }
}
