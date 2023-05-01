package org.eml;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;


public class EmlReader {

    static PrintWriter printWriter;
    static File outFile = outputFile();

    public static File getOutFile() {
        return outFile;
    }
    static {
        if (printWriter== null) {
            try {
                printWriter = new PrintWriter(getOutFile());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
    /*
        The file path is passed in as parameter which then checks whether the entered path is directory or a eml file
        Later it calls readEml method which reads the eml content and further calls printToDestination method which
        prints the output in a created file.
     */
  public static String initial(String selectedPath) throws MessagingException, IOException {
        File path = new File(selectedPath);
        // if the entered path is a directory
        if (path.isDirectory()) {
            File[] files = path.listFiles();
            assert files != null;
            for (File file : files) {
                File newFile = new File(file.getAbsolutePath());
                if (isEml(newFile)) {
                    readEml(newFile);
                }
            }
            return "Eml reading successful";
        }
        // if the entered path is a eml file
        else if (isEml(path)) {
            readEml(path);
            return "Eml reading successful";
        }
        // a file other than eml or directory is entered
        else {
            return "Not an eml file";
        }
    }

    /*
        Checks whether the file is an .eml file or not
     */
    public static boolean isEml(File path) {
        String name = path.getName();
        StringBuilder type = new StringBuilder();
        for (int i = (name.length() - 1); i >= 0; i--) {
            type.insert(0, String.valueOf(name.charAt(i)));
            if (name.charAt(i) == '.') {
                break;
            }
        }
        // check
        return type.toString().equals(".eml");
    }

    /*
        Reads the eml file and extracts information from it.
     */
    public static void readEml(File emlFile) throws IOException, MessagingException {
        Properties properties = new Properties();
        Session session = Session.getDefaultInstance(properties, null);
        //if file not found throws file not found exception
        InputStream inputStream = Files.newInputStream(emlFile.toPath());
        //throws messaging exception
        MimeMessage mimeMessage = new MimeMessage(session, inputStream);
        printToDestination(mimeMessage);
    }

    /*
        Returns the path of the output file with a date and time format
     */
    public static File outputFile() {
        File destinationDirectory = new File("C:\\Eml Output");
        //Creates directory if directory does not exist
        if (!destinationDirectory.exists()) {
            Boolean created =destinationDirectory.mkdir();
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        //Creates a new file using date time stamp.
        return new File(destinationDirectory + "/" + dateTimeFormatter.format(now) + ".txt");
    }

    /*
        Writes the subject and from of the .eml to a new file
     */
    public static void printToDestination(MimeMessage mimeMessage) throws MessagingException{
        String str = mimeMessage.getFrom()[0].toString();
        // Extracts only name from mimeMessage.getFrom() and excludes email
        int i = 0;
        int end = 0;
        while (i < str.length()) {
            if (str.charAt(i) == '<') {
                end = i - 1;
                break;
            }
            i++;
        }
        // Writing output in the destination directory
        printWriter.println(str.substring(0, end) + ": " + mimeMessage.getSubject());
        printWriter.flush();
    }
}