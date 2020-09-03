package metrics.common.model.serialization;

import com.google.gson.Gson;
import metrics.common.model.Host;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class HostSerializer implements Serializer<Host> {
    private static final Gson gson = new Gson();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // nothing to configure
    }

    @Override
    public byte[] serialize(String topic, Host data) {
        if (data == null)
            throw new IllegalArgumentException("Invalid host data(null)");

        return gson.toJson(data).getBytes();
    }

    @Override
    public void close() {
        // nothing to close
    }
}
