package core.object;

import core.fix.FixType;

public class StringModify {
    public StringOperation stringOperation;
    private int startChar;
    private int endChar;
    private String charReplace;

    public StringModify(StringOperation stringOperation, int startChar, int endChar, String charReplace) {
        this.stringOperation = stringOperation;
        this.startChar = startChar;
        this.endChar = endChar;
        this.charReplace = charReplace;
    }

    public int getStartChar() {
        return startChar;
    }

    public void setStartChar(int startChar) {
        this.startChar = startChar;
    }

    public int getEndChar() {
        return endChar;
    }

    public void setEndChar(int endChar) {
        this.endChar = endChar;
    }

    public String getCharReplace() {
        return charReplace;
    }

    public void setCharReplace(String charReplace) {
        this.charReplace = charReplace;
    }
}
