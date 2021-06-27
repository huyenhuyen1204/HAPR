package AST.stm;

import AST.stm.abstrct.InitStatement;

import java.util.List;

public class InitInClassStm extends InitStatement {
    private String classname;
    private List statements;

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public InitInClassStm(Object type, String varname, Object value, String classname, int line) {
        super();
        this.classname = classname;
        this.value = value;
        this.type = type;
        this.varName = varname;
        this.line = line;
    }

    public void addStatement(Object stm) {
        this.statements.add(stm);
    }

    public List getStatements() {
        return statements;
    }

    public void setStatements(List statements) {
        this.statements = statements;
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
    public String getVarName() {
        return this.varName;
    }

    @Override
    public void setVarName(String varName) {
        this.varName = varName;
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
    public Object getValue() {
        return this.value;
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
    }
}
