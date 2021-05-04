package dataProcessing;

import dataProcessing.object.Behavior;
import dataProcessing.object.RawResult;
import faultlocalization.configure.Configuration;
import faultlocalization.gzoltar.FL;
import faultlocalization.objects.SuspiciousPosition;
import org.eclipse.jgit.diff.*;
import util.FileHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

public class DataProcessing {
    public static final String PATH_SUBMITIONS = "C:\\Users\\Dell\\Desktop\\APR_test\\data_test";
    public static final String PATH_OUT_BEHAVIOR = "C:\\Users\\Dell\\Desktop\\APR_test\\detected_bug";
    public static final String PATH_SUSPICIUS_POSITION = "C:\\Users\\Dell\\IdeaProjects\\HAPR\\SuspiciousCodePositions";
    public static final String FILE_OUT_DATA_PROCESS = PATH_OUT_BEHAVIOR + "\\data_process.csv";


    public static void main(String[] args) {
//        String pathSubmits = "C:\\Users\\Dell\\Desktop\\APR_test\\mysql-files\\submits1243.txt";
        File file = new File("C:\\Users\\Dell\\Desktop\\APR_test\\mysql-files");
        File[] files = file.listFiles();
        assert files != null;
        for (File f : files) {
            List<RawResult> results = new ArrayList<>(); //For export csv file;
            Map<Integer, Integer> lines = new HashMap<>();
            int count = 0;
            String[] splits = f.getName().split("_");
            if (!splits[1].equals("7")) {
                String submitContent = FileHelper.readFile(f);
                String[] listSubmits = submitContent.split(";");
                if (listSubmits.length > 1) {
                    String code100 = listSubmits[0];
                    System.out.println(submitContent.replace(";", " "));
                    int id = Integer.parseInt(splits[3].replace(".txt", ""));
//                    if (id > 1000 && id <= 1500) {
//                        for (String file1 : listSubmits) {
//                            FileHelper.deleteFile(PATH_SUBMITIONS + File.separator + file1);
//                        }
//                        //STEP [1]: Copy result of: code100;codeBuggy1;CodeBuggy2. Run codeCopyData.sh to automatic copy
//                        step1(splits[1], submitContent);
//                    }
//                    //STEP [2]: Using JGit write difference and behavior off Buggy and behavior Fixed to PATH_OUT_BEHAVIOR
//                    step2(code100, listSubmits);
//                    //STEP [3]: Determine Fault localization of submit
//                    step3(listSubmits);
//                    //STEP [4]:  Analysis and write Result to file
                    step4(code100, listSubmits, lines, results, count);
            }
        }
    }

}

