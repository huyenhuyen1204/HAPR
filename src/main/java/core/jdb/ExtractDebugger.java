package core.jdb;

import AST.node.*;
import AST.obj.MethodCalled;
import AST.stm.abst.StatementNode;
import AST.stm.node.BaseVariableNode;
import AST.stm.node.StringStmNode;
import AST.stm.node.InfixExpressionStmNode;
import AST.stm.node.MethodInvocationStmNode;
import core.object.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.DebuggerHelper;
import util.JavaLibraryHelper;

import java.util.ArrayList;
import java.util.List;

public class ExtractDebugger {
    public static final Logger logger = LoggerFactory.getLogger(ExtractDebugger.class);

    private int indexExpected = 0;
    private int indexActual = 0;
    private List<BreakPointInfo> historyDebug = new ArrayList<>();
    private List<BreakPointInfo> aPartOfHistory = new ArrayList<>();


    public int getIndexExpected() {
        return indexExpected;
    }

    public void setIndexExpected(int indexExpected) {
        this.indexExpected = indexExpected;
    }

    public int getIndexActual() {
        return indexActual;
    }

    public void setIndexActual(int indexActual) {
        this.indexActual = indexActual;
    }

    public List<BreakPointInfo> getHistoryDebug() {
        return historyDebug;
    }

    public void setHistoryDebug(List<BreakPointInfo> historyDebug) {
        this.historyDebug = historyDebug;
    }

    public BreakPointInfo watchValueChange(BreakPointHit breakPointHit, DebugData debugData, JDBDebugger jdbDebugger, FolderNode folderNode) {
        DebugPoint debugPoint = DebuggerHelper.findDebugPoint(breakPointHit, debugData.getDebugPoints());
        ClassNode classNode = folderNode.findClassByName(breakPointHit.getClassName());
        BreakPointInfo breakPointInfo = null;
        if (classNode != null) {
            MethodNode methodNode = classNode.findMethodNodeByStmLine(breakPointHit.getMethodName(), breakPointHit.getLine());
            if (methodNode != null) {
                if (debugPoint.getKeyVar() != null) {
                    String contetn222 = jdbDebugger.printVarJDB(debugPoint.getKeyVar());
                    breakPointInfo = new BreakPointInfo(breakPointHit.getLine(),
                            debugPoint.getKeyVar(), contetn222, debugPoint);
                } else {
                    breakPointInfo = getValueNodeNullVar(debugPoint, debugData, jdbDebugger, folderNode);
                }
            } else {
                logger.error("Method not fount in class: " + "{class:" + breakPointHit.getClassName() + ",method:"
                        + breakPointHit.getMethodName() + "}");
            }
        } else {
            logger.error("Method not fount in class: " + "{class:" + breakPointHit.getClassName() + ",method:"
                    + breakPointHit.getMethodName() + "}");
        }
        historyDebug.add(breakPointInfo);
        return breakPointInfo;
    }

    public String watchValueChangeInMethodInvo(MethodInvocationStmNode obj, BreakPointInfo breakPointInfo, JDBDebugger jdbDebugger, FolderNode folderNode) {
        ClassNode classNode = folderNode.findClassByName(obj.getTypeVar());
        String method = obj.getStatementString();
        String content = null;
        VariableInfo variableInfo = new VariableInfo();
        if (classNode != null) {
            for (MethodCalled methodCall : obj.getMethodsCalled()) {
                MethodNode methodNode = classNode.findMethodNode(methodCall.getMethodName(), methodCall.getAgurementTypes());
                if (methodNode != null) {
                    if (methodNode.isReturnStringOrNumber()) {
                        variableInfo.setVarname(method);
                        content = jdbDebugger.printVarJDB(method);
                        variableInfo.setValue(content);
                        variableInfo.setPointToMethod(methodNode);
                    } else {
                        // neu method ton tai => cont de tiep tuc debug
                        //TODO: xu ly sau
                        logger.error("Chuwa xu ly:watchValueChangeInMethodInvo ");
//                        String runningLog = jdbDebugger.contJDB();
//                        BreakPointHit breakPointHit = FixHelper.parserLogRunning(runningLog);
//                        DebugPoint debugPoint = FixHelper.findDebugPoint(breakPointHit, debugData.getDebugPoints());
////                    jdbDebugger.contJDB();
//                        if (debugPoint.getKeyVar() != null) {
//                            content = jdbDebugger.printVarJDB(debugPoint.getKeyVar());
//                            System.out.println("jdb> " + content);
//                        } else {
//                            content = getValueNodeNullVar(debugPoint, debugData, jdbDebugger, folderNode);
//                        }
                    }
                } else {
                    variableInfo.setVarname(method);
                    content = jdbDebugger.printVarJDB(method);
                    variableInfo.setValue(content);
                }
            }
        } else {
            variableInfo.setVarname(method);
            content = jdbDebugger.printVarJDB(method);
            variableInfo.setValue(content);
        }
        breakPointInfo.addVariable(variableInfo);
        return content;
    }

