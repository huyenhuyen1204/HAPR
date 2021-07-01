package core.object;

import java.util.ArrayList;
import java.util.List;

public class BreakPointInfo {
    private int line;
    private String varname;
    private String value;
    private DebugPoint debugPoint;
    private List<Object> variableInfos;

    public BreakPointInfo(int line, String varname, String value, DebugPoint debugPoint) {
        this.line = line;
        this.varname = varname;
        this.value = value;
        this.debugPoint = debugPoint;
        this.variableInfos = new ArrayList<>();
    }

    public BreakPointInfo(int line, String keyVar, DebugPoint debugPoint) {
        this.line = line;
        this.varname = keyVar;
        this.debugPoint = debugPoint;
        this.variableInfos = new ArrayList<>();
    }

    public DebugPoint getDebugPoint() {
        return debugPoint;
    }

    public void setDebugPoint(DebugPoint debugPoint) {
        this.debugPoint = debugPoint;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getVarname() {
        return varname;
    }

    public void setVarname(String varname) {
        this.varname = varname;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<Object> getVariableInfos() {
        return variableInfos;
    }

    public void setVariableInfos(List<Object> variableInfos) {
        this.variableInfos = variableInfos;
    }

    public void addVariable(Object obj) {
        this.variableInfos.add(obj);
    }

    public String toString() {
        return "BreakPointInfo: {line:" + line + "," + "varname:" + varname + "}";
    }
}
