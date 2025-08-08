import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Customer {
    private String username;
    private String password;
    private double balance;

    private static final String USERS_FILE = "data/users.txt";
    private static final String HISTORY_FOLDER = "data/history";

    public Customer(String username, String password, double balance) {
        this.username = username;
        this.password = password;
        this.balance = balance;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    // Customer Login
    public static void login() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Username: ");
        String uname = sc.nextLine();
        System.out.print("Enter Password: ");
        String pass = sc.nextLine();

        Customer customer = findCustomer(uname, pass);
        if (customer != null) {
            System.out.println("Login Successful!");
            customerMenu(customer);
        } else {
            System.out.println("Invalid credentials!");
        }
    }

    // Customer Register 
    public static void register() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Choose Username: ");
        String uname = sc.nextLine();
        System.out.print("Choose Password: ");
        String pass = sc.nextLine();
        double initialBalance = 0.0;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
            bw.write(uname + "," + pass + "," + initialBalance);
            bw.newLine();
            System.out.println("Registration Successful!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Find Customer from file
    private static Customer findCustomer(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3 && parts[0].equals(username) && parts[1].equals(password)) {
                    return new Customer(parts[0], parts[1], Double.parseDouble(parts[2]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void customerMenu(Customer customer) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. View Balance\n2. Deposit\n3. Withdraw\n4. Transaction History\n5. Logout");
            System.out.print("Choose: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    customer.viewBalance();
                    break;
                case 2:
                    System.out.print("Enter amount to deposit: ");
                    double dep = sc.nextDouble();
                    customer.deposit(dep);
                    break;
                case 3:
                    System.out.print("Enter amount to withdraw: ");
                    double wit = sc.nextDouble();
                    customer.withdraw(wit);
                    break;
                case 4:
                    customer.viewTransactionHistory();
                    break;
                case 5:
                    customer.saveUserData();
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    //  Deposit
    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Invalid amount.");
            return;
        }
        balance += amount;
        saveUserData();
        logTransaction("Deposit", amount);
        System.out.println("Deposited: " + amount);
    }

    //  Withdraw 
    public void withdraw(double amount) {
        if (amount <= 0 || amount > balance) {
            System.out.println("Invalid amount or insufficient balance.");
            return;
        }
        balance -= amount;
        saveUserData();
        logTransaction("Withdraw", amount);
        System.out.println("Withdrew: " + amount);
    }

    // View Balance 
    public void viewBalance() {
        System.out.println("Current Balance: " + balance);
    }

    // View Transaction History 
    public void viewTransactionHistory() {
        File file = new File(HISTORY_FOLDER, username + ".txt");
        if (!file.exists()) {
            System.out.println("No history found.");
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            System.out.println("---- Transaction History ----");
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println("-----------------------------");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveUserData() {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username)) {
                    lines.add(username + "," + password + "," + balance);
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USERS_FILE))) {
            for (String l : lines) {
                bw.write(l);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Log Transactions
    private void logTransaction(String type, double amount) {
        File folder = new File(HISTORY_FOLDER);
        if (!folder.exists()) folder.mkdirs();

        File file = new File(folder, username + ".txt");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            bw.write(String.format("%-20s %-10s %-10.2f Balance: %.2f", date, type, amount, balance));
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