    private BreakPointInfo getValueNodeNullVar(DebugPoint debugPoint, DebugData debugData, JDBDebugger jdbDebugger, FolderNode folderNode) {
        String content = null;
        StatementNode statementNode = debugPoint.getStatementNode();
        BreakPointInfo breakPointInfo = new BreakPointInfo(debugPoint.getLine(),
                debugPoint.getKeyVar(), debugPoint);
        //return stm
        if (statementNode instanceof InfixExpressionStmNode) {
            content = getValueInfixExpression((InfixExpressionStmNode) statementNode, breakPointInfo, debugData, jdbDebugger, folderNode);
        } else if (statementNode instanceof MethodInvocationStmNode) {
            logger.error("chuwa xu ly:getValueNodeNullVar instanceof MethodInvocationStm");
        } else if (statementNode instanceof StringStmNode) {
            content = ((StringStmNode) statementNode).getValue();
        } else {
            logger.error("Chua xu ly: getValueNodeNullVar " + statementNode);
        }
        breakPointInfo.setValue(content);
        return breakPointInfo;
    }

    private String getValueInfixExpression(InfixExpressionStmNode statementNode, BreakPointInfo breakPointInfo, DebugData debugData, JDBDebugger jdbDebugger, FolderNode folderNode) {
        Object left = statementNode.getLeft();
        String value = "";
        String txt = getValueObject(left, breakPointInfo, debugData, jdbDebugger, folderNode);
        if (txt != null) {
            value += txt;
        }
        Object right = statementNode.getRight();
        if (statementNode.getOperator().equals("+")) {
            String xtx = getValueObject(right, breakPointInfo, debugData, jdbDebugger, folderNode);
            if (xtx != null) {
                value += xtx;
            }
        } else {
            logger.error("Chua xu ly:getValueInfixExpression operator" + statementNode.getOperator());
        }
        for (Object o : statementNode.getExtendedOperands()) {
            if (statementNode.getOperator().equals("+")) {
                String txtt = getValueObject(o, breakPointInfo, debugData, jdbDebugger, folderNode);
                if (txtt != null) {
                    value += txtt;
                }
            } else {
                logger.error("Chua xu ly:getValueInfixExpression operator" + statementNode.getOperator());
            }
        }
        return value;

    }

    private String getValueObject(Object obj, BreakPointInfo breakPointInfo, DebugData debugData, JDBDebugger jdbDebugger, FolderNode folderNode) {
        if (obj instanceof String) {
            obj = JavaLibraryHelper.removeFirstAndLastChars((String) obj);
            breakPointInfo.addVariable(obj);
            return (String) obj;
        } else if (obj instanceof MethodInvocationStmNode) {
            String value = watchValueChangeInMethodInvo((MethodInvocationStmNode) obj, breakPointInfo, jdbDebugger, folderNode);
            return value;
        } else if (obj instanceof BaseVariableNode) {
            String content = jdbDebugger.printVarJDB(((BaseVariableNode) obj).getKeyVar());
            VariableInfo variableInfo = new VariableInfo(((BaseVariableNode) obj).getKeyVar(), content);
            breakPointInfo.addVariable(variableInfo);
            return content;
        } else {
            logger.error("Chuwa xu lys:getValueInfixExpression " + obj);
            return null;
        }
    }

    public List<BreakPointInfo> getaPartOfHistory() {
        return aPartOfHistory;
    }

    public void setaPartOfHistory(List<BreakPointInfo> aPartOfHistory) {
        this.aPartOfHistory.addAll(aPartOfHistory);
    }
}
