package core.object;

public class BreakPointHit {
    private String className;
    private String methodName;
    private int line;

    public BreakPointHit(String className, String methodName, int line) {
        this.className = className;
        addMethodName(methodName);
        this.line = line;
    }

    private void addMethodName(String methodName) {
        if (methodName.equals("<init>")) {
            this.methodName = className;
        } else {
            this.methodName = methodName;
        }
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }
    public String toString() {
        return "{className:" + className + ", " + "methodName:" + methodName + ", line:" + line + "}";
    }
}
