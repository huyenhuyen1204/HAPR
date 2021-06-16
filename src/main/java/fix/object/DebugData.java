package fix.object;

import AST.obj.DebugPoint;

public class DebugData {
    private DebugPoint debugPoint;
    private Object expected;

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

    public DebugData(DebugPoint debugPoint, Object expected) {
        this.debugPoint = debugPoint;
        this.expected = expected;
    }
}
