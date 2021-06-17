package fix.object;

import AST.obj.DebugPoint;
import AST.stm.abstrct.AssertStatement;

public class DebugData {
    private DebugPoint debugPoint;
    private AssertStatement assertStatement;
    private Object expected;
    private String testname;

    public DebugPoint getDebugPoint() {
        return debugPoint;
    }

    public void setDebugPoint(DebugPoint debugPoint) {
        this.debugPoint = debugPoint;
    }

    public Object getExpected() {
        return expected;
    }

    public void setExpected(Object expected) {
        this.expected = expected;
    }

    public DebugData(DebugPoint debugPoint, Object expected, String testname) {
        this.debugPoint = debugPoint;
        this.expected = expected;
        this.testname = testname;
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
