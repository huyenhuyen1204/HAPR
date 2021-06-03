package AST.parser;

import AST.node.ClassNode;
import org.eclipse.jdt.core.dom.*;
import util.FileService;

import java.io.File;
import java.io.IOException;
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

    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Users\\Dell\\Desktop\\APR_Test\\data_test\\85713\\Account.java");
        String content = FileService.readFileToString(file.getAbsolutePath());
        ArrayList<ClassNode> classNodes = JavaFileParser.parse(content);
        System.out.println(classNodes.toString());
    }

}
