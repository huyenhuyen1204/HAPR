package util;


import core.object.*;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

import static java.lang.String.format;
import static java.util.Arrays.asList;

public class JavaLibraryHelper {

    public static final Logger logger = LoggerFactory.getLogger(JavaLibraryHelper.class);

//    public static void compareTwoString(String a, String b) {
//        String[] listA = a.split(" ");
//        String[] listB = b.split(" ");
//        if (listA.length != listB.length) {
//            System.out.println("Diff");
//        } else {
//            for (int i = 0; i < a.length(); i++) {
//                if (a.charAt(i) != b.charAt(i)) {
//                    System.out.println("At " + i + ": " + a.charAt(i) + " - " + b.charAt(i));
//                }
//            }
//        }
//    }
    public static StringComparisonResult compareTwoString(String expected, String actual, DebugData debugData) {
        DiffMatchPatch dmp = new DiffMatchPatch();
        LinkedList<DiffMatchPatch.Diff> diff = dmp.diffMain(actual, expected, false);
        if (diff.get(0).text.length() == actual.length() && diff.get(0).operation.name().equals("EQUAL")) {
//            debugData.setIndexActual(actual.length() + debugData.getIndexActual());
            debugData.setIndexExpected(actual.length());
            return new StringComparisonResult(true);
        }
        return new StringComparisonResult(diff);
    }

//    public static StringComparisonResult compareTwoString(String expected, String actual) {
//        DiffMatchPatch dmp = new DiffMatchPatch();
//        LinkedList<DiffMatchPatch.Diff> diff = dmp.diffMain(actual, expected, false);
//        if (diff.get(0).text.length() == actual.length() && diff.get(0).operation.name().equals("EQUAL")) {
//            return new StringComparisonResult(true);
//        }
//        return new StringComparisonResult(diff);
//    }

    public static ComparisonResult getStringComparisonResult(String expectedString, String actual, DebugData debugData) {
        StringComparisonResult stringComparisonResult = compareTwoString(expectedString, actual, debugData);
        if (!stringComparisonResult.isEquals()) {
            List<DiffMatchPatch.Diff> diffs = stringComparisonResult.getDifferentCharacters();
            List<StringModify> stringModifies = new ArrayList<>();
            int index = 0;
            int equalSize = 0;
            int i = 0;
            String expected = "";
            do {
                DiffMatchPatch.Diff diff = diffs.get(i);
                if (diff.operation.name().equals("EQUAL")) {
                    String stringEqual = diff.text;
                    expected += stringEqual;
                    equalSize += stringEqual.length();
                    index += stringEqual.length();
                    // DELETE + INSERT = REPLACE
                } else if (diff.operation.name().equals("DELETE") && (i + 1) <= diffs.size() &&
                        diffs.get(i + 1).operation.name().equals("INSERT")) {
                    String stringDelete = diff.text;
                    String stringReplace = diffs.get(i + 1).text;
                    StringModify stringModify = new StringModify(StringOperation.REPLACE, index, index + stringDelete.length(),
                            diffs.get(i + 1).text);
                    index += stringDelete.length();
                    expected += stringReplace;
                    stringModifies.add(stringModify);
                    i++;
                } else if (diff.operation.name().equals("DELETE")) {
                    logger.error("Chua xu ly:diff.operation.name().equals(StringOperation.DELETE");
                } else if (diff.operation.name().equals("INSERT")) {
                    expected += diff.text;
                    logger.error("Chuw xu ly:diff.operation.name().equals(StringOperation.DELETE");
                }
                i++;
            } while (i < diffs.size() && index < actual.length());
            int percentEquals = equalSize * 100 / actual.length();
            return new ComparisonResult(percentEquals, expected, stringModifies);
        } else {
            return new ComparisonResult(100, actual, null);
        }
    }


    public static String removeFirstAndLastChars(String string) {
        if (string != null && string.length() > 0) {
            if (!string.equals("") && !string.equals("\"")) {
                if (string.charAt(0) == '"' && string.charAt(string.length() - 1) == '"') {
                    return string.substring(1, string.length() - 1).replace("\\n", "\n");
                }
                return string.replace("\\n", "\n");
            } else {
                return string.replace("\\n", "\n");
            }
        } else {
            return string.replace("\\n", "\n");
        }

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
