package AST.stm.node;

import AST.obj.MethodCalled;
import AST.stm.abst.StatementNode;
import util.JavaLibraryHelper;

import java.util.ArrayList;
import java.util.List;

public class  MethodInvocationStmNode extends StatementNode {
    public static final String type = "MethodInvocation";
    private String typeVar;
//    private String methodCalled = null; // eg: student.getname() - method called is getname
    //for list method call eg: customerList.get(0).toString()
    // methods called: get, tostring
    private List<MethodCalled> methodsCalled = null; //customerList.get(0).toString() -> toString, get

    public MethodInvocationStmNode() {
        this.methodsCalled = new ArrayList<>();
    }
    public MethodInvocationStmNode(int line, String statementString) {
        this.methodsCalled = new ArrayList<>();
        this.line = line;
        this.statementString = statementString;
    }

    public MethodInvocationStmNode(String varName, String methodName, List<Object> argTypes, int line, String statementString) {
        this.keyVar = varName;
        this.line = line;
        this.methodsCalled = new ArrayList<>();
        addMethodCall(methodName, argTypes);
        this.statementString = statementString;
    }



    public MethodInvocationStmNode(String keyVar, List<MethodCalled> methods) {
        methodsCalled = new ArrayList<>();
        this.keyVar = keyVar;
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

    @Override
    public String getKeyVar() {
        return this.keyVar;
    }

    @Override
    public void setKeyVar(String keyVar) {
        this.keyVar = keyVar;
    }

    @Override
    public String getStatementString() {
        return this.statementString;
    }

    @Override
    public void setStatementString(String statementString) {
        this.statementString = statementString;
    }

    @Override
    public Object getStatement() {
        return this.statement;
    }

    @Override
    public void setStatement(Object statement) {
        this.statement = statement;
    }

    @Override
    public int getStartPostion() {
        return this.startPostion;
    }

    @Override
    public void setStartPostion(int startPostion) {
        this.startPostion = startPostion;
    }
}
