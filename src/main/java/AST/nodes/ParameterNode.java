package AST.nodes;


/**
 * Created by dinht_000 on 3/29/2017.
 */

public class ParameterNode extends JavaNode {
    protected String type;
    protected boolean isFinal;

    public ParameterNode() {

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ParameterNode(String name, String type) {
        super();
        this.name = name;
        this.type = type;
    }

    @Override
    public String toString() {
        return "ParameterNode{" +
                "type='" + type + '\'' +
                '}';
    }

}
