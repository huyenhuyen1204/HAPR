package AST.node;

import AST.stm.abst.AssertStatement;
import AST.stm.node.*;
import AST.obj.InitObjectIndex;
import AST.stm.abst.StatementNode;
import AST.parser.ASTHelper;
import AST.stm.nodetype.StringNode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.jdt.core.dom.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.JavaLibraryHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cuong on 3/22/2017.
 */


public class MethodNode extends AbstractableElementNode {
    public static final Logger logger = LoggerFactory.getLogger(MethodNode.class);
    // protected boolean isAbstract;
    protected String returnType;
    protected List<ParameterNode> parameters;
    private String simpleName;
    private boolean isConstructor = false;
    private int startLine;
    private int endLine;
    private boolean returnStringOrNumber = true;
    List<InitNode> initNodes;
    List<StatementNode> returnStatements;
    List<AssertStatement> assertStatements;
    private static CompilationUnit cu;
    @JsonIgnore
    private List statements;

    public List getStatements() {
        return statements;
    }

    public void setStatements(List statements) {
        this.statements = statements;
    }

    public MethodNode() {
        this.initNodes = new ArrayList<>();
        this.returnStatements = new ArrayList<>();
        parameters = this.getParameters();
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public List<InitNode> getInitNodes() {
        return initNodes;
    }

    public void setInitNodes(List<InitNode> initNodes) {
        this.initNodes = initNodes;
    }

    public List<StatementNode> getReturnStatements() {
        return returnStatements;
    }

    public boolean isReturnStringOrNumber() {
        return returnStringOrNumber;
    }

    public void setReturnStringOrNumber(boolean returnStringOrNumber) {
        this.returnStringOrNumber = returnStringOrNumber;
    }

    public void setReturnStatements(List<StatementNode> returnStatements) {
        this.returnStatements = returnStatements;
    }

    @Override
    public int getStartLine() {
        return startLine;
    }

    @Override
    public void setStartLine(int line) {
        this.startLine = line;
    }

    @Override
    public int getEndLine() {
        return endLine;
    }

    @Override
    public void setEndLine(int line) {
        this.endLine = line;
    }

    public List<ParameterNode> getParameters() {
        ArrayList<ParameterNode> list = new ArrayList<>();
        for (Node node : this.getChildren()) {
            if (node instanceof ParameterNode) {
                list.add((ParameterNode) node);
            }
        }
        return list;
    }

    public void setParameters(List list) {
        this.parameters = list;
    }

    public void addParameter(ParameterNode param) {
        if (param != null) parameters.add(param);
    }

    public void addAllParameters(List<ParameterNode> params) {
        parameters.addAll(params);
    }

    public String getSimpleName() {
        return this.simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    @JsonProperty("isConstructor")
    public boolean isConstructor() {
        return isConstructor;
    }

    public void setConstructor(boolean constructor) {
        isConstructor = constructor;
    }

    @Override
    public String toString() {
        return "MethodNode{" +
                "id=" + this.id +
                "visibility=" + this.getVisibility() +
                ", returnType='" + returnType + '\'' +
                ", name='" + name + '\'' +
                ", isStatic=" + this.isStatic() +
                ", isAbstract=" + this.isAbstract() +
                ", isFinal=" + this.isFinal() +
                ", parameters=" + parameters +
                '}';
    }

    public void setInforFromASTNode(MethodDeclaration node, CompilationUnit cu) {
        this.cu = cu;
        if (node.getName() != null) {
            if (node.getName().getIdentifier() != null) {
                this.name = node.getName().getIdentifier();
            }
        }
        List statements = null;
        if (node.getBody() != null) {
            statements = node.getBody().statements();
        }
        this.setStatements(statements);

        //set ten cho phuong thuc
        this.setStartPosition(node.getStartPosition());
        int nodeLength = node.getLength();
        this.setStartLine(this.cu.getLineNumber(node.getStartPosition()));
        int endLineNumber = this.cu.getLineNumber(node.getStartPosition() + nodeLength) - 1;
        this.setEndLine(endLineNumber);
        if (node.isConstructor() == false) {
            Type s = node.getReturnType2();
            if (s != null) {
                this.setReturnType(ASTHelper.getFullyQualifiedName(s, this.cu));
            }
        } else {
            this.isConstructor = true;
            this.setReturnType("");
        }

        List visibilityList = node.modifiers();
        if (visibilityList.size() == 0) this.setVisibility(DEFAULT_MODIFIER);
        else {
            for (Object o : visibilityList) {
                if (o instanceof Modifier) {
                    Modifier m = (Modifier) o;
                    if (m.getKeyword().toFlagValue() == Modifier.ModifierKeyword.PUBLIC_KEYWORD.toFlagValue()) {
                        this.setVisibility(PUBLIC_MODIFIER);
                    } else if (m.getKeyword().toFlagValue() == Modifier.ModifierKeyword.PRIVATE_KEYWORD.toFlagValue()) {
                        this.setVisibility(PRIVATE_MODIFIER);
                    } else if (m.getKeyword().toFlagValue() == Modifier.ModifierKeyword.PROTECTED_KEYWORD.toFlagValue()) {
                        this.setVisibility(PROTECTED_MODIFIER);
                    } else if (m.getKeyword().toFlagValue() == Modifier.ModifierKeyword.STATIC_KEYWORD.toFlagValue()) {
                        this.setStatic(true);
                    } else if (m.getKeyword().toFlagValue() == Modifier.ModifierKeyword.FINAL_KEYWORD.toFlagValue()) {
                        this.setFinal(true);
                    } else if (m.getKeyword().toFlagValue() == Modifier.ModifierKeyword.ABSTRACT_KEYWORD.toFlagValue()) {
                        this.setAbstract(true);
                    } else {
                        this.setVisibility(DEFAULT_MODIFIER);
                    }
                }
            }
        }

        List parameters = node.parameters();
        List<Node> paraNodes = new ArrayList<>();
        for (Object o : parameters) {
            ParameterNode paraNode = new ParameterNode();
            if (o instanceof SingleVariableDeclaration) {
                SingleVariableDeclaration temp = (SingleVariableDeclaration) o;
                if (temp.getType() != null) {
                    if (temp.getType() instanceof SimpleType) {
                        SimpleType temp2 = (SimpleType) temp.getType();
                        if (temp2.getName() != null) {
                            if (temp2.getName().getFullyQualifiedName() != null) {
                                paraNode.setType(ASTHelper.getFullyQualifiedName(temp2, cu));
                            }
                            if (temp.getName().getIdentifier() != null) {
                                paraNode.setName(temp.getName().getIdentifier());
                            }
                        }
                        List modifiers = temp.modifiers();
                        for (Object m : modifiers) {
                            if (m instanceof Modifier) {
                                Modifier n = (Modifier) m;
                                if (n.getKeyword().toFlagValue() == Modifier.ModifierKeyword.FINAL_KEYWORD.toFlagValue()) {
                                    paraNode.setFinal(true);
                                }
                            }
                        }
                        paraNodes.add(paraNode);
                    } else if (temp.getType() instanceof PrimitiveType) {
                        PrimitiveType primitiveType = (PrimitiveType) temp.getType();
                        if (primitiveType.getPrimitiveTypeCode() != null) {
                            paraNode.setType(ASTHelper.getFullyQualifiedName(primitiveType, cu));
                        }
                        if (temp.getName() != null) {
                            if (temp.getName().getIdentifier() != null) {
                                paraNode.setName(temp.getName().getIdentifier());
                            }
                        }
                        List<Modifier> modifiers = temp.modifiers();
                        for (Modifier m : modifiers) {
                            if (m.getKeyword().toFlagValue() == Modifier.ModifierKeyword.FINAL_KEYWORD.toFlagValue()) {
                                paraNode.setFinal(true);
                            }
                        }
                        paraNodes.add(paraNode);
                    } else if (temp.getType() instanceof ParameterizedType) {
                        ParameterizedType parameterizedType = (ParameterizedType) temp.getType();
                        paraNode.setType(ASTHelper.getFullyQualifiedName(parameterizedType, cu));
                        if (temp.getName() != null) {
                            if (temp.getName().getIdentifier() != null) {
                                paraNode.setName(temp.getName().getIdentifier());
                            }
                        }
                        List<Modifier> modifiers = temp.modifiers();
                        for (Modifier m : modifiers) {
                            if (m.getKeyword().toFlagValue() == Modifier.ModifierKeyword.FINAL_KEYWORD.toFlagValue()) {
                                paraNode.setFinal(true);
                            }
                        }
                        paraNodes.add(paraNode);
                    } else if (temp.getType() instanceof ArrayType) {
                        ArrayType arrayType = (ArrayType) temp.getType();
                        paraNode.setType(ASTHelper.getFullyQualifiedName(arrayType, cu));
                        if (temp.getName() != null) {
                            if (temp.getName().getIdentifier() != null) {
                                paraNode.setName(temp.getName().getIdentifier());
                            }
                        }
                        List<Modifier> modifiers = temp.modifiers();
                        for (Modifier m : modifiers) {
                            if (m.getKeyword().toFlagValue() == Modifier.ModifierKeyword.FINAL_KEYWORD.toFlagValue()) {
                                paraNode.setFinal(true);
                            }
                        }
                        paraNodes.add(paraNode);
                    }
                }
            }
        }
        this.addChildren(paraNodes, cu);
    }

    //===================== By Huyen: parser statement=============================

    /**
     * parser and get information of statement
     *
     * @param statements
     */
    public void parserStatements(int level, List statements) {
        if (statements != null) {
            for (Object stm : statements) {
                //Init
                int line = this.cu.getLineNumber(((org.eclipse.jdt.core.dom.Statement) stm).getStartPosition());
                if (stm instanceof VariableDeclarationStatement) {
                    VariableDeclarationStatement variable = (VariableDeclarationStatement) stm;
                    parserVariableDeclarationInfo(level, variable, line);

                } else if (stm instanceof IfStatement) {
//                    logger.error("Chưa xử lý: IfStatement");
                    parserIfStatementInfo((IfStatement) stm, level);

                } else if (stm instanceof ExpressionStatement) {
                    parserExpressionStatementInfo((ExpressionStatement) stm, line);

                } else if (stm instanceof ReturnStatement) {
                    parserReturnInfo((ReturnStatement) stm, line);

                } else if (stm instanceof TryStatement) {
                    parserTryInfo ((TryStatement) stm, level);
//                    logger.error("Chưa xử lý Trystatement");

                } else if (stm instanceof EnhancedForStatement) {
                    parserEnhancedForInfo((EnhancedForStatement) stm, line, level);
//                    logger.error("Chưa xử lý ForStatement");

                } else if (stm instanceof SwitchStatement) {
                    parserSwitchStatementInfo((SwitchStatement) stm);
//                    logger.error("Chưa xử lý SwitchStatement");

                } else if (stm instanceof SuperConstructorInvocation) {
                    logger.info("Không xử lý: SuperConstructorInvocation");

                } else if (stm instanceof ForStatement) {
                    parserForInfo((ForStatement) stm, line, level);
                } else {
                    logger.error("Chưa xử lý:parserStatements " + statements.toString());
                }
            }
        }
    }

    private void parserTryInfo(TryStatement tryStm,int level) {
        if (tryStm.getBody() != null) {
            if (tryStm.getBody() instanceof Block) {
                List statements = ((Block) tryStm.getBody()).statements();
                parserStatements(level + 1, statements);
            } else {
                logger.error("Chưa xử lý:parserTryInfo ");
            }
        }
    }

    private void parserSwitchStatementInfo(SwitchStatement switchStatement) {
        List statements = switchStatement.statements();
        for (Object stm : statements) {
            Statement stmConvert = (Statement) stm;
            int line = cu.getLineNumber(stmConvert.getStartPosition());
            if (stm instanceof ReturnStatement) {
                parserReturnInfo((ReturnStatement) stm, line);
            }
        }
    }

    private void parserEnhancedForInfo(EnhancedForStatement stm, int line, int level) {
        if (stm.getParameter() != null) {
            InitNode initNode = new InitNode(level + 1, stm.getParameter().getName().getIdentifier(),
                    ASTHelper.getFullyQualifiedName(stm.getParameter().getType(), cu), line);
            this.initNodes.add(initNode);
        } else {
            logger.error("Chưa xử lý:parserEnhancedForInfo: " + stm.getParameter());
        }
        if (stm.getBody() != null) {
            if (stm.getBody() instanceof Block) {
                List statements = ((Block) stm.getBody()).statements();
                parserStatements(level + 1, statements);
            } else {
                logger.error("Chưa xử lý:parserEnhancedForInfo ");
            }
        }
    }

    private void parserForInfo(ForStatement stm, int line, int level) {
        List initializers = stm.initializers();
        // step1 parser inits;
        for (Object objInit : initializers) {
            if (objInit instanceof VariableDeclarationFragment) {
                parserVariableDeclarationInfo(level + 1, (VariableDeclarationStatement) objInit, line);
            }
        }
        //TODO: step2: need to parser "optionalConditionExpression (i <n ) of forStatement
        //step 3: parser Block;
        if (stm.getBody() != null) {
            if (stm.getBody() instanceof Block) {
                List statements = ((Block) stm.getBody()).statements();
                parserStatements(level + 1, statements);
            } else {
                logger.error("Chưa xử lý:parserEnhancedForInfo ");
            }
        }
        System.out.println("Parserfor");
    }

    private void parserIfStatementInfo(IfStatement ifStatement, int level) {
        if (ifStatement.getThenStatement() != null) {
            if (ifStatement.getThenStatement() instanceof Block) {
                List statements = ((Block) ifStatement.getThenStatement()).statements();
                parserStatements(level + 1, statements);
            } else {
                logger.error("Chưa xử lý:parserEnhancedForInfo ");
            }
        }
        //TODO: DO IT
    }

    private void parserReturnInfo(ReturnStatement stm, int line) {
        StatementNode statementNode = null;
        if (stm.getExpression() instanceof MethodInvocation) {
            returnStringOrNumber = false;
            MethodInvocationStmNode methodInvocationStmNode = parserMethodInvoStm((MethodInvocation) stm.getExpression(), line);
            //if it doesnt has "var" to call method (or this method can call in class)
            if (methodInvocationStmNode.getKeyVar() == null) {
                methodInvocationStmNode.setTypeVar(this.getParent().getName());
            }
            statementNode = methodInvocationStmNode;
//            statementNode.setStatementNode(methodInvocationStmNode, methodInvocationStmNode.getVarName(), stm.getExpression().toString());

        } else if (stm.getExpression() instanceof Assignment) {
            returnStringOrNumber = false;
            logger.error("Chua xu ly: parserReturnInfo " + stm.getExpression());
        } else if (stm.getExpression() instanceof SimpleName) {
            returnStringOrNumber = false;
            String varname = ((SimpleName) stm.getExpression()).getIdentifier();
            statementNode = new BaseVariableNode(line, varname, stm.getExpression().toString());
            setStatementToInits(statementNode, ((SimpleName) stm.getExpression()).getIdentifier(), line, stm.getExpression().toString());

        } else if (stm.getExpression() instanceof StringLiteral) {
            String value = JavaLibraryHelper.removeFirstAndLastChars(stm.getExpression().toString());
            statementNode = new StringNode(line, null, value, stm.getExpression().toString());
        } else if (stm.getExpression() instanceof NumberLiteral) {
            logger.error("Chuwa xu ly");
        } else if (stm.getExpression() instanceof InfixExpression) {
            returnStringOrNumber = false;
            InfixExpression infixEx = (InfixExpression) stm.getExpression();
            List<Object> objects = convertListToMethodInvoStms(infixEx.extendedOperands(), line);
            Object left = convertToMethodInvoStm(infixEx.getLeftOperand(), line);
            Object right = convertToMethodInvoStm(infixEx.getRightOperand(), line);
            statementNode = new InfixExpressionStmNode(infixEx.getOperator().toString(),
                    left, right, objects, line, null, stm.getExpression().toString());
        } else {
//            statementNode.setStatementNode(stm.getExpression(), null, stm.getExpression().toString());
            logger.error("Chua xu ly:parserReturnInfo " + stm.getExpression());
        }
        this.returnStatements.add(statementNode);
    }

    /**
     * Almost convert MethodInvocation to methodNode
     *
     * @param extendedOperands
     * @return
     */
    private List<Object> convertListToMethodInvoStms(List extendedOperands, int line) {
        List<Object> objects = new ArrayList<>();
        for (Object obj : extendedOperands) {
            Object object = convertToMethodInvoStm(obj, line);
            objects.add(object);
        }
        return objects;
    }

    private Object convertToMethodInvoStm(Object obj, int line) {
        if (obj instanceof MethodInvocation) {
            MethodInvocationStmNode methodInvocationStmNode = parserMethodInvoStm((MethodInvocation) obj, line);
            //if it doesnt has "var" to call method (or this method can call in class)
            if (methodInvocationStmNode != null) {
                if (methodInvocationStmNode.getKeyVar() == null) {
                    methodInvocationStmNode.setTypeVar(this.getParent().getName());
                }
            }
            return methodInvocationStmNode;
        } else if (obj instanceof StringLiteral) {
//            String value = JavaLibraryHelper.convertStringToNumbers(obj.toString());
            String value = JavaLibraryHelper.removeFirstAndLastChars(obj.toString());
            return value;
        } else if (obj instanceof SimpleName) {
            return new BaseVariableNode(line, ((SimpleName) obj).getIdentifier(), obj.toString());
        } else {
            logger.error("chua xu ly:convertToMethodInvoStm "+ obj.toString());
            return obj;
        }
    }

    private void parserExpressionStatementInfo(ExpressionStatement stm, int line) {
        if (stm.getExpression() instanceof MethodInvocation) {
            parserMethodInvoStm((MethodInvocation) ((ExpressionStatement) stm).getExpression(), line);
        } else if (stm.getExpression() instanceof Assignment) {
            parserAssignmentStm(stm, line);
        } else {
            logger.error("Chuwa xu ly:parserExpressionStatementInfo " + stm.getExpression());
        }
    }

    private void parserVariableDeclarationInfo(int level, VariableDeclarationStatement variableDeclarationStatement,  int line) {
        List<VariableDeclarationFragment> astNodes = variableDeclarationStatement.fragments();
        for (VariableDeclarationFragment astNode : astNodes) {
            InitNode initNode = new InitNode(level, astNode.getName().getIdentifier(),
                    ASTHelper.getFullyQualifiedName(variableDeclarationStatement.getType(), cu), line);
            initNodes.add(initNode);
        }
    }

    private void parserAssignmentStm(ExpressionStatement stm, int line) {
        Assignment asm = (Assignment) stm.getExpression();

        Object leftSide = asm.getLeftHandSide();
        InitObjectIndex indexLeft = parserObjectInAssignment(leftSide, line, stm);

        Object rightSide = asm.getRightHandSide();
        InitObjectIndex indexRight = parserObjectInAssignment(rightSide, line, stm);
        if (indexLeft != null && indexRight != null) {
            if (indexRight.getScope().equals("method") && indexLeft.getScope().equals("method")) {
                StatementNode leftNode = initNodes.get(indexLeft.getIndex()).getStatementsUsed().get(initNodes.get(indexLeft.getIndex())
                        .getStatementsUsed().size() - 1);
                StatementNode rightNode = initNodes.get(indexRight.getIndex()).getStatementsUsed().get(initNodes.get(indexRight.getIndex())
                        .getStatementsUsed().size() - 1);

                AssignmentStmNode assignmentStmNode = new AssignmentStmNode(leftNode, rightNode, line, leftNode.getKeyVar(), stm.toString());

                initNodes.get(indexLeft.getIndex()).replaceStatementsUsed(assignmentStmNode);
                initNodes.get(indexRight.getIndex()).replaceStatementsUsed(assignmentStmNode);

            } else if (indexRight.getScope().equals("class") && indexLeft.getScope().equals("class")) {
                StatementNode leftNode = ((ClassNode) this.getParent()).getInitNodes().get(indexLeft.getIndex()).getStatementsUsed().get(((ClassNode) this.getParent()).getInitNodes().get(indexLeft.getIndex())
                        .getStatementsUsed().size() - 1);
                StatementNode rightNode = ((ClassNode) this.getParent()).getInitNodes().get(indexRight.getIndex()).getStatementsUsed().get(((ClassNode) this.getParent()).getInitNodes().get(indexRight.getIndex())
                        .getStatementsUsed().size() - 1);

                AssignmentStmNode assignmentStmNode = new AssignmentStmNode(leftNode, rightNode, line, leftNode.getKeyVar(), stm.toString());

                ((ClassNode) this.getParent()).getInitNodes().get(indexLeft.getIndex()).replaceStatementsUsed(assignmentStmNode);
                ((ClassNode) this.getParent()).getInitNodes().get(indexRight.getIndex()).replaceStatementsUsed(assignmentStmNode);
            } else if (indexRight.getScope().equals("method") && indexLeft.getScope().equals("class")) {
                StatementNode rightNode = initNodes.get(indexRight.getIndex()).getStatementsUsed().get(initNodes.get(indexRight.getIndex())
                        .getStatementsUsed().size() - 1);
                StatementNode leftNode = ((ClassNode) this.getParent()).getInitNodes().get(indexLeft.getIndex()).getStatementsUsed().get(((ClassNode) this.getParent()).getInitNodes().get(indexLeft.getIndex())
                        .getStatementsUsed().size() - 1);

                AssignmentStmNode assignmentStmNode = new AssignmentStmNode(leftNode, rightNode, line, leftNode.getKeyVar(), stm.toString());

                initNodes.get(indexRight.getIndex()).replaceStatementsUsed(assignmentStmNode);
                ((ClassNode) this.getParent()).getInitNodes().get(indexLeft.getIndex()).replaceStatementsUsed(assignmentStmNode);
            } else if (indexRight.getScope().equals("class") && indexLeft.getScope().equals("method")) {
                StatementNode rightNode = ((ClassNode) this.getParent()).getInitNodes().get(indexRight.getIndex()).getStatementsUsed().get(((ClassNode) this.getParent()).getInitNodes().get(indexRight.getIndex())
                        .getStatementsUsed().size() - 1);
                StatementNode leftNode = initNodes.get(indexLeft.getIndex()).getStatementsUsed().get(initNodes.get(indexLeft.getIndex())
                        .getStatementsUsed().size() - 1);

                AssignmentStmNode assignmentStmNode = new AssignmentStmNode(leftNode, rightNode, line, leftNode.getKeyVar(), stm.toString());

                ((ClassNode) this.getParent()).getInitNodes().get(indexRight.getIndex()).replaceStatementsUsed(assignmentStmNode);
                initNodes.get(indexLeft.getIndex()).replaceStatementsUsed(assignmentStmNode);

            }
        }

    }

    private InitObjectIndex parserObjectInAssignment(Object obj, int line, ExpressionStatement stm) {
        ClassNode classNode = (ClassNode) this.getParent();
        InitObjectIndex initObjectIndex = null;
        if (obj instanceof FieldAccess) {
            FieldAccess fieldAccess = (FieldAccess) obj;
            int index = classNode.findIndexTypeVar(fieldAccess.getName().getIdentifier());
            if (index >= 0) {
                classNode.initNodes.get(index).addStatement(
                        new BaseVariableNode(line, fieldAccess.getName().getIdentifier(), stm.toString())
                );
            } else {
                logger.error("Not found: in class" + fieldAccess.getName());
            }
        } else if (obj instanceof SimpleName) {
            BaseVariableNode baseVariableNode = new BaseVariableNode(line, ((SimpleName) obj).getIdentifier(), stm.toString());
            initObjectIndex = setStatementToInits(baseVariableNode, ((SimpleName) obj).getIdentifier(), line, stm.getExpression().toString());
        } else if (obj instanceof MethodInvocation) {
            MethodInvocationStmNode methodInvocationStmNode = parserMethodInvoStm((MethodInvocation) obj, line);
            initObjectIndex = setStatementToInits(methodInvocationStmNode, classNode, line, obj.toString());
        } else {
            logger.error("Chưa xử lý: Assignment");
        }
        return initObjectIndex;
    }

    /**
     * Parser a MethodInvocation (in JDT) to MethodInvocationStm (of me)
     * MethodInvocationStm contents: varname, list MethodCalleds eg: customers.get(0).getTransaction().
     * => list MethodCalleds: { getTransaction, get}
     * in MethodCalled has agurementTypes: eg: MethodInvocation, String, ...
     *
     * @param methodInvocation
     * @param line
     * @return
     */
    public MethodInvocationStmNode parserMethodInvoStm(MethodInvocation methodInvocation, int line) {
        ClassNode classNode = (ClassNode) this.getParent();
        MethodInvocationStmNode methodInvocationStmNode = null;
        if (methodInvocation.getExpression() instanceof MethodInvocation) {
            methodInvocationStmNode = new MethodInvocationStmNode(line, methodInvocation.toString());
            parserInMethodInvo(methodInvocationStmNode, methodInvocation, line);
            //if it doesnt has "var" to call method (or this method can call in class)
            if (methodInvocationStmNode.getKeyVar() == null) {
                methodInvocationStmNode.setTypeVar(this.getParent().getName());
            }
            setStatementToInits(methodInvocationStmNode, classNode, line, methodInvocation.toString());

        } else if (methodInvocation.getExpression() instanceof SimpleName) {
            String varname = ((SimpleName) methodInvocation.getExpression()).getIdentifier();

            List<Object> argTypes = parserAruments(methodInvocation.arguments(), line);
            methodInvocationStmNode = new MethodInvocationStmNode(varname, methodInvocation.getName().getIdentifier(), argTypes,
                    line, methodInvocation.toString());
            //if it doesnt has "var" to call method (or this method can call in class)
            if (varname == null) {
                methodInvocationStmNode.setTypeVar(this.getParent().getName());
            }
            setStatementToInits(methodInvocationStmNode, classNode, line, methodInvocation.toString());

        } else if (methodInvocation.getExpression() instanceof FieldAccess) {
            FieldAccess fieldAccess = (FieldAccess) methodInvocation.getExpression();
            List<Object> argTypes = parserAruments(methodInvocation.arguments(), line);
            methodInvocationStmNode = new MethodInvocationStmNode(fieldAccess.getName().getIdentifier(),
                    methodInvocation.getName().getIdentifier(), argTypes, line, methodInvocation.toString());

            setStatementToInits(methodInvocationStmNode, classNode, line, methodInvocation.toString());

        } else if (methodInvocation.getExpression() == null) {
            List<Object> argTypes = parserAruments(methodInvocation.arguments(), line);
            methodInvocationStmNode = new MethodInvocationStmNode(null, methodInvocation.getName().getIdentifier(), argTypes, line,
                    methodInvocation.toString());
            setStatementToInits(methodInvocationStmNode, classNode, line, methodInvocation.toString());
        } else if (methodInvocation.getExpression() instanceof ClassInstanceCreation) {
            //method Invocation
            List<Object> args = parserAruments(methodInvocation.arguments(), line);
            methodInvocationStmNode = new MethodInvocationStmNode(null, methodInvocation.getName().getIdentifier(), args, line, methodInvocation.toString() );
            // method in ClassInstanceCreation
            ClassInstanceCreation classInstance = (ClassInstanceCreation) methodInvocation.getExpression();
            List<Object> argsMethod = parserAruments(classInstance.arguments(), line);
            String type = ASTHelper.getFullyQualifiedName(classInstance.getType(), this.cu);
            String stm = methodInvocation.toString().replace(classInstance.getType().toString(), type);
            methodInvocationStmNode.setStatementString(stm);
            methodInvocationStmNode.addMethodCall(ASTHelper.getFullyQualifiedName(classInstance.getType(), this.cu), argsMethod);
        } else {
            logger.error("Chưa xử lý:parserMethodInvoStm " + methodInvocation.toString() + "-end");
        }
        return methodInvocationStmNode;
    }

    /**
     * set a statements to List init variable. So, from "varName", we can find type in InitNodes
     * and can find all related statements with "varName" to debug
     *
     * @param invocationStm
     * @param classNode
     * @param line
     */
    private InitObjectIndex setStatementToInits(MethodInvocationStmNode invocationStm, ClassNode classNode, int line, String stm) {
        if (invocationStm != null) {
            int index = findIndexTypeVar(invocationStm.getKeyVar(), line);
            String scope = "";
            if (index >= 0) {
                invocationStm.setTypeVar(initNodes.get(index).getType());
//            statementNode = invocationStm;
                initNodes.get(index).addStatement(invocationStm);
                scope = "method";
            } else {
                index = classNode.findIndexTypeVar(invocationStm.getKeyVar());
                if (index >= 0) {
                    invocationStm.setTypeVar(classNode.getInitNodes().get(index).getType());
//                statementNode =new StatementNode(line, invocationStm, invocationStm.getVarName(), stm);
                    classNode.getInitNodes().get(index).addStatement(invocationStm);
                    scope = "class";
                } else {
                    String methodname = invocationStm.getMethodsCalled() == null ? "null" : invocationStm.getMethodsCalled().toString();
//                if (invocationStm.getKeyVar().equals("Assert")) {
//                    if (invocationStm.getMethodsCalled().get(0).getMethodName().equals("assertEquals")) {
//
//                    }
//                }
                    logger.info("Not found in class: {line:" + line + ", classname:" + invocationStm.getKeyVar()
                            + ", methodName:" + methodname + "}");

                }
            }
            return new InitObjectIndex(index, scope);
        } else {
            return  null;
        }
    }

    /**
     * set a statements to List init variable. So, from "varName", we can find type in InitNodes
     * and can find all related statements with "varName" to debug
     *
     * @param stm
     * @param varname
     * @param line
     */
    private InitObjectIndex setStatementToInits(StatementNode stm, String varname, int line, String statement) {
        String scope = "";
        ClassNode classNode = (ClassNode) this.getParent();
//        StatementNode statementNode = null;
        int index = findIndexTypeVar(varname, line);
        if (index >= 0) {
//            statementNode = new StatementNode(line, stm, varname, statement);
            initNodes.get(index).addStatement(stm);
            scope = "method";
        } else {
            index = classNode.findIndexTypeVar(varname);
            if (index >= 0) {
//                statementNode = new StatementNode(line, stm, varname, statement);
                classNode.getInitNodes().get(index).addStatement(stm);
                scope = "class";
            } else {
                logger.info("Not found in class: {line:" + line + ", varname:" + varname
                        + "}");
            }
        }
        return new InitObjectIndex(index, scope);
    }

    /**
     * parser a MethodInvocation, get all import information from MethodInvocation to MethodInvocationStm
     *
     * @param methodInvocationStmNode
     * @param obj
     * @param line
     */
    private void parserInMethodInvo(MethodInvocationStmNode methodInvocationStmNode, MethodInvocation obj, int line) {
        if (obj.getExpression() instanceof MethodInvocation) {
            List<Object> argTypes = parserAruments(obj.arguments(), line);
            methodInvocationStmNode.addMethodCall(((MethodInvocation) obj).getName().getIdentifier(), argTypes);
            parserInMethodInvo(methodInvocationStmNode, (MethodInvocation) obj.getExpression(), line);
        } else if (obj.getExpression() instanceof SimpleName) {
            List<Object> argTypes = parserAruments(obj.arguments(), line);
            methodInvocationStmNode.addMethodCall(((MethodInvocation) obj).getName().getIdentifier(), argTypes);
            //if it doesnt has "var" to call method (or this method can call in class)
            if ((((SimpleName) obj.getExpression()).getIdentifier()) == null) {
                methodInvocationStmNode.setKeyVar(this.getParent().getName());
            } else {
                methodInvocationStmNode.setKeyVar((((SimpleName) obj.getExpression()).getIdentifier()));
            }
        }
    }

    /**
     * parser Argurement, if (argurement instanceof MethodInvocation), need to countinue parser
     * and save in List<Object> is list argurements of methodInvocation.
     *
     * @param arguments
     * @param line
     * @return
     */
    private List<Object> parserAruments(List arguments, int line) {
        List<Object> argTypes = new ArrayList<>();
        if (arguments.size() > 0) {
            for (Object obj : arguments) {
                if (obj instanceof SimpleName) {
                    InitNode initNode = findTypeVar(((SimpleName) obj).getIdentifier(), line);
                    if (initNode != null) {
                        argTypes.add(initNode.getType());
                    } else {
                        logger.error("Chưa xử lý:parserArgToString Param:" + obj.getClass().getName());
                        argTypes.add(obj.getClass().getName());
                    }
                } else if (obj instanceof MethodInvocation) {
                    argTypes.add(obj.getClass().getName());
                    MethodInvocationStmNode methodInvocationStmNode = parserMethodInvoStm((MethodInvocation) obj, line);
                    argTypes.add(methodInvocationStmNode);
                } else {
//                logger.info("Chưa xu ly:parserArgToString Param:" + obj.getClass().getName());
                    argTypes.add(obj.getClass().getName());
                }
            }
        }
        return argTypes;
    }

    /**
     * find index of InitNode in initNodes (initNode has type var)
     *
     * @param varname
     * @param line
     * @return
     */
    public int findIndexTypeVar(String varname, int line) {
        for (int i = 0; i < initNodes.size(); i++) {
            InitNode initNode = initNodes.get(i);
            if (initNode.getVarname().equals(varname)) {
                if (line <= this.getEndLine() &&
                        line >= this.getStartLine()) {
                    return i;
                }
            }
        }
        return -1;
    }

    public InitNode findTypeVar(String varname, int line) {
        for (InitNode init : initNodes) {
            if (init.getVarname().equals(varname)) {
                if (line <= this.getEndLine() &&
                        line >= this.getStartLine()) {
                    return init;
                } else {
                    logger.error("Chưa xử lý findTypeVar1");
                }

            }

        }
        ClassNode classNode = (ClassNode) this.getParent();
        List<InitNode> initNodes = classNode.getInitNodes();
        for (InitNode init : initNodes) {
            if (init.getVarname().equals(varname)) {
                return init;
            }
        }
        return null;
    }

    public void printInfor() {
        System.out.println("Method name: " + name + "   Type: " + returnType + "  Visibility: " + this.getVisibility() + " startline:" + this.getStartLine() + " ENDLINE: " + this.endLine);
    }
}
