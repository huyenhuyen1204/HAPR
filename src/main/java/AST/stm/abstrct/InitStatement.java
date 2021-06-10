package AST.stm.abstrct;

import AST.model.Statement;

public abstract class InitStatement implements Statement {
    protected int line;
    protected String varName;
    protected Object type;
    protected Object value;

    public abstract int getLine();

    public abstract void setLine(int line) ;

    public abstract String getVarName();

    public abstract void setVarName(String varName);

    public abstract Object getType();

    public abstract void setType(Object type);

    public abstract Object getValue() ;

    public abstract void setValue(Object value);

    public String print(String methodName) {
        return "{type =" + this.type +
                "; varName =" + this.varName +
                "; methodName =" + methodName + "}";
    }
}
