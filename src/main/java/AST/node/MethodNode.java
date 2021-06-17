package AST.node;

import AST.stm.MethodInvocationStm;
import AST.stm.abstrct.InitStatement;
import AST.parser.ASTHelper;
import AST.stm.abstrct.Statement;
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
    List<InitNode> initNodes = new ArrayList<>();
    @JsonIgnore
    private List statements;

    public List getStatements() {
        return statements;
    }

    public void setStatements(List statements) {
        this.statements = statements;
    }

    public MethodNode() {

        parameters = this.getParameters();
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
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


    /**
     * parser and get info of statement
     *
     * @param statements
     */
    public void parserStatements(List statements, CompilationUnit cu) {
        for (Object stm : statements) {

            //Init
            if (stm instanceof VariableDeclarationStatement) {
                int line = cu.getLineNumber(((VariableDeclarationStatement) stm).getStartPosition());
                VariableDeclarationStatement variable = (VariableDeclarationStatement) stm;
                parserVariableDeclarationInfo(variable, this, line);
//                System.out.println(((VariableDeclarationStatement) stm).getType());
            } else if (stm instanceof IfStatement) {
                logger.error("Chưa xử lý: IfStatement");
            } else if (stm instanceof ExpressionStatement) {
                int line = cu.getLineNumber(((ExpressionStatement) stm).getStartPosition());
                parserExpressionStatement((ExpressionStatement) stm, line);
            } else if (stm instanceof ReturnStatement) {
                logger.error("Chưa xử lý: ReturnStatement");
            }
        }
    }

    private void parserExpressionStatement(ExpressionStatement stm, int line) {
        if (stm.getExpression() instanceof MethodInvocation) {
            List<Statement> statementList = new ArrayList<>();
            parserMethodInvoStm(statementList, (MethodInvocation) ((ExpressionStatement) stm).getExpression(), line);

        } else if (stm.getExpression() instanceof Assignment) {
            Assignment asm = (Assignment) stm.getExpression();
            Object leftSide = asm.getLeftHandSide();
            if (leftSide instanceof FieldAccess) {
                FieldAccess fieldAccess = (FieldAccess) leftSide;
                ClassNode classNode = (ClassNode) this.getParent();
                int index = classNode.findIndexTypeVar(fieldAccess.getName().getIdentifier());
                if (index >= 0) {
                    ((InitInClassNode) classNode.initNodes.get(index)).addStatement(
                            new StatementNode(line, stm)
                    );
                } else {
                    logger.error("Not found: in class" + fieldAccess.getName());
                }
            } else {
                logger.error("Chưa xử lý: Assignment");
            }
//                    System.out.println("ASSIGN ment");
//                    System.out.println(((Assignment) ((ExpressionStatement) stm).getExpression()).getLeftHandSide());
        }
    }

    public void parserVariableDeclarationInfo(VariableDeclarationStatement variableDeclarationStatement, MethodNode methodNode, int line) {
        List<VariableDeclarationFragment> astNodes = variableDeclarationStatement.fragments();
        for (VariableDeclarationFragment astNode : astNodes) {
            InitInMethodNode initNode = new InitInMethodNode(line, astNode.getName().getIdentifier(), variableDeclarationStatement.getType(), methodNode);
            initNodes.add(initNode);
        }
    }

    private void parserMethodInvoStm(List<Statement> statements, MethodInvocation methodInvocation, int line) {
        ClassNode classNode = (ClassNode) this.getParent();

        if (methodInvocation.getExpression() instanceof MethodInvocation) {
            //TODO: Test this case with AssertEquals
            MethodInvocation methodInvo = (MethodInvocation) methodInvocation.getExpression();
            MethodInvocationStm methodInvocationStm = new MethodInvocationStm(methodInvo.getName().getIdentifier(),
                    this.getName());
            String classname = methodInvocationStm.getVarClass();
            String methodName = methodInvocation.getName().getIdentifier();
            //set type
            int index = findIndexTypeVar(methodInvocationStm.getVarClass(), this.getName(), line);

            if (index >= 0) {
                methodInvocationStm.setTypeVarClass(initNodes.get(index).getType());
                initNodes.get(index).addStatement(new StatementNode(line, methodInvocationStm));
            } else {
                index = classNode.findIndexTypeVar(methodInvocationStm.getVarClass());
                if (index >= 0) {
                    methodInvocationStm.setTypeVarClass(classNode.getInitNodes().get(index).getType());
                } else {
                    logger.error("Not found: " + line + " " + methodInvo.toString());
                }
                classNode.getInitNodes().get(index).addStatement(new StatementNode(line, methodInvocationStm));
            }
            //--end set type
//            statements.add(methodInvocationStm);
            if (methodInvo.arguments().size() > 0) {
                for (Object arg : methodInvo.arguments()) {
                    if (arg instanceof MethodInvocation) {
                        parserMethodInvoStm(statements, (MethodInvocation) arg, line);
                    }
                }
            }
//            return getVarClassAndMethod((MethodInvocation) methodInvocation.getExpression());
        } else if (methodInvocation.getExpression() instanceof SimpleName) {
            String classname = ((SimpleName) methodInvocation.getExpression()).getIdentifier();
            String methodName = methodInvocation.getName().getIdentifier();
            //set type
            int index = findIndexTypeVar(classname, this.getName(), line);
            MethodInvocationStm invocationStm = new MethodInvocationStm(classname, methodName);
            if (index >= 0) {
                invocationStm.setTypeVarClass(initNodes.get(index).getType());
                initNodes.get(index).addStatement(new StatementNode(line, invocationStm));
            } else {
                index = classNode.findIndexTypeVar(classname);
                if (index >= 0) {
                    invocationStm.setTypeVarClass(classNode.getInitNodes().get(index).getType());
                    classNode.getInitNodes().get(index).addStatement(new StatementNode(line, invocationStm));
                } else {
                    logger.error("Not found in class: " + line + "-" + methodInvocation.toString() );
                }

            }
            //end set type
//                statements.add(invocationStm);
            if (methodInvocation.arguments().size() > 0) {
                for (Object arg : methodInvocation.arguments()) {
                    if (arg instanceof MethodInvocation) {
                        parserMethodInvoStm(statements, (MethodInvocation) arg, line);
                    }
                }
            }
        } else if (methodInvocation.getExpression() instanceof FieldAccess) {
            FieldAccess fieldAccess = (FieldAccess) methodInvocation.getExpression();
            int index = classNode.findIndexTypeVar(fieldAccess.getName().getIdentifier());
            if (index >= 0) {
                classNode.getInitNodes().get(index).addStatement(new StatementNode(line, methodInvocation));
            } else {
                logger.error("Not found Init => ERROR");
            }
        } else {

            logger.error("Chưa xử lý: " + methodInvocation.getExpression().toString());

        }

    }

    public int findIndexTypeVar(String varname, String methodName, int line) {
        for (int i = 0; i < initNodes.size(); i++) {
            InitNode initNode = initNodes.get(i);
            if (initNode instanceof InitInMethodNode) {
                InitInMethodNode inMethodNode = (InitInMethodNode) initNode;
                if (inMethodNode.getMethodNode().getName().equals(methodName)) {
                    if (inMethodNode.getVarname().equals(varname)) {
                        if (line <= inMethodNode.getMethodNode().getEndLine() &&
                                line >= inMethodNode.getMethodNode().getStartLine()) {
                            return i;
                        }
                    }
                }
            } else {
                logger.error("Chua xu ly:findIndexTypeVar");
            }
        }
        return -1;
    }

    public InitNode findTypeVar(String varname, int line) {
        for (InitNode init : initNodes) {
            if (init instanceof InitInMethodNode) {
                InitInMethodNode initNode = (InitInMethodNode) init;
                if (initNode.getVarname().equals(varname)) {
                    if (this.getName().equals(initNode.getMethodNode().getName())) {
                        if (line <= initNode.getMethodNode().getEndLine() &&
                                line >= initNode.getMethodNode().getStartLine()) {
                            return initNode;
                        } else {
                            logger.error("Chưa xử lý findTypeVar1");
                        }
                    } else {
                        logger.error("Chưa xử lý findTypeVar2");
                    }
                } else {
                    logger.error("Chưa xử lý findTypeVar3");
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
