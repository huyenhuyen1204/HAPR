package gui.views;

import gui.objects.TreeNode;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.util.Objects;

public class ProjectTreeView {


        /**
         * load icon of images
         * @param file
         * @return
         */
        public static ImageView getImageTree(File file) {
            ImageView imageView = new ImageView();
            Image image = null;
            if (file != null) {
                if (file.isDirectory()) {
                    image = new Image(ProjectTreeView.class.getResource("/imgs/folder.png").toExternalForm());
                    imageView.setImage(image);
                }  else if (file.getName().endsWith(".jar")) {
                    image = new Image(ProjectTreeView.class.getResource("/imgs/ppJar.png").toExternalForm());
                    imageView.setImage(image);
                } else if (file.getName().endsWith(".class")) {
                    image = new Image(ProjectTreeView.class.getResource("/imgs/modelClass.png").toExternalForm());
                    imageView.setImage(image);
                } else if (file.getName().endsWith("Test.java")) {
                    image = new Image(ProjectTreeView.class.getResource("/imgs/testClass.png").toExternalForm());
                    imageView.setImage(image);
                } else if (file.getName().endsWith(".java")) {
                    image = new Image(ProjectTreeView.class.getResource("/imgs/class.png").toExternalForm());
                    imageView.setImage(image);
                } else if (file.getName().endsWith(".zip")) {
                    image = new Image(ProjectTreeView.class.getResource("/imgs/ZipFolderClosed.png").toExternalForm());
                    imageView.setImage(image);
                } else {
                    image = new Image(ProjectTreeView.class.getResource("/imgs/any_type.png").toExternalForm());
                    imageView.setImage(image);
                }
            }
            imageView.setFitHeight(18);
            imageView.setFitWidth(18);
            return imageView;
        }

        public static void loadChildren(TreeNode treeNode) {
            if (treeNode.getValue() != null) {
                File file = treeNode.getValue();
                if (file.isDirectory()) {
                    for (File child : Objects.requireNonNull(file.listFiles())) {
                        TreeNode childNode = new TreeNode(child);
                        treeNode.getChildren().add(childNode);
                        loadChildren(childNode);
                    }
                }
            }
        }

}
