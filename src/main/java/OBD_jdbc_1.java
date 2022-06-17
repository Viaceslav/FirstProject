public class OBD_jdbc_1 {

    public static void main(String[] args) {
        String nazwaSterownika = "oracle.jdbc.driver.OracleDriver";


        try {
            Class<?> c = Class.forName(nazwaSterownika); // Rejestracja sterownika JDBC
            System.out.println("Pakiet   :" + c.getPackage());
            System.out.println("Nazwa klasy :"  + c.getName());


        }
        catch (Exception e){
            System.out.println("Exception: " + e.getMessage());
            e.printStackTrace();
            return;
        }
        System.out.println("Sukces.");
    }
}
