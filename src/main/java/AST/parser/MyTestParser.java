package AST.parser;

import AST.node.ClassNode;
import AST.node.MethodNode;
import AST.object.Assert;
import AST.object.AssertEqual;
import AST.object.AssertTrue;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.internal.compiler.ast.DoubleLiteral;
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
    enum TestType {
        ANNOTATION_TEST, EXTENT_TEST_CASE
    }

    public void myTestParser(String pathToFile, TestType type) throws IOException {
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
            List<MethodNode> methodNodes = classNode.getMethodList();
            for (MethodNode methodNode : methodNodes) {
                List<Assert> asserts = parserAssertStatements(methodNode, type, cu);
                System.out.println(asserts);
            }
        }
    }

    /**
     * parser for annotation
     * @param methodNode
     * @param type
     * @return
     */
    private List<Assert> parserAssertStatements(MethodNode methodNode, TestType type, CompilationUnit cu) {
        List<Assert> asserts = new ArrayList<>();
        List statements = methodNode.getStatements();
        int line = -1;

        for (Object stm : statements) {
            if (type.equals(TestType.ANNOTATION_TEST)) {
                if (stm instanceof ExpressionStatement) {
                    Expression expression = ((ExpressionStatement) stm).getExpression();
                    line = cu.getLineNumber(expression.getStartPosition());
                    if (expression instanceof MethodInvocation) {
                        if (((MethodInvocation) expression).getName().toString().equals("assertEquals")) {
                            List arguments = ((MethodInvocation) expression).arguments();
                            Assert assertEqual = getAssertEquals(arguments, line);
                            asserts.add(assertEqual);

                        } else if (((MethodInvocation) expression).getName().toString().equals("assertTrue")) {
                            List args = ((MethodInvocation) expression).arguments();
                            Assert assertTrue = getAssertTrue(args, line);
                            asserts.add(assertTrue);

                        }
                    }
                }
            }
        }
        return asserts;
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
    private Assert getAssertEquals(List arguments, int line) {
        AssertEqual assertEqual = new AssertEqual();
        // 1.for Assert.assertEquals(message, expected, actual)
        if (arguments.size() == 3) {
            if (arguments.get(0) instanceof StringLiteral) {
                assertEqual = new AssertEqual((StringLiteral) arguments.get(0),
                        arguments.get(1), arguments.get(2), line);
            } else {
                //2. Assert.assertEquals(double expected, double actual, double delta),
                if (arguments.get(0) instanceof NumberLiteral
                        && arguments.get(2) instanceof NumberLiteral) {
                    assertEqual = new AssertEqual(arguments.get(0), arguments.get(1),
                            (NumberLiteral) arguments.get(2), line);
                }
            }
            //3.  for Assert.assertEquals(expected, actual)
        } else if (arguments.size() == 2) {
            assertEqual = new AssertEqual(
                    arguments.get(0), arguments.get(1), line);
        }
        return assertEqual;
    }

    /**
     * Now, I have 2 cases:
     * 1. Assert.assertTrue(boolean condition),
     * 2. Assert.assertTrue(message, boolean condition),
     *
     * @param args
     * @return
     */
    private Assert getAssertTrue(List args, int line) {
        AssertTrue assertTrue = new AssertTrue();
        if (args.size() == 1) {
            assertTrue = new AssertTrue(args.get(0), line);
        } else if (args.size() == 2) {
            if (args.get(0) instanceof StringLiteral) {
                assertTrue = new AssertTrue((StringLiteral) args.get(0), args.get(1), line);
            }
        }
        return assertTrue;
    }

    /**
     * parser and get info of statement
     *
     * @param statements
     */
    public void parserStatements(List statements) {
        for (Object stm : statements) {
            if (stm instanceof VariableDeclarationStatement) {
                System.out.println(((VariableDeclarationStatement) stm).getType());
            } else if (stm instanceof IfStatement) {
                System.out.println(((IfStatement) stm).getExpression());
            } else if (stm instanceof ExpressionStatement) {
                if (((ExpressionStatement) stm).getExpression() instanceof MethodInvocation) {
                    System.out.println(stm.toString());
                } else if (((ExpressionStatement) stm).getExpression() instanceof Assignment) {
                    System.out.println("ASSIGN ment");
                    System.out.println(((Assignment) ((ExpressionStatement) stm).getExpression()).getLeftHandSide());
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
