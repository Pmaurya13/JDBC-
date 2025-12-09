import java.sql.*;
import java.util.Scanner;

public class Transction {
    private static final String url = "jdbc:postgresql://localhost:5433/MYDB";
    private static final String UserName = "postgres";
    private static final String Password = "1234";

    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        Scanner sc = new Scanner(System.in);

        System.out.println("Press 1 : To Add Customer");
        System.out.println("Press 2 : To Make Transaction");
        int choice = sc.nextInt();

        switch(choice) {

            case 1:
                try  {
                    Connection con = DriverManager.getConnection(url, UserName, Password);
                    String query = "INSERT INTO Accounts(ACC_NO, NAME) VALUES (?, ?)";
                    PreparedStatement pat = con.prepareStatement(query);

                    while (true) {
                        System.out.println("Enter Account Number:");
                        int acc = sc.nextInt();

                        System.out.println("Enter Name:");
                        String name = sc.next();

                        pat.setInt(1, acc);
                        pat.setString(2, name);
                        pat.addBatch();

                        System.out.println("Add more? (Y/N)");
                        if (sc.next().equalsIgnoreCase("N")) break;
                    }

                    pat.executeBatch();
                    System.out.println("Customers Added Successfully.");
                }
                catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                break;


            case 2:
                try{
                    Connection con = DriverManager.getConnection(url, UserName, Password);

                    con.setAutoCommit(false);

                    String debitQuery = "UPDATE Accounts SET BALANCE = BALANCE - ? WHERE ACC_NO = ?";
                    String creditQuery = "UPDATE Accounts SET BALANCE = BALANCE + ? WHERE ACC_NO = ?";

                    PreparedStatement debit = con.prepareStatement(debitQuery);
                    PreparedStatement credit = con.prepareStatement(creditQuery);

                    System.out.println("Enter Amount to Transfer:");
                    int amount = sc.nextInt();

                    System.out.println("Enter Your Account Number:");
                    int sender = sc.nextInt();

                    System.out.println("Enter Receiver Account Number:");
                    int receiver = sc.nextInt();

                    // CHECK BALANCE FIRST
                    if (!sufficient(con, sender, amount)) {
                        System.out.println("Transaction Failed: Insufficient balance!");
                        con.rollback();
                        break;
                    }

                    // Now debit & credit
                    debit.setInt(1, amount);
                    debit.setInt(2, sender);

                    credit.setInt(1, amount);
                    credit.setInt(2, receiver);

                    debit.executeUpdate();
                    credit.executeUpdate();

                    con.commit();
                    System.out.println("Transaction Successful!");

                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                break;

            default:
                System.out.println("Invalid choice!");
        }
    }


    public static boolean sufficient(Connection con, int acc, int amount) {
        try {
            PreparedStatement pst = con.prepareStatement("SELECT BALANCE FROM Accounts WHERE ACC_NO = ?");
            pst.setInt(1, acc);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int balance = rs.getInt("BALANCE");
                return balance >= amount;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
