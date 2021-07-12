package AST.stm.node;

import AST.stm.abst.StatementNode;

import java.util.List;

public class InfixExpressionStmNode extends StatementNode {
    private String operator;
    private Object left;
    private Object right;
    private List<Object> extendedOperands;

    public InfixExpressionStmNode() {
    }

    public InfixExpressionStmNode(String operator, Object left, Object right,
                                  List<Object> extendedOperands, int line, String keyVar, String stmString) {
        this.operator = operator;
        this.left = left;
        this.right = right;
        this.extendedOperands = extendedOperands;
        this.line = line;
        this.keyVar = keyVar;
        this.statementString = stmString;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Object getLeft() {
        return left;
    }

    public void setLeft(Object left) {
        this.left = left;
    }

    public Object getRight() {
        return right;
    }

    public void setRight(Object right) {
        this.right = right;
    }

    public List<Object> getExtendedOperands() {
        return extendedOperands;
    }

    public void setExtendedOperands(List<Object> extendedOperands) {
        this.extendedOperands = extendedOperands;
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
        return statementString;
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
