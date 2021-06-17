package AST.stm.parser;

import AST.node.ClassNode;
import AST.node.MethodNode;
import AST.stm.MethodInvocationStm;
import AST.stm.abstrct.InitStatement;
import AST.stm.abstrct.Statement;
import org.eclipse.jdt.core.dom.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

//Just for return method
public class ReturnStatementParser {

    public static final Logger logger = LoggerFactory.getLogger(ReturnStatementParser.class);

    private MethodNode methodNode;
    List<Statement> rootVar;
    List<Statement> varRelationship;

//    public ReturnStatementParser(MethodNode methodNode) {
//        this.methodNode = methodNode;
//        rootVar = new ArrayList<>();
//        varRelationship = new ArrayList<>();
//        parserStatements();
//    }

//    public void parserStatements() {
//        List statements = methodNode.getStatements();
//        for (int i = statements.size() - 1; i >= 0; i--) {
//            parserStatement(statements.get(i));
//        }
//    }

//    private void parserStatement(Object statement) {
//        if (statement instanceof ReturnStatement) {
//            ReturnStatement returnstm = (ReturnStatement) statement;
//            Object returnExpression = returnstm.getExpression();
//            // return method invocation
//            rootVar = parserExpression(returnExpression, methodNode);
//
//        } else if (statement instanceof ExpressionStatement) {
//            //find var relationship with rootVar
//            if (rootVar.size() > 0) {
//                Object exp = ((ExpressionStatement) statement).getExpression();
//                List<Statement> stms = parserExpression(exp, methodNode);
//                addToVarList(stms);
//            } else {
//                logger.error("Chưa xử lý:parserStatement: " + statement.toString());
//            }
//
//        } else if (statement instanceof EnhancedForStatement) {
//            List stms  = parserBlock(statement);
//            if (stms != null) {
//                for (Object o : stms) {
//                    parserStatement(o);
////                    addToVarList(statementList);
//                }
//            }
//            System.out.println(statement.toString());
//        } else {
//            logger.error("Chưa xử lý:parserStatement: " + statement.toString());
//        }
//    }



    private List parserBlock(Object statement) {
        if (statement instanceof EnhancedForStatement) {
            Object block = ((EnhancedForStatement) statement).getBody();
            if (block instanceof Block) {
                return  ((Block) block).statements();
            } else {
                logger.error("Chưa xử lý:parserBlock" + block.toString());
            }
        } else {
            logger.error("Chưa xử lý:parserBlock" + statement.toString());
        }
        return null;
    }

    private void addToVarList(List<Statement> statements) {
        for (Statement stm : statements) {
            for (Statement statement : rootVar) {
                if (statement instanceof MethodInvocationStm
                && stm instanceof MethodInvocationStm ) {
                    if (((MethodInvocationStm) stm).getVarClass()
                    .equals(((MethodInvocationStm) statement).getVarClass())) {
                        varRelationship.add(stm);
                    }
                }
            }
        }
    }

//    private List<Statement> parserExpression(Object expression, MethodNode methodNode) {
//        List<Statement> statementList = new ArrayList<>();
//        //case 1: return methodInvocation
//        if (expression instanceof MethodInvocation) {
//            parserMethodInvoStm(methodNode, statementList, (MethodInvocation) expression);
//        } else {
//            logger.error("Chưa xử lý:parserExpression:" + expression);
//        }
//        return statementList;
//    }

//    private void parserMethodInvoStm(MethodNode methodNodee, List<Statement> statements, MethodInvocation methodInvocation) {
//        ClassNode classNode = (ClassNode) methodNodee.getParent();
//
//        if (methodInvocation.getExpression() instanceof MethodInvocation) {
//            MethodInvocation methodInvo = (MethodInvocation) methodInvocation.getExpression();
//            MethodInvocationStm methodInvocationStm = new MethodInvocationStm(methodInvo.getName().getIdentifier(),
//                    methodNodee.getName());
//            //set type
//            InitStatement initStatement = classNode.findTypeVar(methodInvocationStm.getVarClass(),
//                    methodInvocationStm.getMethodName(), methodNodee.getParameters());
//            if (initStatement != null) {
//                methodInvocationStm.setTypeVarClass(initStatement.getType());
//            }
//            //--end set type
//            statements.add(methodInvocationStm);
//            if (methodInvo.arguments().size() > 0) {
//                for (Object arg : methodInvo.arguments()) {
//                    if (arg instanceof MethodInvocation) {
//                        parserMethodInvoStm(methodNodee, statements, (MethodInvocation) arg);
//                    }
//                }
//            }
////            return getVarClassAndMethod((MethodInvocation) methodInvocation.getExpression());
//        } else {
//            if (methodInvocation.getExpression() instanceof SimpleName) {
//                String classname = ((SimpleName) methodInvocation.getExpression()).getIdentifier();
//                String methodName = methodInvocation.getName().getIdentifier();
//                //set type
//                InitStatement initStatement = classNode.findTypeVar(classname,
//                        methodNodee.getName(), methodNodee.getParameters());
//                MethodInvocationStm invocationStm = new MethodInvocationStm(classname, methodName);
//                invocationStm.setTypeVarClass(initStatement.getType());
//                //end set type
//                statements.add(invocationStm);
//                if (methodInvocation.arguments().size() > 0) {
//                    for (Object arg : methodInvocation.arguments()) {
//                        if (arg instanceof MethodInvocation) {
//                            parserMethodInvoStm(methodNodee, statements, (MethodInvocation) arg);
//                        }
//                    }
//                }
//            } else {
//                logger.error("Chưa xử lý: " + methodInvocation.getExpression().toString());
//            }
//        }
//
//    }
}
