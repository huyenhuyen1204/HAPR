package AST.node;

public class StatementNode {
    private int line;
    private String keyVar;
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

    public void setStatementNode(Object statementNode, String keyVar) {
        this.statementNode = statementNode;
        this.keyVar = keyVar;
    }

    public String getKeyVar() {
        return keyVar;
    }

    public void setKeyVar(String keyVar) {
        this.keyVar = keyVar;
    }

    public StatementNode(int line) {
        super();
        this.line = line;
    }

    public StatementNode(int line, Object statementNode, String keyvar) {
        super();
        this.line = line;
        this.statementNode = statementNode;
        this.keyVar = keyvar;
    }
}
