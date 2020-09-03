package metrics.consumer.db;

import com.typesafe.config.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flywaydb.core.Flyway;

import java.io.File;
import java.io.IOException;

public class DBMigration {
    private static final String METRICS_DB_URL = "db.jdbc.url";
    private static final String MIGRATION_LOCATION = "flyway.migration.locations";
    private static final String MIGRATION_DB_USER = "flyway.migration.user";
    private static final String MIGRATION_DB_PASSWORD = "flyway.migration.password";

    private static final Logger logger = LogManager.getLogger(DBMigration.class);


    public static void run(Config config) {
        logger.info("Run DB migration");

        Flyway flyway = Flyway.configure()
                        .locations(config.getString(MIGRATION_LOCATION))
                        .dataSource(
                                config.getString(METRICS_DB_URL),
                                config.getString(MIGRATION_DB_USER),
                                config.getString(MIGRATION_DB_PASSWORD))

                        .load();
//        flyway.repair();
        flyway.migrate();
//        flyway.validate();
        logger.info("DB migration done");
    }
}
