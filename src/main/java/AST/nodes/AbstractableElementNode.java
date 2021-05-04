package AST.nodes;


/**
 * Created by cuong on 3/28/2017.
 */
public class AbstractableElementNode extends VisibleElementNode {

    protected boolean isAbstract = false;

    public boolean isAbstract() {
        return isAbstract;
    }

    public void setAbstract(boolean anAbstract) {
        isAbstract = anAbstract;
    }
}
