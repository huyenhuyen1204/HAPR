package util;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import faultlocalization.objects.SuspiciousPosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHelper {
    private static Logger logger = LoggerFactory.getLogger(FileHelper.class);

    /**
     * @param filePath
     */
    public static void createDirectory(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            deleteDirectory(filePath);
        }
        file.mkdirs();
    }

    public static void createFile(File file, String content) {
        FileWriter writer = null;
        BufferedWriter bw = null;

        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) file.createNewFile();
            writer = new FileWriter(file);
            bw = new BufferedWriter(writer);
            bw.write(content);
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(bw);
            close(writer);
        }
    }

    public static void deleteDirectory(String dir) {
        File file = new File(dir);

        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files.length > 0) {
                    for (File f : files) {
                        if (f.isFile()) {
                            deleteFile(f.getAbsolutePath());
                        } else {
                            deleteDirectory(f.getAbsolutePath());
                        }
                    }
                }
                file.delete();
            } else {
                deleteFile(dir);
            }
        }
    }

    public static void deleteFiles(String dir) {
        File file = new File(dir);

        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files.length > 0) {
                    for (File f : files) {
                        if (f.isFile()) {
                            deleteFile(f.getAbsolutePath());
                        } else {
                            deleteFiles(f.getAbsolutePath());
                        }
                    }
                }
            } else {
                deleteFile(dir);
            }
        }
    }

    public static void deleteFile(String fileName) {
        File file = new File(fileName);

        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else {
                deleteDirectory(fileName);
            }
        }
    }

    public static List<File> getAllDirectories(String filePath) {
        return listAllDirectories(new File(filePath));
    }

    /**
     * List all files in the directory.
     *
     * @param filePath
     * @param type
     * @return
     */
    public static List<File> getAllFiles(String filePath, String type) {
        return listAllFiles(new File(filePath), type);
    }

    public static List<File> getAllFilesInCurrentDiectory(String filePath, String type) {
        return getAllFilesInCurrentDiectory(new File(filePath), type);
    }

    public static List<File> getAllFilesInCurrentDiectory(File directory, String type) {
        List<File> fileList = new ArrayList<>();

        if (!directory.exists()) {
            return null;
        }

        File[] files = directory.listFiles();

        for (File file : files) {
            if (file.isFile()) {
                if (file.toString().endsWith(type)) {
                    fileList.add(file);
                }
            }
        }

        return fileList;
    }

    public static String getFileName(String filePath) {
        File file = new File(filePath);

        if (file.exists()) {
            return file.getName();
        } else {
            return null;
        }
    }

    public static String getFileNameWithoutExtension(File file) {
        if (file.exists()) {
            String fileName = file.getName();
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
            return fileName;
        } else {
            return null;
        }
    }

    public static String getFileExtension(File file) {
        String fileName = file.getName();
        String extension = fileName.substring(fileName.lastIndexOf("."));
        return extension;
    }

    public static String getFileParentPath(String filePath) {
        File file = new File(filePath);

        if (file.exists()) {
            return file.getParent() + File.separator;
        }
        return "";
    }

    /**
     * Check whether a file path is valid or not.
     *
     * @param path, file path.
     * @return true, the file path is valid.
     * false, the file path is invalid.
     */
    public static boolean isValidPath(String path) {
        File file = new File(path);

        if (file.exists()) {
            return true;
        }

        return false;
    }

    /**
     * Recursively list all files in file.
     *
     * @param file
     * @return
     */
    private static List<File> listAllFiles(File file, String type) {
        List<File> fileList = new ArrayList<>();

        if (!file.exists()) {
            return null;
        }

        File[] files = file.listFiles();

        for (File f : files) {
            if (f.isFile()) {
                if (f.toString().endsWith(type)) {
                    fileList.add(f);
                }
            } else {
                List<File> fl = listAllFiles(f, type);
                if (fl != null && fl.size() > 0) {
                    fileList.addAll(fl);
                }
            }
        }

        return fileList;
    }

    /**
     * Recursively list all directories in file.
     *
     * @param file
     * @return
     */
    private static List<File> listAllDirectories(File file) {
        List<File> fileList = new ArrayList<>();

        File[] files = file.listFiles();

        for (File f : files) {
            if (f.isDirectory()) {
                fileList.add(f);
                fileList.addAll(listAllDirectories(f));
            }
        }

        return fileList;
    }

    public static void makeDirectory(String fileName) {
        deleteFile(fileName);
        File file = new File(fileName).getParentFile();
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * Read the content of a file.
     *
     * @param fileName
     * @return String, the content of a file.
     */
    public static String readFile(String fileName) {
        return readFile(new File(fileName));
    }

    /**
     * Read the content of a file.
     *
     * @param file
     * @return String, the content of a file.
     */
    public static String readFile(File file) {
        byte[] input = null;
        BufferedInputStream bis = null;

        try {

            bis = new BufferedInputStream(new FileInputStream(file));
            input = new byte[bis.available()];
            bis.read(input);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(bis);
        }

        String sourceCode = null;
        if (input != null) {
            sourceCode = new String(input);
        }

        return sourceCode;
    }

    /**
     * Output output into a file.
     *
     * @param fileName, output file name.
     * @param data,     output data.
     * @param append,   the output data will be appended previous data in the file or not.
     */
    public static void outputToFile(String fileName, StringBuilder data, boolean append) {
        outputToFile(fileName, data.toString(), append);
    }

    public static void outputToFile(File file, StringBuilder data, boolean append) {
        outputToFile(file, data.toString(), append);
    }

    public static void outputToFile(String fileName, String data, boolean append) {
        File file = new File(fileName);
        outputToFile(file, data, append);
    }

    public static void outputToFile(File file, String data, boolean append) {
        FileWriter writer = null;
        BufferedWriter bw = null;

        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            writer = new FileWriter(file, append);
            bw = new BufferedWriter(writer);
            bw.write(data);
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(bw);
            close(writer);
        }
    }

    private static void close(FileWriter writer) {
        try {
            if (writer != null) {
                writer.close();
                writer = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void close(BufferedWriter bw) {
        try {
            if (bw != null) {
                bw.close();
                bw = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void close(BufferedInputStream bis) {
        try {
            if (bis != null) {
                bis.close();
                bis = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<File> getAllSubDirectories(String fileName) {
        File file = new File(fileName);
        List<File> subDirectories = new ArrayList<>();
        if (file.exists()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.isDirectory()) {
                    subDirectories.add(f);
                }
            }
        }
        return subDirectories;
    }

    public static List<SuspiciousPosition> readSuspiciousCodeFromFile(String suspiciousFile, List<String> susClassnameList) {
        List<SuspiciousPosition> suspiciousCodeList = new
                ArrayList<>();
        File suspCodePosFile = new File(suspiciousFile);
        if (suspCodePosFile.exists()) {
            try {
                FileReader fileReader = new FileReader(suspiciousFile);
                BufferedReader reader = new BufferedReader(fileReader);
                String line = null;
                while ((line = reader.readLine()) != null) {
                    String[] elements = line.split("@");
                    SuspiciousPosition sp = new SuspiciousPosition();
                    sp.classPath = elements[0];
                    sp.lineNumber = Integer.parseInt(elements[1]);
                    sp.ratio = Float.parseFloat(elements[2]);
                    if (susClassnameList != null) {
//                        String classPath = sp.classPath.replace(".", "/") + ".java";
                        if (!susClassnameList.contains(sp.classPath)) {
                            susClassnameList.add(sp.classPath);
                        }
                    }
                    suspiciousCodeList.add(sp);
                }
                reader.close();
                fileReader.close();
            } catch (Exception e) {
                e.printStackTrace();
                logger.debug("Reloading Localization Result...");
                return null;
            }
        } else {
            logger.warn("File not found: " + suspCodePosFile);
        }


        if (suspiciousCodeList.isEmpty()) return null;
        return suspiciousCodeList;
    }

    public static void addToFile(File f1, String newLine) {
        FileWriter fw = null;
        BufferedWriter out = null;
        try {
            if (!f1.exists()) {
                f1.createNewFile();
            }
            FileReader fr = new FileReader(f1);
            BufferedReader br = new BufferedReader(fr);
            String line = "";
            List<String> lines = new ArrayList<String>();
            while ((line = br.readLine()) != null) {
                lines.add(line + "\n");
            }
            lines.add(newLine + "\n");
            fr.close();
            br.close();

            fw = new FileWriter(f1);
            out = new BufferedWriter(fw);
            for (String s : lines)
                out.write(s);
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            close(out);
            close(fw);
        }
    }


    public static boolean isStringInFile(File file, String text) {
        String content = readFile(file);
        return content.contains(text);
    }

    /**
     * Read data from file path
     *
     * @param path path to file
     * @return data in string
     */
    public static List<String> readDataAsList(String path) {
        List<String> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {

            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                data.add(sCurrentLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static String readCodeFromLineToLine(File source, int beginLine, int endLine) {
        FileWriter fw = null;
        BufferedWriter out = null;
        String lineContent = "";
        try {
            if (!source.exists()) {
                logger.error("File not found: " + source.getAbsolutePath());
            }
            FileReader fr = new FileReader(source);
            BufferedReader br = new BufferedReader(fr);
            String line = "";
//            line = br.readLine();
            int count = 1;
//			List<String> lines = new ArrayList<String>();
            while ((line = br.readLine()) != null && count <= endLine) {
                if (beginLine <= count && endLine >= count) {
                    lineContent += line + "\n";
                }
                count++;
            }

            fr.close();
            br.close();

//			fw = new FileWriter(source);
//			out = new BufferedWriter(fw);
//			for(String s : lines)
//				out.write(s);
//			out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
//			close(out);
//			close(fw);
        }
        return lineContent;
    }

    public static void writeLineToCSV(String filePath, String[] newContents) {
        File file = new File(filePath);
//        try (CSVReader reader = new CSVReader(new FileReader("file.csv"))) {
//            List<String[]> r = reader.readAll();
//            r.forEach(x -> System.out.println(Arrays.toString(x)));
//        }
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileInputStream  inputStream = new FileInputStream (file);
            CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
            List<String[]> oldContent = reader.readAll();
            reader.close();

            FileOutputStream  outputfile = new FileOutputStream (file);
            //UTF8
            outputfile.write(0xef);
            outputfile.write(0xbb);
            outputfile.write(0xbf);
            CSVWriter writer = new CSVWriter(new OutputStreamWriter(outputfile));
            writer.writeAll(oldContent);
            writer.writeNext(newContents);
            writer.close();
        } catch (IOException e) {
            logger.error("WRITE FILE NOT SUCCESS: " + filePath);
        }
    }

    public static Object ReadObjectFromFile(String filepath) {

        try {

            FileInputStream fileIn = new FileInputStream(filepath);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            Object obj = objectIn.readObject();

            System.out.println("The Object has been read from the file");
            objectIn.close();
            return obj;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
