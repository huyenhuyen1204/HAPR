package AST.stm;

import AST.stm.abstrct.Statement;

import java.util.List;

public class InfixExpressionNode extends Statement {
    private String operator;
    private Object left;
    private Object right;
    private List<Object> extendedOperands;

    public InfixExpressionNode() {
    }

    public InfixExpressionNode(String operator, Object left, Object right, List<Object> extendedOperands) {
        this.operator = operator;
        this.left = left;
        this.right = right;
        this.extendedOperands = extendedOperands;
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
        return 0;
    }

    @Override
    public void setLine(int line) {

    }
}
