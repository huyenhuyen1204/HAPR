package AST.stm.node;

import AST.stm.abst.StatementNode;

public class BaseVariableNode extends StatementNode {


    public BaseVariableNode(int line, String keyVar, String stmString) {
        super();
        this.line = line;
        this.keyVar = keyVar;
        this.statementString = stmString;
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
}
