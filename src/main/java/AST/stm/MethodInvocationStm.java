package AST.stm;

import AST.obj.MethodCalled;
import AST.stm.abstrct.Statement;

import java.util.ArrayList;
import java.util.List;

public class MethodInvocationStm extends Statement {
    private String varName;
    private Object typeVarClass;
//    private String methodCalled = null; // eg: student.getname() - method called is getname
    //for list method call eg: customerList.get(0).toString()
    // methods called: get, tostring
    private List<MethodCalled> methodsCalled = null; //customerList.get(0).toString() -> toString, get

    public MethodInvocationStm() {
        this.methodsCalled = new ArrayList<>();
    }


    public MethodInvocationStm(String varName, String methodName, List<String> argTypes) {
        this.varName = varName;
        this.methodsCalled = new ArrayList<>();
        addMethodCall(methodName, argTypes);
    }

    public MethodInvocationStm(String varname, List<MethodCalled> methods) {
        methodsCalled = new ArrayList<>();
        this.varName = varname;
        for (int i = methods.size()-1; i >=0; i --) {
            methodsCalled.add(methods.get(i));
        }
    }

    public void addMethodCall(String methodName, List<String> argTypes) {
        MethodCalled methodCalled = new MethodCalled(methodName, argTypes);
        this.methodsCalled.add(methodCalled);
    }

    public List<MethodCalled> getMethodsCalled() {
        return methodsCalled;
    }

    public void setMethodsCalled(List<MethodCalled> methodsCalled) {
        this.methodsCalled = methodsCalled;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
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
