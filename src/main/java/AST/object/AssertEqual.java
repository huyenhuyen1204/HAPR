package AST.object;

import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.StringLiteral;

public class AssertEqual implements Assert{
    private StringLiteral message;
    private Object expected;
    private Object actual;
    private NumberLiteral delta;
    private int line;

    public AssertEqual() {

    }
    public AssertEqual(StringLiteral message, Object expected, Object actual, int line) {
        this.message = message;
        this.expected = expected;
        this.actual = actual;
        this.line = line;
    }

    public AssertEqual(Object expected, Object actual, NumberLiteral delta, int line) {
        this.delta = delta;
        this.expected = expected;
        this.actual = actual;
        this.line = line;
    }

    public AssertEqual(Object expected, Object actual, int line) {
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
