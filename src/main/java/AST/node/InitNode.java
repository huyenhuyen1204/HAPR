package AST.node;

import java.util.ArrayList;
import java.util.List;

public class InitNode {
    private int level;
    private int line;
    private String varname;
    private List<StatementNode> statementsUsed;
    private String type;
    public InitNode() {
        super();
    }
    public InitNode(int level, String varname,  String type, int line) {
        this.level = level;
        this.varname = varname;
        this.line = line;
        this.type = type;
        this.statementsUsed = new ArrayList();
    }

    public String getVarname() {
        return this.varname;
    }

    public void setVarname(String varname) {
        this.varname = varname;
    }

    public int getLine() {
        return this.line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public List getStatementsUsed() {
        return this.statementsUsed;
    }

    public void setStatementsUsed(List statementsUsed) {
        this.statementsUsed = statementsUsed;
    }

    public Object getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void addStatement(StatementNode obj) {
        this.statementsUsed.add(obj);
    }

}
