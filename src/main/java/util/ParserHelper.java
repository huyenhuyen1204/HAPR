package util;

import AST.node.ClassNode;
import AST.node.FolderNode;
import AST.node.MethodNode;

import java.util.List;

public class ParserHelper {
    public static MethodNode findMethodInClass (List<ClassNode> classNodes, String classname, String methodname, List params ) {
        for (ClassNode classNode : classNodes) {
            if(classNode.getName().equals(classname)) {
                return classNode.findMethodNode(methodname, params);
            }
        }
        return null;
    }
    public static ClassNode findByClassName (FolderNode folderNode, String classname) {
        List<ClassNode> classNodes = folderNode.getClassNodes();
        for (ClassNode classNode : classNodes) {
            if(classNode.getName().equals(classname)) {
                return classNode;
            }
        }
        return null;
    }
}
