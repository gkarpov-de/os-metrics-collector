package metrics.producer;

import com.typesafe.config.Config;
import metrics.common.utils.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;


public class ProducerAppLauncher {
    private static final String PRODUCER_CONFIG = "producer.conf";
    private static final String SEND_ACTION_RATE = "omc.sendrate"; // os.metrics.collector.sendrate
    private static final Logger logger = LogManager.getLogger(ProducerAppLauncher.class);

    public static void main(String[] args) {
        logger.info("Start OS Metrics producer");

        final Config config = Utils.loadConfig(PRODUCER_CONFIG);
        final long period = config.getDuration(SEND_ACTION_RATE, TimeUnit.MILLISECONDS);
        MetricsProvider.createMetricsProvider(config).scheduleAction(period);
    }
}
