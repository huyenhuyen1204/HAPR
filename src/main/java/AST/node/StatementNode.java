package AST.node;

public class StatementNode extends Node{
    private int line;
    private String keyVar;
    private String statement;
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

    public void setStatementNode(Object statementNode, String keyVar, String statement) {
        this.statementNode = statementNode;
        this.keyVar = keyVar;
        this.statement = statement;
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

    public StatementNode(int line, Object statementNode, String keyvar, String statement) {
        super();
        this.line = line;
        this.statementNode = statementNode;
        this.keyVar = keyvar;
        this.statement = statement;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public void setStatementNode(Object statementNode) {
        this.statementNode = statementNode;
    }
}
