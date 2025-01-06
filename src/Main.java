import java.util.ArrayList;
import java.util.List;

// Main Class
public class Main {
    public static void main(String[] args) {
        // Create accounts
        List<BankAccount> accounts = new ArrayList<>();
        accounts.add(new BankAccount(1, 500));
        accounts.add(new BankAccount(2, 1000));
        accounts.add(new BankAccount(3, 200));

        // Create TransactionSystem
        TransactionSystem transactionSystem = new TransactionSystem(accounts);

        // Simulate transactions with threads
        Thread t1 = new Thread(() -> transactionSystem.transfer(1, 2, 100));
        Thread t2 = new Thread(() -> transactionSystem.transfer(2, 3, 200));
        Thread t3 = new Thread(() -> transactionSystem.transfer(3, 1, 50));
        Thread t4 = new Thread(() -> {
            System.out.println("Balance of Account 1: " + accounts.get(0).getBalance());
            System.out.println("Balance of Account 3: " + accounts.get(2).getBalance());
        });

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
    }
}