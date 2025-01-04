import java.util.Scanner;

    public class BankingSystemSimulator {
    public static void main(String[] args) {
        // Create accounts
        Account account1 = new Account(1, 500);
        Account account2 = new Account(2, 1000);
        Account account3 = new Account(3, 300);

        TransactionManager transactionManager = new TransactionManager(3);

        // Simulate transactions
        transactionManager.submitTransaction(new Transaction(account1, account2, 100)); // Thread 1
        transactionManager.submitTransaction(new Transaction(account2, account3, 200)); // Thread 2
        transactionManager.submitTransaction(new Transaction(account3, account1, 50));  // Thread 3

        // Simulate balance reads
        new Thread(() -> System.out.println("Account 1 Balance: $" + account1.getBalance())).start();
        new Thread(() -> System.out.println("Account 3 Balance: $" + account3.getBalance())).start();

        // Shutdown transaction manager
        transactionManager.shutdown();
    }
}
