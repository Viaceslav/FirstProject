import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OBD_jdbc_2 {

    public static void main(String[] args) {
        String url = "jdbc:oracle:thin:@//ora4.ii.pw.edu.pl:1521/pdb1.ii.pw.edu.pl";
        String uzytkownik = "vpavlovs";
        String haslo= "vpavlovs";

        try {
            Connection polaczenie = DriverManager.getConnection(url, uzytkownik, haslo);
            System.out.println("AutoCommit"  + polaczenie.getAutoCommit());
            polaczenie.close();

        }
        catch (SQLException e){
            System.out.println("catch");
            e.printStackTrace();
            return;
        }
        System.out.println("Sukces.");
    }
}
