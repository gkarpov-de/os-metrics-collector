package metrics.consumer;

import com.typesafe.config.Config;
import metrics.common.model.Host;
import metrics.consumer.db.DBConnectionProvider;
import metrics.consumer.db.MetricsController;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ConsumerWorker implements Runnable {
    private static final Logger logger = LogManager.getLogger(ConsumerWorker.class);

    private final static String POLL_DURATION = "omc.pollduration";
    private final Consumer<UUID, Host> metricsConsumer;

    private final long durationOfMillis;

    private final MetricsController metricsController;

    public ConsumerWorker(Consumer<UUID, Host> metricsConsumer, MetricsController metricsController, long durationOfMillis) {
        if (metricsConsumer == null)
            throw new IllegalArgumentException("Consumer not set(null)");

        if (metricsController == null)
            throw new IllegalArgumentException("DB connection not set(null)");

        this.metricsConsumer = metricsConsumer;
        this.durationOfMillis = durationOfMillis;
        this.metricsController = metricsController;
    }

    public static ConsumerWorker create(Config config){
        if(config == null)
            throw new IllegalArgumentException("Configuration not set(null)");

        MetricsController metricsController = new MetricsController(DBConnectionProvider.createConnection(config));
        logger.info("Create consumer worker");
        return new ConsumerWorker(ConsumerBuilder.createConsumer(config), metricsController,
                config.getDuration(POLL_DURATION, TimeUnit.MILLISECONDS));
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    ConsumerRecords<UUID, Host> records = metricsConsumer.poll(Duration.ofMillis(durationOfMillis));
                    for (ConsumerRecord<UUID, Host> record : records) {
                        logger.debug("topic = {}, partition = {}, offset = {}, host = {}",
                                record.topic(), record.partition(), record.offset(), record.value());

                        metricsController.addMetrics(record.value());

                        metricsConsumer.commitSync();
                    }
                } catch (Exception e) {
                    logger.error("Could not process metrics from Kafka", e);
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        } finally {
            metricsController.close();
            metricsConsumer.close();
            logger.info("Consumer worker stopped");
        }
    }
}
