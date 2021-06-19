package AST.node;

public class StatementNode {
    private int line;
    private Object statementNode;

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public Object getStatementNode() {
        return statementNode;
    }

    public void setStatementNode(Object statementNode) {
        this.statementNode = statementNode;
    }

    public StatementNode(int line, Object statementNode) {
        super();
        this.line = line;
        this.statementNode = statementNode;
    }
}
