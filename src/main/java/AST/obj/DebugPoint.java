package AST.obj;

public class DebugPoint {
    private Object classname;
    private int line;

    public DebugPoint() {
    }

    public DebugPoint(Object classname, int line) {
        this.classname = classname;
        this.line = line;
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
}
