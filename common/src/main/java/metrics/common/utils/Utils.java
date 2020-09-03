package metrics.common.utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;


public class Utils {
    private static final long TIMEOUT = 500;

    private static final Logger logger = LogManager.getLogger(Utils.class);

    public static Config loadConfig(String fileName) {
        logger.info("Loading configuration from {}", fileName);

        return ConfigFactory.parseFile(Paths.get(fileName).toFile());//.withFallback(ConfigFactory.defaultReference());
    }

    public static void prepareSafeShutdownOnExit(ExecutorService executorService){
        if(executorService != null)
            Runtime.getRuntime().addShutdownHook(new Thread( () -> {
                executorService.shutdown();
                try {
                    if(!executorService.awaitTermination(TIMEOUT, TimeUnit.MILLISECONDS))
                        executorService.shutdownNow();
                } catch (InterruptedException e) {
                    executorService.shutdownNow();
                    Thread.currentThread().interrupt();
                }
            }));
    }
}
