import java.sql.*;
import java.util.Scanner;
public class Main {
    private static final String url = "jdbc:postgresql://localhost:5433/MYDB";
    private static final String UserName = "postgres";
    private static final String Password = "1234";

    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Choose operation according to their use case :");

        System.out.println("Press 1 : To Get complete Table of data from Data-Base");
        System.out.println("Press 2 : To Insert the data from Data-Base");
        System.out.println("Press 3 : To Update the data from Data-Base");
        System.out.println("Press 4 : To Delete the data from Data-Base");
        System.out.println("Press 5 : To Retrive the data from Data-Base on condition");
        System.out.println("Press 6 : To Do Batch processing using Statement");
        System.out.println("Press 7 : To Do Batch processing using PreparedStatement");

        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        switch (n) {
            case 1:
                try {
                    // DriverManger.getconnection is method of Driver class in takes the parameter to bulid the connection with database
                    // Connection is interface that accept the object of that class which implements Connection
                    // IN connection there is con.create Statement(); it help to write the statement which it take and store in Statement interafce
                    // in statement interface it has function call executeQuerry which the querry
                    // if you execute query it gives some output of database which we used to store in ResultSet
                    // then we use while and .next() to check wheather it has next row or not
                    // in ResultSet has function  we can store each value using the function of their Type and in parameter we pass (coloumn name);

                     Connection con = DriverManager.getConnection(url, UserName, Password);
                    Statement sat = con.createStatement();
                    sc.nextLine();
                    System.out.println("Enter the query");
                    String querry = sc.nextLine();
                    ResultSet res = sat.executeQuery(querry);
                    System.out.println(" ID " + " NAME " + " AGE " + " MARKS ");
                    while (res.next()) {
                        int id = res.getInt("ID");
                        String name = res.getString("NAME");
                        int age = res.getInt("AGE");
                        Double marks = res.getDouble("MARKS");
                        System.out.println(" " + id + " " + name + " " + age + " " + marks);
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case 2:
                try{
                    Connection con = DriverManager.getConnection(url , UserName ,Password);
                    String Query = "INSERT INTO Student(NAME , AGE , MARKS) VALUES(?,?,?)";
                    PreparedStatement sat = con.prepareStatement(Query);
                    // while using set Method in PreparedStatement first is used to take for index according to parameter of question mark you use
                    sat.setString(1, "Rahul");
                    sat.setInt(2, 25);
                    sat.setDouble(3, 93.75);

                   // In this we can use normal statement also but this I wrote because it is a better way to write
                    int result = sat.executeUpdate(); // because it gives value of number of rows affected

                    if(result > 0) System.out.println("Query Insert Successfully");
                    else System.out.println("No Query is Inserted");
                }
                catch(SQLException e){
                    System.out.println(e.getMessage());
                }


                break;
            case 3:
                try{
                    Connection con = DriverManager.getConnection(url,UserName,Password);
                    Statement sat = con.createStatement();
                    String Query = String.format("UPDATE Student SET AGE = %d WHERE ID = %d",38,6);
                    int result = sat.executeUpdate(Query);
                    if(result > 0) System.out.println("Query updated Successfully");
                    else System.out.println("No Query is updated");


                }
                catch (SQLException e){
                    System.out.println(e.getMessage());
                }


                break;

            case 4:

                try{
                    Connection con = DriverManager.getConnection(url,UserName,Password);
                    Statement sat = con.createStatement();
                    String Query = String.format("DELETE FROM Student WHERE ID = %o",4);
                    int result = sat.executeUpdate(Query);
                    if(result > 0) System.out.println("Query Deleted Successfully");
                    else System.out.println("No Query is Deleted");


                }
                catch (SQLException e){
                    System.out.println(e.getMessage());
                }



                break;


            case 5:
                try{
                    Connection con = DriverManager.getConnection(url , UserName , Password);
                    String Query = "SELECT MARKS FROM Student WHERE ID = ?";
                    PreparedStatement pat = con.prepareStatement(Query);
                    pat.setInt(1,1);
                    ResultSet res = pat.executeQuery();
                    if(res.next()){
                        System.out.println("Marks : " + res.getDouble("MARKS"));
                    }else System.out.println(" oops!! NO data is found");

                }
                catch(SQLException e){
                    System.out.println(e.getMessage());
                }

                break;

            case 6:
                try{
                    Connection con = DriverManager.getConnection(url , UserName , Password);
                    Statement sat = con.createStatement();
                    while(true){
                        System.out.println("Enter Name of Student");
                        String Name = sc.next();
                        System.out.println("Enter Age of Student");
                        int Age = sc.nextInt();
                        System.out.println("Enter Marks of Student");
                        double Marks = sc.nextDouble();
                        String Query = String.format("INSERT INTO Student(NAME , AGE , MARKS) VALUES('%s' , %o , %f)" , Name , Age , Marks);
                        sat.addBatch(Query);
                        System.out.println("IF YOU WANT TO ADD MORE BATCH OF STUDENT PRESS Y/N");
                        String choice = sc.next();
                        if(choice.toUpperCase().equals("N")) break;


                    }

                    int[] arr = sat.executeBatch();

                    for(int i = 0;i<arr.length;i++){
                        if(arr[i] == 0) System.out.println("Query no " + i + " is not executed ");
                    }

                }
                catch(SQLException e){
                    System.out.println(e.getMessage());
                }

                break;

            case 7:

                try{
                    Connection con = DriverManager.getConnection(url , UserName , Password);
                    String Query = "INSERT INTO Student(NAME , AGE , MARKS) VALUES(?,?,?)";
                    PreparedStatement sat = con.prepareStatement(Query);
                    while(true){
                        System.out.println("Enter Name of Student");
                        String Name = sc.next();
                        System.out.println("Enter Age of Student");
                        int Age = sc.nextInt();
                        System.out.println("Enter Marks of Student");
                        double Marks = sc.nextDouble();

                        sat.setString(1,Name);
                        sat.setInt(2,Age);
                        sat.setDouble(3,Marks);

                        sat.addBatch();
                        System.out.println("IF YOU WANT TO ADD MORE BATCH OF STUDENT PRESS Y/N");
                        String choice = sc.next();
                        if(choice.toUpperCase().equals("N")) break;


                    }

                    int[] arr = sat.executeBatch();

                    for(int i = 0;i<arr.length;i++){
                        if(arr[i] == 0) System.out.println("Query no " + i + " is not executed ");
                    }

                }
                catch(SQLException e){
                    System.out.println(e.getMessage());
                }

                break;

            default:

                System.out.println("Choose Number according to given list only");
        }
        System.out.println("HERE WE CONNECT OUR DATABASE WITH Postgres");

        // this is how we can setup JDBC using the Database
    }
}
