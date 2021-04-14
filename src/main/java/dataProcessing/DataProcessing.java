package dataProcessing;

import dataProcessing.object.Behavior;
import dataProcessing.object.RawResult;
import dataProcessing.object.StudentSubmission;
import faultlocalization.configure.Configuration;
import faultlocalization.gzoltar.FL;
import faultlocalization.objects.SuspiciousPosition;
import org.eclipse.jgit.diff.*;
import util.FileHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.sql.SQLOutput;
import java.util.*;
import java.util.stream.Collectors;

public class DataProcessing {
    public static final String PATH_SUBMITIONS = "C:\\Users\\Dell\\Desktop\\APR_test\\data_test";
    public static final String PATH_OUT_BEHAVIOR = "C:\\Users\\Dell\\Desktop\\APR_test\\detectedBug";
    public static final String PATH_SUSPICIUS_POSITION = "C:\\Users\\Dell\\IdeaProjects\\HAPR\\SuspiciousCodePositions";
    public static final String FILE_OUT_DATA_PROCESS = "C:\\Users\\Dell\\Desktop\\APR_test\\data_test\\data_process.csv";
    public static List<RawResult> results = new ArrayList<>(); //For export csv file;
    public static Map<Integer, Integer> lines = new HashMap<>();
    public static int count = 0;

    public static void main(String[] args) {
        String pathSubmits = "C:\\Users\\Dell\\Desktop\\APR_test\\mysql-files\\submits1243.txt";
        String content = FileHelper.readFile(new File(pathSubmits));
        String[] listSubmits = content.split(";");
        String code100 = listSubmits[0];
        System.out.println(content.replace(";", " "));
//        //STEP [1] copy result of: code100;codeBuggy1;CodeBuggy2 ...
//        // Run sh to automatic copy
//                try
//        {
//            String pathSh = "C:\\Program Files\\Git\\git-bash.exe C:\\Users\\Dell\\Desktop\\APR_test\\CodeCopyData.sh";
//            String cmd = pathSh + " " + content.replace(";", " ");
//            Runtime rt = Runtime.getRuntime();
//            Process proc = rt.exec(cmd);
//
//        } catch (Throwable t)
//        {
//            t.printStackTrace();
//        }
//        //======== End run sh to automatic copy =========

//        //STEP [2] ===========Write difference and behavior off Buggy and Fixed to File ===========
//        File fixedFolder = new File(PATH_SUBMITIONS + File.separator + code100);
//
//        for (int i = 1; i < listSubmits.length; i++) {
//            File buggyFolder = new File(PATH_SUBMITIONS + File.separator + listSubmits[i]);
//            diffTwoFolder(buggyFolder, fixedFolder, PATH_OUT_BEHAVIOR);
//        }
//        //===========Write difference and behavior off Buggy and Fixed to File ===========

//        //STEP [3] ==========Determine Faulocalization =========
//        for (int i = 1; i < listSubmits.length; i++) {
//                FL.faultLocalization(PATH_SUBMITIONS, listSubmits[i], Configuration.faultLocalizationMetric);
//        }
//        //==============End Determine Faulocalization

        //STEP [4] ============Extract Buggy with localization and history code
        for (int i = 1; i < listSubmits.length; i++) {
            String pathSus = PATH_SUSPICIUS_POSITION + File.separator + listSubmits[i] + File.separator + Configuration.faultLocalizationMetric + ".txt";
            String pathToDetectedBug = PATH_OUT_BEHAVIOR + File.separator + listSubmits[i] + "_" + code100;
            extractBuggy(pathToDetectedBug, new File(pathSus));
        }
        if (results != null) {
            writeRawToCSV(results);
        }


    }

    public static void diffTwoFolder(File folderBuggy, File folderFixed, String pathOutBehavior) {
        if (folderBuggy != null && folderFixed != null) {
            File[] buggyFiles = folderBuggy.listFiles();
            File[] fixedFiles = folderFixed.listFiles();
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

    public static void extractBuggy(String pathToBehaviorFolder, File faultLocalizationFile) {
        if (!faultLocalizationFile.isDirectory()) {
            List<String> susClassNameList = new ArrayList<>();
            List<SuspiciousPosition> suspiciousPositions = FileHelper.readSuspiciousCodeFromFile(
                    faultLocalizationFile.getAbsolutePath(), susClassNameList);

            for (String susName : susClassNameList) {
                String justSusName = susName.replace(".java", "");
                List<SuspiciousPosition> suspiciousPositionList = suspiciousPositions.stream().filter(suspiciousPosition
                        -> suspiciousPosition.classPath.replace(".", "")
                        .equals(justSusName)).collect(Collectors.toList());
                List<Behavior> behaviors = findBehaviorFile(pathToBehaviorFolder,justSusName);
                if (behaviors.size() > 0) {
                    compareBuggyLine(suspiciousPositionList, behaviors);
                }
            }
        }
    }

    public static void compareBuggyLine(List<SuspiciousPosition> suspiciousPositions, List<Behavior> behaviors) {
        float minSuspicious = 1.0f;
        Collections.sort(suspiciousPositions);

        for (SuspiciousPosition suspiciousPosition : suspiciousPositions) {
            for (Behavior behavior : behaviors) {
                if (suspiciousPosition.lineNumber < behavior.getBeginA()) {
                    break;
                }
                if (suspiciousPosition.lineNumber >= behavior.getBeginA()
                        && suspiciousPosition.lineNumber <= behavior.getEndA()) {
                        if (Float.compare(minSuspicious, suspiciousPosition.ratio) > 0) {
                            minSuspicious = suspiciousPosition.ratio;
                        }
                    //TODO: edit RawResult
                        String line = "("+ behavior.getBeginA() + "-" + behavior.getEndA() + ";" +
                                behavior.getBeginB() +"-" + behavior.getEndB() + ")";
                        RawResult rawResult = new RawResult(suspiciousPosition.classPath, line, null,
                                null, behavior.getType().toString(), String.valueOf(minSuspicious));
                        if (!lines.containsKey(line.hashCode())) {
                            //add new raw
                            lines.put(line.hashCode(), count);
                            results.add(rawResult);
                            count++;
                        } else {
                            //add ratio in Raw
                            int index = lines.get(line.hashCode());
                            results.get(index).setRatio( String.valueOf(minSuspicious) );
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
            writeDataProcessingToFile(FILE_OUT_DATA_PROCESS, rawResult.toString());
        }
    }

//    public static RawResult getRawResult (File file, int startLine, int endLine) {
//        String buggyContent = FileHelper.readCodeFromLineToLine()
//        RawResult rawResult = new RawResult(suspiciousPosition.classPath, )
//    }

    public static void writeDataProcessingToFile(String pathToWrite, String content) {
        String title ;
        title = "Classname,"    + "Line,"
                + "Bugyy line," + "Fixed Line,"
                + "Type,"        + "Ratio (FL)";
        File file = new File(pathToWrite );
        if (file.exists()) {
            FileHelper.addToFile(file, content );
        } else {
            FileHelper.createFile(file, title + "\n" + content);
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


}
