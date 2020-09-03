package metrics.consumer;

import com.typesafe.config.Config;
import metrics.common.utils.Utils;
import metrics.consumer.db.DBMigration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConsumerAppLauncher {
    private static final String CONSUMER_CONFIG = "consumer.conf";

    private static final Logger logger = LogManager.getLogger(ConsumerAppLauncher.class);

    public static void main(String[] args) {
        logger.info("Start OS Metrics consumer");

        Config config = Utils.loadConfig(CONSUMER_CONFIG);
//        DBMigration.run(config);
        ConsumerWorkersExecutor.run(config);
    }
}
