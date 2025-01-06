import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Main Class
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input initial balances
        System.out.print("Enter initial balance for Account 1: ");
        double balance1 = scanner.nextDouble();

        System.out.print("Enter initial balance for Account 2: ");
        double balance2 = scanner.nextDouble();

        System.out.print("Enter initial balance for Account 3: ");
        double balance3 = scanner.nextDouble();

        // Create accounts
        List<BankAccount> accounts = new ArrayList<>();
        accounts.add(new BankAccount(1, balance1));
        accounts.add(new BankAccount(2, balance2));
        accounts.add(new BankAccount(3, balance3));

        // Create TransactionSystem
        TransactionSystem transactionSystem = new TransactionSystem(accounts);

        // Simulate transactions with threads
        Thread t1 = new Thread(() -> transactionSystem.transfer(1, 2, 100), "Transaction-Thread-1"); // Hardcoded amount
        Thread t2 = new Thread(() -> transactionSystem.transfer(2, 3, 200), "Transaction-Thread-2"); // Hardcoded amount
        Thread t3 = new Thread(() -> transactionSystem.transfer(3, 1, 50), "Transaction-Thread-3"); // Hardcoded amount

        // Add a 4th thread for reading balances concurrently
        Thread t4 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + ": Balance of Account 1: " + accounts.get(0).getBalance());
            System.out.println(Thread.currentThread().getName() + ": Balance of Account 2: " + accounts.get(1).getBalance());
            System.out.println(Thread.currentThread().getName() + ": Balance of Account 3: " + accounts.get(2).getBalance());
        }, "Read-Balances-Thread");

        // Start threads
        t1.start();
        t2.start();
        t3.start();
        t4.start();

        // Join threads to ensure all operations complete before printing final balances
        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Print final account balances
        transactionSystem.printAccountBalances();

        scanner.close();
    }
}