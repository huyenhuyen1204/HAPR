package AST.nodes;


/**
 * Created by dinht_000 on 3/29/2017.
 */


public class VisibleElementNode extends JavaNode {

    public static final String PUBLIC_MODIFIER = "public";
    public static final String DEFAULT_MODIFIER = "default";
    public static final String PROTECTED_MODIFIER = "protected";
    public static final String PRIVATE_MODIFIER = "private";

    private String visibility = DEFAULT_MODIFIER;

    private boolean isStatic = false;

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }
}
