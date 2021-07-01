package core.object;

import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;

import java.util.ArrayList;
import java.util.List;

public class SuspicionString {
    private String expected;
    private String actual;
    private DebugPoint debugPoint;
    private List<DiffMatchPatch.Diff> diffs;

    public SuspicionString(String expected, String actual, DebugPoint debugPoint, List<DiffMatchPatch.Diff> diffs) {
        this.expected = expected;
        this.actual = actual;
        this.debugPoint = debugPoint;
        if (diffs != null) {
            if (diffs.size() > 0) {
                this.diffs = new ArrayList<>();
                this.diffs.addAll(diffs);
            }
        }
    }

    public DebugPoint getDebugPoint() {
        return debugPoint;
    }

    public void setDebugPoint(DebugPoint debugPoint) {
        this.debugPoint = debugPoint;
    }

    public List<DiffMatchPatch.Diff> getDiffs() {
        return diffs;
    }

    public void setDiffs(List<DiffMatchPatch.Diff> diffs) {
        this.diffs = diffs;
    }

    public String getExpected() {
        return expected;
    }

    public void setExpected(String expected) {
        this.expected = expected;
    }

    public String getActual() {
        return actual;
    }

    public void setActual(String actual) {
        this.actual = actual;
    }
    public String toString () {
        return  "SuspicionString: { expected: " + expected + "\n"
        + "actual: " + actual + "\n"
        + debugPoint.toString() + "\n" + diffsToString() +"}";
    }

    private String diffsToString() {
        String s = "";
        for (DiffMatchPatch.Diff diff : diffs) {
            s+= " -" + diff.toString() + "\n";
        }
        return s;
    }

}
