import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

class ScriptRunner {
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