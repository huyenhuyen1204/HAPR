package AST.node;

import java.util.ArrayList;
import java.util.List;

public class InitInMethodNode extends InitNode{

    private MethodNode methodNode;

    public MethodNode getMethodNode() {
        return methodNode;
    }

    public void setMethodNode(MethodNode methodNode) {
        this.methodNode = methodNode;
    }


    public InitInMethodNode(int line, String varname, Object type,MethodNode methodNode) {
        super();
        this.line = line;
        this.methodNode = methodNode;
        this.varname = varname;
        setType();
        this.statements = new ArrayList();
    }
    @Override
    public void addStatement(StatementNode stm) {
        this.statements.add(stm);
    }

    @Override
    public String getVarname() {
        return this.varname;
    }

    @Override
    public void setVarname(String varname) {
        this.varname = varname;
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
    public List getStatements() {
        return this.statements;
    }

    @Override
    public void setStatements(List statements) {
        this.statements = statements;
    }

    @Override
    public Object getType() {
        return this.type;
    }

    @Override
    public void setType(Object type) {
        this.type = type;
    }
}
