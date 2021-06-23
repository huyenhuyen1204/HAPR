package AST.parser;

import AST.node.ClassNode;
import AST.node.FieldNode;
import AST.node.MethodNode;
import AST.stm.abstrct.AssertStatement;
import AST.stm.AssertEqualStm;
import AST.stm.AssertTrueStm;
import AST.obj.MethodTest;
import common.TestType;
import org.eclipse.jdt.core.dom.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.FileService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser mytest, have to list all AssertEquals usted to
 * Now, I have 3 cases:
 * 1. Assert.assertEquals(message, expect, actual),
 * 2. Assert.assertEquals(double expected, double actual, double delta),
 * 3. Assert.assertEquals(expect, actual)
 */
public class MyTestParser {
    private static Logger logger = LoggerFactory.getLogger(MyTestParser.class);


    public List<ClassNode> myTestParser(String pathToFile, TestType type) throws IOException {
        List<MethodTest> methodTests = new ArrayList<>();
        File file = new File(pathToFile);
        String content = FileService.readFileToString(file.getAbsolutePath());

        ASTParser parser = ASTParser.newParser(AST.JLS8);
        parser.setSource(content.toCharArray());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
        List<ClassNode> classNodes = new ArrayList<>();
        cu.accept(new ASTVisitor() {
            public boolean visit(TypeDeclaration node) {
                ClassNode classNode = new ClassNode();
                if (node != null) {
                    classNode.setInforFromASTNode(node, cu);
                    classNodes.add(classNode);
                    return false;
                }
                return true;
            }
        });

        for (ClassNode classNode : classNodes) {
            System.out.println(classNode.toString());
            List<FieldNode> fieldNodes = classNode.getFieldList();
            for (FieldNode fieldNode : fieldNodes) {
                System.out.println(fieldNode.toString());
            }
            List<MethodNode> methodNodes = classNode.getMethodList();
            for (MethodNode methodNode : methodNodes) {
                MethodTest methodTest = parserAssertStatements(methodNode, type, cu);
                methodTests.add(methodTest);
//                System.out.println(methodNode.toString());
            }
            classNode.setMethodTests(methodTests);
        }
        return classNodes;
    }

    /**
     * parser for annotation
     *
     * @param methodNode
     * @param type
     * @return
     */
    private MethodTest parserAssertStatements(MethodNode methodNode, TestType type, CompilationUnit cu) {
        MethodTest methodTest = new MethodTest(methodNode.getName(), methodNode);
        List statements = methodNode.getStatements();
        int line = -1;

        for (Object stm : statements) {
            if (type.equals(TestType.ANNOTATION_TEST)) {
                if (stm instanceof ExpressionStatement) {
                    Expression expression = ((ExpressionStatement) stm).getExpression();
                    line = cu.getLineNumber(expression.getStartPosition());
                    //Case 1:
                    if (expression instanceof MethodInvocation) {
                        if (((MethodInvocation) expression).getName().toString().equals("assertEquals")) {
                            List arguments = ((MethodInvocation) expression).arguments();
                            AssertStatement assertEqual = getAssertEquals(arguments, line);

                            if (assertEqual != null) {
                                methodTest.addToAsserts(assertEqual);
                            }

                        } else if (((MethodInvocation) expression).getName().toString().equals("assertTrue")) {
                            List args = ((MethodInvocation) expression).arguments();
                            AssertStatement assertTrue = getAssertTrue(args, line);
                            if (assertTrue != null) {
                                methodTest.addToAsserts(assertTrue);
                            }

                        }
                    }
                }
            }
        }
        return methodTest;
    }

    /**
     * Now, I have 3 cases:
     * 1. Assert.assertEquals(message, expect, actual),
     * 2. Assert.assertEquals(double expected, double actual, double delta),
     * 3. Assert.assertEquals(expect, actual)
     *
     * @param arguments
     * @return
     */
    private AssertStatement getAssertEquals(List arguments, int line) {
        AssertEqualStm assertEqualStm = null;
        // 1.for Assert.assertEquals(message, expected, actual)
        if (arguments.size() == 3) {
            if (arguments.get(0) instanceof StringLiteral) {
                assertEqualStm = new AssertEqualStm((StringLiteral) arguments.get(0),
                        arguments.get(1), arguments.get(2), line);
            } else {
                //2. Assert.assertEquals(double expected, double actual, double delta),
                if (arguments.get(0) instanceof NumberLiteral
                        && arguments.get(2) instanceof NumberLiteral) {
                    assertEqualStm = new AssertEqualStm(arguments.get(0), arguments.get(1),
                            (NumberLiteral) arguments.get(2), line);
                }
            }
            //3.  for Assert.assertEquals(expected, actual)
        } else if (arguments.size() == 2) {
            assertEqualStm = new AssertEqualStm(
                    arguments.get(0), arguments.get(1), line);
        }
        if (assertEqualStm == null) {
            logger.error("CAN'T PARSER: assertEquals(" + arguments.toString() + ")");
        }
        return assertEqualStm;
    }

    /**
     * Now, I have 2 cases:
     * 1. Assert.assertTrue(boolean condition),
     * 2. Assert.assertTrue(message, boolean condition),
     *
     * @param args
     * @return
     */
    private AssertStatement getAssertTrue(List args, int line) {
        AssertTrueStm assertTrueStm = null;
        if (args.size() == 1) {
            assertTrueStm = new AssertTrueStm(args.get(0), line);
        } else if (args.size() == 2) {
            if (args.get(0) instanceof StringLiteral) {
                assertTrueStm = new AssertTrueStm((StringLiteral) args.get(0), args.get(1), line);
            }
        }
        if (assertTrueStm == null) {
            logger.error("CAN'T PARSER: assertTrue(" + args.toString() + ")");
        }
        return assertTrueStm;
    }

    /**
     * parser and get info of statement
     *
     * @param statements
     */
    public void parserStatements(List statements) {
        for (Object stm : statements) {
            if (stm instanceof VariableDeclarationStatement) {
//                System.out.println(((VariableDeclarationStatement) stm).getType());
            } else if (stm instanceof IfStatement) {
//                System.out.println(((IfStatement) stm).getExpression());
            } else if (stm instanceof ExpressionStatement) {
                if (((ExpressionStatement) stm).getExpression() instanceof MethodInvocation) {
//                    System.out.println(stm.toString());
                } else if (((ExpressionStatement) stm).getExpression() instanceof Assignment) {
//                    System.out.println("ASSIGN ment");
//                    System.out.println(((Assignment) ((ExpressionStatement) stm).getExpression()).getLeftHandSide());
                }
            } else if (stm instanceof ReturnStatement) {

            }
        }
    }

    public static void main(String[] args) throws IOException {
        MyTestParser myTestParser = new MyTestParser();
        myTestParser.myTestParser("C:\\Users\\Dell\\Desktop\\APR_Test\\data_test\\83778\\MyTest.java",
                TestType.ANNOTATION_TEST);

//        JavaFileParser javaFileParser
    }
}
