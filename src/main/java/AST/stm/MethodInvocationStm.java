package AST.stm;

import AST.obj.MethodCalled;
import AST.stm.abstrct.Statement;
import util.JavaLibraryHelper;

import java.util.ArrayList;
import java.util.List;

public class MethodInvocationStm extends Statement {
    private String varName;
    private String typeVar;
    private String expression;
//    private String methodCalled = null; // eg: student.getname() - method called is getname
    //for list method call eg: customerList.get(0).toString()
    // methods called: get, tostring
    private List<MethodCalled> methodsCalled = null; //customerList.get(0).toString() -> toString, get

    public MethodInvocationStm() {
        this.methodsCalled = new ArrayList<>();
    }
    public MethodInvocationStm(int line, String expression) {
        this.methodsCalled = new ArrayList<>();
        this.line = line;
        this.expression = expression;
    }

    public MethodInvocationStm(String varName, String methodName, List<Object> argTypes, int line, String expression) {
        this.varName = varName;
        this.line = line;
        this.methodsCalled = new ArrayList<>();
        addMethodCall(methodName, argTypes);
        this.expression = expression;
    }

    public MethodInvocationStm(String varname, List<MethodCalled> methods) {
        methodsCalled = new ArrayList<>();
        this.varName = varname;
        for (int i = methods.size()-1; i >=0; i --) {
            methodsCalled.add(methods.get(i));
        }
    }

    public void addMethodCall(String methodName, List<Object> argTypes) {
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

    public String getTypeVar() {
        if (typeVar != null) {
            String[] list = JavaLibraryHelper.getClassName(typeVar);
            if (list == null) {
                return typeVar;
            } else {
                if (list.length == 1) {
                    return list[0];
                } else {
                    return typeVar;
                }
            }
        } else {
            return null;
        }
    }

    public void setTypeVar(String typeVar) {
        this.typeVar = typeVar;
    }

    @Override
    public int getLine() {
        return line;
    }

    @Override
    public void setLine(int line) {
        this.line = line;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}
