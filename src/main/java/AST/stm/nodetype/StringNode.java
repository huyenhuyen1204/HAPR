package AST.stm.nodetype;

import AST.stm.abst.StatementNode;

public class StringNode extends StatementNode {
    public static final String type = "StringLiteral";
    private String value;
    public StringNode(int line, String keyVar, String value, String stmString) {
        super();
        this.line = line;
        this.keyVar = keyVar;
        this.statementString = stmString;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int getLine() {
        return this.line;
    }

    @Override
    public void setLine(int line) {
        this.line = line;
    }

    @Override
    public String getKeyVar() {
        return this.keyVar;
    }

    @Override
    public void setKeyVar(String keyVar) {
        this.keyVar = keyVar;
    }

    @Override
    public String getStatementString() {
        return this.statementString;
    }

    @Override
    public void setStatementString(String statementString) {
        this.statementString = statementString;
    }

    @Override
    public Object getStatement() {
        return this.statement;
    }

    @Override
    public void setStatement(Object statement) {
        this.statement = statement;
    }

    @Override
    public int getStartPostion() {
        return this.startPostion;
    }

    @Override
    public void setStartPostion(int startPostion) {
        this.startPostion = startPostion;
    }
}
