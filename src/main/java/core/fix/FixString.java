package core.fix;

import AST.node.MethodNode;
import AST.stm.abst.StatementNode;
import AST.stm.node.StringStmNode;
import core.jdb.ExtractDebugger;
import core.object.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.JavaLibraryHelper;

import java.util.ArrayList;
import java.util.List;

public class FixString {
    public static final Logger logger = LoggerFactory.getLogger(FixString.class);

    public static List<Candidate> fixString(ExtractDebugger extractDebugger, DebugData debugData) {
        List<BreakPointInfo> breakPointInfos = extractDebugger.getHistoryDebug();
        List<Candidate> candidates = new ArrayList<>();
        for (int i = breakPointInfos.size() - 2; i >= 0; i--) {
            BreakPointInfo breakPointInfo = breakPointInfos.get(i);
            if (breakPointInfo.getVarname() != null) {
                logger.error("Chuwa xu ly:fixString ");
            } else {
                if (breakPointInfo.getVariableInfos().size() > 0) {
                    for (Object obj : breakPointInfo.getVariableInfos()) {
                        Candidate candidate = fixReturns(obj, debugData, breakPointInfo.getLine());
                        if (candidate != null) {
                            candidates.add(candidate);
                        }
                    }
                } else {
                    logger.error("Chuwa xu ly: breakPointInfo.getVariableInfos().size() <= 0");
                }
            }
        }
        return candidates;
    }

    private static Candidate fixReturns(Object obj, DebugData debugData, int line) {
        Candidate candidate = null;
        String expected = JavaLibraryHelper.subString(debugData.getTmpExpected(), debugData.getIndexExpected());
        debugData.setTmpExpected(expected);
        if (obj instanceof String) {
            StringComparisonResult stringComparisonResult = JavaLibraryHelper.compareTwoString(
                    expected, (String) obj, debugData);
            if (!stringComparisonResult.isEquals()) {
                logger.error("Chuwa xu ly: obj instanceof String");
                //TODO: get DIFF
            }
        } else if (obj instanceof VariableInfo) {
            String actual = ((VariableInfo) obj).getValue();
            if (actual.length() + 10 <= expected.length()) {
                expected = expected.substring(0, actual.length() + 10);
            }
            candidate = fixStringStm(expected, obj, line, debugData);

        }
        return candidate;
    }

    private static CandidateString fixStringStm(String expected, Object obj, int line, DebugData debugData) {
        if (obj instanceof VariableInfo) {
            VariableInfo variableInfo = (VariableInfo) obj;
            ComparisonResult comparisonResult = JavaLibraryHelper.getStringComparisonResult(expected, variableInfo.getValue(), debugData);
            int isline = line;
            //TH1: dong thuc thi hien tai equals
            if (comparisonResult.getPercent() > 50) {
                if (variableInfo.getPointToMethod() != null) {
                    isline = findLineReturn(variableInfo.getValue(), variableInfo.getPointToMethod());
                    if (isline == -1) {
                        isline = line;
                    }
                    return new CandidateString(isline, ((MethodNode) variableInfo.getPointToMethod()).getParent().getName(),
                            ((MethodNode) variableInfo.getPointToMethod()).getName(),
                            FixType.EDIT_RETURN, comparisonResult.getStringModifies(), comparisonResult.getDiffs());
                }
            } else {
                //Xu ly returns khac equals => fix bug bang re nhanh return.
                logger.error("Chua xu ly:fixStringStm ");
            }
        }
        return null;
    }

    private static int findLineReturn(Object string, Object obj) {
        if (obj instanceof MethodNode) {
            MethodNode methodNode = (MethodNode) obj;
            List<StatementNode> returnStatements = methodNode.getReturnStatements();
            for (StatementNode statementNode : returnStatements) {
                if (string instanceof String && statementNode instanceof StringStmNode) {
                    if (string.equals(((StringStmNode) statementNode).getValue())) {
                        return statementNode.getLine();
                    }
                }
            }
        }
        return -1;

    }


}
