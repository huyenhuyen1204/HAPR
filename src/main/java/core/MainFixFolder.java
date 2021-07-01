package core;

import AST.node.ClassNode;
import AST.node.FolderNode;
import core.fix.FixString;
import core.object.*;
import AST.obj.MethodTest;
import AST.parser.MyTestParser;
import AST.parser.ProjectParser;
import common.Configure;
import common.TestType;
import common.error.ObjectNotFound;
import core.jdb.ExtractDebugger;
import core.jdb.JDBDebugger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainFixFolder {
    public static final Logger logger = LoggerFactory.getLogger(MainFixFolder.class);
    //    static final String pathToSouce = "/home/huyenhuyen/Desktop/HAPR/data_test/83102/";
//    static final String pathToOutput = "/home/huyenhuyen/Desktop/HAPR/data_test/83102/";
    static String pathToSouce = "C:\\Users\\Dell\\Desktop\\DebuRepair\\data_test\\83453";
    static String pathToOutput = "C:\\Users\\Dell\\Desktop\\DebuRepair\\data_test\\83453";
    static final String MyTest_Name = "MyTest";
    static final String TestRunner_Name = "TestRunner";
    static final String Path_AST_Output = pathToSouce + File.separator + "AST.txt"; // path_to_source/AST.txt

    public static void main(String[] args) throws IOException, NoSuchFieldException, IllegalAccessException, InterruptedException {

        //TODO: copy TestRunner to folder
        //Compile and run (to get testcase)
        RunningHelper.compileFolder(pathToSouce, pathToSouce);
        RunningHelper.runFolder(pathToSouce, TestRunner_Name);

        String output = pathToSouce + File.separator + Configure.OUTPUT_TestRunner;
        File outputFile = new File(output);

        //PARSER MyTest
        List<ClassNode> classNodes = (new MyTestParser()).myTestParser
                (pathToSouce + File.separator + MyTest_Name + ".java",
                        TestType.ANNOTATION_TEST);

        //Parser folder
        FolderNode folderNode = ProjectParser.parse(pathToSouce);
        List<SuspicionString> suspicionStrings = new ArrayList<>();
        List<Candidate> candidates = new ArrayList<>();
        if (!outputFile.exists()) {
            logger.error(ObjectNotFound.MSG + Configure.OUTPUT_TestRunner);
        } else {
            // Get List testName Error & get list debug point
            List<DebugData> debugDatas = getListDebugData(classNodes, folderNode, output);
            JDBDebugger jdbDebugger = new JDBDebugger(pathToSouce
                    , pathToOutput, MyTest_Name);
            //add debug to JDB
            DebugData debugData = debugDatas.get(0);
            for (DebugPoint debugPoint : debugData.getDebugPoints()) {
                jdbDebugger.addDebugJDB(debugPoint.getClassname(), debugPoint.getLine());
            }

            String stopAt = jdbDebugger.runJDB();
            BreakPointHit breakPointHit = DebuggerHelper.parserLogRunning(stopAt);
            ExtractDebugger extractDebugger = new ExtractDebugger();
            BreakPointInfo breakPointInfo = extractDebugger.watchValueChange(breakPointHit, debugData, jdbDebugger, folderNode);
            BreakPointHit breakPointHitNext;


            do {
                String log = jdbDebugger.contJDB();
                breakPointHitNext = DebuggerHelper.parserLogRunning(log);
                breakPointInfo = extractDebugger.watchValueChange(breakPointHitNext, debugData, jdbDebugger, folderNode);

                //compare & fixing
                if (breakPointInfo.getVarname() == null) {
                    continue;
                } else if (breakPointInfo.getVarname().equals
                        (debugData.getDebugPoints().get(0).getKeyVar())) {
                    findCandidates(candidates, suspicionStrings, extractDebugger, breakPointHitNext, breakPointInfo,
                            debugData);
                }
            }
            while (debugData.getDebugPoints().get(0).getLine() != breakPointHitNext.getLine());
            for (Candidate candidate : candidates) {
                System.out.println(candidate.toString());
            }
            for (SuspicionString string : suspicionStrings) {
                System.out.println(string.toString());
            }
        }

    }

    public static void findCandidates(List<Candidate> candidates, List<SuspicionString> suspicionStrings, ExtractDebugger extractDebugger, BreakPointHit breakPointHitHere, BreakPointInfo breakPointInfo, DebugData debugData) {
        ComparisonResult comparisonResult = JavaLibraryHelper.getStringComparisonResult(false, debugData.getExpected(), breakPointInfo.getValue(), debugData);
        if (!comparisonResult.isEquals()) {
            FixString.fixString(extractDebugger, debugData, candidates, suspicionStrings, breakPointHitHere, comparisonResult);

        }
    }




//    private static void addCandidate (List<Candidate> candidates, Candidate candidate) {
//        if (candidates.size() > 0) {
//            List<Candidate> temps = new ArrayList<>();
//            temps.addAll(candidates);
//            for (Candidate cd : temps) {
//                CandidateString cdStr = (CandidateString) cd;
//                if (cd instanceof CandidateString && candidate instanceof CandidateString) {
////                    CandidateString candidateString = (CandidateString) candidate;
////                    if (cdStr.equalsDiff(candidateString.getDiffs())) {
////                        candidates.remove(cd);
////                    }
//                    candidates.add(candidate);
//                } else {
//                    logger.error("Chua xu ly:addCandidate");
//                }
//            }
//        } else {
//            candidates.add(candidate);
//        }
//    }


    /**
     * @param classNodes      of MyTest
     * @param folderNode      of Project
     * @param pathToRunOutput of TestRunner (get test fails)
     * @return
     */
    public static List<DebugData> getListDebugData(List<ClassNode> classNodes, FolderNode folderNode, String pathToRunOutput) {
        List<DebugData> debugDataList = new ArrayList<>();
        List<String> tests = FileHelper.readDataAsList(pathToRunOutput);
        logger.info(tests.toString());
        for (ClassNode classNode : classNodes) {
            List<MethodTest> methodTests = classNode.getMethodTests();
            for (MethodTest methodTest : methodTests) {
                for (String testname : tests) {
                    if (testname.equals(methodTest.getMethodName())) {
                        List<DebugData> debugDatas = DebugPointSetter.genDebugPoints(folderNode, methodTest);
                        if (debugDataList.size() == 0) {
                            debugDataList.addAll(debugDatas);
                        } else {
                            for (DebugData debugData : debugDatas) {
                                DebugPointSetter.addDebugData(debugData, debugDataList);
                            }
                        }
                    }
                }
            }
        }
        return debugDataList;
    }

}

