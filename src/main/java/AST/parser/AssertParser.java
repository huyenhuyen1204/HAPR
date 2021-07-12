package AST.parser;

import AST.stm.abst.AssertStatement;
import AST.stm.node.AssertEqualStmNode;
import AST.stm.node.AssertTrueStmNode;
import AST.stm.node.InfixExpressionStmNode;
import AST.stm.nodetype.BooleanNode;
import AST.stm.nodetype.NumbericNode;
import AST.stm.nodetype.StringNode;
import org.eclipse.jdt.core.dom.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
/**
 * Parser mytest, have to list all AssertEquals usted to
 * Now, I have 3 cases:
 * 1. Assert.assertEquals(message, expect, actual),
 * 2. Assert.assertEquals(double expected, double actual, double delta),
 * 3. Assert.assertEquals(expect, actual)
 */
public class AssertParser {
    private static Logger logger = LoggerFactory.getLogger(AssertParser.class);

    public static AssertStatement parserAssert (ExpressionStatement stm, CompilationUnit cu) {
        Expression expression =  stm.getExpression();
        int line = cu.getLineNumber(expression.getStartPosition());
        //Case 1:
        if (expression instanceof MethodInvocation) {
            if (((MethodInvocation) expression).getName().toString().equals("assertEquals")) {
                List arguments = ((MethodInvocation) expression).arguments();
                AssertStatement assertEqual = getAssertEquals(arguments, line);

                if (assertEqual != null) {
                    return assertEqual;
                }

            } else if (((MethodInvocation) expression).getName().toString().equals("assertTrue")) {
                List args = ((MethodInvocation) expression).arguments();
                AssertStatement assertTrue = getAssertTrue(args, line );
                if (assertTrue != null) {
                    return assertTrue;
                }

            }
        } else {
            logger.error("Chua xu lý:parserAssert");
            return null;
        }
        return null;
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
    private static AssertStatement getAssertEquals(List arguments, int line) {
        AssertEqualStmNode assertEqualStmNode = null;
        // 1.for Assert.assertEquals(message, expected, actual)
        if (arguments.size() == 3) {
            if (arguments.get(0) instanceof StringLiteral) {
                Object numbersString =  parserParam(arguments.get(1), line);
                assertEqualStmNode = new AssertEqualStmNode((StringLiteral) arguments.get(0),
                        numbersString, arguments.get(2), line);
            } else {
                //2. Assert.assertEquals(double expected, double actual, double delta),
                if (arguments.get(0) instanceof NumberLiteral
                        && arguments.get(2) instanceof NumberLiteral) {
                    assertEqualStmNode = new AssertEqualStmNode(arguments.get(0), arguments.get(1),
                            (NumberLiteral) arguments.get(2), line);
                }
            }
            //3.  for Assert.assertEquals(expected, actual)
        } else if (arguments.size() == 2) {
            Object expected = parserParam(arguments.get(0), line);
            assertEqualStmNode = new AssertEqualStmNode(
                    expected, arguments.get(1), line);
        }
        if (assertEqualStmNode == null) {
            logger.error("CAN'T PARSER: assertEquals(" + arguments.toString() + ")");
        }
        return assertEqualStmNode;
    }

    private static Object parserParam(Object obj, int line) {
        if (obj instanceof StringLiteral) {
            return new StringNode(line, null, ((StringLiteral) obj).getLiteralValue(),obj.toString() );
        } else if (obj instanceof NumberLiteral) {
            return new NumbericNode(obj); //?
        } else if (obj instanceof BooleanLiteral) {
            return new BooleanNode(((BooleanLiteral) obj).booleanValue(), ((BooleanLiteral) obj).getStartPosition(), ((BooleanLiteral) obj).getStartPosition() + ((BooleanLiteral) obj).getLength(),
                    obj.toString(), line);
        } else if (obj instanceof InfixExpression) {
            InfixExpression infixEx = (InfixExpression) obj;
            List<Object> objects = parserListObject(infixEx.extendedOperands(), line);
            Object left = parserParam(infixEx.getLeftOperand(), line);
            Object right = parserParam(infixEx.getRightOperand(), line);
            InfixExpressionStmNode infixExpressionStmNode = new InfixExpressionStmNode(infixEx.getOperator().toString(),
                    left, right, objects, line, null, infixEx.toString() );
            return infixExpressionStmNode;
        } else {
            return obj;
        }
    }


    private static List<Object> parserListObject(List extendedOperands, int line) {
        List<Object> objects = new ArrayList<>();
        for (Object obj : extendedOperands) {
            Object var = null;
            if (obj instanceof StringLiteral) {
                var =  new StringNode(line, null, ((StringLiteral) obj).getLiteralValue(),obj.toString() );
            } else if (obj instanceof NumberLiteral) {
                var = new NumbericNode(obj); //?
            } else if (obj instanceof BooleanLiteral) {
                var = new BooleanNode(((BooleanLiteral) obj).booleanValue(), ((BooleanLiteral) obj).getStartPosition(), ((BooleanLiteral) obj).getStartPosition() + ((BooleanLiteral) obj).getLength(),
                        obj.toString(), line);
            } else {
                var = obj;
            }
            objects.add(var);
        }
        return objects;
    }

    /**
     * Now, I have 2 cases:
     * 1. Assert.assertTrue(boolean condition),
     * 2. Assert.assertTrue(message, boolean condition),
     *
     * @param args
     * @return
     */
    private static AssertStatement getAssertTrue(List args, int line) {
        AssertTrueStmNode assertTrueStmNode = null;
        if (args.size() == 1) {
            assertTrueStmNode = new AssertTrueStmNode(args.get(0), line);
        } else if (args.size() == 2) {
            if (args.get(0) instanceof StringLiteral) {
                assertTrueStmNode = new AssertTrueStmNode((StringLiteral) args.get(0), args.get(1), line);
            }
        }
        if (assertTrueStmNode == null) {
            logger.error("CAN'T PARSER: assertTrue(" + args.toString() + ")");
        }
        return assertTrueStmNode;
    }

}
