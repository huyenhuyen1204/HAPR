package core.object;

import core.fix.FixType;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;

import java.util.List;

public class CandidateString extends Candidate {
    private List<StringModify> stringModifies;
    private List<DiffMatchPatch.Diff> diffs;
    private String classname;
    private String methodName;
    private String varError;
    private int line;

    public CandidateString(int line, String className, String methodName, FixType fixType, List<StringModify> stringModifies,
                           List<DiffMatchPatch.Diff> diffs, String varError) {
        this.fixType = fixType;
        this.stringModifies = stringModifies;
        this.line = line;
        this.classname = className;
        this.methodName = methodName;
        this.diffs = diffs;
        this.varError = varError;
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

    public List<DiffMatchPatch.Diff> getDiffs() {
        return diffs;
    }

    public void setDiffs(List<DiffMatchPatch.Diff> diffs) {
        this.diffs = diffs;
    }

    public String toString() {
        return "{line:" + this.line + ", " +
                "classname:" + this.classname + ", " +
                "methodname:" + this.classname + ", " +
                "methodname:" + this.classname + ", " +
                diffsToString() + "}";
    }

    private String diffsToString() {
        String s = "";
        if (diffs != null) {
            for (DiffMatchPatch.Diff diff : diffs) {
                s += " -" + diff.toString() + "\n";
            }
        }
        return s;
    }

    public boolean equalsDiff(List<DiffMatchPatch.Diff> diffs) {
        if (this.diffs.size() == diffs.size()) {
            for (int i = 0; i < diffs.size(); i++) {
                if (!this.diffs.get(i).equals(diffs.get(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
