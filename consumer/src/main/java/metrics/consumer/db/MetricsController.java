package metrics.consumer.db;

import metrics.common.model.Hardware;
import metrics.common.model.Host;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class MetricsController {
    private static final Logger logger = LogManager.getLogger(MetricsController.class);
    private final static String HOST_HARDWARE_INSERT_SQL =
            "insert into host(" +
                    "id, hostname, ip_address, " +
                    "system_load_average, " +
                    "total_physical_memory_size, free_physical_memory_size, " +
                    "total_swap_space_size, free_swap_space_size) " +
                    "values(?, ?, ?::inet, ?, ?, ?, ?, ?)";

    private final static String CREATE_HOST_TABLE = "" +
            "create table if not exists host\n" +
            "(\n" +
            "    id                         uuid                                not null constraint host_pkey primary key,\n" +
            "    hostname                   varchar(100)                        not null,\n" +
            "    ip_address                 inet,\n" +
            "    system_load_average        bigint                              not null,\n" +
            "    total_physical_memory_size bigint                              not null,\n" +
            "    free_physical_memory_size  bigint                              not null,\n" +
            "    total_swap_space_size      bigint                              not null,\n" +
            "    free_swap_space_size       bigint                              not null,\n" +
            "    time_stamp                 timestamp default CURRENT_TIMESTAMP not null\n" +
            ") without oids;";

    private final Connection connection;

    private final PreparedStatement hostPreparedStatement;

    public MetricsController(Connection connection) {
        this.connection = connection;
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(CREATE_HOST_TABLE);
        } catch (SQLException throwables) {
            logger.error("Could not create table for metrics");
        }

        try {
            hostPreparedStatement = connection.prepareStatement(HOST_HARDWARE_INSERT_SQL);
        } catch (SQLException throwables) {
            throw new RuntimeException("Could not prepare insert statment", throwables);
        }
    }

    public void addMetrics(Host host) {
        logger.info("Sending new metrics to DB: {}", host);

        try {
            prepareInsertStatement(host);
        } catch (SQLException throwables) {
            logger.error("Could not prepare insert statement {}", throwables);
        }


        try {
            hostPreparedStatement.execute();
        } catch (SQLException throwables) {
            logger.error("Could not insert data into DB {}", throwables);

            try {
                connection.rollback();
            } catch (SQLException e) {
                ;
            }

            throw new RuntimeException("Could not update DB with new metrics");
        }
    }

    private void prepareInsertStatement(Host host) throws SQLException {
        hostPreparedStatement.setObject(1, UUID.randomUUID());
        hostPreparedStatement.setObject(2, host.getName());
        hostPreparedStatement.setObject(3, host.getIpAddress());
        Hardware hw = host.getOsData();
        hostPreparedStatement.setObject(4, hw.getSystemLoadAverage());
        hostPreparedStatement.setObject(5, hw.getTotalPhysicalMemorySize());
        hostPreparedStatement.setObject(6, hw.getFreePhysicalMemorySize());
        hostPreparedStatement.setObject(7, hw.getTotalSwapSpaceSize());
        hostPreparedStatement.setObject(8, hw.getFreeSwapSpaceSize());

    }

    public void close() {
        try {
            logger.info("Closing DB resources");
            hostPreparedStatement.close();
            connection.close();
            logger.info("DB resources closed");
        } catch (SQLException throwables) {
            logger.warn("Could not close DB resources {}", throwables);
        }
    }
}
