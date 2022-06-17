import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ODB_jdbc_4 {

    public static void main(String[] args) {
        String url = "jdbc:oracle:thin:@//ora4.ii.pw.edu.pl:1521/pdb1.ii.pw.edu.pl";
        String uzytkownik = "vpavlovs";
        String haslo= "vpavlovs";
        String sql1 = "INSERT INTO dzialy (nr_dzialu, nazwa_dzialu, siedziba) VALUES (1, 'DYREKCJA', 'UL. 1. STYCZNIA 23')";

        try(Connection polaczenie = DriverManager.getConnection(url, uzytkownik, haslo);
            Statement polecenie = polaczenie.createStatement()){

            System.out.println("AutoCommit: " + polaczenie.getAutoCommit());

            System.out.println("execute: "  + polecenie.executeUpdate(sql1));


        } catch (SQLException e){
            System.out.println("catch");
            e.printStackTrace();
            return;
        }

        System.out.println("Sukes.");

    }

}

