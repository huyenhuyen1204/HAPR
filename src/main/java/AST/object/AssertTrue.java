package AST.object;

import org.eclipse.jdt.core.dom.StringLiteral;

public class AssertTrue implements Assert{
    private Object object;
    private StringLiteral message;
    public final boolean  expected = true;
    private int line;

    public AssertTrue() {
    }

    public AssertTrue(Object object, int line) {
        this.object = object;
        this.line = line;
    }

    public AssertTrue(StringLiteral message, Object object, int line) {
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
}
