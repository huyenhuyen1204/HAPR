package AST.node;

import AST.obj.BaseVariable;
import AST.stm.MethodInvocationStm;
import AST.parser.ASTHelper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.jdt.core.dom.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    List<InitNode> initNodes;
    List<StatementNode> returnStatements;
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
        this.setStartLine(cu.getLineNumber(node.getStartPosition()));
        int endLineNumber = cu.getLineNumber(node.getStartPosition() + nodeLength) - 1;
        this.setEndLine(endLineNumber);
        if (node.isConstructor() == false) {
            Type s = node.getReturnType2();
            if (s != null) {
                this.setReturnType(ASTHelper.getFullyQualifiedName(s, cu));
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

        //TODO Tim parametersof methods
        List parameters = node.parameters();
        List<Node> paraNodes = new ArrayList<>();
        for (Object o : parameters) {
            ParameterNode paraNode = new ParameterNode();
//            paraNode.setId(Node.countId);
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
     * parser and get info of statement
     *
     * @param statements
     */
    public void parserStatements(int level, List statements, CompilationUnit cu) {
        if (statements != null) {
            for (Object stm : statements) {
                //Init
                int line = cu.getLineNumber(((org.eclipse.jdt.core.dom.Statement) stm).getStartPosition());
                if (stm instanceof VariableDeclarationStatement) {
                    VariableDeclarationStatement variable = (VariableDeclarationStatement) stm;
                    parserVariableDeclarationInfo(level, variable, cu, line);

                } else if (stm instanceof IfStatement) {
                    logger.error("Chưa xử lý: IfStatement");
                    parserIfStatementInfo((IfStatement) stm, line, level);

                } else if (stm instanceof ExpressionStatement) {
                    parserExpressionStatementInfo((ExpressionStatement) stm, line);

                } else if (stm instanceof ReturnStatement) {
                    parserReturnInfo((ReturnStatement) stm, line);

                } else if (stm instanceof TryStatement) {
                    logger.error("Chưa xử lý Trystatement");

                } else if (stm instanceof EnhancedForStatement) {
                    parserEnhancedForInfo((EnhancedForStatement) stm, line, level, cu);
//                    logger.error("Chưa xử lý ForStatement");

                } else if (stm instanceof SwitchStatement) {
                    parserSwitchStatementInfo((SwitchStatement) stm, cu);
//                    logger.error("Chưa xử lý SwitchStatement");

                } else if (stm instanceof SuperConstructorInvocation) {
                    logger.info("Không xử lý: SuperConstructorInvocation");

                } else {
                    logger.error("Chưa xử lý:parserStatements " + statements.toString());
                }
            }
        }
    }

    private void parserSwitchStatementInfo(SwitchStatement switchStatement, CompilationUnit cu) {
        List statements = switchStatement.statements();
        for (Object stm : statements) {
            Statement stmConvert = (Statement) stm;
            int line = cu.getLineNumber(stmConvert.getStartPosition());
            if (stm instanceof ReturnStatement) {
                parserReturnInfo((ReturnStatement) stm, line);
            }
        }
    }

    private void parserEnhancedForInfo(EnhancedForStatement stm, int line, int level, CompilationUnit cu) {
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
                parserStatements(level + 1, statements, cu);
            } else {
                logger.error("Chưa xử lý:parserEnhancedForInfo ");
            }
        }
    }

    private void parserIfStatementInfo(IfStatement ifStatement, int line, int level) {
        //TODO: DO IT
    }

    private void parserReturnInfo(ReturnStatement stm, int line) {
        StatementNode statementNode = new StatementNode(line);
        if (stm.getExpression() instanceof MethodInvocation) {
            MethodInvocationStm methodInvocationStm = parserMethodInvoStm((MethodInvocation) stm.getExpression(), line);
            statementNode.setStatementNode(methodInvocationStm);

        } else if (stm.getExpression() instanceof Assignment) {
            logger.error("Chua xu ly: parserReturnInfo " + stm.getExpression());

        } else if (stm.getExpression() instanceof SimpleName) {
            String value = ((SimpleName) stm.getExpression()).getIdentifier();
            BaseVariable baseVariable = new BaseVariable(line,  value);
            statementNode.setStatementNode(baseVariable);
            setStatementToInits(stm, ((SimpleName) stm.getExpression()).getIdentifier(), line);

        } else if (stm.getExpression() instanceof StringLiteral) {
            String value = stm.getExpression().toString().substring(1, stm.getExpression().toString().length() - 1);
//            BaseVariable baseVariable = new BaseVariable(line, value);
            statementNode.setStatementNode(value);

        } else {
            statementNode.setStatementNode(stm.getExpression());
            logger.error("Chua xu ly:parserReturnInfo " + stm.getExpression());
        }
        this.returnStatements.add(statementNode);
    }

    private void parserExpressionStatementInfo(ExpressionStatement stm, int line) {
        if (stm.getExpression() instanceof MethodInvocation) {
            parserMethodInvoStm((MethodInvocation) ((ExpressionStatement) stm).getExpression(), line);

        } else if (stm.getExpression() instanceof Assignment) {
            parserAssignmentStm(stm, line);
        }
    }

    public void parserVariableDeclarationInfo(int level, VariableDeclarationStatement variableDeclarationStatement, CompilationUnit cu, int line) {
        List<VariableDeclarationFragment> astNodes = variableDeclarationStatement.fragments();
        for (VariableDeclarationFragment astNode : astNodes) {
            InitNode initNode = new InitNode(level, astNode.getName().getIdentifier(), ASTHelper.getFullyQualifiedName(variableDeclarationStatement.getType(), cu), line);
            initNodes.add(initNode);
        }
    }

    private void parserAssignmentStm(ExpressionStatement stm, int line) {
        Assignment asm = (Assignment) stm.getExpression();
        Object leftSide = asm.getLeftHandSide();
        if (leftSide instanceof FieldAccess) {
            FieldAccess fieldAccess = (FieldAccess) leftSide;
            ClassNode classNode = (ClassNode) this.getParent();
            int index = classNode.findIndexTypeVar(fieldAccess.getName().getIdentifier());
            if (index >= 0) {
                classNode.initNodes.get(index).addStatement(
                        new StatementNode(line, stm)
                );
            } else {
                logger.error("Not found: in class" + fieldAccess.getName());
            }
        } else {
            logger.error("Chưa xử lý: Assignment");
        }
    }


    //    private void parserMethodInvoStm(MethodInvocation methodInvocation, int line) {
//        ClassNode classNode = (ClassNode) this.getParent();
//        if (methodInvocation.getExpression() instanceof MethodInvocation) {
//            MethodInvocationStm methodInvocationStm = new MethodInvocationStm();
//            parserInMethodInvo(methodInvocationStm, methodInvocation, line);
//            setStatementToInits(methodInvocationStm, classNode, line);
//            parserArgurement(methodInvocation, line);
//
//        } else if (methodInvocation.getExpression() instanceof SimpleName) {
//            String varname = ((SimpleName) methodInvocation.getExpression()).getIdentifier();
//
//            List<String> argTypes = parserArgToString(methodInvocation.arguments(), line);
//            MethodInvocationStm methodInvocationStm = new MethodInvocationStm(varname, methodInvocation.getName().getIdentifier(), argTypes);
//
//            setStatementToInits(methodInvocationStm, classNode, line);
//            parserArgurement(methodInvocation, line);
//        } else if (methodInvocation.getExpression() instanceof FieldAccess) {
//            FieldAccess fieldAccess = (FieldAccess) methodInvocation.getExpression();
//            List<String> argTypes = parserArgToString(methodInvocation.arguments(), line);
//            MethodInvocationStm methodInvocationStm = new MethodInvocationStm(fieldAccess.getName().getIdentifier(), methodInvocation.getName().getIdentifier(), argTypes);
//
//            setStatementToInits(methodInvocationStm, classNode, line);
//            parserArgurement(methodInvocation, line);
//
//        } else {
//
//            logger.error("Chưa xử lý:parserMethodInvoStm " + methodInvocation.toString() + "-end");
//
//        }
//
//    }
    //ver2
    public MethodInvocationStm parserMethodInvoStm(MethodInvocation methodInvocation, int line) {
        ClassNode classNode = (ClassNode) this.getParent();
        MethodInvocationStm methodInvocationStm = null;
        if (methodInvocation.getExpression() instanceof MethodInvocation) {
            methodInvocationStm = new MethodInvocationStm();
            parserInMethodInvo(methodInvocationStm, methodInvocation, line);
            setStatementToInits(methodInvocationStm, classNode, line);
            parserArgurement(methodInvocation, line);

        } else if (methodInvocation.getExpression() instanceof SimpleName) {
            String varname = ((SimpleName) methodInvocation.getExpression()).getIdentifier();

            List<String> argTypes = parserArgToString(methodInvocation.arguments(), line);
            methodInvocationStm = new MethodInvocationStm(varname, methodInvocation.getName().getIdentifier(), argTypes, line);

            setStatementToInits(methodInvocationStm, classNode, line);
            parserArgurement(methodInvocation, line);

        } else if (methodInvocation.getExpression() instanceof FieldAccess) {
            FieldAccess fieldAccess = (FieldAccess) methodInvocation.getExpression();
            List<String> argTypes = parserArgToString(methodInvocation.arguments(), line);
            methodInvocationStm = new MethodInvocationStm(fieldAccess.getName().getIdentifier(),
                    methodInvocation.getName().getIdentifier(), argTypes, line);

            setStatementToInits(methodInvocationStm, classNode, line);
            parserArgurement(methodInvocation, line);

        } else {

            logger.error("Chưa xử lý:parserMethodInvoStm " + methodInvocation.toString() + "-end");

        }
        return methodInvocationStm;

    }

    private void parserArgurement(MethodInvocation methodInvocation, int line) {
        if (methodInvocation.arguments().size() > 0) {
            for (Object arg : methodInvocation.arguments()) {
                if (arg instanceof MethodInvocation) {
                    parserMethodInvoStm((MethodInvocation) arg, line);
                }
            }
        }
    }

    private void setStatementToInits(MethodInvocationStm invocationStm, ClassNode classNode, int line) {
        int index = findIndexTypeVar(invocationStm.getVarName(), line);
        if (index >= 0) {
            invocationStm.setTypeVar(initNodes.get(index).getType());
            initNodes.get(index).addStatement(new StatementNode(line, invocationStm));
        } else {
            index = classNode.findIndexTypeVar(invocationStm.getVarName());
            if (index >= 0) {
                invocationStm.setTypeVar(classNode.getInitNodes().get(index).getType());
                classNode.getInitNodes().get(index).addStatement(new StatementNode(line, invocationStm));
            } else {
                String methodname = invocationStm.getMethodsCalled() == null ? "null" : invocationStm.getMethodsCalled().toString();
                logger.info("Not found in class: {line:" + line + ", classname:" + invocationStm.getVarName()
                        + ", methodName:" + methodname + "}");
            }
        }
    }

    private void setStatementToInits(Object stm, String varname, int line) {
        ClassNode classNode = (ClassNode) this.getParent();
        int index = findIndexTypeVar(varname, line);
        if (index >= 0) {
            initNodes.get(index).addStatement(new StatementNode(line, stm));
        } else {
            index = classNode.findIndexTypeVar(varname);
            if (index >= 0) {
                classNode.getInitNodes().get(index).addStatement(new StatementNode(line, stm));
            } else {
                logger.info("Not found in class: {line:" + line + ", varname:" + varname
                        + "}");
            }
        }
    }


    private void parserInMethodInvo(MethodInvocationStm methodInvocationStm, MethodInvocation obj, int line) {
        if (obj.getExpression() instanceof MethodInvocation) {
            List<String> argTypes = parserArgToString(obj.arguments(), line);
            methodInvocationStm.addMethodCall(((MethodInvocation) obj).getName().getIdentifier(), argTypes);
            parserInMethodInvo(methodInvocationStm, (MethodInvocation) obj.getExpression(), line);
            parserArgurement(obj, line);
        } else if (obj.getExpression() instanceof SimpleName) {
            List<String> argTypes = parserArgToString(obj.arguments(), line);
            methodInvocationStm.addMethodCall(((MethodInvocation) obj).getName().getIdentifier(), argTypes);
            methodInvocationStm.setVarName((((SimpleName) obj.getExpression()).getIdentifier()));
            parserArgurement(obj, line);
        }
    }

    private List<String> parserArgToString(List arguments, int line) {
        List<String> argTypes = new ArrayList<>();
        for (Object obj : arguments) {
            if (obj instanceof SimpleName) {
                InitNode initNode = findTypeVar(((SimpleName) obj).getIdentifier(), line);
                if (initNode != null) {
                    argTypes.add(initNode.getType().toString());
                } else {
                    logger.error("Chưa xử lý:parserArgToString Param:" + obj.getClass().getName());
                    argTypes.add(obj.getClass().getName());
                }
            } else {
//                logger.info("Chưa xu ly:parserArgToString Param:" + obj.getClass().getName());
                argTypes.add(obj.getClass().getName());
            }
        }
        return argTypes;
    }


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
