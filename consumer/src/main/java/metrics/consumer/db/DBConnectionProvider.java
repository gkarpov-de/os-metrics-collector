package metrics.consumer.db;

import com.typesafe.config.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnectionProvider {
    private static final Logger logger = LogManager.getLogger(DBConnectionProvider.class);

//    private static final String POSTGRES_DRIVER = "org.postgresql.Driver";
    private static final String METRICS_DB_URL = "db.jdbc.url";
    private static final String METRICS_DB_USER = "db.user";
    private static final String METRICS_DB_PASSWORD = "db.password";
    private static final String METRICS_DB_CA_LOCATION = "db.sslrootcert";
    private static final String METRICS_DB_SSL = "db.ssl";
    private static final String METRICS_DB_SSLMODE = "db.sslmode";

    public static Connection createConnection(Config config) {
        final Connection connection;
        final String url = config.getString(METRICS_DB_URL);

        Properties props = new Properties();
        props.put("jdbc.url", url);
        props.put("user", config.getString(METRICS_DB_USER));
        props.put("password", config.getString(METRICS_DB_PASSWORD));
        props.put("ssl", config.getString(METRICS_DB_SSL));
        props.put("sslmode", config.getString(METRICS_DB_SSLMODE));
        props.put("sslrootcert", config.getString(METRICS_DB_CA_LOCATION));

        logger.info("Connecting to DB: {}", url);
        try {
            connection = DriverManager.getConnection(url, props);
        } catch (SQLException e) {
            throw new RuntimeException("Could not create connection to DB", e);
        }
        logger.info("Connected");

        return connection;
    }
}
