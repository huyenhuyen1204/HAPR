package core.object;

import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;

import java.util.ArrayList;
import java.util.List;

public class SuspicionString {
    private String expected;
    private String actual;
    private BreakPointHit breakPointHit;
    private List<DiffMatchPatch.Diff> diffs;

    public SuspicionString(String expected, String actual, BreakPointHit breakPointHit, List<DiffMatchPatch.Diff> diffs) {
        this.expected = expected;
        this.actual = actual;
        this.breakPointHit = breakPointHit;
        if (diffs != null) {
            if (diffs.size() > 0) {
                this.diffs = new ArrayList<>();
                this.diffs.addAll(diffs);
            }
        }
    }

    public BreakPointHit getBreakPointInfo() {
        return breakPointHit;
    }

    public void setBreakPointInfo(BreakPointHit breakPointInfo) {
        this.breakPointHit = breakPointInfo;
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
        + breakPointHit.toString() + "}";
    }

}
