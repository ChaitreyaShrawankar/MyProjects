package Bank;
import java.sql.*;
import java.util.Scanner;
class InvalidAgeException extends Exception {
    public InvalidAgeException(String s) {
        System.out.println(s);
    }
}

class InvalidWithdrawAmount extends Exception {
    public InvalidWithdrawAmount(String s) {
        System.out.println(s);
    }
}

class InvalidStartingAmount extends Exception {
    public InvalidStartingAmount(String s) {
        System.out.println(s);
    }
}

class UsernameNotFound extends Exception {
    public UsernameNotFound(String s) {
        System.out.println(s);
    }
}
public class ZamasuBank {
    private String Name;
    private int Age;
    private int Balance;

    public String getName() {
        return Name;
    }

    public int getAge() {
        return Age;
    }

    public int getBalance() {
        return Balance;
    }

    public int usernamechecker(String n) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/zamasubank", "root", "root");
        PreparedStatement pstml1 = con.prepareStatement("select Name from Account where Name=?");
        pstml1.setString(1, n);
        ResultSet rs = pstml1.executeQuery();
        if (rs.next()) {
            return 1;
        } else {
            return 0;
        }
    }

    public void createAccount() throws ClassNotFoundException, SQLException {
        System.out.println("Enter the Username of Account:");
        Scanner sc = new Scanner(System.in);
        this.Name = sc.next();
        System.out.println("Enter the Age:");
        this.Age = sc.nextInt();
        try {
            if (this.Age < 18) {
                throw new InvalidAgeException("Invalid Age for Account Creation!");
            } else {
                System.out.println("Enter Opening Balance:");
                this.Balance = sc.nextInt();
                try {
                    if (this.Balance < 1000) {
                        throw new InvalidStartingAmount("Invalid Starting Amount!");
                    } else {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/zamasubank", "root", "root");
                        PreparedStatement pstml = con.prepareStatement("insert into Account(Name,Age,Balance) values(?,?,?)");
                        pstml.setString(1, this.Name);
                        pstml.setInt(2, this.Age);
                        pstml.setInt(3, this.Balance);
                        pstml.executeUpdate();
                        System.out.println("Account Creation Successful");
                        PreparedStatement pstml1 = con.prepareStatement("select * from Account where Name=?");
                        pstml1.setString(1, this.Name);
                        ResultSet rs = pstml1.executeQuery();
                        while (rs.next()) {
                            System.out.println("\nYour Account Details are:\nName:" + rs.getString(1) + "\nAge:" + rs.getInt(2) + "\nBalance:" + rs.getInt(3));
                        }
                        System.out.println("Visit Again!");
                    }
                } catch (InvalidStartingAmount a) {
                    System.out.println(a);
                }
            }
        } catch (InvalidAgeException a) {
            System.out.println(a);
        }
    }

    public void depositAccount() throws ClassNotFoundException, SQLException, UsernameNotFound {
        System.out.println("Enter your Username:");
        Scanner sc = new Scanner(System.in);
        String n = sc.next();
        if (usernamechecker(n) == 1) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/zamasubank", "root", "root");
            System.out.println("Enter the Amount to deposit:");
            int amt = sc.nextInt();
            this.Balance += amt;
            System.out.println("Deposition Successful");
            PreparedStatement pstml1 = con.prepareStatement("update Account set Balance=? where Name=?");
            pstml1.setInt(1, this.Balance);
            pstml1.setString(2, n); // Fixed indexing
            pstml1.executeUpdate();
            PreparedStatement pstml11 = con.prepareStatement("select * from Account where Name=?");
            pstml11.setString(1, n);
            ResultSet rs = pstml11.executeQuery();
            while (rs.next()) {
            	System.out.println("\nYour Account Details are:\nName:"+rs.getString(1)+"\nAge:"+rs.getInt(2)+"\nBalance:"+rs.getInt(3));
            }
            System.out.println("Visit Again!");
        } else {
            throw new UsernameNotFound("UserName Not Found!");
        }
    }

    public void withdrawAccount() throws ClassNotFoundException, SQLException, InvalidWithdrawAmount, UsernameNotFound {
        System.out.println("Enter your Username:");
        Scanner sc = new Scanner(System.in);
        String n = sc.next();
        if (usernamechecker(n) == 1) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/zamasubank", "root", "root");
            System.out.println("Enter the Amount to withdraw:");
            int amt = sc.nextInt();
            if (this.Balance - amt < 1000) {
                throw new InvalidWithdrawAmount("Invalid Amount to Withdraw!");
            } else {
                this.Balance -= amt;
                System.out.println("Withdrawal Successful");
                PreparedStatement pstml1 = con.prepareStatement("update Account set Balance=? where Name=?");
                pstml1.setInt(1, this.Balance);
                pstml1.setString(2, n); // Fixed indexing
                pstml1.executeUpdate();
                PreparedStatement pstml11 = con.prepareStatement("select * from Account where Name=?");
                pstml11.setString(1, n);
                ResultSet rs = pstml11.executeQuery();
                while (rs.next()) {
                	System.out.println("\nYour Account Details are:\nName:"+rs.getString(1)+"\nAge:"+rs.getInt(2)+"\nBalance:"+rs.getInt(3));
                }
                System.out.println("Visit Again!");
            }
        } else {
            throw new UsernameNotFound("UserName Not Found!");
        }
    }

    public void viewAccount() throws ClassNotFoundException, SQLException, UsernameNotFound {
        System.out.println("Enter your Username:");
        Scanner sc = new Scanner(System.in);
        String n = sc.next();
        if (usernamechecker(n) == 1) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/zamasubank", "root", "root");
            PreparedStatement pstml1 = con.prepareStatement("select * from Account where Name=?");
            pstml1.setString(1, n);
            ResultSet rs = pstml1.executeQuery();
            while (rs.next()) {
            	System.out.println("\nYour Account Details are:\nName:"+rs.getString(1)+"\nAge:"+rs.getInt(2)+"\nBalance:"+rs.getInt(3));
                System.out.println("Visit Again!");
            }
        } else {
            throw new UsernameNotFound("UserName Not Found!");
        }
    }

    public static void main(String args[]) throws ClassNotFoundException, SQLException, UsernameNotFound, InvalidWithdrawAmount {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/zamasubank", "root", "root");
        Statement stmt = con.createStatement();
        int op = 1;
        while (op != 0) {
            System.out.println("Welcome to Zamasu Bank!");
            System.out.println("Enter your Choice:");
            System.out.println("1:Create an Account");
            System.out.println("2:Deposit");
            System.out.println("3:Withdraw");
            System.out.println("4:View Account details");
            System.out.println("5:Edit Account Info");
            Scanner s1 = new Scanner(System.in);
            int choice = s1.nextInt();
            ZamasuBank z = new ZamasuBank();
            switch (choice) {
                case 1: {
                    ZamasuBank z1 = new ZamasuBank();
                    z1.createAccount();
                    break;
                }
                case 2: {
                    ZamasuBank z2 = new ZamasuBank();
                    z2.depositAccount();
                    break;
                }
                case 3: {
                    ZamasuBank z3 = new ZamasuBank();
                    z3.withdrawAccount();
                    break;
                }
                case 4: {
                    ZamasuBank z4 = new ZamasuBank();
                    z4.viewAccount();
                    break;
                }
            }
            System.out.println("Do you want to continue?");
            System.out.println("Press 1 to Continue or 0 to Exit:");
            Scanner o = new Scanner(System.in);
            op = o.nextInt();
        }
    }
}

