package AST.node;

import java.util.ArrayList;
import java.util.List;

public abstract class InitNode {
    protected int line;
    protected String varname;
    protected List<StatementNode> statements;
    protected Object type;
    public abstract String getVarname();

    public abstract void setVarname(String varname);

    public abstract int getLine();

    public abstract void setLine(int line);
    public abstract List getStatements();

    public abstract void setStatements(List statements);

    public abstract Object getType();

    public abstract void setType(Object type);
    public abstract void addStatement(StatementNode obj);

}
