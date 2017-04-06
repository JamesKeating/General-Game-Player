package Test

import GDLTokens.IdToken
import GDLTokens.ImplicationToken
import GDLTokens.LparToken
import GDLTokens.RparToken
import GDLTokens.Token
import com.sun.org.glassfish.gmbal.Description
import org.junit.Before
import org.junit.Test

/**
 * Created by siavj on 06/04/2017.
 */
class DescriptionTest extends GroovyTestCase {


    private DeductiveDatabase.Description description;

    @Before
    public void setUp() throws Exception {
        ArrayList<Token> test = new ArrayList<>();
        test.add(new LparToken());
        test.add(new ImplicationToken())
        test.add(new LparToken());
        test.add(new IdToken("legal"));
        test.add(new RparToken());
        test.add(new RparToken());
        description = new DeductiveDatabase.Description(test);
    }
    @Test
    void testGetArity() {
        assertEquals("only has one arity token", 1, description.getArity());
    }

    @Test
    void testGetLeadAtom() {
        Token t = description.getLeadAtom();
        assertEquals("lead atom is legal","legal", t.toString());
    }
}
