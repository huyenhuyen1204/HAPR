//package AST.node;
//
//import AST.obj.BaseType;
//
//import org.eclipse.jdt.core.dom.ParameterizedType;
//import java.util.ArrayList;
//import java.util.List;
//
//public class InitInClassNode extends InitNode{
//    public InitInClassNode() {
//        super();
//    }
//    public InitInClassNode(String varname, int line, Object type) {
//        this.varname = varname;
//        this.line = line;
//        addType(type);
//        this.statementsUsed = new ArrayList();
//    }
//
//    private void addType(Object type) {
//        if (type instanceof ParameterizedType) {
//            BaseType baseType = new BaseType();
//            baseType.setType(((ParameterizedType) type).getType().toString());
//            List types = ((ParameterizedType) type).typeArguments();
//            baseType.setArgurements(types);
//            this.setType(baseType);
//        } else {
//            this.setType(type);
//        }
//    }
//
//    @Override
//    public String getVarname() {
//        return this.varname;
//    }
//
//    @Override
//    public void setVarname(String varname) {
//        this.varname = varname;
//    }
//
//    @Override
//    public int getLine() {
//        return this.line;
//    }
//
//    @Override
//    public void setLine(int line) {
//        this.line = line;
//    }
//
//    @Override
//    public List getStatementsUsed() {
//        return this.statementsUsed;
//    }
//
//    @Override
//    public void setStatementsUsed(List statementsUsed) {
//        this.statementsUsed = statementsUsed;
//    }
//
//    @Override
//    public Object getType() {
//        return this.type;
//    }
//
//    @Override
//    public void setType(Object type) {
//        this.type = type;
//    }
//
//    @Override
//    public void addStatement(StatementNode obj) {
//        this.statementsUsed.add(obj);
//    }
//}
