package player.csv;

import java.io.*;
import java.util.stream.Stream;

/**
 * Created by azertify on 06/03/15.
 */
public class CsvFile {
    public static final String DELIMITER = ",";
    public static final String SAFESTRING = "§";

    private File file;
    private BufferedReader reader;
    private BufferedWriter writer;

    /**
     * Constructor to make CsvFile from a filename represented by a string
     * @param filename
     */
    public CsvFile(String filename) {
        this(new File(filename));
    }

    /**
     * Constructor to make CsvFile from a file
     * @param file
     */
    public CsvFile(File file) {
        this.file = file;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            reader = new BufferedReader(new FileReader(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes a single record to the csv file
     * @param record
     */
    public void add(String[] record) {
        if (writer == null) {
            setWriter();
        }
        if (record.length < 1) {
            return;
        }
        try {
            writer.write(escape(record[0]));
            for (int i = 1; i < record.length; i++) {
                writer.write("," + escape(record[i]));
            }
            writer.newLine();
            writer.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Gets a stream of arrays of strings from the file
     * @return Stream of array of strings
     */
    public Stream<String[]> getStream() {
        return reader.lines().map(s -> splitLine(s));
    }

    /**
     * Closes file
     */
    public void close() {
        if (writer == null) {
            return;
        }
        try {
            writer.close();
            writer = null;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Escapes entry to go into csv file
     * @param string Entry
     * @return Escaped string
     */
    private String escape(String string) {
        return string.replace(DELIMITER, SAFESTRING);
    }

    /**
     * Unescapes entry from csv file
     * @param string Entry
     * @return Unescaped string
     */
    private String unescape(String string) {
        return string.replace(SAFESTRING, DELIMITER);
    }

    /**
     * Returns an array of strings from a csv record
     * @param line Csv record
     * @return Array of strings representing csv record
     */
    private String[] splitLine(String line) {
        String[] array = line.split(",");
        for (int i = 0; i < array.length; i++) {
            array[i] = unescape(array[i]);
        }
        return array;
    }

    /**
     * Initialises the writer, and wipes the file if it exists
     */
    private void setWriter() {
        try {
            writer = new BufferedWriter(new FileWriter(file));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}