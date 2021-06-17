package AST.node;

public class StatementNode {
    private int line;
    private Object statement;

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public Object getStatement() {
        return statement;
    }

    public void setStatement(Object statement) {
        this.statement = statement;
    }

    public StatementNode(int line, Object statement) {
        super();
        this.line = line;
        this.statement = statement;
    }
}
