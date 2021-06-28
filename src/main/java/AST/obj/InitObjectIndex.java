package AST.obj;

public class InitObjectIndex {
    private int index;
    private String scope;

    public InitObjectIndex() {
    }

    public InitObjectIndex(int index, String scope) {
        this.index = index;
        this.scope = scope;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
