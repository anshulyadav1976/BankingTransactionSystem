import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Account {
    private final int accountId;
    private double balance;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final StringBuilder transactionHistory = new StringBuilder();

    public Account(int accountId, double initialBalance) {
        this.accountId = accountId;
        this.balance = initialBalance;
    }

    public int getAccountId() {
        return accountId;
    }

    public void deposit(double amount) {
        lock.writeLock().lock();
        try {
            balance += amount;
            transactionHistory.append("Deposited: $").append(amount).append("\n");
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean withdraw(double amount) {
        lock.writeLock().lock();
        try {
            if (balance >= amount) {
                balance -= amount;
                transactionHistory.append("Withdrew: $").append(amount).append("\n");
                return true;
            }
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public double getBalance() {
        lock.readLock().lock();
        try {
            return balance;
        } finally {
            lock.readLock().unlock();
        }
    }

    public String getTransactionHistory() {
        lock.readLock().lock();
        try {
            return transactionHistory.toString();
        } finally {
            lock.readLock().unlock();
        }
    }

    public ReentrantReadWriteLock getLock() {
        return lock;
    }
}
