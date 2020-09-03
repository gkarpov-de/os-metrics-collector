package metrics.common.model.serialization;

import com.google.gson.Gson;
import metrics.common.model.Host;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.StandardCharsets;

import java.util.Map;

public class HostDeserializer implements Deserializer<Host> {
    private static final Gson gson = new Gson();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // nothing to configure
    }

    @Override
    public Host deserialize(String topic, byte[] data) {
        if (data == null)
            throw new IllegalArgumentException("Invalid host data(null)");

            return gson.fromJson(new String(data, StandardCharsets.UTF_8), Host.class);
    }

    @Override
    public void close() {
        // nothing to close
    }
}
