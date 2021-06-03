package gui.objects;

import javafx.scene.control.TreeItem;

import java.io.File;

public class TreeNode extends TreeItem <File> {
    public TreeNode(File value) {
        super(value);
    }
}