    /**
     * STEP [1] copy result of: code100;codeBuggy1;CodeBuggy2 ...
     * Run sh to automatic copy
     *
     * @param content submitContent
     */
    public static void step1(String problemId, String content) {
        try {
            String pathSh = "C:\\Program Files\\Git\\git-bash.exe C:\\Users\\Dell\\Desktop\\APR_test\\codeCopyData_" + problemId + ".sh";
            String cmd = pathSh + " " + content.replace(";", " ");
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(cmd);

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * Write difference and behavior off Buggy and Fixed to File
     *
     * @param code100
     * @param listSubmits
     */
    public static void step2(String code100, String[] listSubmits) {
        File fixedFolder = new File(PATH_SUBMITIONS + File.separator + code100);
        for (int i = 1; i < listSubmits.length; i++) {
            File buggyFolder = new File(PATH_SUBMITIONS + File.separator + listSubmits[i]);
            diffTwoFolder(buggyFolder, fixedFolder, PATH_OUT_BEHAVIOR);
        }
    }

    /**File not found
     * Determine Fault localization of submit
     *
     * @param listSubmits
     */
    public static void step3(String[] listSubmits) {
        for (int i = 1; i < listSubmits.length; i++) {
            FL.faultLocalization(PATH_SUBMITIONS, listSubmits[i], Configuration.faultLocalizationMetric);
        }
    }


    /**
     * Save all result data Processing to file
     *
     * @param code100
     * @param listSubmits
     */
    public static void step4(String code100, String[] listSubmits, Map<Integer, Integer> lines, List<RawResult> results, int count) {
        for (int i = 1; i < listSubmits.length; i++) {
            String pathSus = PATH_SUSPICIUS_POSITION + File.separator + listSubmits[i] + File.separator + Configuration.faultLocalizationMetric + ".txt";
            String pathToDetectedBug = PATH_OUT_BEHAVIOR + File.separator + listSubmits[i] + "_" + code100;
            extractBuggy(pathToDetectedBug, new File(pathSus), lines, results, count);
        }
        //
        String pathToFixedFolder = PATH_SUBMITIONS + File.separator + code100;
        //Read String from line
        for (int i = 0; i < results.size(); i++) {
            RawResult result = results.get(i);
            String pathToFile = PATH_SUBMITIONS + File.separator + result.getProjectName()
                    + File.separator + result.getClassName() + ".java";
            String[] lineSplits = result.getLine().replace("(", "").replace(")", "")
                    .split(";");
            String[] buggyLine = lineSplits[0].split("-");
            String[] fixedLine = lineSplits[1].split("-");
//            if (Integer.valueOf(buggyLine[1]) - Integer.valueOf(buggyLine[0]) < 5) {
            String buggyContent = FileHelper.readCodeFromLineToLine(new File(pathToFile), Integer.parseInt(buggyLine[0]),
                    Integer.parseInt(buggyLine[1]));
            String pathToFixedCode = pathToFixedFolder + File.separator + result.getClassName() + ".java";
            String fixedContend = FileHelper.readCodeFromLineToLine(new File(pathToFixedCode), Integer.parseInt(fixedLine[0]),
                    Integer.parseInt(fixedLine[1]));
            results.get(i).setBuggyContent(buggyContent);
            results.get(i).setFixedContent(fixedContend);
//            }

        }
//        WRITE to CSV in $PATH_OUT_BEHAVIOR
        if (results != null) {
            writeRawToCSV(results);
        }
    }

    public static void diffTwoFolder(File folderBuggy, File folderFixed, String pathOutBehavior) {
        if (folderBuggy != null && folderFixed != null) {
            File[] buggyFiles = folderBuggy.listFiles();
            File[] fixedFiles = folderFixed.listFiles();
            if (buggyFiles!= null) {
                for (int i = 0; i < buggyFiles.length; i++) {
                    File elementBuggy = buggyFiles[i];
                    for (int j = 0; j < fixedFiles.length; j++) {
                        File elementFixed = fixedFiles[j];
                        if (!elementBuggy.isDirectory() && !elementFixed.isDirectory()) {
                            String nameBuggyClass = elementBuggy.getName();
                            String nameFixedClass = elementFixed.getName();
                            if (!nameBuggyClass.endsWith(".class") && !nameBuggyClass.endsWith(".zip") && !nameBuggyClass.endsWith(".txt") &&
                                    !nameBuggyClass.endsWith("Test.java")) {
                                if (nameBuggyClass.equals(nameFixedClass)) {
                                    getDiff(elementBuggy, elementFixed, pathOutBehavior);
                                }
                            }
                        }
                    }
                }
            }
        }

    }
//    private static diffTwoFile () {
//        File file1 = new File("C:\\Users\\Dell\\Desktop\\APR_test\\data_test\\83109\\Bank.java");
//        File file2 = new File("C:\\Users\\Dell\\Desktop\\APR_test\\data_test\\83104\\Bank.java");
//        String path = "C:\\Users\\Dell\\Desktop\\APR_test\\detectedBug";
//        // Code bug, code fix
//        DataProcessing.getDiff( file2, file1, path);
//        DataProcessing dataProcessing = new DataProcessing();
//        File behaviorFile = new File("C:\\Users\\Dell\\Desktop\\APR_test\\detectedBug\\83104_83109\\behavior_Bank.txt");
//        List<Behavior> behaviors = dataProcessing.readBehaviorFromFile(behaviorFile);
//        System.out.println(behaviors.toString());
//    }

    /**
     * get diff of source code
     *
     * @param buggyFile:      file error of student < 100
     * @param fixedFile:      file pass all test of student = 100
     * @param pathOutBehavior
     * @return
     */
    private static String getDiff(File buggyFile, File fixedFile, String pathOutBehavior) {
        OutputStream out = new ByteArrayOutputStream();
        EditList diffList = new EditList();
        try {
            RawText rt1 = new RawText(buggyFile);
            RawText rt2 = new RawText(fixedFile);
            diffList.addAll(new HistogramDiff().diff(RawTextComparator.DEFAULT, rt1, rt2));
            new DiffFormatter(out).format(diffList, rt1, rt2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String edit = "";
        if (diffList != null) {
            for (int i = 0; i < diffList.size(); i++) {
                if (diffList.get(i) != null) {
                    edit += diffList.get(i).toString() + "\n";
                }
            }
        }

        System.out.println(out);
        // Save diff to File
        if (diffList.size() > 0) {
            String classname = buggyFile.getName().replace(".java", "");
            String pathOutput = pathOutBehavior + File.separator + buggyFile.getParentFile().getName() + "_" + fixedFile.getParentFile().getName();
            File fileOut = new File(pathOutput + File.separator + classname + ".txt");
            FileHelper.createFile(fileOut, out.toString());
            FileHelper.createFile(new File(pathOutput + File.separator + "behavior" + "_" + classname + ".txt"), edit);
        }
        return out.toString();
    }

    public static List<Behavior> readBehaviorFromFile(File file) {
        List<Behavior> behaviors = new ArrayList<>();
        if (file != null) {
            List<String> lines = FileHelper.readDataAsList(file.getAbsolutePath());
            for (String l : lines) {
                Behavior behavior = readBehaviorFromString(l);
                behaviors.add(behavior);
            }
        }
        return behaviors;
    }

    public static Behavior readBehaviorFromString(String behavior) {
        String[] splits = behavior.split("[()]");
        Behavior behaviorInfo = new Behavior();
        behaviorInfo.setType(Behavior.Type.valueOf(splits[0]));

        String[] posSplits = splits[1].split(",");
        String[] positionA = posSplits[0].split("-");
        behaviorInfo.setBeginA(Integer.parseInt(positionA[0]));
        behaviorInfo.setEndA(Integer.parseInt(positionA[1]));

        String[] positionB = posSplits[1].split("-");
        behaviorInfo.setBeginB(Integer.parseInt(positionB[0]));
        behaviorInfo.setEndB(Integer.parseInt(positionB[1]));
        return behaviorInfo;
    }

    public static void extractBuggy(String pathToBehaviorFolder, File faultLocalizationFile,
                                    Map<Integer, Integer> lines,
                                    List<RawResult> results, int count) {
        if (!faultLocalizationFile.isDirectory()) {
            List<String> susClassNameList = new ArrayList<>();
            List<SuspiciousPosition> suspiciousPositions = FileHelper.readSuspiciousCodeFromFile(
                    faultLocalizationFile.getAbsolutePath(), susClassNameList);

            for (String susName : susClassNameList) {
                String justSusName = susName.replace(".java", "");
                List<SuspiciousPosition> suspiciousPositionList = suspiciousPositions.stream().filter(suspiciousPosition
                        -> suspiciousPosition.classPath.replace(".", "")
                        .equals(justSusName)).collect(Collectors.toList());
                List<Behavior> behaviors = findBehaviorFile(pathToBehaviorFolder, justSusName);
                if (behaviors.size() > 0) {
                    File file = new File((pathToBehaviorFolder));
                    if (file != null) {
                        String[] splits = file.getName().split("_");
                        compareBuggyLine(suspiciousPositionList, behaviors, splits[0], lines, results, count);
                    }
                }
            }
        }
    }

    public static void compareBuggyLine(List<SuspiciousPosition> suspiciousPositions, List<Behavior> behaviors, String projectName,
                                        Map<Integer, Integer> lines, List<RawResult> results, int count) {
        float minSuspicious = 1.0f;
        Collections.sort(suspiciousPositions);

        for (SuspiciousPosition suspiciousPosition : suspiciousPositions) {
            for (Behavior behavior : behaviors) {
                String line = "(" + behavior.getBeginA() + "-" + behavior.getEndA() + ";" +
                        behavior.getBeginB() + "-" + behavior.getEndB() + ")";
                if (behavior.getType().equals(Behavior.Type.INSERT)) { //ADD INSERT INTO RAW
                    if (!lines.containsKey(line.hashCode())) {
                        RawResult rawResult = new RawResult(projectName, suspiciousPosition.classPath, line, null,
                                null, behavior.getType().toString(), "null");
                        //add new raw
                        lines.put(line.hashCode(), count);
                        results.add(rawResult);
                        count++;
                    }
                }
                if (suspiciousPosition.lineNumber < behavior.getBeginA()) {
                    break;
                } else if (suspiciousPosition.lineNumber > behavior.getEndA()) {
                    continue;
                }

                if (!behavior.getType().equals(Behavior.Type.INSERT)) {
                    if (suspiciousPosition.lineNumber >= behavior.getBeginA()
                            && suspiciousPosition.lineNumber <= behavior.getEndA()) {
                        if (Float.compare(minSuspicious, suspiciousPosition.ratio) > 0) {
                            minSuspicious = suspiciousPosition.ratio;
                        }
                        //TODO: edit RawResult
                        RawResult rawResult = new RawResult(projectName, suspiciousPosition.classPath, line, null,
                                null, behavior.getType().toString(), String.valueOf(minSuspicious));
                        //ADD result to CSV
                        if (!lines.containsKey(line.hashCode())) {
                            //add new raw
                            lines.put(line.hashCode(), count);
                            results.add(rawResult);
                            count++;
                        } else {
                            //add ratio in Raw
                            int index = lines.get(line.hashCode());
                            results.get(index).setRatio(String.valueOf(minSuspicious));
                        }
                        System.out.println(suspiciousPosition.lineNumber + "@" + suspiciousPosition.ratio + "@" +
                                behavior.getType());
                    }
                }
            }
        }
        System.out.println("======MIN FL: " + minSuspicious);
    }

    public static void compareBuggyLineVer2(List<SuspiciousPosition> suspiciousPositions, List<Behavior> behaviors, String projectName,
                                        Map<Integer, Integer> lines, List<RawResult> results, int count) {
        float minSuspicious = 1.0f;
        Collections.sort(suspiciousPositions);

        for (Behavior behavior : behaviors) {
            for (SuspiciousPosition suspiciousPosition : suspiciousPositions) {
                if (suspiciousPosition.lineNumber < behavior.getBeginA()) {
                    break;
                } else if (suspiciousPosition.lineNumber > behavior.getEndA()) {
                    continue;
                }
                if (suspiciousPosition.lineNumber >= behavior.getBeginA()
                        && suspiciousPosition.lineNumber <= behavior.getEndA()) {
                    if (Float.compare(minSuspicious, suspiciousPosition.ratio) > 0) {
                        minSuspicious = suspiciousPosition.ratio;
                    }
                    //TODO: edit RawResult
                    String line = "(" + behavior.getBeginA() + "-" + behavior.getEndA() + ";" +
                            behavior.getBeginB() + "-" + behavior.getEndB() + ")";
                    RawResult rawResult = new RawResult(projectName, suspiciousPosition.classPath, line, null,
                            null, behavior.getType().toString(), String.valueOf(minSuspicious));
                    //ADD result to CSV
                    if (!lines.containsKey(line.hashCode())) {
                        //add new raw
                        lines.put(line.hashCode(), count);
                        results.add(rawResult);
                        count++;
                    } else {
                        //add ratio in Raw
                        int index = lines.get(line.hashCode());
                        results.get(index).setRatio(String.valueOf(minSuspicious));
                    }
                    System.out.println(suspiciousPosition.lineNumber + "@" + suspiciousPosition.ratio + "@" +
                            behavior.getType());

                }
            }
        }
        System.out.println("======MIN FL: " + minSuspicious);
    }


    public static void writeRawToCSV(List<RawResult> results) {
        for (RawResult rawResult : results) {
            writeDataProcessingToFile(FILE_OUT_DATA_PROCESS, rawResult.getList());
        }
    }

//    public static RawResult getRawResult (File file, int startLine, int endLine) {
//        String buggyContent = FileHelper.readCodeFromLineToLine()
//        RawResult rawResult = new RawResult(suspiciousPosition.classPath, )
//    }

    public static void writeDataProcessingToFile(String pathToWrite, String[] content) {
        String[] title = {"Project Name", "Classname", "Line", "Buggy line", "Fixed line", "Type", "Ratio (FL)"};
        File file = new File(pathToWrite);
        if (file.exists()) {
            FileHelper.writeLineToCSV(pathToWrite, content);
        } else {
            FileHelper.writeLineToCSV(pathToWrite, title);
            FileHelper.writeLineToCSV(pathToWrite, content);
        }
    }

    /**
     * find a file behavior_CLASSNAME.java
     *
     * @param pathToBehaviorFile: path/detectedBug/buggyID_fixedID
     * @param classname           eg: Account
     * @return
     */
    public static List<Behavior> findBehaviorFile(String pathToBehaviorFile, String classname) {
        List<Behavior> behaviors = new ArrayList<>();
        File file = new File(pathToBehaviorFile + File.separator + "behavior_" + classname + ".txt");
        if (file.exists()) {
            behaviors = readBehaviorFromFile(file);
        }
        return behaviors;
    }
//
//    public static void main(String[] args) {
//        int problemId = 7;
//        String path = "C:\\Users\\Dell\\Desktop\\APR_test\\query_problem\\2_result_QuerySubmitProblem_"+ problemId+".txt";
//        List<String> contents = FileHelper.readDataAsList(path);
//        int count = 0;
//        String content ="";
//        for (String line: contents) {
//            String[] splits = line.split(";");
//            count++;
//            content += "#SV"+count+"\nselect max(s.submission_id) from submission s join zip_submission_result zr on s.submission_id = zr.submission_id where s.student_id =  "+splits[0]+" and s.course_problem_id = "+splits[1]+ " and s.runtime_result_id != 9  and zr.junit_score > 0 group by s.total_score order by  s.total_score desc, s.created_at desc\n" +
//                    "INTO OUTFILE '/var/lib/mysql-files/problem_"+problemId+"_student_"+ splits[0] +".txt'\n" +
//                    "FIELDS TERMINATED BY ';'\n" +
//                    "LINES TERMINATED BY ';';\n\n";
//        }
//        FileHelper.createFile(new File("C:\\Users\\Dell\\Desktop\\APR_test\\query_problem\\3_query_buggy_"+problemId+".txt"), content);
//    }


}
