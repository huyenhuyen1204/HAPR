package fix;

import AST.node.*;
import AST.obj.BaseVariable;
import AST.obj.DebugPoint;
import AST.obj.MethodCalled;
import AST.obj.MethodTest;
import AST.stm.AssertEqualStm;
import AST.stm.MethodInvocationStm;
import AST.stm.abstrct.AssertStatement;
import fix.object.DebugData;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            if (assertStatement instanceof AssertEqualStm) {
                //Case 1.1 actual  = MethodInvocation
                //TODO: FIX debugData (convertString...)
                String expected = expectedToStm((((AssertEqualStm) assertStatement).getExpected()));
                DebugData debugData = new DebugData(expected,
                        methodTest.getMethodName());
                if (((AssertEqualStm) assertStatement).getActual() instanceof MethodInvocation) {
                    MethodInvocation methodInvocation =
                            (MethodInvocation) ((AssertEqualStm) assertStatement).getActual();
                    MethodInvocationStm methodInvocationStm = methodTest.getMethodNode()
                            .parserMethodInvoStm(methodInvocation, assertStatement.getLine());
                    //parser methodInvo
                    findDebugWithMethodInvoStm(methodInvocationStm, folderNode);
                    debugData.setDebugPoints(debugPoints);
                }
                debugDataList.add(debugData);
            } else {
                logger.error("Chưa xử lý 1");
            }
        }
        return debugDataList;
    }

    private static String expectedToStm(Object expected) {
        if (expected instanceof InfixExpressionNode) {
            InfixExpressionNode infix = (InfixExpressionNode) expected;
            String stm = infixToStm(infix) ;
            return stm;
        } else {
            return expected.toString();
        }
    }

    private static String infixToStm(InfixExpressionNode infix) {
        String operator = infix.getOperator();
        String content = infix.getLeft() + infix.getOperator() + infix.getRight();
        for (Object obj : infix.getExtendedOperands()) {
            if (obj instanceof String) {
                if (operator.equals("+") ) {
                    content = content + obj;
                } else {
                    logger.error("Chuwa xuw ly TH: '" + operator + "'");
                }
            } else {
                logger.error("Chuwa xuw ly:infixToStm " + obj );
            }
        }
        return content;
    }

    private static void findDebugWithMethodInvoStm(MethodInvocationStm methodInvocationStm, FolderNode folderNode) {
        List<MethodCalled> methodCalleds = methodInvocationStm.getMethodsCalled();
        ClassNode classNode = folderNode.findClassByName(methodInvocationStm.getTypeVar());
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
        findDebugWithArgurements(methodCalleds, folderNode);
    }

    /**
     * Parser argurements if has MethodInvocationStm need to continue parser and set DebugPoint
     * @param methodCalleds
     * @param folderNode
     */
    private static void findDebugWithArgurements(List<MethodCalled> methodCalleds, FolderNode folderNode) {
        for (MethodCalled methodCalled : methodCalleds) {
            List<Object> argurements = methodCalled.getAgurementTypes();
            if (argurements != null) {
                for (Object obj : argurements) {
                    if (obj instanceof MethodInvocationStm) {
                        findDebugWithMethodInvoStm((MethodInvocationStm) obj, folderNode);
                    }
                }
            }
        }
    }

    /**
     * set DebugPoints for all Returns statements if methodNode
     * @param methodNode
     * @param classNode
     * @param folderNode
     */
    private static void addDebugPointInReturnStatements(MethodNode methodNode, ClassNode classNode, FolderNode folderNode) {
        List<StatementNode> returnStatements = methodNode.getReturnStatements();
        for (StatementNode statementNode : returnStatements) {
            DebugPoint debugPoint = new DebugPoint(classNode.getName(), statementNode.getLine(), statementNode.getKeyVar());
            addDebugPoint(debugPoint);
            findDebugFromStatement(statementNode, methodNode, classNode, folderNode);
        }
    }

    /**
     * Set debugPoint of imported statement ( If statement has MethodInvocation => continue
     * Debug at return statements of MethodInvocation.)
     * @param statementNode
     * @param methodNode
     * @param classNode
     * @param folderNode
     */
    private static void findDebugFromStatement(StatementNode statementNode, MethodNode methodNode, ClassNode classNode, FolderNode folderNode) {
        if (statementNode.getStatementNode() instanceof MethodInvocationStm) {
            // set in method of methodInvocation
            findDebugWithMethodInvoStm((MethodInvocationStm) statementNode.getStatementNode(), folderNode);
            //set related var
            findDebugWithRelatedVar(statementNode, methodNode, classNode, folderNode);
        } else if (statementNode.getStatementNode() instanceof BaseVariable) {
            //set Related var
            findDebugWithRelatedVar(statementNode, methodNode, classNode, folderNode);
        } else if (statementNode.getStatementNode() instanceof InfixExpressionNode) {
            //execute
            findDebugInfixExpressionNode((InfixExpressionNode) statementNode.getStatementNode(), methodNode, classNode, folderNode);
        } else {
            logger.error("Chưa xử lý:setDebugPointFromStatement " + statementNode.getStatementNode());
        }
    }

    /**
     * parser all elements in InfixExpression, if it has methodInvocation => continue set Debug
     * @param infixExpressionNode
     */
    private static void findDebugInfixExpressionNode(InfixExpressionNode infixExpressionNode, MethodNode methodNode, ClassNode classNode, FolderNode folderNode) {
        if (infixExpressionNode.getLeft() instanceof MethodInvocationStm) {
            findDebugWithMethodInvoStm((MethodInvocationStm) infixExpressionNode.getLeft(), folderNode);
        }
        if (infixExpressionNode.getRight() instanceof MethodInvocationStm) {
            findDebugWithMethodInvoStm((MethodInvocationStm) infixExpressionNode.getRight(), folderNode);
        }
        for (Object obj:
             infixExpressionNode.getExtendedOperands()) {
            if (obj instanceof MethodInvocationStm) {
                findDebugWithMethodInvoStm((MethodInvocationStm) obj, folderNode);
            }
        }
    }

    /**
     * Set all DebugPoint with related statement of "varName" - imported var
     * @param statementNode
     * @param classNode
     * @param folderNode
     */
    private static void addDebugPointRelatedFromStatement(StatementNode statementNode, ClassNode classNode, FolderNode folderNode) {
        if (statementNode.getStatementNode() instanceof MethodInvocationStm) {
            DebugPoint debugPoint = new DebugPoint(classNode.getName(), ((MethodInvocationStm) statementNode.getStatementNode()).getLine(),
                   statementNode.getKeyVar());
            addDebugPoint(debugPoint);
            findDebugWithMethodInvoStm((MethodInvocationStm) statementNode.getStatementNode(), folderNode);
        } else if (statementNode.getStatementNode() instanceof BaseVariable) {
            logger.info("I have not needed to use this yet");
        } else {
            logger.error("Chưa xử lý:setDebugPointFromStatement " + statementNode.getStatementNode());
        }
    }

    /**
     * Get ALl Statements from imported var => then setDugbug related var
     * @param statementNode
     * @param classNode
     * @param folderNode
     */
    private static void findDebugWithRelatedVar(StatementNode statementNode, MethodNode methodNode, ClassNode classNode, FolderNode folderNode) {
        if (statementNode.getStatementNode() instanceof MethodInvocationStm) {
            String varname = ((MethodInvocationStm) statementNode.getStatementNode()).getVarName();
            int line = ((MethodInvocationStm) statementNode.getStatementNode()).getLine();
            List<StatementNode> statementsRelated = getStatementsRelated(methodNode, classNode, varname, line);
            if (statementsRelated != null) {
                findStatementsRelated(statementsRelated, classNode, folderNode);
            }
        } else if (statementNode.getStatementNode() instanceof BaseVariable) {
            String varName = ((BaseVariable) statementNode.getStatementNode()).getVarname();
            int line = ((BaseVariable) statementNode.getStatementNode()).getLine();
            List statementsRelated = getStatementsRelated(methodNode, classNode, varName, line);
            if (statementsRelated != null) {
                findStatementsRelated(statementsRelated, classNode, folderNode);
            }
        } else {
            logger.error("Chưa xử lý:setDebugPointWithRelatedVar " + statementNode);
        }
    }

    private static List<DebugPoint> findStatementsRelated(List<StatementNode> statementsRelated, ClassNode classNode, FolderNode folderNode) {
        for (StatementNode stmRelated :
                statementsRelated) {
            addDebugPointRelatedFromStatement(stmRelated, classNode, folderNode);
        }
        return null;
    }

    /**
     * Get All related statements form varname
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
