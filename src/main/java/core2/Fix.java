package core2;

import AST.node.ClassNode;
import AST.node.FolderNode;
import AST.node.MethodNode;
import AST.stm.abst.StatementNode;
import faultlocalization.objects.SuspiciousPosition;

import java.util.List;

public class Fix {
    public static void fix(List<SuspiciousPosition> ss, FolderNode folderNode) {
        for(SuspiciousPosition s : ss) {
            ClassNode classNode = folderNode.findClassByName(s.classPath);
            StatementNode statementNode = classNode.findStatemmentByLine(s.lineNumber);
            System.out.println("BBBB");
        }
    }
}
