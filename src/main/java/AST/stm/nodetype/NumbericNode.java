package AST.stm.nodetype;

import AST.stm.abst.StatementNode;

public class NumbericNode extends StatementNode {
    public static final String type = "NumberLiteral";
    private Object numberic;

    public NumbericNode(Object numberic) {
        this.numberic = numberic;
    }

    public Object getNumberic() {
        return numberic;
    }

    public void setNumberic(Object numberic) {
        this.numberic = numberic;
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
