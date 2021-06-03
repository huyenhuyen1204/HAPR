package gui.views;

import faultlocalization.configure.Configuration;
import faultlocalization.objects.SuspiciousPosition;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.FileHelper;
import util.GuiHelper;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class using to create code viewer pane with syntax styled for C++
 *
 * @author zizoz
 */

public class SourceCodeView {

    private static Logger logger = LoggerFactory.getLogger(SourceCodeView.class);

    private static final String[] KEYWORDS = new String[]{
            "asm", "else", "new", "this", "auto", "enum", "operator", "throw",
            "explicit", "private", "true", "break", "export", "protected",
            "try", "case", "extern", "public", "typedef", "catch", "false", "register",
            "typeid", "reinterpret_cast", "typename", "class", "for",
            "return", "union", "const", "friend", "const_cast", "goto",
            "using", "continue", "if", "sizeof", "virtual", "default", "inline", "include",
            "static", "delete", "static_cast", "volatile", "do", "struct",
            "wchar_t", "mutable", "switch", "while", "dynamic_cast", "namespace", "template"
    };

    private static final String[] PRIMITIVES = new String[]{
            "bool", "char", "float", "double", "void", "unsigned", "long", "short", "signed", "int"
    };

    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String PRIMITIVE_PATTERN = "\\b(" + String.join("|", PRIMITIVES) + ")\\b";
    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String SEMICOLON_PATTERN = "\\;";
    private static final String STRING_PATTERN = "\"([^\"]|\n)*\"";
    private static final String COMMENT_PATTERN = "(/\\*([^*]|[\\r\\n]|(\\*+([^*/]|[\\r\\n])))*\\*+/)|(//.*)";

