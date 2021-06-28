package AST.stm;

import AST.node.Node;
import AST.node.Node;
import AST.stm.abstrct.Statement;

public class AssignmentNode extends Node {
    private Node leftSide;
    private Node rightNode;
    private String assignmentOperator;

    public AssignmentNode(Node leftSide, Node rightNode) {
        this.leftSide = leftSide;
        this.rightNode = rightNode;
    }

    public Node getLeftSide() {
        return leftSide;
    }

    public void setLeftSide(Node leftSide) {
        this.leftSide = leftSide;
    }

    public Node getRightNode() {
        return rightNode;
    }

    public void setRightNode(Node rightNode) {
        this.rightNode = rightNode;
    }

    public String getAssignmentOperator() {
        return assignmentOperator;
    }

    public void setAssignmentOperator(String assignmentOperator) {
        this.assignmentOperator = assignmentOperator;
    }

}
