package core2;

import AST.node.FolderNode;
import AST.parser.ProjectParser;
import faultlocalization.gzoltar.FL;
import faultlocalization.gzoltar.SuspiciousCode;
import faultlocalization.objects.SuspiciousPosition;
import util.FileHelper;
import util.PathUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        String projectPath = "/home/huyenhuyen/Desktop/benmarks/";
        String projectName = "Chart_11";
//        FL.faultLocalization(projectPath, projectName, "Ochiai");
        List<String> paths = PathUtils.getSrcPath(projectName);
        List<SuspiciousPosition> ss = FileHelper.readSuspiciousCodeFromFile("/home/huyenhuyen/Desktop/HAPR/output/SuspiciousCodePositions/Chart_11/Ochiai.txt");
        FolderNode folderNode = ProjectParser.parse(projectPath + projectName + File.separator + paths.get(2));
        Fix.fix(ss, folderNode);
    }
}
