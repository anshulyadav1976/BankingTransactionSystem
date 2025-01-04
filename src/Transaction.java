import java.util.concurrent.locks.Lock;

public class Transaction implements Runnable {
    private final Account fromAccount;
    private final Account toAccount;
    private final double amount;

    public Transaction(Account fromAccount, Account toAccount, double amount) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
    }

    @Override
    public void run() {
        // Acquire locks in a fixed order based on account IDs to avoid deadlock
        Account firstLock = fromAccount.getAccountId() < toAccount.getAccountId() ? fromAccount : toAccount;
        Account secondLock = fromAccount.getAccountId() < toAccount.getAccountId() ? toAccount : fromAccount;

        Lock firstWriteLock = firstLock.getLock().writeLock();
        Lock secondWriteLock = secondLock.getLock().writeLock();

        firstWriteLock.lock();
        secondWriteLock.lock();
        try {
            if (fromAccount.withdraw(amount)) {
                toAccount.deposit(amount);
                System.out.println("Transferred $" + amount + " from Account " + fromAccount.getAccountId() +
                        " to Account " + toAccount.getAccountId());
            } else {
                System.out.println("Insufficient funds to transfer $" + amount +
                        " from Account " + fromAccount.getAccountId());
            }
        } finally {
            secondWriteLock.unlock();
            firstWriteLock.unlock();
        }
    }
}
