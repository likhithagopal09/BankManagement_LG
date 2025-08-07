import java.io.*;
import java.util.Scanner;

public class Admin {
    public static void login() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Admin username: ");
        String user = sc.next();
        System.out.print("Admin password: ");
        String pass = sc.next();

        if (user.equals("admin") && pass.equals("admin123")) {
            adminMenu();
        } else {
            System.out.println("Invalid admin credentials.");
        }
    }

    private static void adminMenu() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. View All Users");
            System.out.println("2. Logout");
            System.out.print("Choice: ");
            int ch = sc.nextInt();

            switch (ch) {
                case 1:
                    try (BufferedReader br = new BufferedReader(new FileReader("data/users.txt"))) {
                        String line;
                        System.out.println("\nRegistered Users:");
                        while ((line = br.readLine()) != null) {
                            String[] parts = line.split(",");
                            System.out.println("User: " + parts[0] + ", Balance: ₹" + parts[2]);
                        }
                    } catch (IOException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case 2:
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
