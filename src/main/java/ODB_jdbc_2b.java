import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ODB_jdbc_2b {

    public static void main(String[] args) {
        String url = "jdbc:oracle:thin:@//ora4.ii.pw.edu.pl:1521/pdb1.ii.pw.edu.pl";
        String uzytkownik = "vpavlovs";
        String haslo= "vpavlovs";

        // Suger context
        // try with resources


        try (Connection polaczenie = DriverManager.getConnection(url, uzytkownik, haslo)){
            int i=1/0;
        }
        catch (SQLException e){
            System.out.println("catch");
            e.printStackTrace();
            return;
        }
        finally {
            System.out.println("finally");
        }
        System.out.println("Sukces.");
    }

}
