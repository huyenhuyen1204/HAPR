package core.object;

import AST.stm.abst.AssertStatement;

import java.util.List;

public class DebugData {
    private List<DebugPoint> debugPoints;
    private AssertStatement assertStatement;
    private String expected;
    private String tmpExpected;
    private String testname;
    private int indexExpected = 0;
    private int indexActual = 0;

    public String getTmpExpected() {
        return tmpExpected;
    }

    public void setTmpExpected(String tmpExpected) {
        this.tmpExpected = tmpExpected;
    }

    public int getIndexExpected() {
        return indexExpected;
    }

    public void setIndexExpected(int indexExpected) {
        this.indexExpected = indexExpected;
    }

    public int getIndexActual() {
        return indexActual;
    }

    public void setIndexActual(int indexActual) {
        this.indexActual = indexActual;
    }

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
