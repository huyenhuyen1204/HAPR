package AST.obj;

import java.util.ArrayList;
import java.util.List;

public class MethodCalled {
    private String methodName;
    private List<String> agurementTypes = null;

    public MethodCalled(String methodName, List<String> agurementTypes) {
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

    public List<String> getAgurementTypes() {
        return agurementTypes;
    }

    public void setAgurementTypes(List<String> agurementTypes) {
        this.agurementTypes = agurementTypes;
    }
}
