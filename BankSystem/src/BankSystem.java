import java.util.Scanner;

public class BankSystem {
    public static void showMenu() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\nWelcome to Bank Management System\n");
            System.out.println("1. Admin Login");
            System.out.println("2. Customer Login");
            System.out.println("3. Customer Register");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int option = sc.nextInt();

            switch (option) {
                case 1:
                    adminLogin();
                    break;
                case 2:
                    Customer.login();
                    break;
                case 3:
                    Customer.register();
                    break;
                case 4:
                    System.out.println("Exiting... Thank you!");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void adminLogin() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Admin username: ");
        String user = sc.next();
        System.out.print("Admin password: ");
        String pass = sc.next();

        if (user.equals("admin") && pass.equals("admin123")) {
            System.out.println("Admin login successful.");
            // Admin functionalities can be added here
        } else {
            System.out.println("Invalid admin credentials.");
        }
    }
}
