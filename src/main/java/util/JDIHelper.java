package util;

import AST.node.ClassNode;
import AST.node.FolderNode;
import AST.node.MethodNode;
import AST.obj.DebugPoint;
import AST.stm.AssertEqualStm;
import AST.stm.MethodInvocationStm;
import AST.stm.abstrct.AssertStatement;
import AST.stm.abstrct.InitStatement;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class JDIHelper {
    public static final Logger logger = LoggerFactory.getLogger(JDIHelper.class);
    public static List<DebugPoint> genDebugPoints(FolderNode folderNode, List<AssertStatement> assertStatements, ClassNode classNode) {
        List<ClassNode> classNodes = folderNode.getClassNodes();
        List<DebugPoint> debugPoints = new ArrayList<>();
        for (AssertStatement assertStatement : assertStatements) {
            if (assertStatement instanceof AssertEqualStm) {
                if (((AssertEqualStm) assertStatement).getActual() instanceof MethodInvocation) {
                    MethodInvocation methodInvocation =
                            (MethodInvocation) ((AssertEqualStm) assertStatement).getActual();
                    List params = methodInvocation.arguments();
                    MethodInvocationStm methodInvoStm = getClassName(methodInvocation);
                    InitStatement initStatement = classNode.findTypeVar(methodInvoStm.getVarClass(), methodInvoStm.getMethodName(), params);
                    if (initStatement != null) {
                        if (methodInvoStm != null) {
                            MethodNode methodNode = ParserHelper.findMethodInClass(classNodes, initStatement.getType().toString(), methodInvoStm.getMethodName(), params);
                            if (initStatement != null) {
                                DebugPoint debugPoint = new DebugPoint(initStatement.getType(), methodNode.getEndLine());
                                if (!JavaLibrary.debugPointContainsName(debugPoints, debugPoint)) {
                                    debugPoints.add(debugPoint);
                                }
                            }
                        }
                    }
                } else {
                    //TODO: chưa xử lý
                    logger.error("Chưa xử lý 1");
                }
            }
        }
        return debugPoints;
    }

    public static MethodInvocationStm getClassName(MethodInvocation methodInvocation) {
        if (methodInvocation.getExpression() instanceof MethodInvocation) {
            return getClassName((MethodInvocation) methodInvocation.getExpression());
        } else {
            if (methodInvocation.getExpression() instanceof SimpleName) {
                String classname = ((SimpleName) methodInvocation.getExpression()).getIdentifier();
                String methodName = methodInvocation.getName().getIdentifier();
                return new MethodInvocationStm(classname, methodName);
            } else {
                return null;
            }
        }

    }
}
