package AST.node;

import AST.obj.BaseType;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class InitInClassNode extends InitNode{
    public InitInClassNode() {
        super();
    }
    public InitInClassNode(String varname, int line, Object type) {
        this.varname = varname;
        this.line = line;
        addType(type);
        this.statements = new ArrayList();
    }

    private void addType(Object type) {
        if (type instanceof ParameterizedType) {
            BaseType baseType = new BaseType();
            baseType.setType(((ParameterizedType) type).getTypeName());
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            baseType.setArgurements(types);
        } else {
            this.setType(type);
        }
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
