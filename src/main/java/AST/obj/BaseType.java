package AST.obj;

import org.eclipse.jdt.core.dom.SimpleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BaseType {
    public static Logger logger = LoggerFactory.getLogger(BaseType.class);
    private String type;
    private List<String> argurements;

    public BaseType() {
        this.argurements = new ArrayList<>();
    }

    public BaseType(String type, List<String> argurements) {
        this.type = type;
        this.argurements = new ArrayList<>();
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

    public void setArgurements(List argurements) {
        for (Object type : argurements) {
            if (type instanceof SimpleType) {
                this.argurements.add(((SimpleType) type).getName().getFullyQualifiedName());
            } else {
                logger.info("Chưa xu ly:setArgurements " + type.toString());
            }
        }
    }
}
