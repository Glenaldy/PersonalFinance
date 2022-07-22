import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/*
 * ScriptRunner is an interface that will read all the lines from SQL file and return it as a string.
 */
public interface ScriptRunner {
    /**
     * Get all the lines in the in the location of path argument.
     * Append it into a string and returns it.
     * 
     * @param path
     * @return String of all the lines in the sql files
     */
    public static String runScript(String path) {
        String line;
        StringBuffer sql = new StringBuffer();
        try {
            BufferedReader rd = new BufferedReader(new FileReader(path));
            while ((line = rd.readLine()) != null) {
                sql.append(line);
            }
            rd.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("I/O error.");
            e.printStackTrace();
        }
        return sql.toString();
    }
}