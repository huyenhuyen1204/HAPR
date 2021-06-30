package AST.stm.node;

import AST.stm.abst.AssertStatement;
import org.eclipse.jdt.core.dom.StringLiteral;

public class AssertTrueStmNode extends AssertStatement {
    private Object object;
    private StringLiteral message;
    public final boolean  expected = true;

    public AssertTrueStmNode() {
        super();
    }

    public AssertTrueStmNode(Object object, int line) {
        super();
        this.object = object;
        this.line = line;
    }

    public AssertTrueStmNode(StringLiteral message, Object object, int line) {
        super();
        this.object = object;
        this.message = message;
        this.line = line;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public StringLiteral getMessage() {
        return message;
    }

    public void setMessage(StringLiteral message) {
        this.message = message;
    }

    public boolean isExpected() {
        return expected;
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


}
