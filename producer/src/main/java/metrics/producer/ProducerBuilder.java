package metrics.producer;

import com.typesafe.config.Config;
import metrics.common.model.Host;
import metrics.common.model.serialization.HostSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;
import java.util.UUID;

public class ProducerBuilder {
    private static final Logger logger = LogManager.getLogger(ProducerBuilder.class);
    private static final String SECURITY_PROTOCOL = "security.protocol";
    private static final String ENDPOINT_IDENT_ALGO = "ssl.endpoint.identification.algorithm";
    private static final String TRUSTSTORE_LOCATION = "ssl.truststore.location";
    private static final String TRUSTSTORE_PASSWORD = "ssl.truststore.password";
    private static final String KEYSTORE_TYPE = "ssl.keystore.type";
    private static final String KEYSTORE_LOCATION = "ssl.keystore.location";
    private static final String KEYSTORE_PASSWORD = "ssl.keystore.password";
    private static final String KEY_PASSWORD = "ssl.key.password";

    public static KafkaProducer<UUID, Host> createProducer(Config config){
        final Properties props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG, config.getString(ProducerConfig.CLIENT_ID_CONFIG));
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getString(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG));
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, HostSerializer.class.getName());
        if (config.hasPath(ProducerConfig.ACKS_CONFIG))
            props.setProperty(ProducerConfig.ACKS_CONFIG, config.getString(ProducerConfig.ACKS_CONFIG));

        props.setProperty(SECURITY_PROTOCOL, config.getString(SECURITY_PROTOCOL));
        props.setProperty(ENDPOINT_IDENT_ALGO, config.getString(ENDPOINT_IDENT_ALGO));
        props.setProperty(TRUSTSTORE_LOCATION, config.getString(TRUSTSTORE_LOCATION));
        props.setProperty(TRUSTSTORE_PASSWORD, config.getString(TRUSTSTORE_PASSWORD));
        props.setProperty(KEYSTORE_TYPE, config.getString(KEYSTORE_TYPE));
        props.setProperty(KEYSTORE_LOCATION, config.getString(KEYSTORE_LOCATION));
        props.setProperty(KEYSTORE_PASSWORD, config.getString(KEYSTORE_PASSWORD));
        props.setProperty(KEY_PASSWORD, config.getString(KEY_PASSWORD));

        logger.info("Producer created");
        return new KafkaProducer<>(props);
    }

}
