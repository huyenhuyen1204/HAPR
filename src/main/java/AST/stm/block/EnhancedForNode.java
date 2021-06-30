package AST.stm.block;

import AST.stm.abst.ForNode;
import AST.stm.node.InitNode;

import java.util.ArrayList;
import java.util.List;

public class EnhancedForNode extends ForNode {
    private String listName;
    public EnhancedForNode() {
        super();
        this.initNodes = new ArrayList<>();
    }


    @Override
    public int getStartLine() {
        return this.startLine;
    }

    @Override
    public void setStartLine(int startFor) {
        this.startLine = startFor;
    }

    @Override
    public int getEndLine() {
        return endLine;
    }

    @Override
    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }

    @Override
    public List<InitNode> getInitNodes() {
        return this.initNodes;
    }

    @Override
    public void setInitNodes(List<InitNode> initNodes) {
        this.initNodes.addAll(initNodes);
    }
}
