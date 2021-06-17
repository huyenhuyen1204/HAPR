package AST.stm;

import AST.stm.abstrct.Statement;

public class MethodInvocationStm extends Statement {
    private String varClass;
    private Object typeVarClass;
    private String methodName;

    public MethodInvocationStm() {
    }

    public MethodInvocationStm(String varClass, String methodName) {
        this.varClass = varClass;
        this.methodName = methodName;
    }

    public String getVarClass() {
        return varClass;
    }

    public void setVarClass(String varClass) {
        this.varClass = varClass;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object getTypeVarClass() {
        return typeVarClass;
    }

    public void setTypeVarClass(Object typeVarClass) {
        this.typeVarClass = typeVarClass;
    }

    @Override
    public int getLine() {
        return line;
    }

    @Override
    public void setLine(int line) {
        this.line = line;
    }
}
