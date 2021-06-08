import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

public class MyTest1 extends TestCase{
    @Test
    public void test1() {
        Debuggee debuggee = new Debuggee(1000, "Greeen");
        try {
            Runtime.getRuntime().exec(new String[]{"touch", "C:\\Users\\Dell\\IdeaProjects\\jdb\\libs\\hello.txt"});
            Assert.assertEquals(1, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void test2() {
        Debuggee debuggee = new Debuggee(1000, "Greeen");
        int a = 100000;
        try {
            Runtime.getRuntime().exec(new String[]{"touch", "C:\\Users\\Dell\\IdeaProjects\\jdb\\libs\\hello.txt"});
           Assert.assertEquals(1, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
