package AST.parser;

import AST.node.ClassNode;
import AST.node.FieldNode;
import AST.node.MethodNode;
import AST.node.Node;
import AST.stm.abstrct.InitStatement;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.ArrayList;
import java.util.List;

public class Convert {
    public static List<Node> convertASTListNodeToFieldNode(FieldDeclaration[] fields, List<InitStatement> variableElements) {
        List<Node> result = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            List<FieldNode> fieldNodes = FieldNode.setInforFromASTNode(fields[i], variableElements);
            result.addAll(fieldNodes);
        }
        return result;
    }

    public static List<Node> convertASTListNodeToMethodNode(MethodDeclaration[] methods, List<InitStatement> variableElements , CompilationUnit cu) {
        List<Node> result = new ArrayList<>();
        for (int i = 0; i < methods.length; i++) {
            MethodNode methodNode = new MethodNode();
            methodNode.setInforFromASTNode(methods[i], variableElements, cu);
            result.add(methodNode);
        }
        return result;
    }


    public static List<Node> convertASTListNodeToClassNode(TypeDeclaration[] innerClasses, CompilationUnit cu) {
        List<Node> result = new ArrayList<>();
        for (int i = 0; i < innerClasses.length; i++) {
            ClassNode classNode = new ClassNode();
            classNode.setInforFromASTNode(innerClasses[i], cu);
            result.add(classNode);
        }
        return result;
    }

}
