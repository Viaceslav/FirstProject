import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ODB_jdbc_3 {

    public static void main(String[] args) {
        String url = "jdbc:oracle:thin:@//ora4.ii.pw.edu.pl:1521/pdb1.ii.pw.edu.pl";
        String uzytkownik = "vpavlovs";
        String haslo= "vpavlovs";
        String sql1 = "CREATE TABLE dzialy (nr_dzialu integer not null, nazwa_dzialu varchar2(30), siedziba varchar2(30) not null)";
        Statement polecenie = null;

        // Suger context
        // try with resources
        try (Connection polaczenie = DriverManager.getConnection(url, uzytkownik, haslo)){
            System.out.println("AutoCommit:  " + polaczenie.getAutoCommit());
            polecenie = polaczenie.createStatement();
            System.out.println("" + polecenie.execute(sql1));
        }
        catch (SQLException e){
            System.out.println("catch");
            e.printStackTrace();
            return;
        }
        finally {
            System.out.println("finally");
            try {
                if (polecenie != null) {
                    polecenie.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Sukces.");
        }

}
