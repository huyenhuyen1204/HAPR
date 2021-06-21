package AST.obj;

import java.util.ArrayList;
import java.util.List;

public class MethodCalled {
    private String methodName;
    private List<Object> agurementTypes = null;

    public MethodCalled(String methodName, List<Object> agurementTypes) {
        this.methodName = methodName;
        if (agurementTypes.size() > 0) {
            this.agurementTypes = new ArrayList<>();
            this.agurementTypes.addAll(agurementTypes);
        }
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<Object> getAgurementTypes() {
        return agurementTypes;
    }

    public void setAgurementTypes(List<Object> agurementTypes) {
        this.agurementTypes = agurementTypes;
    }
}
