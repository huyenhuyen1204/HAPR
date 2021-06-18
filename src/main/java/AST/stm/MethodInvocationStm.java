package AST.stm;

import AST.stm.abstrct.Statement;

import java.util.ArrayList;
import java.util.List;

public class MethodInvocationStm extends Statement {
    private String varName;
    private Object typeVarClass;
    private String methodCalled = null; // eg: student.getname() - method called is getname
    //for list method call eg: customerList.get(0).toString()
    // methods called: get, tostring
    private List<String> methodsCalled;

    public MethodInvocationStm() {
    }

    public MethodInvocationStm(List<String> methods) {
        methodsCalled = new ArrayList<>();
        this.varName = methods.get(methods.size() -1);
        for (int i = methods.size()-2; i >=0; i --) {
            methodsCalled.add(methods.get(i));
        }
    }

    public List<String> getMethodsCalled() {
        return methodsCalled;
    }

    public void setMethodsCalled(List<String> methodsCalled) {
        this.methodsCalled = methodsCalled;
    }

    public MethodInvocationStm(String varName, String methodCalled) {
        this.varName = varName;
        this.methodCalled = methodCalled;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public String getMethodCalled() {
        return methodCalled;
    }

    public void setMethodCalled(String methodCalled) {
        this.methodCalled = methodCalled;
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
