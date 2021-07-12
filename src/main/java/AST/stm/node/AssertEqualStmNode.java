package AST.stm.node;

import AST.stm.abst.AssertStatement;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.StringLiteral;

public class AssertEqualStmNode extends AssertStatement {
    private StringLiteral message;
    private Object expected;
    private Object actual;
    private NumberLiteral delta;


    public AssertEqualStmNode() {
        super();

    }
    public AssertEqualStmNode(StringLiteral message, Object expected, Object actual, int line) {
        super();
        this.message = message;
        this.expected = expected;
        this.actual = actual;
        this.line = line;
    }

    public AssertEqualStmNode(Object expected, Object actual, NumberLiteral delta, int line) {
        super();
        this.delta = delta;
        this.expected = expected;
        this.actual = actual;
        this.line = line;
    }

    public AssertEqualStmNode(Object expected, Object actual, int line) {
        super();
        this.expected = expected;
        this.actual = actual;
        this.line = line;
    }


    public Object getMessage() {
        return message;
    }

    public void setMessage(StringLiteral message) {
        this.message = message;
    }

    public Object getExpected() {
        return expected;
    }

    public void setExpected(Object expected) {
        this.expected = expected;
    }

    public Object getActual() {
        return actual;
    }

    public void setActual(Object actual) {
        this.actual = actual;
    }

    public NumberLiteral getDelta() {
        return delta;
    }

    public void setDelta(NumberLiteral delta) {
        this.delta = delta;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    @Override
    public String getKeyVar() {
        return this.keyVar;
    }

    @Override
    public void setKeyVar(String keyVar) {
        this.keyVar = keyVar;
    }

    @Override
    public String getStatementString() {
        return this.statementString;
    }

    @Override
    public void setStatementString(String statementString) {
        this.statementString = statementString;
    }

    @Override
    public Object getStatement() {
        return this.statement;
    }

    @Override
    public void setStatement(Object statement) {
        this.statement = statement;
    }

    @Override
    public int getStartPostion() {
        return this.startPostion;
    }

    @Override
    public void setStartPostion(int startPostion) {
        this.startPostion = startPostion;
    }

}
