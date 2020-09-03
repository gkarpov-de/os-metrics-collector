package metrics.producer;

import com.typesafe.config.Config;
import metrics.common.HostBuilder;
import metrics.common.model.Host;
import org.apache.kafka.clients.producer.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MetricsProvider {
    private static final String TOPIC_NAME = "omc.topicname"; // os.metrics.collector.topicname

    private static final Logger logger = LogManager.getLogger(MetricsProvider.class);
    private final Producer<UUID, Host> metricsProducer;
    private final String topicName;
    private final Host host;
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    private MetricsProvider(KafkaProducer<UUID, Host> metricsProducer, String topicName, Host host) {
        this.metricsProducer = metricsProducer;
        this.topicName = topicName;
        this.host = host;
    }

    public static MetricsProvider createMetricsProvider(Config config) {
        if (config == null)
            throw new IllegalArgumentException("Configuration not set(null)");

        logger.info("Create metrics provider");
        final String topic = config.getString(TOPIC_NAME);

        return new MetricsProvider(ProducerBuilder.createProducer(config),
                topic,
                HostBuilder.createHost(config)).onExitCloseProducer();
    }

    public void scheduleAction(long period) {
        logger.info("Start repeated action of collect and send metrics");
        scheduledExecutorService.scheduleAtFixedRate(this::collectMetricsAndSend,
                2 * period,
                period,
                TimeUnit.MILLISECONDS);
    }

    private MetricsProvider onExitCloseProducer() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::closeProducer));
        return this;
    }

    private void closeProducer() {
        if (metricsProducer != null) {
            logger.info("Closing producer");
            metricsProducer.close();
        }
    }

    private static class ProducerCallback implements Callback {
        @Override
        public void onCompletion(RecordMetadata recordMetadata, Exception e) {
            if (e != null) {
                logger.error("Could not send message to Kafka: {}", e);
            }
        }
    }

    private void collectMetricsAndSend() {
        try {
            logger.trace("Sending message to Kafka");
            ProducerRecord<UUID, Host> producerRecord =new ProducerRecord<>(topicName, UUID.randomUUID(), HostBuilder.getUpdated(host));
            metricsProducer.send(producerRecord, new ProducerCallback());
        } catch (Exception e) {
            logger.error("Could not process metrics to Kafka {}", e);
            Thread.currentThread().interrupt();
        }
    }

}
