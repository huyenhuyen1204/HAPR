package AST.stm.abst;

import AST.stm.node.InitNode;

import java.util.List;

public abstract class ForNode {
    protected int startLine;
    protected int endLine;

    protected List<InitNode> initNodes;

    public abstract int getStartLine();

    public abstract void setStartLine(int startFor);

    public abstract int getEndLine();

    public abstract void setEndLine(int endLine);

    public abstract List<InitNode> getInitNodes();

    public abstract void setInitNodes(List<InitNode> initNodes);
}