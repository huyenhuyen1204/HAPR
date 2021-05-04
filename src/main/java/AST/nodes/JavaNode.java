package AST.nodes;


import org.eclipse.jdt.core.dom.ASTNode;

public class JavaNode extends Node {

    protected boolean isFinal = false;

    protected ASTNode astNode;

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }

//    protected NodePosition startPosition;
//    protected NodePosition endPosition;
//
//    public static class NodePosition implements Serializable {
//        private int lineNumber;
//        private int columnNumber;
//
//        public NodePosition(int lineNumber, int columnNumber) {
//            this.lineNumber = lineNumber;
//            this.columnNumber = columnNumber;
//        }
//
//        public int getLineNumber() {
//            return lineNumber;
//        }
//
//        public int getColumnNumber() {
//            return columnNumber;
//        }
//    }
}
