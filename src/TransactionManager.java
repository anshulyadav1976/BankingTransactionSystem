import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TransactionManager {
    private final ExecutorService executorService;

  

    public TransactionManager(int numThreads) {
        this.executorService = Executors.newFixedThreadPool(numThreads);
    }

    public void submitTransaction(Transaction transaction) {
        executorService.execute(transaction);
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
