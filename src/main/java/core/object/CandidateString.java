package core.object;

import core.fix.FixType;

import java.util.List;

public class CandidateString extends Candidate {
     private List<StringModify> stringModifies;
    private String classname;
    private String methodName;
    private int line;

    public CandidateString(int line, String className, String methodName, FixType fixType, List<StringModify> stringModifies) {
        this.fixType = fixType;
        this.stringModifies = stringModifies;
        this.line = line;
        this.classname = className;
        this.methodName = methodName;
    }

    public FixType getFixType() {
        return fixType;
    }

    public void setFixType(FixType fixType) {
        this.fixType = fixType;
    }

    public List<StringModify> getStringModifies() {
        return stringModifies;
    }

    public void setStringModifies(List<StringModify> stringModifies) {
        this.stringModifies = stringModifies;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
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
}
