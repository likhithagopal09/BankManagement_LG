import java.io.*;
import java.util.*;

public class Customer extends User {

    public Customer(String username, String password, double balance) {
        super(username, password, balance);
    }

    public static void register() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Choose a username: ");
        String username = sc.next();
        System.out.print("Choose a password: ");
        String password = sc.next();
        String encrypted = PasswordUtil.encrypt(password);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/users.txt", true))) {
            bw.write(username + "," + encrypted + "," + 0.0);
            bw.newLine();
            System.out.println("Registered successfully!");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void login() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter username: ");
        String username = sc.next();
        System.out.print("Enter password: ");
        String password = sc.next();
        String encryptedInput = PasswordUtil.encrypt(password);

        try (BufferedReader br = new BufferedReader(new FileReader("data/users.txt"))) {
            String line;
            boolean found = false;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username) && parts[1].equals(encryptedInput)) {
                    found = true;
                    double balance = Double.parseDouble(parts[2]);
                    Customer customer = new Customer(username, parts[1], balance);
                    customerMenu(customer);
                    break;
                }
            }

            if (!found) {
                System.out.println("Invalid credentials.");
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void customerMenu(Customer customer) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Customer Menu ---");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. View Transaction History");
            System.out.println("5. Logout");
            System.out.print("Choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Your current balance is: ₹" + customer.balance);
                    break;
                case 2:
                    System.out.print("Enter amount to deposit: ");
                    double dep = sc.nextDouble();
                    customer.balance += dep;
                    updateBalance(customer);
                    logTransaction(customer.username, "DEPOSIT", dep);
                    System.out.println("Deposited successfully.");
                    break;
                case 3:
                    System.out.print("Enter amount to withdraw: ");
                    double wd = sc.nextDouble();
                    if (wd <= customer.balance) {
                        customer.balance -= wd;
                        updateBalance(customer);
                        logTransaction(customer.username, "WITHDRAW", wd);
                        System.out.println("Withdrawn successfully.");
                    } else {
                        System.out.println("Insufficient balance.");
                    }
                    break;
                case 4:
                    viewTransactionHistory(customer.username);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void updateBalance(Customer customer) {
        try {
            File inputFile = new File("data/users.txt");
            File tempFile = new File("data/temp.txt");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] parts = currentLine.split(",");
                if (parts[0].equals(customer.username)) {
                    writer.write(customer.username + "," + customer.password + "," + customer.balance);
                } else {
                    writer.write(currentLine);
                }
                writer.newLine();
            }

            writer.close();
            reader.close();

            inputFile.delete();
            tempFile.renameTo(inputFile);
        } catch (IOException e) {
            System.out.println("Error updating balance: " + e.getMessage());
        }
    }

    public static void logTransaction(String username, String type, double amount) {
        try {
            File dir = new File("data/history");
            if (!dir.exists()) dir.mkdirs();

            BufferedWriter writer = new BufferedWriter(new FileWriter("data/history/" + username + ".txt", true));
            writer.write(type + " ₹" + amount + " on " + new Date());
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            System.out.println("Error logging transaction: " + e.getMessage());
        }
    }

    public static void viewTransactionHistory(String username) {
        File file = new File("data/history/" + username + ".txt");
        if (!file.exists()) {
            System.out.println("No transactions yet.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            System.out.println("\n--- Transaction History ---");
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading transaction history: " + e.getMessage());
        }
    }
}
