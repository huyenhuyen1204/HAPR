package AST.stm.abst;

public abstract class StatementNode {
    protected int line;
    protected String keyVar = null;
    protected String statementString;
    protected Object statement;
    protected int startPostion;
    protected int endPostion;

    public abstract Object getStatement();

    public abstract void setStatement(Object statement);

    public abstract int getStartPostion();

    public abstract void setStartPostion(int startPostion);

    public int getEndPostion() {
        return endPostion;
    }

    public void setEndPostion(int endPostion) {
        this.endPostion = endPostion;
    }

    public abstract int getLine();

    public abstract void setLine(int line);

    public abstract String getKeyVar();

    public abstract void setKeyVar(String keyVar);

    public abstract String getStatementString();

    public abstract void setStatementString(String statementString);
}
