package AST.stm.node;

import AST.stm.abst.StatementNode;

public class AssignmentStmNode extends StatementNode {
    private StatementNode leftSide;
    private StatementNode rightNode;
    private String assignmentOperator;

    public AssignmentStmNode(StatementNode leftSide, StatementNode rightNode, int line, String keyVar, String stmString) {
        super();
        this.leftSide = leftSide;
        this.rightNode = rightNode;
        this.line = line;
        this.keyVar = keyVar;
        this.statementString = stmString;
    }

    public StatementNode getLeftSide() {
        return leftSide;
    }

    public void setLeftSide(StatementNode leftSide) {
        this.leftSide = leftSide;
    }

    public StatementNode getRightNode() {
        return rightNode;
    }

    public void setRightNode(StatementNode rightNode) {
        this.rightNode = rightNode;
    }

    public String getAssignmentOperator() {
        return assignmentOperator;
    }

    public void setAssignmentOperator(String assignmentOperator) {
        this.assignmentOperator = assignmentOperator;
    }

    @Override
    public int getLine() {
        return this.getLine();
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
