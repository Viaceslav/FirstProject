import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OBD_jdbc_2a {

    public static void main(String[] args) {
        String url = "jdbc:oracle:thin:@//ora4.ii.pw.edu.pl:1521/pdb1.ii.pw.edu.pl";
        String uzytkownik = "vpavlovs";
        String haslo= "vpavlovs";
        Connection polaczenie = null;

        try {
            polaczenie = DriverManager.getConnection(url, uzytkownik, haslo);
            int i=1/0;
        }
        catch (SQLException e){
            System.out.println("catch");
            e.printStackTrace();
            return;
        }
        finally {
            System.out.println("finally");
            try {
                if (polaczenie !=null){
                    polaczenie.close();
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        System.out.println("Sukces.");
    }
}
