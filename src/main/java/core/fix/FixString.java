package core.fix;

import AST.node.MethodNode;
import AST.stm.nodetype.StringNode;
import AST.stm.abst.StatementNode;
import AST.stm.nodetype.BaseVariableNode;
import core.jdb.ExtractDebugger;
import core.object.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.JavaLibraryHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FixString {
    public static final Logger logger = LoggerFactory.getLogger(FixString.class);
    private static int indexDebug = 0;

    public static void fixString(ExtractDebugger extractDebugger, DebugData debugData, List<Candidate> candidates, List<SuspicionString> suspicionStrings, ComparisonResult comparisonResult, BreakPointInfo breakHere ) {
        List<BreakPointInfo> breakPointInfos = extractDebugger.getaPartOfHistory();
        Set<BreakPointInfo> done = new HashSet<>();
        if (debugData.getExpected() instanceof StringNode) {
            done.add(breakHere);
            List<BreakPointInfo> breakPointInfoList = filterBreakpointInfor(breakPointInfos, breakHere);
            for (BreakPointInfo breakPointIf : breakPointInfoList) {
                Candidate candidate = null;
                if (breakPointIf.getVariableInfos().size() > 0) {
                    for (Object obj : breakPointIf.getVariableInfos()) {
                        candidate = fixReturns(obj, debugData, breakPointIf.getLine(), breakPointIf.getDebugPoint().getClassname(), breakPointIf.getDebugPoint().getMethodName());
                        if (candidate != null) {
                            candidates.add(candidate);
                        }
                    }
                } else {
                    logger.error("Chuwa xu ly: breakPointInfo.getVariableInfos().size() <= 0");
                }
                if (candidate == null) {
                    SuspicionString suspicionString = new SuspicionString(comparisonResult.getExpected(),
                            comparisonResult.getActual(), breakPointIf.getDebugPoint(), comparisonResult.getDiffs());
                    addSuspicious(suspicionStrings, suspicionString);
                }
            }
        }
    }

    private static void addSuspicious(List<SuspicionString> suspicionStrings, SuspicionString suspicionString) {
        if (suspicionStrings.size() > 0) {
            int size = suspicionStrings.size();
            for (int i = 0; i < size; i++) {
                SuspicionString ss = suspicionStrings.get(i);
                if (ss.getDebugPoint().getLine() == suspicionString.getDebugPoint().getLine()
                        && ss.getDebugPoint().getClassname().equals(suspicionString.getDebugPoint().getClassname())) {
                    suspicionStrings.remove(i);
                    i--;
                    size--;
                }
            }
            suspicionStrings.add(suspicionString);
        } else {
            suspicionStrings.add(suspicionString);
        }
    }


    private static List<BreakPointInfo> filterBreakpointInfor(List<BreakPointInfo> breakPointInfos, BreakPointInfo breakPointInfoLatest) {
        List<BreakPointInfo> breakPointInfoList = new ArrayList<>();
        int floor = indexDebug;
        for (int i = breakPointInfos.size() - 2; i > floor; i--) {
            BreakPointInfo brp = breakPointInfos.get(i);
            if (brp.getLine() == breakPointInfoLatest.getLine()) {
                String varname1 = brp.getVarname() == null ? "" : brp.getVarname();
                String varname2 = breakPointInfoLatest.getVarname() == null ? "" : breakPointInfoLatest.getVarname();
                if (varname1.equals(varname2)) {
                    indexDebug = i;
                    break;
                }
            }
            breakPointInfoList.add(brp);
        }
        return breakPointInfoList;
    }

    private static boolean breakPointDone(Set<BreakPointInfo> breakPointInfos, BreakPointInfo breakPointInfo) {
        if (breakPointInfos.size() > 0) {
            for (BreakPointInfo brp : breakPointInfos) {
                if (brp.getLine() == breakPointInfo.getLine()) {
                    String varname1 = brp.getVarname() == null ? "" : brp.getVarname();
                    String varname2 = breakPointInfo.getVarname() == null ? "" : breakPointInfo.getVarname();
                    if (varname1.equals(varname2)) {
                        return true;
                    }
                }
            }
        }
        breakPointInfos.add(breakPointInfo);
        return false;
    }

    private static Candidate fixReturns(Object obj, DebugData debugData, int line, String classname, String methodname) {
        Candidate candidate = null;
        String expected = JavaLibraryHelper.subString(((StringNode)debugData.getTmpExpected()).getValue(), debugData.getIndexExpected());
        debugData.setTmpExpected(expected);
//        String expected = debugData.getTmpExpected();
        if (obj instanceof String) {
            ComparisonResult comparisonResult = JavaLibraryHelper.getStringComparisonResult(true,
                    expected, (String) obj, debugData);
            if (!comparisonResult.isEquals()) {
                logger.error("Chuwa xu ly: obj instanceof String");
                //TODO: get DIFF
            }
        } else if (obj instanceof VariableInfo) {
            String actual = ((VariableInfo) obj).getValue();
            if (actual.length() + 10 <= expected.length()) {
                expected = expected.substring(0, actual.length() + 10);
            }
            candidate = fixStringStm(expected, obj, line, classname, methodname, debugData);

        } else if (obj instanceof BaseVariableNode) {

        }
        return candidate;
    }

    private static CandidateString fixStringStm(String expected, Object obj, int line, String classname, String methodName, DebugData debugData) {
        if (obj instanceof VariableInfo) {
            VariableInfo variableInfo = (VariableInfo) obj;
            ComparisonResult comparisonResult = JavaLibraryHelper.getStringComparisonResult(true, expected, variableInfo.getValue(), debugData);
            int isline = line;
            //TH1: dong thuc thi hien tai equals
            if (comparisonResult.getPercent() > 50 && comparisonResult.getPercent() < 100) {
                if (variableInfo.getPointToMethod() != null) {
                    isline = findLineReturn(variableInfo.getValue(), variableInfo.getPointToMethod());
                    if (isline == -1) {
                        isline = line;
                    }
                    return new CandidateString(isline, ((MethodNode) variableInfo.getPointToMethod()).getParent().getName(),
                            ((MethodNode) variableInfo.getPointToMethod()).getName(),
                            FixType.EDIT_RETURN, comparisonResult.getStringModifies(), comparisonResult.getDiffs(), variableInfo.getVarname());
                } else {
                    //Xu ly returns khac equals => fix bug bang re nhanh return.
                    logger.error("Chua xu ly:fixStringStm ");
                    return new CandidateString(isline, classname,
                            methodName,
                            FixType.EDIT_RETURN, comparisonResult.getStringModifies(), comparisonResult.getDiffs(), variableInfo.getVarname());
                }
            } else {
                //TODO: find diff type
            }
        }
        return null;
    }

    private static int findLineReturn(Object string, Object obj) {
        if (obj instanceof MethodNode) {
            MethodNode methodNode = (MethodNode) obj;
            List<StatementNode> returnStatements = methodNode.getReturnStatements();
            for (StatementNode statementNode : returnStatements) {
                if (string instanceof String && statementNode instanceof StringNode) {
                    if (string.equals(((StringNode) statementNode).getValue())) {
                        return statementNode.getLine();
                    }
                }
            }
        }
        return -1;

    }


}
