package Test;

/**
 * Created by siavj on 06/04/2017.
 */
import GDLParser.RDP;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        DescriptionTableTest.class,
        RDPTest.class,
        LexicalAnalyserTest.class,
        FlattenerTest.class,
        DescriptionTest.class,
        FactTest.class

})

public class TestSuite {
}