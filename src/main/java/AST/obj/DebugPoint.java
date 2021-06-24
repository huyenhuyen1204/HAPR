package AST.obj;

public class DebugPoint {
    private String classname;
    private int line;
    private String keyVar;

    public DebugPoint() {
    }

    public DebugPoint(String classname, int line, String keyVar) {
        this.classname = classname;
        this.line = line;
        this.keyVar = keyVar;
    }

    public String getClassname() {
        return classname.toString();
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getKeyVar() {
        return keyVar;
    }

    public void setKeyVar(String keyVar) {
        this.keyVar = keyVar;
    }
}
