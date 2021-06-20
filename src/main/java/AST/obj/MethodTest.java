package AST.obj;

import AST.node.MethodNode;
import AST.stm.abstrct.AssertStatement;

import java.util.ArrayList;
import java.util.List;

public class MethodTest {
    private MethodNode methodNode;
    private String methodName;
    private List<AssertStatement> assertList;

    public MethodTest() {
    }

    public MethodTest (String methodName, MethodNode methodNode) {
        this.methodName = methodName;
        this.methodNode = methodNode;
        assertList = new ArrayList<>();
    }
    public void addToAsserts (AssertStatement ass) {
        this.assertList.add(ass);
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<AssertStatement> getAssertList() {
        return assertList;
    }

    public void setAssertList(List<AssertStatement> assertList) {
        this.assertList = assertList;
    }

    public MethodNode getMethodNode() {
        return methodNode;
    }

    public void setMethodNode(MethodNode methodNode) {
        this.methodNode = methodNode;
    }
}
