package AST.stm.abstrct;

public abstract class Statement {
    protected int line;
    public abstract int getLine();

    public abstract void setLine(int line) ;
}
