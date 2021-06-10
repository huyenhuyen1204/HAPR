//package AST.stm;
//
//import common.config.AccessRange;
//
//public class VariableElement {
//    private String accessRangeName;
//    private String varName;
//    private Object type;
//    private Object value;
//    public AccessRange accessRange;
//    private String params;
//    private int line;
//
//    public VariableElement() {
//    }
//
//    public VariableElement(AccessRange accessRange, String scopeAccessName, String params, String varName, Object type, Object value, int line) {
//        this.accessRangeName = scopeAccessName;
//        this.varName = varName;
//        this.params = params;
//        this.type = type;
//        this.value = value;
//        this.accessRange = accessRange;
//        this.line = line;
//    }
//
//    public String getAccessRangeName() {
//        return accessRangeName;
//    }
//
//    public void setAccessRangeName(String accessRangeName) {
//        this.accessRangeName = accessRangeName;
//    }
//
//    public AccessRange getAccessRange() {
//        return accessRange;
//    }
//
//    public void setAccessRange(AccessRange accessRange) {
//        this.accessRange = accessRange;
//    }
//
//    public String getVarName() {
//        return varName;
//    }
//
//    public void setVarName(String varName) {
//        this.varName = varName;
//    }
//
//    public Object getType() {
//        return type;
//    }
//
//    public void setType(Object type) {
//        this.type = type;
//    }
//
//    public Object getValue() {
//        return value;
//    }
//
//    public void setValue(Object value) {
//        this.value = value;
//    }
//
//    public String getParams() {
//        return params;
//    }
//
//    public void setParams(String params) {
//        this.params = params;
//    }
//
//    public int getLine() {
//        return line;
//    }
//
//    public void setLine(int line) {
//        this.line = line;
//    }
//}
