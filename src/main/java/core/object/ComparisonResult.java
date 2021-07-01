package core.object;

import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;

import java.util.ArrayList;
import java.util.List;

public class ComparisonResult {
    private boolean isEquals;
    private int percent;
    private String expected;
    private String actual;
    private List<StringModify> stringModifies = null;
    private List<DiffMatchPatch.Diff> diffs;

    public ComparisonResult(boolean isEquals, int percent, String expected, String actual, List<StringModify> stringModifies, List<DiffMatchPatch.Diff> diffList) {
        this.isEquals = isEquals;
        this.percent = percent;
        this.expected = expected;
        this.actual = actual;
        if (stringModifies != null) {
            this.stringModifies = new ArrayList<>();
            this.stringModifies.addAll(stringModifies);
        }
        if (diffList != null) {
            if (diffList.size() > 0) {
                this.diffs = new ArrayList<>();
                this.diffs.addAll(diffList);
            }
        }
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public String getExpected() {
        return expected;
    }

    public void setExpected(String expected) {
        this.expected = expected;
    }

    public List<StringModify> getStringModifies() {
        return stringModifies;
    }

    public void setStringModifies(List<StringModify> stringModifies) {
        this.stringModifies = stringModifies;
    }

    public List<DiffMatchPatch.Diff> getDiffs() {
        return diffs;
    }

    public void setDiffs(List<DiffMatchPatch.Diff> diffs) {
        this.diffs = diffs;
    }

    public boolean isEquals() {
        return isEquals;
    }

    public void setEquals(boolean equals) {
        isEquals = equals;
    }

    public String getActual() {
        return actual;
    }
}
