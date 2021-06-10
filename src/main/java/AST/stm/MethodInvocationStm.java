package AST.stm;

public class MethodInvocationStm {
    private String varClass;
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
}
