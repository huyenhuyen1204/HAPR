package fix.object;

import AST.obj.DebugPoint;
import AST.stm.abstrct.AssertStatement;

import java.util.List;

public class DebugData {
    private List<DebugPoint> debugPoints;
    private AssertStatement assertStatement;
    private String expected;
    private String testname;



    public String getExpected() {
        return expected;
    }

    public void setExpected(String expected) {
        this.expected = expected;
    }

    public DebugData( String expected, String testname) {
        this.expected = expected;
        this.testname = testname;
    }

    public List<DebugPoint> getDebugPoints() {
        return debugPoints;
    }

    public void setDebugPoints(List<DebugPoint> debugPoints) {
        this.debugPoints = debugPoints;
    }

    public String getTestname() {
        return testname;
    }

    public void setTestname(String testname) {
        this.testname = testname;
    }

    public AssertStatement getAssertStatement() {
        return assertStatement;
    }

    public void setAssertStatement(AssertStatement assertStatement) {
        this.assertStatement = assertStatement;
    }
}
