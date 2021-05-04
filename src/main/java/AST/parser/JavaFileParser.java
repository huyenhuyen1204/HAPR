package AST.parser;

import AST.nodes.ClassNode;
import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;

public class JavaFileParser {

    public static ArrayList<ClassNode> parse(String sourceCode) {
        ASTParser parser = ASTParser.newParser(AST.JLS8);
        parser.setSource(sourceCode.toCharArray());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
        ArrayList<ClassNode> classes = new ArrayList<>();

        cu.accept(new ASTVisitor() {
            public boolean visit(TypeDeclaration node) {
                ClassNode classNode = new ClassNode();
                if (node != null) {
                    classNode.setInforFromASTNode(node, cu);
                    classes.add(classNode);
                    return false;
                }
                return true;
            }
        });
        return classes;
    }

}
