package metrics.consumer;

import com.typesafe.config.Config;
import metrics.common.model.Host;
import metrics.common.model.serialization.HostDeserializer;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;


public class ConsumerBuilder {
    private static final String TOPIC_NAME = "omc.topicname";
    private static final String SECURITY_PROTOCOL = "security.protocol";
    private static final String ENDPOINT_IDENT_ALGO = "ssl.endpoint.identification.algorithm";
    private static final String TRUSTSTORE_LOCATION = "ssl.truststore.location";
    private static final String TRUSTSTORE_PASSWORD = "ssl.truststore.password";
    private static final String KEYSTORE_TYPE = "ssl.keystore.type";
    private static final String KEYSTORE_LOCATION = "ssl.keystore.location";
    private static final String KEYSTORE_PASSWORD = "ssl.keystore.password";
    private static final String KEY_PASSWORD = "ssl.key.password";

    private static final Logger logger = LogManager.getLogger(ConsumerBuilder.class);

    public static Consumer<UUID, Host> createConsumer(Config config){
        Properties props = new Properties();

        props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getString(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG));
        props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, config.getString(ConsumerConfig.GROUP_ID_CONFIG));
        props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class.getName());
        props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, HostDeserializer.class.getName());

        props.setProperty(SECURITY_PROTOCOL, config.getString(SECURITY_PROTOCOL));
        props.setProperty(ENDPOINT_IDENT_ALGO, config.getString(ENDPOINT_IDENT_ALGO));
        props.setProperty(TRUSTSTORE_LOCATION, config.getString(TRUSTSTORE_LOCATION));
        props.setProperty(TRUSTSTORE_PASSWORD, config.getString(TRUSTSTORE_PASSWORD));
        props.setProperty(KEYSTORE_TYPE, config.getString(KEYSTORE_TYPE));
        props.setProperty(KEYSTORE_LOCATION, config.getString(KEYSTORE_LOCATION));
        props.setProperty(KEYSTORE_PASSWORD, config.getString(KEYSTORE_PASSWORD));
        props.setProperty(KEY_PASSWORD, config.getString(KEY_PASSWORD));

        final Consumer<UUID, Host> consumer = new KafkaConsumer<>(props);
        logger.info("Consumer created");
        final String topicName =config.getString(TOPIC_NAME);
        consumer.subscribe(Collections.singletonList(topicName));
        logger.info("Consumer subscribed to {} topic", topicName);
        return consumer;
    }
}
