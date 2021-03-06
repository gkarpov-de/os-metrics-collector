# OS metrics collector

This project is example of using Kafka producer/consumer.
It takes some operation system metrics and puts them into PostgreSQL database through Kafka broker

### Project structure
- `common` - contains common code and model for the consumer and producer
- `consumer` - consumer implementation
- `producer` - producer implementation

### Requirements and how to build
Before building the project make sure that you have installed:
 - `JDK` version >= 11
 - PostgreSQL database
 - Kafka server version >= 2.3

To build the project run **./gradlew clean installDist** command

*To prepare you SSL certificates you need to obtain following files:*
- `Access Key` - expected file name is: `service.key`
- `Access Certificate` - expected file name is: `service.cert`
- `CA Certificate` - expected file name is: `ca.pem`- there should be two - one for the Kafka, one for the PostgreSql

Keystore and truststore for holding the keys and certificates can be created with the following commands:

**openssl pkcs12 -export -inkey service.key -in service.cert -out client.keystore.p12 -name service_key**<br>
**keytool -import -file ca.pem -alias CA -keystore client.truststore.jks**

### To run Kafka consumer
To run Kafka consumer you need:

1. Execute following on you PostgreSQL server:<br>
    **create database metricsdb;** <br>
    **create user metricsapp with password 'metricsapp';**<br>
    **grant all privileges on database metricsdb to metricsapp;**

2. Create a kafka topic, default kafka topic is: `os-metrics`.
if you want to use another name please see the full list of parameters in the `consumer/consumer.conf` file

3. Change parameters in the `consumer/consumer.conf` file:<br> 
    `bootstrap.servers` - comma-separated list of Kafka brokers<br>
    `db.jdbc.url` - PgSQL jdbc URL to the database<br>
    `db.user` - application user name<br>
    `db.password` - application user password<br>
    `omc.topicname` - Kafka topic name<br>
    
### To run Kafka producer
To run kafka producer you need:

1. Create a kafka topic, default kafka topic is: `os-metrics`. 
if you want to use another name please see the full list of parameters in the `producer/producer.conf` file

2. Change parameters in the `producer/producer.conf` file:<br>
    `bootstrap.servers` - comma-separated list of Kafka brokers<br>
    `omc.hostname` - producer host name<br>
    `omc.ipAddress` - producer IP address<br>
    `omc.topicname` - Kafka topic name<br>
 
Configs should be visible for apps - e.g. put them into corresponding folders.