    private static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                    + "|(?<PRIMITIVE>" + PRIMITIVE_PATTERN + ")"
                    + "|(?<PAREN>" + PAREN_PATTERN + ")"
                    + "|(?<BRACE>" + BRACE_PATTERN + ")"
                    + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                    + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                    + "|(?<STRING>" + STRING_PATTERN + ")"
                    + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
    );

    private CodeArea codeArea;
    private List<String> sourceCodeTex;
    private List<SuspiciousPosition> suspiciousPositionList;
    private List<String> suspiciousClassNameList; // To hightlight

    private Map<Integer, Tab> openTabs = new HashMap<>();

    private String projectName;

    private String projectPath;

    //========== Constructors & gettors & settors ==========
    public SourceCodeView(String projectPath, String projectName) {
        this.projectName = projectName;
        this.projectPath = projectPath;
        this.suspiciousClassNameList = new ArrayList<>();
        this.codeArea = new CodeArea();
        suspiciousPositionList = new ArrayList<>();
        readSuspicious();

    }

    public List<SuspiciousPosition> getSuspiciousPositionList() {
        return suspiciousPositionList;
    }

    public CodeArea getCodeArea() {
        return codeArea;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getProjectPath() {
        return projectPath;
    }

    //========== End constructors & gettors & settors ==========

    /**
     * Get main UI of source code viewer
     *
     * @param isHighlight true if user want to hightlight and focus to a function
     * @return normal pane
     */
    public AnchorPane getSourceCodePane(File file, boolean isHighlight) {
        this.sourceCodeTex = FileHelper.readDataAsList(file.getAbsolutePath());
        CodeArea codeArea = new CodeArea();
        String sampleCode = String.join("\n", sourceCodeTex);
        codeArea.getStylesheets().add("/css/keywords.css");
        codeArea.getStylesheets().add("/css/hightlightFL.css");
        codeArea.replaceText(0, 0, sampleCode);

        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        codeArea.setStyleSpans(0, computeHighlighting(codeArea.getText()));
        codeArea.setEditable(false);
        VirtualizedScrollPane<CodeArea> virtualizedScrollPane = new VirtualizedScrollPane<>(codeArea);

        if (isHighlight) {
            hightlightSourceCode(file, codeArea);
        }
        AnchorPane anchorPane = new AnchorPane(virtualizedScrollPane);
        AnchorPane.setBottomAnchor(virtualizedScrollPane, 0.0);
        AnchorPane.setTopAnchor(virtualizedScrollPane, 0.0);
        AnchorPane.setLeftAnchor(virtualizedScrollPane, 0.0);
        AnchorPane.setRightAnchor(virtualizedScrollPane, 0.0);
        return anchorPane;
    }

    /**
     * Highlight and focus a line
     */
    private void highlightLineCode(int line, float ratio, CodeArea codeArea, String color) {
        try {
            int start = line - 1;
            int end = line - 1;
            int startPos = codeArea.position(start, 0).toOffset();
            int endPos = codeArea.position(end, sourceCodeTex.get(end).length()).toOffset();

            File file = new File(getClass().getResource("/css/hightlightFL.css").getFile());
            GuiHelper.writeStyleClassToStyleFL(file, ratio, color);
            String styleClass = "fl" + String.valueOf(ratio).replace(".", "_");

            codeArea.setStyleClass(startPos, endPos, styleClass);
        } catch (Exception e) {
            // TODO: fix bug of displaying file localename.c
        }
    }

    private void hightlightSourceCode(File file, CodeArea codeArea) {
        if (suspiciousClassNameList.size() > 0) {
            suspiciousPositionList.forEach(suspiciousPosition -> {
                if (suspiciousPosition != null) {
                    if (isClassSus(file.getAbsolutePath(), suspiciousPosition.classPath)) {
//                        if (file.getName().contains(suspiciousPosition.classPath)) {
                            highlightLineCode(suspiciousPosition.lineNumber, suspiciousPosition.ratio, codeArea, "red");
//                        }
                    }
                }
            });
        }
    }

    public void viewSourceCode(File file, TabPane tabPane) {
        Tab sourceCodeTab = new Tab(file.getName());

        int tabId = file.hashCode();
        if (openTabs != null) {
            if (!openTabs.containsKey(tabId)) {
                tabPane.getTabs().add(sourceCodeTab);
                this.openTabs.put(tabId, sourceCodeTab);
                if (!file.isDirectory() && !file.getName().contains(".jar") && !file.getName().contains(".zip")) {
                    sourceCodeTab.setContent(getSourceCodePane(file, true));
                }
            } else {
                tabPane.getSelectionModel().select(openTabs.get(tabId));
            }
        }
        tabPane.getSelectionModel().select(sourceCodeTab);
        sourceCodeTab.setOnClosed(event -> {
            openTabs.remove(tabId);
        });
    }

    public void readSuspicious() {
        String pathSuspiciousPosition = Configuration.suspPositionsFilePath + projectName
                + File.separator + Configuration.faultLocalizationMetric + ".txt";
        if (FileHelper.isValidPath(pathSuspiciousPosition)) {
            suspiciousPositionList = FileHelper.readSuspiciousCodeFromFile(pathSuspiciousPosition, suspiciousClassNameList);
        } else {
            logger.error("File not found: " + pathSuspiciousPosition);
        }
    }

    public void setEditable(boolean isEditable) {
        codeArea.setEditable(isEditable);
    }


    /**
     * Computing highlighting code syntax after edit code
     *
     * @param text code need to be computed
     * @return all style for code area
     */
    public static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while (matcher.find()) {
            System.out.println("Hello");
            String styleClass = matcher.group("KEYWORD") != null ? "keyword" :
                    matcher.group("PRIMITIVE") != null ? "primitive" :
                            matcher.group("PAREN") != null ? "paren" :
                                    matcher.group("BRACE") != null ? "brace" :
                                            matcher.group("BRACKET") != null ? "bracket" :
                                                    matcher.group("SEMICOLON") != null ? "semicolon" :
                                                            matcher.group("STRING") != null ? "string" :
                                                                    matcher.group("COMMENT") != null ? "comment" :
                                                                            null; /* never happens */
            assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }

    //check xem có thuộc class nghi ngờ không???
    public boolean isSuspicious(String pathClass) {
//        String pkgName = pathClass.replace(projectPath + File.separator + projectName + File.separator, "");
        String pkgName = "";
        if (!projectName.contains("_")) {
            pkgName = pathClass.replace(projectPath + File.separator + projectName + File.separator, "");
            pkgName = pkgName.replace(File.separator, ".").replace(".java", "");
        } else { // FOR OASIS
            String src = "src" + File.separator + "main" + File.separator + "java" + File.separator;
            pkgName = pathClass.replace(projectPath + File.separator + projectName + File.separator + src, "");
            pkgName = pkgName.replace(File.separator, ".").replace(".java", "");
        }
        if (!pkgName.equals("")) {
            if (pathClass.endsWith(".java")) {
                for (String susClassName : suspiciousClassNameList) {
                    pkgName = pkgName.replace(File.separator, ".").replace(".java", "");
                    if (pkgName.equals(susClassName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public boolean isClassSus(String classpath, String classname) {
        if (classpath.endsWith(".java")) {
            String pkgName = "";
            if (!projectName.contains("_")) {
                pkgName = classpath.replace(projectPath + File.separator + projectName + File.separator, "");
                pkgName = pkgName.replace(File.separator, ".").replace(".java", "");
            } else { // FOR OASIS
                String src = "src" + File.separator + "main" + File.separator + "java" + File.separator;
                pkgName = classpath.replace(projectPath + File.separator + projectName + File.separator + src, "");
                pkgName = pkgName.replace(File.separator, ".").replace(".java", "");
            }
            if (pkgName.equals(classname)) {
                return true;
            }
        }

        return false;
    }
}