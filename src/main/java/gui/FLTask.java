package gui;

import faultlocalization.configure.Configuration;
import faultlocalization.gzoltar.ProjectLocalization;
import faultlocalization.gzoltar.StudentFaultLocalization;
import javafx.concurrent.Task;

import java.io.File;

public class FLTask extends Task<Void> {
    private final String projectPath;
    private final String projectName;
    private final String metric;

    public FLTask(String projectPath, String projectName, String metric) {
        this.projectPath = projectPath;
        this.projectName = projectName;
        this.metric = metric;
    }

    private void faultLocalization() {
        String outputPath = Configuration.suspPositionsFilePath;//args[0]; // Configuration.SUSPICIOUS_POSITIONS_FILE_APTH;
        String path = projectPath + File.separator;

        String metricStr = metric; //caculator dupplicate

        if (!this.projectName.contains("_")) {
            //Every thing in a folder
            StudentFaultLocalization st = new StudentFaultLocalization();
            st.locateSuspiciousCode(path, projectName, outputPath, metricStr);
        } else {
            ProjectLocalization projectLocalization = new ProjectLocalization();
            projectLocalization.locateSuspiciousCode(path, projectName, outputPath, metricStr);
        }
    }

    @Override
    protected Void call() throws Exception {
        faultLocalization();
        return null;
    }
}
