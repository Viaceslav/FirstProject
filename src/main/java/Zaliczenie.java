import oracle.net.jdbc.nl.UninitializedObjectException;

import java.sql.*;
import java.text.ParseException;
import java.util.*;

public class Zaliczenie {

    public static void main(String[] args) {
        String url = "jdbc:oracle:thin:@//ora4.ii.pw.edu.pl:1521/pdb1.ii.pw.edu.pl";
        String uzytkownik = "vpavlovs";
        String haslo = "vpavlovs";


        /// DDL
        String crt1 = "CREATE TABLE Nauczyciel2 (idn INTEGER NOT NULL, nazwisko_nauczyciela char(30), imie_nauczyciela char(50))";
        String crt2 = "CREATE TABLE Przedmiot2 (idp INTEGER NOT NULL, nazwa_przedmiotu char(30))";
        String crt3 = "CREATE TABLE Uczen2 (idu INTEGER NOT NULL, nazwisko_ucznia char(30) NOT NULL, imie_ucznia char(20))";
        String crt4 = "CREATE TABLE Ocena2 (ido INTEGER NOT NULL, wartosc_opisowa char(30), wartosc_numeryczna float)";
        String crt5 = "CREATE TABLE Ocenianie2 (rodzaj_oceny char(1), idn INTEGER NOT NULL, idp INTEGER NOT NULL, idu INTEGER NOT NULL, ido INTEGER NOT NULL)";

        ///DDL

        String insert1 = "INSERT INTO Nauczyciel2 (idn, nazwisko_nauczyciela , imie_nauczyciela) VALUES (1, 'Ganczyk', 'Joanna')";
        String insert2 = "INSERT INTO Przedmiot2 (idp, nazwa_przedmiotu) VALUES (1, 'Fizyka')";
        String insert3 = "INSERT INTO Uczen2 (idu , nazwisko_ucznia , imie_ucznia) VALUES (1, 'Florczyk', 'Sylwester')";
        String insert4 = "INSERT INTO Ocena2 (ido ,wartosc_opisowa ,wartosc_numeryczna) VALUES (1, 'Fizyka', 4)";


        Scanner oce = new Scanner(System.in);
        Scanner idn = new Scanner(System.in);
        Scanner idu = new Scanner(System.in);
        Scanner idp = new Scanner(System.in);
        Scanner ido = new Scanner(System.in);


        System.out.println("Wprowadź rodzaj_oceny oce: ");
        System.out.println("Wprowadź klucz idn: ");
        System.out.println("Wprowadź klucz idu: ");
        System.out.println("Wprowadź klucz idp: ");
        System.out.println("Wprowadź klucz ido: ");


        String ocena = oce.nextLine();
        String id_n = idn.nextLine();
        String id_u = idu.nextLine();
        String id_p = idp.nextLine();
        String id_o = ido.nextLine();


        String insert5 = "INSERT INTO Ocenianie2 (rodzaj_oceny, idn, idu, idp, ido) VALUES (?, ?, ?, ?, ?)";

        List<String> qryList = new ArrayList<>();
        List<String> insertList = new ArrayList<>();

        qryList.add(crt1);
        qryList.add(crt2);
        qryList.add(crt3);
        qryList.add(crt4);
        qryList.add(crt5);

        insertList.add(insert1);
        insertList.add(insert2);
        insertList.add(insert3);
        insertList.add(insert4);

        List<String> listParams = new ArrayList<>();

//        listParams.add(ocena);
        listParams.add(id_n);
        listParams.add(id_u);
        listParams.add(id_p);
        listParams.add(id_o);

        // Suger context
        // try with resources
        try (Connection polaczenie = DriverManager.getConnection(url, uzytkownik, haslo)) {
            System.out.println("AutoCommit:  " + polaczenie.getAutoCommit());
            try (Statement polecenie = polaczenie.createStatement()) {
                for (int i = 0; i < qryList.size(); i++) {
                    System.out.println("zapytanie SQL Create Table");
                    System.out.println(qryList.get(i));
                    System.out.println("execute:  " + polecenie.execute(qryList.get(i)));
                    System.out.println("koniec zapytania SQL");
                }


                for (int i = 0; i < insertList.size(); i++) {
                    System.out.println("Zapytanie Insert");
                    System.out.println(insertList.get(i));
                    System.out.println("execute:  " + polecenie.execute(insertList.get(i)));
                    System.out.println("Kończę zapytanie:");

                }

                boolean klucz = true;

                // analiza parametru oceny "S" lub "C"

                boolean k = weryfikacjaOceny(ocena);
                if (!k) {
                    klucz = false;
                }


                for (int i = 0; i < listParams.size(); i++) {

                    System.out.println("drukuje parametr wprowadzony:  " + listParams.get(i));
                    boolean p = weryfikacjaKluczy(listParams.get(i), polaczenie);

                    if (!p) {
                        klucz = false;
                        System.out.println("niepoprawne parametry kluczy obcych");
                    }

                }

                if (klucz) {
                    PreparedStatement polecenie2 = polaczenie.prepareStatement(insert5);

                    polecenie2.setString(1, ocena);
                    polecenie2.setInt(2, Integer.parseInt(id_n));
                    polecenie2.setInt(3, Integer.parseInt(id_u));
                    polecenie2.setInt(4, Integer.parseInt(id_p));
                    polecenie2.setInt(5, Integer.parseInt(id_o));

                    System.out.println("wykonuje:  " + polecenie2.executeUpdate());

                    String selQries =  "Select * from Ocenianie2";

                    ResultSet rs = polecenie2.executeQuery(selQries);

                    while (rs.next()) {
                        System.out.println(rs.getString(1) + "|" + rs.getInt(2) + "|" + rs.getInt(3) + "|" + rs.getInt(4) + "|" + rs.getInt(5));
                    }
                    rs.close();
                }

            } catch (SQLTransactionRollbackException tr) {
                System.out.println("catch - transaction");
                return;
            } catch (SQLIntegrityConstraintViolationException en) {
                System.out.println("catch - unique error");
                return;
            } catch (IndexOutOfBoundsException out) {
                System.out.println("blad:  " + out);
            } catch (Exception e) {
                System.out.println("catch-last: " + e);
                e.printStackTrace();
                return;
            } finally {
                System.out.println("finally -1");
            }

        } catch (SQLException e) {
            System.out.println("catch -2");
            e.printStackTrace();
            return;
        } finally {
            System.out.println("finally - 2");
        }


        /////   OCENIANIE
        // Suger context
        // try with resources


//        try (Connection polaczenie = DriverManager.getConnection(url, uzytkownik, haslo)) {
//            System.out.println("AutoCommit:  " + polaczenie.getAutoCommit());
//            try (PreparedStatement polecenie = polaczenie.prepareStatement(insert5)) {
//
//                boolean kwalif = false;
//                boolean par = false;
//                boolean cor = false;
//
//
//                polecenie.setString(1, oce.nextLine());
//
//                Boolean kwal = poprawnaWartosc(ocena);
//
//
//
//
//                Boolean kwal1 = isNotNullValues(id_n);
//                Boolean kwal2 = isNotNullValues(id_u);
//                Boolean kwal3 = isNotNullValues(id_p);
//                Boolean kwal4 = isNotNullValues(id_o);
//
//
//                if (kwal | kwal1 & kwal2 | kwal2 | kwal3 | kwal4) {
//                    kwalif = true;
//
//                }
//
//                Boolean par1 = isParsingCorrectly(id_n);
//                Boolean par2 = isParsingCorrectly(id_u);
//                Boolean par3 = isParsingCorrectly(id_p);
//                Boolean par4 = isParsingCorrectly(id_o);
//
//
//                if (par1 | par2 & par3 | par4) {
//                    par = true;
//                }
//                Boolean cor1 = correctness(id_n);
//                Boolean cor2 = correctness(id_u);
//                Boolean cor3 = correctness(id_p);
//                Boolean cor4 = correctness(id_o);
//
//
//                if (cor1 | cor2 & cor3 | cor4) {
//                    cor = true;
//                }
//
//
//                if (cor | par | kwalif) {
//                    polecenie.setString(1, ocena);
//                    polecenie.setInt(2, Integer.parseInt(id_n));
//                    polecenie.setInt(3, Integer.parseInt(id_u));
//                    polecenie.setInt(4, Integer.parseInt(id_p));
//                    polecenie.setInt(5, Integer.parseInt(id_o));
//
//                    System.out.println("wykonuje:  " + polecenie.executeUpdate());
//
//                    ResultSet rs = polecenie.executeQuery();
//
//                    while (rs.next()) {
//                        System.out.println(rs.getString(1) + "|" + rs.getInt(2) + "|" + rs.getInt(3) + "|" + rs.getInt(4) + "|" + rs.getInt(5));
//                    }
//                    rs.close();
//
//                }
//
//            } catch (SQLTransactionRollbackException tr) {
//                System.out.println("catch - transaction");
//                return;
//            } catch (SQLIntegrityConstraintViolationException en) {
//                System.out.println("catch - unique error");
//                return;
//            } catch (Exception e) {
//                System.out.println("catch-1");
//                e.printStackTrace();
//                return;
//            } finally {
//                System.out.println("finally -1");
//            }
//
//        } catch (SQLException e) {
//            System.out.println("catch -2");
//            e.printStackTrace();
//            return;
//        } finally {
//            System.out.println("finally - 2");
//        }

        System.out.println("Sukces.");
    }


