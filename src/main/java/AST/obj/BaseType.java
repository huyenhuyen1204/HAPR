package AST.obj;

import java.lang.reflect.Type;
import java.util.List;

public class BaseType {
    private String type;
    private List<String> argurements;

    public BaseType() {
    }

    public BaseType(String type, List<String> argurements) {
        this.type = type;
        this.argurements = argurements;
    }

    public void addArgurements (String arg) {
        this.argurements.add(arg);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getArgurements() {
        return argurements;
    }

    public void setArgurements(Type[] argurements) {
        for (Type type : argurements) {
            this.argurements.add(type.getTypeName());
        }
    }
}
