package AST.stm;

import AST.stm.abstrct.InitStatement;

import java.util.ArrayList;
import java.util.List;

public class InitInMethodStm extends InitStatement {
    private List params;
    private String methodName;

    public List getParams() {
        return params;
    }

    public void setParams(List params) {
        this.params.addAll(params);
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public InitInMethodStm(Object type, String varname, Object value, String methodName, List paramss, int line) {
        this.params = new ArrayList();
        this.params.addAll(paramss);
        this.value = value;
        this.type = type;
        this.methodName = methodName;
        this.line = line;
        this.varName = varname;
    }

    public InitInMethodStm() {
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
