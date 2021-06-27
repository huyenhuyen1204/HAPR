package core.object;

public class VariableInfo {
    private String varname;
    private String value;
    private Object pointToMethod =  null;

    public VariableInfo(String varname, String value) {
        this.varname = varname;
        this.value = value;
    }

    public VariableInfo() {

    }

    public Object getPointToMethod() {
        return pointToMethod;
    }

    public void setPointToMethod(Object pointToMethod) {
        this.pointToMethod = pointToMethod;
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
}
