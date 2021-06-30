package AST.stm.abst;

public abstract class StatementNode {
    protected int line;
    protected String keyVar = null;
    protected String statementString;

    public abstract int getLine();

    public abstract void setLine(int line);

    public abstract String getKeyVar();

    public abstract void setKeyVar(String keyVar);

    public abstract String getStatementString();

    public abstract void setStatementString(String statementString);
}
