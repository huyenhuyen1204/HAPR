package core.object;

import AST.node.StatementNode;

public class DebugPoint {
    private String classname;
    private int line;
    private String keyVar;
    private StatementNode statementNode;

    public DebugPoint() {
    }

    public DebugPoint(String classname, int line, String keyVar, StatementNode statementNode) {
        this.classname = classname;
        this.line = line;
        this.keyVar = keyVar;
        this.statementNode = statementNode;
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

    public String getKeyVar() {
        return keyVar;
    }

    public void setKeyVar(String keyVar) {
        this.keyVar = keyVar;
    }

    public StatementNode getStatementNode() {
        return statementNode;
    }

    public void setStatementNode(StatementNode statementNode) {
        this.statementNode = statementNode;
    }
}
