import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TransactionSystem Class
class TransactionSystem {
    private final Map<Integer, BankAccount> accounts;

    public TransactionSystem(List<BankAccount> accountList) {
        accounts = new HashMap<>();
        for (BankAccount account : accountList) {
            accounts.put(account.getId(), account);
        }
    }

    public boolean transfer(int fromAccountId, int toAccountId, double amount) {
        BankAccount fromAccount = accounts.get(fromAccountId);
        BankAccount toAccount = accounts.get(toAccountId);

        if (fromAccount == null || toAccount == null) {
            throw new IllegalArgumentException("Invalid account IDs provided.");
        }

        // Lock accounts in a consistent order to avoid deadlock
        BankAccount firstLock = fromAccountId < toAccountId ? fromAccount : toAccount;
        BankAccount secondLock = fromAccountId < toAccountId ? toAccount : fromAccount;

        firstLock.lock();
        secondLock.lock();
        try {
            if (fromAccount.getBalance() < amount) {
                System.out.println(Thread.currentThread().getName() + ": Insufficient funds in Account " + fromAccountId + ". Reversing transaction...");
                reverseTransaction(toAccountId, fromAccountId, amount);
                return false;
            }

            fromAccount.withdraw(amount);
            toAccount.deposit(amount);
            System.out.println(Thread.currentThread().getName() + ": Transferred $" + amount + " from Account " + fromAccountId + " to Account " + toAccountId);
            return true;
        } finally {
            firstLock.unlock();
            secondLock.unlock();
        }
    }

    public void reverseTransaction(int fromAccountId, int toAccountId, double amount) {
        BankAccount fromAccount = accounts.get(fromAccountId);
        BankAccount toAccount = accounts.get(toAccountId);

        // Lock accounts in a consistent order to avoid deadlock
        BankAccount firstLock = fromAccountId < toAccountId ? fromAccount : toAccount;
        BankAccount secondLock = fromAccountId < toAccountId ? toAccount : fromAccount;

        firstLock.lock();
        secondLock.lock();
        try {
            toAccount.withdraw(amount);
            fromAccount.deposit(amount);
            System.out.println(Thread.currentThread().getName() + ": Reversed transaction: $" + amount + " from Account " + toAccountId + " to Account " + fromAccountId);
        } finally {
            firstLock.unlock();
            secondLock.unlock();
        }
    }

    public void printAccountBalances() {
        System.out.println("Account Balances:");
        for (BankAccount account : accounts.values()) {
            System.out.println("Account " + account.getId() + ": $" + account.getBalance());
        }
    }
}