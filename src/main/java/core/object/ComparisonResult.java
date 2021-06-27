package core.object;

import java.util.ArrayList;
import java.util.List;

public class ComparisonResult {
    private int percent;
    private String expected;
    private List<StringModify> stringModifies = null;

    public ComparisonResult(int percent, String expected, List<StringModify> stringModifies) {
        this.percent = percent;
        this.expected = expected;
        if (stringModifies != null) {
            this.stringModifies = new ArrayList<>();
            this.stringModifies.addAll(stringModifies);
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
}
