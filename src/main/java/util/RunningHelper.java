package util;

import common.config.Configure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class RunningHelper {
    public static final Logger logger = LoggerFactory.getLogger(RunningHelper.class);

    public static void compileFolder(String pathToSource, String pathToOutClass) throws IOException {
        //TODO: need edit here when compile Project
        String cmd = "javac -J-Dfile.encoding=UTF-8 -g -d " + pathToOutClass +
                " -cp " + Configure.APR_JAR_LIB + "\\tools.jar;" + Configure.APR_JAR_LIB + "\\hamcrest-core-1.3.jar;" +
                Configure.APR_JAR_LIB + "\\junit-4.12.jar;" + Configure.APR_JAR_LIB + "\\oasis-junit-1.0.jar  *.java TestRunner.java";
        logger.info(cmd);
        Runtime.getRuntime().exec(cmd, null, new File(pathToSource));
    }

    public static void runFolder(String pathToSource, String pathToOutClass, String className) throws IOException {
        String cmd = "java" +
                " -cp " + Configure.APR_JAR_LIB + "\\tools.jar;" + Configure.APR_JAR_LIB + "\\hamcrest-core-1.3.jar;" +
                Configure.APR_JAR_LIB + "\\junit-4.12.jar;" + Configure.APR_JAR_LIB + "\\oasis-junit-1.0.jar;. " + className;
        logger.info(cmd);
        Runtime.getRuntime().exec(cmd, null, new File(pathToSource));
    }
}
