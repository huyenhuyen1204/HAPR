package gui.controllers;

import faultlocalization.configure.Configuration;
import faultlocalization.gzoltar.GZoltarFaultLoclaization;
import gui.FLTask;
import gui.objects.TreeNode;
import gui.views.ProjectTreeView;
import gui.views.SourceCodeView;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.File;
import java.net.URL;;
import java.util.ResourceBundle;

public class BaseSceneController implements Initializable {

    @FXML
    private TabPane tabPane;

    @FXML
    private TreeView<File> treeView;

    @FXML
    private ProgressBar pbProgressBar;

    private SourceCodeView sourceCodeView;

    private static Logger logger = LoggerFactory.getLogger(GZoltarFaultLoclaization.class);

    public void initialize(URL location, ResourceBundle resources) {
        openFolder(new File("/home/huyenhuyen/Desktop/HAPR/data_test/80776"));
        pbProgressBar.setVisible(false);
        treeView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // double click
                TreeItem<File> item = treeView.getSelectionModel().getSelectedItem();
                if (item != null) {
                    if (item.getValue() != null) {
                        File file = item.getValue();
                        if (file != null && !file.isDirectory()) {
                            sourceCodeView.viewSourceCode(file, tabPane);
                        }
                    }
                }
            }
        });
    }

//    private void refreshOpeningTabs () {
//        for (sourceCodeView.)
//    }

    @FXML
    void faultLocalizationAction(ActionEvent event) {
        pbProgressBar.setVisible(true);
        Task<Void> task = new FLTask(sourceCodeView.getProjectPath(),
                sourceCodeView.getProjectName(), Configuration.faultLocalizationMetric);
        task.setOnSucceeded(e -> {
            pbProgressBar.setVisible(false);
            sourceCodeView.readSuspicious();
            treeView.refresh();
        });
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }



    @FXML
    void openFolderAction(ActionEvent event) {
        System.out.println("open folder");
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(MasterController.getPrimaryStage());
        openFolder(file);
    }

    /**
     * Open a new Folder
     * @param folder
     */
    private void openFolder(File folder) {
        if (folder != null) {
            treeView.setCellFactory(param -> new MyTreeCell());
            TreeNode root = new TreeNode(folder);
            sourceCodeView = new SourceCodeView(folder.getParentFile().getAbsolutePath(), folder.getName());
            treeView.setRoot(root);
            ProjectTreeView.loadChildren(root);
        }
    }


    /**
     * Loading tree node for a Folder
     */
    public class MyTreeCell extends TreeCell<File> {
        @Override
        protected void updateItem(File item, boolean empty) {

            super.updateItem(item, empty);
            setText(null);
            setGraphic(null);

            if (getTreeItem() != null) {
                File file = getItem();
                if (file != null) {
                    setText(file.getName());
                    ImageView imageView = ProjectTreeView.getImageTree(file);
                    setGraphic(imageView);
                    if(sourceCodeView.isSuspicious(file.getAbsolutePath())) {
                        setStyle("-fx-text-fill: #ff0000");
                    } else {
                        setStyle("");
                    }
                }

                setContextMenu(new ContextMenu());

                if (!file.isDirectory() && !file.getName().contains(".jar")) {
                    MenuItem miViewSourceCode = new MenuItem("View Source Code");
                    miViewSourceCode.setOnAction(event -> {
                        sourceCodeView.viewSourceCode(file, tabPane);
                    });
                    MenuItem miDelete = new MenuItem("Delete");
                    getContextMenu().getItems().add(miViewSourceCode);
                    getContextMenu().getItems().add(miDelete);
                }
            }
        }

    }

}