    static Boolean isNotNullValues(String zmienna) throws NullPointerException {
        return zmienna != null;
    }


    static Boolean poprawnaWartosc(String zmienna) {

        if (Objects.equals(zmienna.toUpperCase(Locale.ROOT), "S") | Objects.equals(zmienna.toUpperCase(Locale.ROOT), "C")) {
            return true;
        }
        return false;
    }

    static Boolean isParsingCorrectly(String zmienna) throws ParseException {

        Integer con_int = Integer.parseInt(zmienna);
        return true;
    }

    static Boolean weryfikacjaKluczy(String zmienna, Connection polaczenie) throws ParseException, SQLException {

        Boolean kwal = isNotNullValues(zmienna);

        // wlasciwe parsowanie zmiennej "INT"
        Boolean parsowanie = isParsingCorrectly(zmienna);

        // czy istnieje klucz obcy w tabelach macierzystych
        Boolean correctness = correctness2(zmienna, polaczenie);

        // warunek koncowy = true
        return correctness | parsowanie | kwal;
    }


    static Boolean weryfikacjaOceny(String zmienna) {

        return poprawnaWartosc(zmienna);
    }

//    static Boolean correctness(String zmienna) throws NullPointerException {
//
//        String selQry1 = "SELECT idn FROM Nauczyciel WHERE idn = ?";
//        String selQry2 = "SELECT idp FROM Przedmiot WHERE idp = ?";
//        String selQry3 = "SELECT idu FROM Uczen WHERE idu = ?";
//        String selQry4 = "SELECT ido FROM Ocena WHERE ido = ?";
//
//        List<String> selQrs = new ArrayList<>();
//        selQrs.add(selQry1);
//        selQrs.add(selQry2);
//        selQrs.add(selQry3);
//        selQrs.add(selQry4);
//
//        String url = "jdbc:oracle:thin:@//ora4.ii.pw.edu.pl:1521/pdb1.ii.pw.edu.pl";
//        String uzytkownik = "vpavlovs";
//        String haslo = "vpavlovs";
//
//        try (Connection polaczenie = DriverManager.getConnection(url, uzytkownik, haslo)) {
//            System.out.println("AutoCommit:  " + polaczenie.getAutoCommit());
//            do {
//                int i = 0;
//                PreparedStatement polecenie = polaczenie.prepareStatement(selQrs.get(i));
//                polecenie.setString(1, zmienna);
//                System.out.println("execute:  " + polecenie.executeUpdate());
//                ResultSet rs = polecenie.executeQuery();
//
//                while (rs.next()) {
//                    int out = rs.getInt(1);
//                    System.out.println(rs.getInt(1));
//
//                    if (Integer.parseInt(zmienna) == out) {
//                        return true;
//                    }
//                }
//                rs.close();
//            } while (true);
//
//
//        } catch (SQLSyntaxErrorException synErr) {
//            System.out.println("blad:  " + synErr);
//        } catch (SQLException e) {
//            System.out.println("catch -5" + e);
//            e.printStackTrace();
//            return false;
//        } finally {
//            System.out.println("finally - 2");
//        }
//    }




