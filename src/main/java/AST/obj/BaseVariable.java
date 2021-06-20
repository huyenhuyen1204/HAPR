package AST.obj;

public class BaseVariable {
    private int line;
    private String varname;

    public BaseVariable(int line, String varname) {
        this.line = line;
        this.varname = varname;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getVarname() {
        return varname;
    }

    public void setVarname(String varname) {
        this.varname = varname;
    }
}
