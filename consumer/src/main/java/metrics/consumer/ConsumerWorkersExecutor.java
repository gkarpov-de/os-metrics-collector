package metrics.consumer;

import com.typesafe.config.Config;
import metrics.common.utils.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConsumerWorkersExecutor {
    private static final String CONSUMER_THREADS_QTY = "omc.threads.qty";

    private static final Logger logger = LogManager.getLogger(ConsumerWorkersExecutor.class);

    final ExecutorService executorService;

    private ConsumerWorkersExecutor(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public static ConsumerWorkersExecutor run(Config config) {
        int threadsQty = 1;
        if(config.hasPath(CONSUMER_THREADS_QTY))
         threadsQty = config.getInt(CONSUMER_THREADS_QTY);

        final ExecutorService execService = Executors.newFixedThreadPool(threadsQty);
        for (int i = 0; i < threadsQty; i++) {
            logger.info("Consumer Worker #{} created", i);
            execService.submit(ConsumerWorker.create(config));
        }

        return new ConsumerWorkersExecutor(execService).safeShutdownOnExit();
    }

    private ConsumerWorkersExecutor safeShutdownOnExit(){
        Utils.prepareSafeShutdownOnExit(executorService);
        return this;
    }
}
