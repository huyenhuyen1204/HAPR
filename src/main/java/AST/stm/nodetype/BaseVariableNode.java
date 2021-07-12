package AST.stm.nodetype;

import AST.stm.abst.StatementNode;

public class BaseVariableNode extends StatementNode {


    public BaseVariableNode(int line, String keyVar, Object stmNode) {
        super();
        this.line = line;
        this.keyVar = keyVar;
        this.statement = stmNode;
        this.statementString = stmNode.toString();
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
