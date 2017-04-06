package Test

import DeductiveDatabase.Description
import DeductiveDatabase.DescriptionTable
import DescriptionProcessing.Flattener
import GDLParser.LexicalAnalyser
import GDLParser.RDP
import org.junit.Before

/**
 * Created by siavj on 06/04/2017.
 */
class FlattenerTest extends GroovyTestCase {



    private LexicalAnalyser lexicalAnalyser;
    private RDP rdp;
    private DescriptionTable descriptionTable;
    private Flattener flattener;

    @Before
    public void setUp() throws Exception {
        lexicalAnalyser = new LexicalAnalyser();
        rdp = new RDP();
        lexicalAnalyser.analyseFile("Test\\FlattenerTest")
        descriptionTable = new DescriptionTable(lexicalAnalyser.tokenStream);
        flattener = new Flattener(descriptionTable.listTable());



    }

    void testFlatten() {
        String expected = "[( <= ( init ( test a ) ) ), " +
                "( <= ( flatten ) ( true ( test a ) ) )," +
                " ( <= ( flatten ) ( true ( test b ) ) )]";
        ArrayList<Description> x = flattener.flatten();


        assertEquals("false as file has invalid syntax", expected, x.toString());
    }
}
