package AST.node;

import AST.parser.ASTHelper;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cuong on 3/22/2017.
 */


public class MethodNode extends AbstractableElementNode {
    // protected boolean isAbstract;
    protected String returnType;
    protected List<ParameterNode> parameters;
    private String simpleName;
    private boolean isConstructor = false;
    private int startLine;
    private int endLine;
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
     * @param statements
     */
    public void parserStatements (List statements) {
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

    public void printInfor() {
        System.out.println("Method name: " + name + "   Type: " + returnType + "  Visibility: " + this.getVisibility() + " startline:" + this.getStartLine() + " ENDLINE: " + this.endLine);
    }
}
