package faultlocalization.configure;

public class Configuration {
	
	/*
	 * Data path of Defects4J bugs.
	 * and finish with '\' char
	 * example: C:\Users\Dell\Desktop\APRresearch\Test\
	 */
	public static final String BUGGY_PROJECTS_PATH = "C:\\Users\\Dell\\Desktop\\APR_test\\Test\\";
	
	public static final String TEMP_FILES_PATH = ".temp/";
	public static final long SHELL_RUN_TIMEOUT = 10800L;
	
	public static String knownBugPositions = "BugPositions.txt";
	public static String suspPositionsFilePath = "SuspiciousCodePositions/";
	public static String failedTestCasesFilePath = "FailedTestCases/";
	public static String faultLocalizationMetric = "Ochiai";
	public static String outputPath = "OUTPUT/";

	//FOR OASIS
	public static final String OASIS_PROJECT_PATH ="C:\\Users\\Dell\\Desktop\\APR_test\\data_test\\";

}