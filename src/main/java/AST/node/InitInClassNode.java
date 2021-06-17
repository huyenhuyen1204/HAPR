package AST.node;

import java.util.ArrayList;
import java.util.List;

public class InitInClassNode extends InitNode{
    public InitInClassNode() {
        super();
    }
    public InitInClassNode(String varname, int line, Object type) {
        this.varname = varname;
        this.line = line;
        this.type = type;
        this.statements = new ArrayList();
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

    @Override
    public void addStatement(StatementNode obj) {
        this.statements.add(obj);
    }
}