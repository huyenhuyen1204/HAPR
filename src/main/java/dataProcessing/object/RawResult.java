package dataProcessing.object;

import java.io.File;

/**
 * Write result data process to csv file
 */
public class RawResult {
    private String className;
    private String line;
    private String buggyContent;
    private String fixedContent;
    private String type;
    private String ratio;

    private static final String SEMICOLON = ",";

    public RawResult(String className, String line, String buggyContent, String fixedContent, String type, String ratio) {
        this.className = className;
        this.line = line;
        this.buggyContent = buggyContent;
        this.fixedContent = fixedContent;
        this.type = type;
        this.ratio = ratio;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getBuggyContent() {
        return buggyContent;
    }

    public void setBuggyContent(String buggyContent) {
        this.buggyContent = buggyContent;
    }

    public String getFixedContent() {
        return fixedContent;
    }

    public void setFixedContent(String fixedContent) {
        this.fixedContent = fixedContent;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String toString() {
        return this.className + SEMICOLON + line + SEMICOLON + buggyContent + SEMICOLON + fixedContent + SEMICOLON + type
                + SEMICOLON + ratio;
    }
}
