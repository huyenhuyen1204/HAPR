//package AST.node;
//
//import AST.obj.BaseType;
//import org.eclipse.jdt.core.dom.ParameterizedType;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class InitInMethodNode extends InitNode{
//
//    private MethodNode methodNode;
//
//    public MethodNode getMethodNode() {
//        return methodNode;
//    }
//
//    public void setMethodNode(MethodNode methodNode) {
//        this.methodNode = methodNode;
//    }
//
//
//    public InitInMethodNode(int line, String varname, Object type,MethodNode methodNode) {
//        super();
//        this.line = line;
//        this.methodNode = methodNode;
//        this.varname = varname;
//        addType(type);
//        this.statementsUsed = new ArrayList();
//    }
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
//    @Override
//    public void addStatement(StatementNode stm) {
//        this.statementsUsed.add(stm);
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
//}
