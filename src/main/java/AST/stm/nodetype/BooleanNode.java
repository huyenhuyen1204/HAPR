package AST.stm.nodetype;

import AST.stm.abst.StatementNode;

public class BooleanNode extends StatementNode {
    private boolean value;



    public BooleanNode(boolean value, int startPostion, int endPostion, String stmString, int line) {
        this.value = value;
        this.startPostion = startPostion;
        this.endPostion = endPostion;
        this.statementString = stmString;
        this.line = line;
    }

    public boolean isValue() {
        return value;
    }

    public void setValua(boolean value) {
        this.value = value;
    }

    @Override
    public Object getStatement() {
        return this.getStatement();
    }

    @Override
    public void setStatement(Object statement) {
        this.statement = statement;
    }

    @Override
    public int getStartPostion() {
        return startPostion;
    }

    @Override
    public void setStartPostion(int startPostion) {
        this.startPostion = startPostion;
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
        return this.keyVar ;
    }

    @Override
    public void setKeyVar(String keyVar) {
        this.keyVar = keyVar;
    }

    @Override
    public String getStatementString() {
        return statementString;
    }

    @Override
    public void setStatementString(String statementString) {
        this.statementString = statementString;
    }
}
