package core;

import AST.node.*;
import AST.stm.abst.StatementNode;
import AST.stm.node.*;
import core.jdb.JDBDebugger;
import core.object.DebugPoint;
import AST.obj.MethodCalled;
import AST.obj.MethodTest;
import AST.stm.abst.AssertStatement;
import core.object.DebugData;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.JavaLibraryHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DebugPointSetter {
    public static final Logger logger = LoggerFactory.getLogger(DebugPointSetter.class);
    private static List<DebugPoint> debugPoints;

    /**
     * Convert "Hello" + " world" -> "Hello world"
     *
     * @param string
     * @return
     */
    private static String convertString(String string) {
        String[] elements = string.split("\"\\s*\\+\\s*\"");
        string = String.join("", elements);
        string = string.replace("\"", "");
        string = string.replace("\\n", "\n");
        return string;
    }

    /**
     * Generate List DebugDate, a debugData contents: expected result & list DebugPoint
     *
     * @param folderNode
     * @param methodTest
     * @return
     */
    public static List<DebugData> genDebugPoints(FolderNode folderNode, MethodTest methodTest) {
        List<DebugData> debugDataList = new ArrayList<>();
        List<AssertStatement> assertStatements = methodTest.getAssertList();
        for (AssertStatement assertStatement : assertStatements) {
            debugPoints = new ArrayList<>();
            //Case 1: AssertEqualStm
            if (assertStatement instanceof AssertEqualStmNode) {
                //Case 1.1 actual  = MethodInvocation
                //TODO: FIX debugData (convertString...)
                String expected = expectedToStm((((AssertEqualStmNode) assertStatement).getExpected()));
//                try {
//                    JDBDebugger jdbDebugger = new JDBDebugger("C:\\Users\\Dell\\Desktop\\DebuRepair\\data_test\\80776",
//                            "C:\\Users\\Dell\\Desktop\\DebuRepair\\data_test\\80776", "MyTest");
//                    jdbDebugger.addDebugJDB("MyTest", assertStatement.getLine());
//                    jdbDebugger.runJDB();
//                    String text = jdbDebugger.printVarJDB(((AssertEqualStmNode) assertStatement).getActual().toString());
//                    String text = jdbDebugger.printVarJDB("board");
//                    System.out.println(text);
                    DebugData debugData = new DebugData(expected,
                            methodTest.getMethodName());
                    debugData.setTmpExpected(expected);
                    parserStm(((AssertEqualStmNode) assertStatement).getActual(), assertStatement.getLine(), methodTest, debugData, folderNode);
                    if (debugData.getDebugPoints() != null) {
                        addDebugData(debugData, debugDataList);
                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

            } else {
                logger.error("Chưa xử lý 1");
            }
        }
        return debugDataList;
    }

    public static void parserStm (Object actual, int line,  MethodTest methodTest, DebugData debugData, FolderNode folderNode) {
        if (actual instanceof MethodInvocation) {
            MethodInvocation methodInvocation =
                    (MethodInvocation) actual;
            MethodInvocationStmNode methodInvocationStmNode = methodTest.getMethodNode()
                    .parserMethodInvoStm(methodInvocation, line);
            //parser methodInvo
            findDebugWithMethodInvoStm(methodInvocationStmNode, folderNode);
            debugData.setDebugPoints(debugPoints);
        } else if (actual instanceof InfixExpression) {
            InfixExpression infixExpression = (InfixExpression) actual;
            Object left = infixExpression.getLeftOperand();
            parserStm(left, line, methodTest, debugData, folderNode);
            Object right = infixExpression.getRightOperand();
            parserStm(right, line, methodTest, debugData, folderNode);
            if (infixExpression.extendedOperands().size() > 0) {
                for (Object obj: infixExpression.extendedOperands()) {
                    parserStm(obj, line, methodTest, debugData, folderNode);
                }
            }
        }
    }

    public static void addDebugData(DebugData debugData, List<DebugData> debugDataList) {
        boolean dupplicate = false;
        int count = -1;
        int index = -1;
        for (DebugData debugData1 : debugDataList) {
            count++;
            if (debugData1.getDebugPoints().size() == debugData.getDebugPoints().size()) {
                for (DebugPoint debugPoint : debugData1.getDebugPoints()) {
                    if (index != -1) {
                        break;
                    }
                    for (DebugPoint debugPoint1 : debugData.getDebugPoints()) {
                        if (debugPoint1.getClassname().equals(debugPoint.getClassname())
                                && debugPoint1.getLine() == debugPoint.getLine()) {
                            dupplicate = true;
                            index = count;
                            break;
                        }
                    }
                }
            }
        }
        if (dupplicate) {
            debugDataList.remove(index);
        }
        debugDataList.add(debugData);
    }

    private static String expectedToStm(Object expected) {
        if (expected instanceof InfixExpressionStmNode) {
            String value = getValueInfixExpression((InfixExpressionStmNode) expected);
            return value;
        } else {
            return JavaLibraryHelper.removeFirstAndLastChars(expected.toString());
        }
    }


    private static String getValueInfixExpression(InfixExpressionStmNode statementNode) {
        Object left = statementNode.getLeft();
        String value = "";
        value = getValueObject(left);
        Object right = statementNode.getRight();
        if (statementNode.getOperator().equals("+")) {
            value += getValueObject(right);
        } else {
            logger.error("Chua xu ly:getValueInfixExpression operator" + statementNode.getOperator());
        }
        for (Object o : statementNode.getExtendedOperands()) {
            if (statementNode.getOperator().equals("+")) {
                value += getValueObject(o);
            } else {
                logger.error("Chua xu ly:getValueInfixExpression operator" + statementNode.getOperator());
            }
        }
        return value;

    }

    private static String getValueObject(Object obj) {
        if (obj instanceof String) {
//            System.out.println((String) obj);
//            System.out.println(JavaLibraryHelper.convertNumbersToString((String) obj));
            obj = JavaLibraryHelper.removeFirstAndLastChars((String) obj);
            return (String) obj;
        } else if (obj instanceof MethodInvocationStmNode) {
            logger.error("Khong xu ly:getValueObject ");
            return null;
        } else {
            logger.error("Chuwa xu lys:getValueInfixExpression " + obj);
            return null;
        }
    }

    private static void findDebugWithMethodInvoStm(MethodInvocationStmNode methodInvocationStmNode, FolderNode folderNode) {
        List<MethodCalled> methodCalleds = methodInvocationStmNode.getMethodsCalled();
        if (methodInvocationStmNode.getTypeVar() != null) {
            ClassNode classNode = folderNode.findClassByName(methodInvocationStmNode.getTypeVar());
            if (classNode != null) {
                List<MethodNode> methodNodes = classNode.getMethodList();
                for (MethodCalled methodCalled :
                        methodCalleds) {
                    MethodNode methodNode = getMethodNode(methodCalled, methodNodes);
                    if (methodNode != null) {
                        addDebugPointInReturnStatements(methodNode, classNode, folderNode);
                    }
                }
            }
        }
        findDebugWithArgurements(methodCalleds, folderNode);
    }

    /**
     * Parser argurements if has MethodInvocationStm need to continue parser and set DebugPoint
     *
     * @param methodCalleds
     * @param folderNode
     */
    private static void findDebugWithArgurements(List<MethodCalled> methodCalleds, FolderNode folderNode) {
        for (MethodCalled methodCalled : methodCalleds) {
            List<Object> argurements = methodCalled.getAgurementTypes();
            if (argurements != null) {
                for (Object obj : argurements) {
                    if (obj instanceof MethodInvocationStmNode) {
                        findDebugWithMethodInvoStm((MethodInvocationStmNode) obj, folderNode);
                    }
                }
            }
        }
    }

    /**
     * set DebugPoints for all Returns statements if methodNode
     *
     * @param methodNode
     * @param classNode
     * @param folderNode
     */
    private static void addDebugPointInReturnStatements(MethodNode methodNode, ClassNode classNode, FolderNode folderNode) {
        List<StatementNode> returnStatements = methodNode.getReturnStatements();
        for (StatementNode statementNode : returnStatements) {
            if (statementNode instanceof StringStmNode) {
                logger.info("nope set debug");
            } else if (statementNode instanceof NumbericStmNode) {
                logger.info("nope set debug");
            } else {
                DebugPoint debugPoint = new DebugPoint(classNode.getName(), statementNode.getLine(), statementNode.getKeyVar(), statementNode, methodNode.getName());
                addDebugPoint(debugPoint);
                findDebugFromStatement(statementNode, methodNode, classNode, folderNode);
            }
        }
    }

    /**
     * Set debugPoint of imported statement ( If statement has MethodInvocation => continue
     * Debug at return statements of MethodInvocation.)
     *
     * @param statementNode
     * @param methodNode
     * @param classNode
     * @param folderNode
     */
    private static void findDebugFromStatement(StatementNode statementNode, MethodNode methodNode, ClassNode classNode, FolderNode folderNode) {
        if (statementNode instanceof MethodInvocationStmNode) {
            // set in method of methodInvocation
            findDebugWithMethodInvoStm((MethodInvocationStmNode) statementNode, folderNode);
            //set related var
            findDebugWithRelatedVar(statementNode, methodNode, classNode, folderNode);
        } else if (statementNode instanceof BaseVariableNode) {
            //set Related var
            findDebugWithRelatedVar(statementNode, methodNode, classNode, folderNode);
        } else if (statementNode instanceof InfixExpressionStmNode) {
            //execute
            findDebugInfixExpressionNode((InfixExpressionStmNode) statementNode, methodNode, classNode, folderNode);
        } else {
            logger.error("Chưa xử lý:setDebugPointFromStatement " + statementNode);
        }
    }

    /**
     * parser all elements in InfixExpression, if it has methodInvocation => continue set Debug
     *
     * @param infixExpressionStmNode
     */
    private static void findDebugInfixExpressionNode(InfixExpressionStmNode infixExpressionStmNode, MethodNode methodNode, ClassNode classNode, FolderNode folderNode) {
        if (infixExpressionStmNode.getLeft() instanceof MethodInvocationStmNode) {
            findDebugWithMethodInvoStm((MethodInvocationStmNode) infixExpressionStmNode.getLeft(), folderNode);
        }
        if (infixExpressionStmNode.getRight() instanceof MethodInvocationStmNode) {
            findDebugWithMethodInvoStm((MethodInvocationStmNode) infixExpressionStmNode.getRight(), folderNode);
        }
        for (Object obj :
                infixExpressionStmNode.getExtendedOperands()) {
            if (obj instanceof MethodInvocationStmNode) {
                findDebugWithMethodInvoStm((MethodInvocationStmNode) obj, folderNode);
            } else if (obj instanceof BaseVariableNode) {

            }
        }
    }

    private static void mappingParserReturnInfo_InfixExpression () {

    }

    /**
     * Set all DebugPoint with related statement of "varName" - imported var
     *
     * @param stmNode
     * @param classNode
     * @param folderNode
     */
    private static void addDebugPointRelatedFromStatement(StatementNode stmNode, ClassNode classNode, FolderNode folderNode, MethodNode methodNode) {

        if (stmNode instanceof MethodInvocationStmNode) {
            DebugPoint debugPoint = new DebugPoint(classNode.getName(), ((MethodInvocationStmNode)stmNode).getLine(),
                    stmNode.getKeyVar(), stmNode, methodNode.getName());
            addDebugPoint(debugPoint);
            findDebugWithMethodInvoStm((MethodInvocationStmNode) stmNode, folderNode);
        } else if (stmNode instanceof BaseVariableNode) {
            BaseVariableNode baseVariableNode = (BaseVariableNode) stmNode;
            DebugPoint debugPoint = new DebugPoint(classNode.getName(), baseVariableNode.getLine(), baseVariableNode.getKeyVar(), stmNode, methodNode.getName());
            addDebugPoint(debugPoint);
//                logger.info("I have not needed to use this yet");

        } else if (stmNode instanceof AssignmentStmNode) {
            addDebugPointWithAssignment((AssignmentStmNode) stmNode, classNode, folderNode, methodNode);
        } else {
            logger.error("Chưa xử lý:setDebugPointFromStatement " + stmNode);
        }
    }

    private static void addDebugPointWithAssignment(AssignmentStmNode node, ClassNode classNode,  FolderNode folderNode, MethodNode methodNode) {
        StatementNode leftNode = node.getLeftSide();
        addDebugPointRelatedFromStatement(leftNode, classNode, folderNode, methodNode);
        StatementNode rightNode = node.getRightNode();
        addDebugPointRelatedFromStatement(rightNode, classNode,  folderNode, methodNode);
    }

    /**
     * Get ALl Statements from imported var => then setDugbug related var
     *
     * @param statementNode
     * @param classNode
     * @param folderNode
     */
    private static void findDebugWithRelatedVar(StatementNode statementNode, MethodNode methodNode, ClassNode classNode, FolderNode folderNode) {
        if (statementNode instanceof MethodInvocationStmNode) {
            String varname = ((MethodInvocationStmNode) statementNode).getKeyVar();
            int line = ((MethodInvocationStmNode) statementNode).getLine();
            List<StatementNode> statementsRelated = getStatementsRelated(methodNode, classNode, varname, line);
            if (statementsRelated != null) {
                findStatementsRelated(statementsRelated, classNode, folderNode, methodNode);
            }
        } else if (statementNode instanceof BaseVariableNode) {
            String varName = ((BaseVariableNode) statementNode).getKeyVar();
            int line = ((BaseVariableNode) statementNode).getLine();
            List statementsRelated = getStatementsRelated(methodNode, classNode, varName, line);
            if (statementsRelated != null) {
                findStatementsRelated(statementsRelated, classNode, folderNode, methodNode);
            }
        } else {
            logger.error("Chưa xử lý:setDebugPointWithRelatedVar " + statementNode);
        }
    }

    private static List<DebugPoint> findStatementsRelated(List<StatementNode> statementsRelated, ClassNode classNode, FolderNode folderNode, MethodNode methodNode) {
        for (StatementNode stmRelated :
                statementsRelated) {
            addDebugPointRelatedFromStatement(stmRelated, classNode, folderNode, methodNode);
        }
        return null;
    }

    /**
     * Get All related statements form varname
     *
     * @param methodNode
     * @param classNode
     * @param varName
     * @param line
     * @return
     */
    private static List<StatementNode> getStatementsRelated(MethodNode methodNode, ClassNode classNode, String varName, int line) {
        InitNode initInMethodNode = methodNode.findTypeVar(varName, line);
        if (initInMethodNode != null) {
            return initInMethodNode.getStatementsUsed();
        } else {
            InitNode initInClassNode = classNode.findTypeVar(varName);
            if (initInClassNode != null) {
                return initInClassNode.getStatementsUsed();
            }
        }
        return null;
    }

    /**
     * Get MethodNode from MethodCalled
     *
     * @param methodCalled
     * @param methodNodes
     * @return
     */
    private static MethodNode getMethodNode(MethodCalled methodCalled, List<MethodNode> methodNodes) {
        List<MethodNode> list = new ArrayList<>();
        for (MethodNode method :
                methodNodes) {
            if (methodCalled.getMethodName().equals(method.getName())) {
                if (method.getParameters().size() == 0 && methodCalled.getAgurementTypes() == null) {
                    list.add(method);
                } else {
                    if (method.getParameters().size() == methodCalled.getAgurementTypes().size()) {
                        list.add(method);
                    }
                }
            }
        }
        if (list.size() == 1) {
            return list.get(0);
        } else {
            logger.error("Dupplicate Method: " + list.toString());
        }
        return null;
    }

    /**
     * Check if !exitst => can add debugPoint to debugPonits
     *
     * @param debugPoint
     */
    private static void addDebugPoint(DebugPoint debugPoint) {
        boolean exist = false;
        for (DebugPoint debugPoint1 : debugPoints) {
            if (debugPoint1.getLine() == debugPoint.getLine()) {
                if (debugPoint1.getClassname().equals(debugPoint.getClassname())) {
                    exist = true;
                }
            }
        }
        if (!exist) {
            debugPoints.add(debugPoint);
        }
    }
}
