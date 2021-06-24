package util;


import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.asList;

public class JavaLibraryHelper {

    public static void compareTwoString(String a, String b) {
        String[] listA = a.split(" ");
        String[] listB = b.split(" ");
        if (listA.length != listB.length) {
            System.out.println("Diff");
        } else {
            for (int i = 0; i < a.length(); i++) {
                if (a.charAt(i) != b.charAt(i)) {
                    System.out.println("At " + i + ": " + a.charAt(i) + " - " + b.charAt(i));
                }
            }
        }
    }

    public static String removeFirstAndLastChars(String string) {
//        return string;
		return string.substring(1, string.length() - 1);
    }

    public static String convertStringToNumbers(String string) {
//        string = convertStringToStatement(string);
		char[] chars = string.toCharArray();
		String numbers = "";
		for (char c : chars) {
		    //accept "
		    if ((int) c != 34) {
                numbers += (int) c + ";";
            } else {
//		        numbers += c ;
            }
        }
		return numbers;
    }

    public static String convertNumbersToString(String string) {
        String[] numbers = string.split(";");
        String content = "";
        for (String number : numbers) {
            int readNumber = Integer.parseInt(number);
            content +=  Character.toString((char) readNumber);
        }
        return content;
    }

    public static void main(String[] args) {
        String arr = convertStringToNumbers("- Kiểu giao dịch: ");
        System.out.println(arr);
        String string = convertNumbersToString(arr);
        System.out.println(string);
    }

    public static void setUTF8() {
        System.setProperty("file.encoding", "UTF-8");
        Field charset = null;
        try {
            charset = Charset.class.getDeclaredField("defaultCharset");
            charset.setAccessible(true);
            charset.set(null, null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static URL[] classPathFrom(String classpath) {
        List<String> folderNames = split(classpath, classpathSeparator());
        URL[] folders = new URL[folderNames.size()];
        int index = 0;
        for (String folderName : folderNames) {
            folders[index] = urlFrom(folderName);
            index += 1;
        }
        return folders;
    }

    public static URL[] extendClassPathWith(String classpath, URL[] destination) {
        List<URL> extended = newLinkedList(destination);
        extended.addAll(asList(classPathFrom(classpath)));
        return extended.toArray(new URL[extended.size()]);
    }

    @SuppressWarnings("unchecked")
    private static <T> List<T> newLinkedList(T... elements) {
        return newLinkedList(asList(elements));
    }

    private static <T> List<T> newLinkedList(Collection<? extends T> collection) {
        List<T> newList = newLinkedList();
        return (List<T>) withAll(newList, collection);
    }

    private static <T> List<T> newLinkedList() {
        return new LinkedList<T>();
    }

    private static <T> Collection<T> withAll(Collection<T> destination, Iterable<? extends T> elements) {
        addAll(destination, elements);
        return destination;
    }

    private static <T> boolean addAll(Collection<T> destination, Iterable<? extends T> elements) {
        boolean changed = false;
        for (T element : elements) {
            changed |= destination.add(element);
        }
        return changed;
    }

    private static List<String> split(String chainedStrings, Character character) {
        return split(chainedStrings, format("[%c]", character));
    }

    private static List<String> split(String chainedStrings, String splittingRegex) {
        return asList(chainedStrings.split(splittingRegex));
    }

    private static URL urlFrom(String path) {
        URL url = null;
        try {
            url = openFrom(path).toURI().toURL();
        } catch (MalformedURLException e) {
            fail("Illegal name for '" + path + "' while converting to URL");
        }
        return url;
    }

    private static File openFrom(String path) {
        File file = new File(path);
        if (!file.exists()) {
            fail("File does not exist in: '" + path + "'");
        }
        return file;
    }

    private static void fail(String message) {
        throw new IllegalArgumentException(message);
    }

    private static Character classpathSeparator() {
        if (javaPathSeparator == null) {
            javaPathSeparator = File.pathSeparatorChar;
        }
        return javaPathSeparator;
    }

    private static Character javaPathSeparator;
}