    static Boolean correctness2(String zmienna, Connection polaczenie) throws NullPointerException, SQLException {

        boolean corr = false;
        String selQry1 = "SELECT idn FROM Nauczyciel2 WHERE idn = ?";
        String selQry2 = "SELECT idp FROM Przedmiot2 WHERE idp = ?";
        String selQry3 = "SELECT idu FROM Uczen2 WHERE idu = ?";
        String selQry4 = "SELECT ido FROM Ocena2 WHERE ido = ?";

        List<String> selQrs = new ArrayList<>();
        selQrs.add(selQry1);
        selQrs.add(selQry2);
        selQrs.add(selQry3);
        selQrs.add(selQry4);

//        String url = "jdbc:oracle:thin:@//ora4.ii.pw.edu.pl:1521/pdb1.ii.pw.edu.pl";
//        String uzytkownik = "vpavlovs";
//        String haslo = "vpavlovs";


        for (String sqlStr: selQrs
             ) {
            PreparedStatement polecenie = polaczenie.prepareStatement(sqlStr);
            polecenie.setString(1, zmienna);
            System.out.println("execute:  " + polecenie.executeUpdate());
            ResultSet rs = polecenie.executeQuery();
            while (rs.next()) {
                int out = rs.getInt(1);
                System.out.println(rs.getInt(1));

                if (Integer.parseInt(zmienna) == out) {
                    corr = true;
                }
            }


        }

        return corr;
    }
}

