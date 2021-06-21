import java.nio.charset.Charset;

public class Hello {
    public static void main(String[] args) {
        String a = "Nguyễn Văn Hoàn";
        String b = "Họ tên";
        System.out.println(a);
        System.out.println(String.format("file.encoding: %s", System.getProperty("file.encoding")));
        System.out.println(String.format("defaultCharset: %s", Charset.defaultCharset().name()));
    }
}
