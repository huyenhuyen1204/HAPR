import junit.framework.TestCase;

public class MyTest1 extends TestCase {
    public void test1() {
        Debuggee debuggee = new Debuggee(1000, "Greeen");
        try {
            Runtime.getRuntime().exec(new String[]{"touch", "C:\\Users\\Dell\\IdeaProjects\\jdb\\libs\\hello.txt"});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
