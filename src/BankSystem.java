import java.util.Scanner;

public class BankSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to Bank Management System");

        while (true) {
            System.out.println("\n1. Admin Login\n2. Customer Login\n3. Customer Register\n4. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    Admin.login();
                    break;
                case 2:
                    Customer.login();
                    break;
                case 3:
                    Customer.register();
                    break;
                case 4:
                    System.out.println("Thank you!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}
