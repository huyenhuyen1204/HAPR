package core;

import AST.node.ClassNode;
import AST.node.FolderNode;
import AST.stm.nodetype.StringNode;
import AST.obj.StackTrace;
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

    static String pathToSouce = "C:\\Users\\Dell\\Desktop\\DebuRepair\\data_test\\81171";
    static String pathToOutput = "C:\\Users\\Dell\\Desktop\\DebuRepair\\data_test\\81171";
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

        if (!outputFile.exists()) {
            logger.error(ObjectNotFound.MSG + Configure.OUTPUT_TestRunner);
        } else {
            // Get List testName Error & get list debug point
            List<DebugData> debugDatas = getListDebugData(classNodes, folderNode, output);
            for (DebugData debugData : debugDatas) {
                if (debugData.getDebugPoints().size() != 0) {
                    fixBug(debugData, folderNode);
                }
            }
        }
    }

    private static void fixBug(DebugData debugData, FolderNode folderNode) throws IOException {
        if (debugData.getDebugPoints().size() > 0) {
            List<SuspicionString> suspicionStrings = new ArrayList<>();
            List<Candidate> candidates = new ArrayList<>();
            JDBDebugger jdbDebugger = new JDBDebugger(pathToSouce
                    , pathToOutput, MyTest_Name);
//        if ()
            //add debug to JDB
            for (DebugPoint debugPoint : debugData.getDebugPoints()) {
                jdbDebugger.addDebugJDB(debugPoint.getClassname(), debugPoint.getLine());
            }

            String stopAt = jdbDebugger.runJDB();
            BreakPointHit breakPointHit = DebuggerHelper.parserLogRunning(stopAt);
            ExtractDebugger extractDebugger = new ExtractDebugger();
            BreakPointInfo breakPointInfo = extractDebugger.watchValueChange(breakPointHit, debugData, jdbDebugger, folderNode);
            BreakPointHit breakPointHitNext;

            //run all debug
            do {
                String log = jdbDebugger.contJDB();
                breakPointHitNext = DebuggerHelper.parserLogRunning(log);
                if (breakPointHitNext != null) {
                    breakPointInfo = extractDebugger.watchValueChange(breakPointHitNext, debugData, jdbDebugger, folderNode);
                } else {
                    logger.error("WHAT?");
                }
            }
            while (debugData.getDebugPoints().get(0).getLine() != breakPointHitNext.getLine());

            jdbDebugger.destroyProcessJDB();

            boolean isFix = false;
            int indexTemp = -1;
            ComparisonResult comparisonResultFix = null;
            for (int i = extractDebugger.getHistoryDebug().size() - 1; i >= 0; i--) {
                BreakPointInfo breakPointIf = extractDebugger.getHistoryDebug().get(i);
                //compare & fixing
                if (breakPointIf.getVarname() == null) {
//                    extractDebugger.getaPartOfHistory().remove(i);
                    continue;
                } else if (breakPointIf.getVarname().equals
                        (debugData.getDebugPoints().get(0).getKeyVar())) {
                    if (debugData.getExpected() instanceof StringNode) {
                        ComparisonResult comparisonResult = JavaLibraryHelper.getStringComparisonResult(false, ((StringNode)debugData.getExpected()).getValue(), breakPointIf.getValue(), debugData);
                        if (!comparisonResult.isEquals()) {
                            indexTemp = i;
                            comparisonResultFix = comparisonResult;
                            isFix = true;
                        } else if (comparisonResult.isEquals()) {
                            break;
                        }
                    } else {
                        logger.error("Chuwa xu ly");
                    }
//                    findCandidates(candidates, suspicionStrings, extractDebugger, breakPointHitNext, breakPointInfo,
//                            debugData);
                }
            }

            if (isFix) {
                extractDebugger.setaPartOfHistory(getBreakpointInfo(extractDebugger.getHistoryDebug(), indexTemp));
                FixString.fixString(extractDebugger, debugData, candidates, suspicionStrings, comparisonResultFix,
                        extractDebugger.getHistoryDebug().get(indexTemp));
            }
            for (Candidate candidate : candidates) {
                System.out.println(candidate.toString());
            }
            for (SuspicionString string : suspicionStrings) {
                System.out.println(string.toString());
            }
        }
    }

    private static List<BreakPointInfo> getBreakpointInfo (List<BreakPointInfo> breakPointInfos, int index) {
        List<BreakPointInfo> breakPointInfoList = new ArrayList<>();
        for (int i = 0; i <= index; i ++) {
            breakPointInfoList.add(breakPointInfos.get(i));
        }
        return breakPointInfoList;
    }

//    public static void findCandidates(List<Candidate> candidates, List<SuspicionString> suspicionStrings, ExtractDebugger extractDebugger, BreakPointHit breakPointHitHere, BreakPointInfo breakPointInfo, DebugData debugData) {
//
//    }


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
        List<StackTrace> stackTraces = FileHelper.readStackTree(tests);
//        logger.info(tests.toString());
        for (ClassNode classNode : classNodes) {
            List<MethodTest> methodTests = classNode.getMethodTests();
            for (MethodTest methodTest : methodTests) {
                for (StackTrace stackTrace : stackTraces) {
                    if (stackTrace.getMethodName().equals(methodTest.getMethodName())) {
                        List<DebugData> debugDatas = DebugPointSetter.genDebugPoints(folderNode, methodTest, stackTrace);
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

