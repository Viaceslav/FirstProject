import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ODB_jdbc_3a {

    public static void main(String[] args) {
        String url = "jdbc:oracle:thin:@//ora4.ii.pw.edu.pl:1521/pdb1.ii.pw.edu.pl";
        String uzytkownik = "vpavlovs";
        String haslo= "vpavlovs";
        String sql1 = "CREATE TABLE dzialy (nr_dzialu integer not null, nazwa_dzialu varchar2(30), siedziba varchar2(50) not null)";

        // Suger context
        // try with resources
        try (Connection polaczenie = DriverManager.getConnection(url, uzytkownik, haslo)){
            System.out.println("AutoCommit:  " + polaczenie.getAutoCommit());
            try (Statement polecenie = polaczenie.createStatement()) {
                System.out.println("execute:  " + polecenie.execute(sql1));
            }
            catch (Exception e){
                System.out.println("catch-1");
                e.printStackTrace();
                return;
            }
            finally {
                System.out.println("finally -1");
            }

        }
        catch (SQLException e){
            System.out.println("catch -2");
            e.printStackTrace();
            return;
        }
        finally {
            System.out.println("finally - 2");
        }
        System.out.println("Sukces.");
    }

}
