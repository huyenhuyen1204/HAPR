package AST.stm;

import AST.stm.abstrct.AssertStatement;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.StringLiteral;

public class AssertEqualStm extends AssertStatement {
    private StringLiteral message;
    private Object expected;
    private Object actual;
    private NumberLiteral delta;


    public AssertEqualStm() {
        super();

    }
    public AssertEqualStm(StringLiteral message, Object expected, Object actual, int line) {
        super();
        this.message = message;
        this.expected = expected;
        this.actual = actual;
        this.line = line;
    }

    public AssertEqualStm(Object expected, Object actual, NumberLiteral delta, int line) {
        super();
        this.delta = delta;
        this.expected = expected;
        this.actual = actual;
        this.line = line;
    }

    public AssertEqualStm(Object expected, Object actual, int line) {
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

}
