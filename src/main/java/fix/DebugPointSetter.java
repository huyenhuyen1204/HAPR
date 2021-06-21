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

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class DebugPointSetter {
    public static final Logger logger = LoggerFactory.getLogger(DebugPointSetter.class);
    private static List<DebugPoint> debugPoints;
//    public static List<DebugData> genDebugPoints(FolderNode folderNode, MethodTest methodTest, ClassNode classNode) {
//        List<AssertStatement> assertStatements = methodTest.getAssertList();
//        List<ClassNode> classNodes = folderNode.getClassNodes();
//        List<DebugData> debugDatas = new ArrayList<>();
//        for (AssertStatement assertStatement : assertStatements) {
//            //Case 1: AssertEqualStm
//            if (assertStatement instanceof AssertEqualStm) {
//                //Case 1.1 MethodInvocation
//                if (((AssertEqualStm) assertStatement).getActual() instanceof MethodInvocation) {
//                    MethodInvocation methodInvocation =
//                            (MethodInvocation) ((AssertEqualStm) assertStatement).getActual();
//
//                    List params = methodInvocation.arguments();
//
//                    MethodInvocationStm methodInvoStm = ParserHelper.getVarClassAndMethod(methodInvocation);
//                    InitStatement initStatement = classNode.findTypeVar(methodInvoStm.getVarClass(), methodTest.getMethodName(), params);
//                    if (initStatement != null) {
//                        if (methodInvoStm != null) {
//                            MethodNode methodNode = ParserHelper.findMethodInClass(classNodes, initStatement.getType().toString(), methodInvoStm.getMethodName(), params);
//                            if (initStatement != null) {
//                                DebugPoint debugPoint = new DebugPoint(initStatement.getType(), methodNode.getEndLine());
//                                String expected = convertString(((AssertEqualStm) assertStatement).getExpected().toString());
//                                DebugData debugData = new DebugData(debugPoint, expected, methodTest.getMethodName());
//                                debugData.setAssertStatement(assertStatement);
//                                debugDatas.add(debugData);
//                            }
//                            ParserHelper.genListByMethodNode(methodNode);
//                        }
//                    }
//                } else {
//                    //TODO: chưa xử lý
//                    logger.error("Chưa xử lý 1");
//                }
//            }
//        }
//        return debugDatas;
//    }


    public static void setUTF8() {
        System.setProperty("file.encoding", "UTF-8");

        Field charset = null;
        try {
            charset = Charset.class.getDeclaredField("defaultCharset");
            charset.setAccessible(true);
            charset.set(null, null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

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

    public static List<DebugData> genDebugPoints(FolderNode folderNode, MethodTest methodTest, ClassNode classNode) {
        List<DebugData> debugDataList = new ArrayList<>();
        List<AssertStatement> assertStatements = methodTest.getAssertList();
        for (AssertStatement assertStatement : assertStatements) {
            debugPoints = new ArrayList<>();
            //Case 1: AssertEqualStm
            if (assertStatement instanceof AssertEqualStm) {
                //Case 1.1 MethodInvocation
                DebugData debugData = new DebugData(convertString(((AssertEqualStm) assertStatement).getExpected().toString()),
                        methodTest.getMethodName());
                if (((AssertEqualStm) assertStatement).getActual() instanceof MethodInvocation) {
                    MethodInvocation methodInvocation =
                            (MethodInvocation) ((AssertEqualStm) assertStatement).getActual();
                    MethodInvocationStm methodInvocationStm = methodTest.getMethodNode()
                            .parserMethodInvoStm(methodInvocation, assertStatement.getLine());
                    setDebugPointWithMethodInvoStm(methodInvocationStm, folderNode);
                    debugData.setDebugPoints(debugPoints);
                }
                debugDataList.add(debugData);
            } else {
                logger.error("Chưa xử lý 1");
            }
        }
        return debugDataList;
    }

    private static void setDebugPointWithMethodInvoStm(MethodInvocationStm methodInvocationStm, FolderNode folderNode) {
        List<MethodCalled> methodCalleds = methodInvocationStm.getMethodsCalled();
        ClassNode classNode = folderNode.findClassByName(methodInvocationStm.getTypeVar());
        if (classNode != null) {
            List<MethodNode> methodNodes = classNode.getMethodList();
            for (MethodCalled methodCalled :
                    methodCalleds) {
                MethodNode methodNode = getMethodNode(methodCalled, methodNodes);
                if (methodNode != null) {
                    setDebugPointInReturnStatements(methodNode, classNode, folderNode);
                }
            }
        }
        setDebugPointWithArgurements(methodCalleds, folderNode);
    }

    private static void setDebugPointWithArgurements(List<MethodCalled> methodCalleds, FolderNode folderNode) {
        for (MethodCalled methodCalled : methodCalleds) {
            List<Object> argurements = methodCalled.getAgurementTypes();
            if (argurements != null) {
                for (Object obj : argurements) {
                    if (obj instanceof MethodInvocationStm) {
                        setDebugPointWithMethodInvoStm((MethodInvocationStm) obj, folderNode);
                    }
                }
            }
        }
    }

    private static void setDebugPointInReturnStatements(MethodNode methodNode, ClassNode classNode, FolderNode folderNode) {
        List<StatementNode> returnStatements = methodNode.getReturnStatements();
        for (StatementNode statementNode : returnStatements) {
            DebugPoint debugPoint = new DebugPoint(classNode.getName(), statementNode.getLine());
            //set for returnStm
            addDebugPoint(debugPoint);
//            debugPoints.add(debugPoint);
            setDebugPointFromStatement(statementNode, methodNode, classNode, folderNode);
        }
    }

    private static void setDebugPointFromStatement(StatementNode statementNode, MethodNode methodNode, ClassNode classNode, FolderNode folderNode) {
        if (statementNode.getStatementNode() instanceof MethodInvocationStm) {
            setDebugPointWithMethodInvoStm((MethodInvocationStm) statementNode.getStatementNode(), folderNode);
            //set related var
            setDebugPointWithRelatedVar(statementNode, methodNode, classNode, folderNode);
        } else if (statementNode.getStatementNode() instanceof BaseVariable) {
            //set Related var
            setDebugPointWithRelatedVar(statementNode, methodNode, classNode, folderNode);
        } else {
            logger.error("Chưa xử lý:setDebugPointFromStatement " + statementNode.getStatementNode());
        }
    }

    private static void setDebugPointRelatedFromStatement(StatementNode statementNode, ClassNode classNode, FolderNode folderNode) {
        if (statementNode.getStatementNode() instanceof MethodInvocationStm) {
            DebugPoint debugPoint = new DebugPoint(classNode.getName(), ((MethodInvocationStm) statementNode.getStatementNode()).getLine());
            addDebugPoint(debugPoint);
            setDebugPointWithMethodInvoStm((MethodInvocationStm) statementNode.getStatementNode(), folderNode);

        } else if (statementNode.getStatementNode() instanceof BaseVariable) {

        } else {
            logger.error("Chưa xử lý:setDebugPointFromStatement " + statementNode.getStatementNode());
        }
    }

    private static void setDebugPointWithRelatedVar(StatementNode statementNode, MethodNode methodNode, ClassNode classNode, FolderNode folderNode) {
        if (statementNode.getStatementNode() instanceof MethodInvocationStm) {
            String varname = ((MethodInvocationStm) statementNode.getStatementNode()).getVarName();
            int line = ((MethodInvocationStm) statementNode.getStatementNode()).getLine();
            List<StatementNode> statementsRelated = getStatementsRelated(methodNode, classNode, varname, line);
            if (statementsRelated != null) {
                debugPointStatementsRelated(statementsRelated, classNode, folderNode);
            }
        } else if (statementNode.getStatementNode() instanceof BaseVariable) {
            String varName = ((BaseVariable) statementNode.getStatementNode()).getVarname();
            int line = ((BaseVariable) statementNode.getStatementNode()).getLine();
            List statementsRelated = getStatementsRelated(methodNode, classNode, varName, line);
            if (statementsRelated != null) {
                debugPointStatementsRelated(statementsRelated, classNode, folderNode);
            }
        } else {
            logger.error("Chưa xử lý:setDebugPointWithRelatedVar " + statementNode);
        }
        System.out.println("ABAAH");
    }

    private static List<DebugPoint> debugPointStatementsRelated(List<StatementNode> statementsRelated, ClassNode classNode, FolderNode folderNode) {
        for (StatementNode stmRelated :
                statementsRelated) {
            setDebugPointRelatedFromStatement(stmRelated, classNode, folderNode);
//            debugPointStatementRelated(Object stmRelated)
            // TODO: need parser
        }
        return null;
    }

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
